public class RIDE {
    int id;
    String source;
    String destination;
    int total_seats;
    int available_seats;
    double fare;
    User user;

    public RIDE(int id, String source, String destination, int total_seats, double fare, int available_seats, User user) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.total_seats = total_seats;
        this.fare = fare;
        this.available_seats = available_seats;
        this.user = user;
    }

    @Override
    public String toString() {
        return "RIDE{" +
                "id=" + id +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", total_seats=" + total_seats +
                ", available_seats=" + available_seats +
                ", fare=" + fare +
                ", user=" + user +
                '}';
    }
}