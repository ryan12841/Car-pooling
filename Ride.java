import java.util.ArrayList;

public class Ride {
    private int rideId;
    private int userId;
    private int numberOfSeats;
    private String source;
    private String destination;
    private long fare;
    private ArrayList<Integer> bookedSeats;

    public Ride(int rideId, int userId, int numberOfSeats, String source, String destination, long fare) {
        this.rideId = rideId;
        this.userId = userId;
        this.numberOfSeats = numberOfSeats;
        this.source = source;
        this.destination = destination;
        this.fare = fare;
        this.bookedSeats = new ArrayList<>();
    }

    // Getters
    public int getRideId() {
        return rideId;
    }

    public int getUserId() {
        return userId;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public long getFare() {
        return fare;
    }

    public ArrayList<Integer> getBookedSeats() {
        return bookedSeats;
    }

    // Book seats on this ride
    public boolean bookSeats(int numSeats, int userId) {
        if (numberOfSeats >= numSeats) {
            numberOfSeats -= numSeats;
            for (int i = 0; i < numSeats; i++) {
                bookedSeats.add(userId);
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Ride ID : " + rideId +
                "\nDriver ID : " + userId +
                "\nSource : " + source +
                "\nDestination : " + destination +
                "\nAvailable seats : " + numberOfSeats +
                "\nFare per seat : " + fare;
    }
}