import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

public class RideBookingSystem {

    // Create a new user account
    public static User createAccount(String name, String password, String emailId) {
        String sql = "INSERT INTO users (name, email, password, created_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, emailId);
            stmt.setString(3, password);
            stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            int affected = stmt.executeUpdate();
            if (affected == 1) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        int userId = keys.getInt(1);
                        User newUser = new User(name, userId, password, emailId);
                        System.out.println("Account created successfully!");
                        System.out.println("Your User ID: " + userId);
                        return newUser;
                    }
                }
            }
            System.out.println("Failed to create account!");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return null;
    }

    // Delete a user account
    public static boolean deleteAccount(int userId, String password) {
        String sql = "DELETE FROM users WHERE id = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, password);
            int affected = stmt.executeUpdate();
            if (affected == 1) {
                System.out.println("Account deleted successfully!");
                return true;
            }
            System.out.println("User not found or password incorrect!");
            return false;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }

    // Find user by ID
    public static User findUserById(int userId) {
        String sql = "SELECT id, name, email, password FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString("name"),
                            rs.getInt("id"),
                            rs.getString("password"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return null;
    }

    // Find ride by ID
    public static Ride findRideById(int rideId) {
        String sql = "SELECT ride_id, user_id, number_of_seats, source, destination, fare FROM rides WHERE ride_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rideId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Ride(
                            rs.getInt("ride_id"),
                            rs.getInt("user_id"),
                            rs.getInt("number_of_seats"),
                            rs.getString("source"),
                            rs.getString("destination"),
                            rs.getLong("fare")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return null;
    }

    // User publishes a ride
    public static Ride publishRide(int userId, int seats, String source, String destination, long fare) {
        String sql = "INSERT INTO rides (user_id, number_of_seats, source, destination, fare, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, seats);
            stmt.setString(3, source);
            stmt.setString(4, destination);
            stmt.setLong(5, fare);
            stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            int affected = stmt.executeUpdate();
            if (affected == 1) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        int rideId = keys.getInt(1);
                        Ride newRide = new Ride(rideId, userId, seats, source, destination, fare);
                        System.out.println("Ride published successfully!");
                        System.out.println("Ride ID: " + rideId);
                        return newRide;
                    }
                }
            }
            System.out.println("Failed to publish ride!");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return null;
    }

    // Search for available rides
    public static ArrayList<Ride> searchRides(int userId, String source, String destination, int seats) {
        User user = findUserById(userId);
        if (user == null) {
            System.out.println("User not found!");
            return new ArrayList<>();
        }
        ArrayList<Ride> results = new ArrayList<>();
        String sql = "SELECT ride_id, user_id, number_of_seats, source, destination, fare " +
                "FROM rides WHERE source = ? AND destination = ? AND number_of_seats >= ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, source);
            stmt.setString(2, destination);
            stmt.setInt(3, seats);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new Ride(
                            rs.getInt("ride_id"),
                            rs.getInt("user_id"),
                            rs.getInt("number_of_seats"),
                            rs.getString("source"),
                            rs.getString("destination"),
                            rs.getLong("fare")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return results;
    }

    // Book a ride
    public static Booking bookRide(int userId, int rideId, int seatsToBook) {
        String checkSql = "SELECT number_of_seats, fare FROM rides WHERE ride_id = ?";
        String updateSql = "UPDATE rides SET number_of_seats = number_of_seats - ? WHERE ride_id = ? AND number_of_seats >= ?";
        String insertSql = "INSERT INTO bookings (ride_id, user_id, seats_booked, booking_time, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            int availableSeats;
            long fare;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, rideId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Ride not found!");
                        conn.rollback();
                        return null;
                    }
                    availableSeats = rs.getInt("number_of_seats");
                    fare = rs.getLong("fare");
                }
            }

            if (availableSeats < seatsToBook) {
                System.out.println("Not enough seats available!");
                conn.rollback();
                return null;
            }

            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, seatsToBook);
                updateStmt.setInt(2, rideId);
                updateStmt.setInt(3, seatsToBook);
                int updated = updateStmt.executeUpdate();
                if (updated != 1) {
                    System.out.println("Failed to reserve seats!");
                    conn.rollback();
                    return null;
                }
            }

            long totalFare = fare * seatsToBook;
            Timestamp bookingTime = new Timestamp(System.currentTimeMillis());
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setInt(1, rideId);
                insertStmt.setInt(2, userId);
                insertStmt.setInt(3, seatsToBook);
                insertStmt.setTimestamp(4, bookingTime);
                insertStmt.setString(5, "CONFIRMED");
                int affected = insertStmt.executeUpdate();
                if (affected == 1) {
                    try (ResultSet keys = insertStmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            int bookingId = keys.getInt(1);
                            Booking booking = new Booking(bookingId, rideId, userId, seatsToBook, totalFare, bookingTime, "CONFIRMED");
                            System.out.println("Ride booked successfully!");
                            System.out.println("Booking ID: " + bookingId);
                            System.out.println("Total Fare: " + totalFare);
                            conn.commit();
                            return booking;
                        }
                    }
                }
            }

            conn.rollback();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return null;
    }

    // Cancel a booking
    public static boolean cancelBooking(int userId, int bookingId) {
        String sql = "UPDATE bookings SET status = ? WHERE booking_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "CANCELLED");
            stmt.setInt(2, bookingId);
            stmt.setInt(3, userId);
            int affected = stmt.executeUpdate();
            if (affected == 1) {
                System.out.println("Booking cancelled successfully!");
                return true;
            }
            System.out.println("Booking not found!");
            return false;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }

    // View all available rides
    public static void viewAllRides() {
        String sql = "SELECT ride_id, user_id, number_of_seats, source, destination, fare FROM rides";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("\n========== AVAILABLE RIDES ==========");
            boolean any = false;
            while (rs.next()) {
                any = true;
                Ride ride = new Ride(
                        rs.getInt("ride_id"),
                        rs.getInt("user_id"),
                        rs.getInt("number_of_seats"),
                        rs.getString("source"),
                        rs.getString("destination"),
                        rs.getLong("fare")
                );
                System.out.println(ride);
                System.out.println("---");
            }
            if (!any) {
                System.out.println("No rides available!");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // View user profile
    public static void viewUserProfile(int userId) {
        User user = findUserById(userId);
        if (user == null) {
            System.out.println("User not found!");
            return;
        }
        System.out.println("\n========== USER PROFILE ==========");
        System.out.println(user);
    }

    // View user's bookings
    public static void viewUserBookings(int userId) {
        String sql = "SELECT b.booking_id, b.ride_id, b.user_id, b.seats_booked, b.booking_time, b.status, r.fare " +
                "FROM bookings b JOIN rides r ON r.ride_id = b.ride_id WHERE b.user_id = ? " +
                "ORDER BY b.booking_time DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("\n========== YOUR BOOKINGS ==========");
                boolean any = false;
                while (rs.next()) {
                    any = true;
                    int seats = rs.getInt("seats_booked");
                    long totalFare = rs.getLong("fare") * seats;
                    Booking booking = new Booking(
                            rs.getInt("booking_id"),
                            rs.getInt("ride_id"),
                            rs.getInt("user_id"),
                            seats,
                            totalFare,
                            rs.getTimestamp("booking_time"),
                            rs.getString("status")
                    );
                    System.out.println(booking);
                    System.out.println("---");
                }
                if (!any) {
                    System.out.println("No bookings found!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
