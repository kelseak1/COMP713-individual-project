const bookingForm = document.getElementById("booking-form");
const roomSelect = document.getElementById("room");
const bookingsList = document.getElementById("bookings-list");
const formMessage = document.getElementById("form-message");
const refreshButton = document.getElementById("refresh-button");
const dateInput = document.getElementById("booking-date");

setMinimumDate();
// loads rooms and bookings when the page is loaded
loadRooms();
loadBookings();

bookingForm.addEventListener("submit", createBooking);
refreshButton.addEventListener("click", loadBookings);

function setMinimumDate() {
    const today = new Date();

    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, "0");
    const day = String(today.getDate()).padStart(2, "0");

    dateInput.min = `${year}-${month}-${day}`;
}

// loads rooms into the form
async function loadRooms() {
    try {
        const response = await fetch("/api/rooms");

        if (!response.ok) {
            throw new Error("Rooms could not be loaded.");
        }

        const rooms = await response.json();

        for (const room of rooms) {
            const option = document.createElement("option");

            option.value = room.id;
            option.textContent =
                `${room.name} - capacity ${room.capacity}`;

            roomSelect.appendChild(option);
        }
    } catch (error) {
        formMessage.textContent = error.message;
        formMessage.className = "error";
    }
}

// loads bookings into the page
async function loadBookings() {
    bookingsList.textContent = "Loading bookings...";

    try {
        const response = await fetch("/api/bookings");

        if (!response.ok) {
            throw new Error("Bookings could not be loaded.");
        }

        const bookings = await response.json();

        bookingsList.textContent = "";

        if (bookings.length === 0) {
            const message = document.createElement("p");
            message.textContent = "There are currently no bookings.";
            bookingsList.appendChild(message);
            return;
        }

        for (const booking of bookings) {
            const card = document.createElement("article");
            card.className = "booking-card";

            const heading = document.createElement("h3");
            heading.textContent = booking.purpose;

            const room = document.createElement("p");
            room.textContent = `Room: ${booking.room.name}`;

            const date = document.createElement("p");
            date.textContent = `Date: ${booking.bookingDate}`;

            const time = document.createElement("p");
            time.textContent =
                `Time: ${booking.startTime.substring(0, 5)}–` +
                `${booking.endTime.substring(0, 5)}`;

            const customer = document.createElement("p");
            customer.textContent =
                `Booked by: ${booking.customerName}`;

            const cancelButton = document.createElement("button");
            cancelButton.type = "button";
            cancelButton.className = "cancel-button";
            cancelButton.textContent = "Cancel booking";

            cancelButton.addEventListener("click", function () {
                cancelBooking(booking.id);
            });

            card.appendChild(heading);
            card.appendChild(room);
            card.appendChild(date);
            card.appendChild(time);
            card.appendChild(customer);
            card.appendChild(cancelButton);

            bookingsList.appendChild(card);
        }
    } catch (error) {
        bookingsList.textContent = error.message;
    }
}

// creates a new booking
async function createBooking(event) {
    event.preventDefault();

    formMessage.textContent = "";
    formMessage.className = "";

    const request = {
        customerName:
            document.getElementById("customer-name").value,
        customerEmail:
            document.getElementById("customer-email").value,
        purpose:
            document.getElementById("purpose").value,
        bookingDate:
            document.getElementById("booking-date").value,
        startTime:
            document.getElementById("start-time").value,
        endTime:
            document.getElementById("end-time").value,
        roomId:
            Number(document.getElementById("room").value)
    };

    try {
        const response = await fetch("/api/bookings", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(request)
        });

        const responseBody = await response.json();

        if (!response.ok) {
            throw new Error(
                responseBody.message || "The booking could not be created."
            );
        }

        formMessage.textContent = "Booking created successfully.";
        formMessage.className = "success";

        bookingForm.reset();
        setMinimumDate();

        await loadBookings();
    } catch (error) {
        formMessage.textContent = error.message;
        formMessage.className = "error";
    }
}

// cancels a booking
async function cancelBooking(id) {
    const confirmed = window.confirm(
        "Are you sure you want to cancel this booking?"
    );

    if (!confirmed) {
        return;
    }

    try {
        const response = await fetch(`/api/bookings/${id}`, {
            method: "DELETE"
        });

        if (!response.ok) {
            const responseBody = await response.json();

            throw new Error(
                responseBody.message ||
                "The booking could not be cancelled."
            );
        }

        await loadBookings();
    } catch (error) {
        window.alert(error.message);
    }
}