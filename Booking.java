import java.sql.Timestamp;

public class Booking {
    private int booking_id;
    private int ride_id;
    private int user_id;
    private int seatsBooked;
    private long totalFare;
    private Timestamp bookingTime;
    private String bookingStatus;

    public Booking(int booking_id, int ride_id, int user_id, int seatsBooked, long totalFare, Timestamp bookingTime, String bookingStatus) {
        this.booking_id = booking_id;
        this.ride_id = ride_id;
        this.user_id = user_id;
        this.seatsBooked = seatsBooked;
        this.totalFare = totalFare;
        this.bookingTime = bookingTime;
        this.bookingStatus = bookingStatus;
    }

    // Getters
    public int getBooking_id() {
        return booking_id;
    }

    public int getRide_id() {
        return ride_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getNumberOfSeats() {
        return seatsBooked;
    }

    public long getTotalFare() {
        return totalFare;
    }

    public Timestamp getBookingTime() {
        return bookingTime;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    // Cancel booking
    public void cancelBooking() {
        this.bookingStatus = "CANCELLED";
    }

    @Override
    public String toString() {
        return "Booking ID : " + booking_id +
                "\nRide ID : " + ride_id +
                "\nUser ID : " + user_id +
                "\nNumber of Seats : " + seatsBooked +
                "\nTotal Fare : " + totalFare +
                "\nBooking Time : " + bookingTime +
                "\nStatus : " + bookingStatus;
    }
}
