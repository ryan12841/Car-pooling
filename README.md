# Car Pooling (Console + PostgreSQL)

A simple console-based ride booking system backed by PostgreSQL. Users can create accounts, publish rides, search rides, book seats, and manage bookings.

## Features
- Create and delete user accounts
- Publish rides with seats, routes, and fare
- Search rides by source/destination and book seats
- View all rides, user profile, and booking history
- Cancel bookings (updates booking status)

## Tech
- Java (console app)
- PostgreSQL
- JDBC driver: `postgresql-42.7.9.jar`

## Project Structure
- `Main.java` — CLI menu + user input
- `RideBookingSystem.java` — business logic + DB operations
- `DBConnection.java` — PostgreSQL connection helper
- `User.java`, `Ride.java`, `Booking.java` — data models

## Database Setup
Create a PostgreSQL database named `carpooling`, then create tables:

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL,
    password VARCHAR(120) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE rides (
    ride_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id),
    number_of_seats INT NOT NULL,
    source VARCHAR(120) NOT NULL,
    destination VARCHAR(120) NOT NULL,
    fare BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE bookings (
    booking_id SERIAL PRIMARY KEY,
    ride_id INT NOT NULL REFERENCES rides(ride_id),
    user_id INT NOT NULL REFERENCES users(id),
    seats_booked INT NOT NULL,
    booking_time TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL
);
```

## Configure DB Connection
Edit `DBConnection.java` if your database credentials differ:

- DB: `carpooling`
- User: `postgres`
- Password: `945713`
- Host: `localhost`
- Port: `5432`

## Compile and Run
From the project folder:

```powershell
javac -cp .;postgresql-42.7.9.jar *.java
java -cp .;postgresql-42.7.9.jar Main
```

## Notes
- Deleting a user uses `user_id` + password verification.
- Booking updates available seats and inserts a booking in a single transaction.
- Cancel booking updates booking status to `CANCELLED`.
