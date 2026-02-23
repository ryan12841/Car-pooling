import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        displayMainMenu();
    }

    private static void displayMainMenu() {
        while (true) {
            System.out.println("\n========== RIDE BOOKING SYSTEM ==========");
            System.out.println("1. Create Account");
            System.out.println("2. Delete Account");
            System.out.println("3. Publish a Ride");
            System.out.println("4. Search and Book Rides");
            System.out.println("5. View All Available Rides");
            System.out.println("6. View Your Profile");
            System.out.println("7. View Your Bookings");
            System.out.println("8. Cancel a Booking");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createNewAccount();
                    break;
                case 2:
                    deleteAccount();
                    break;
                case 3:
                    publishRide();
                    break;
                case 4:
                    searchAndBookRides();
                    break;
                case 5:
                    RideBookingSystem.viewAllRides();
                    break;
                case 6:
                    viewProfile();
                    break;
                case 7:
                    viewBookings();
                    break;
                case 8:
                    cancelBooking();
                    break;
                case 9:
                    System.out.println("Thank you for using Ride Booking System!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void createNewAccount() {
        System.out.println("\n========== CREATE ACCOUNT ==========");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        RideBookingSystem.createAccount(name, password, email);
    }

    private static void deleteAccount() {
        System.out.println("\n========== DELETE ACCOUNT ==========");
        System.out.print("Enter your User ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        RideBookingSystem.deleteAccount(userId, password);
    }

    private static void publishRide() {
        System.out.println("\n========== PUBLISH A RIDE ==========");
        System.out.print("Enter your User ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter number of seats: ");
        int seats = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter source location: ");
        String source = scanner.nextLine();

        System.out.print("Enter destination location: ");
        String destination = scanner.nextLine();

        System.out.print("Enter fare per seat: ");
        long fare = scanner.nextLong();
        scanner.nextLine();

        RideBookingSystem.publishRide(userId, seats, source, destination, fare);
    }

    private static void searchAndBookRides() {
        System.out.println("\n========== SEARCH AND BOOK RIDES ==========");
        System.out.print("Enter your User ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter source location: ");
        String source = scanner.nextLine();

        System.out.print("Enter destination location: ");
        String destination = scanner.nextLine();

        System.out.print("Enter number of seats needed: ");
        int seats = scanner.nextInt();
        scanner.nextLine();

        ArrayList<Ride> availableRides = RideBookingSystem.searchRides(userId, source, destination, seats);
        if (availableRides.isEmpty()) {
            System.out.println("No rides found!");
            return;
        }

        System.out.println("\n========== AVAILABLE RIDES ==========");
        for (int i = 0; i < availableRides.size(); i++) {
            System.out.println("Option " + (i + 1) + ":");
            System.out.println(availableRides.get(i));
            System.out.println("---");
        }

        System.out.print("Select a ride (enter option number): ");
        int rideChoice = scanner.nextInt();
        scanner.nextLine();

        if (rideChoice < 1 || rideChoice > availableRides.size()) {
            System.out.println("Invalid selection!");
            return;
        }

        Ride selectedRide = availableRides.get(rideChoice - 1);
        System.out.print("How many seats do you want to book? ");
        int seatsToBook = scanner.nextInt();
        scanner.nextLine();

        RideBookingSystem.bookRide(userId, selectedRide.getRideId(), seatsToBook);
    }

    private static void viewProfile() {
        System.out.print("Enter your User ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();

        RideBookingSystem.viewUserProfile(userId);
    }

    private static void viewBookings() {
        System.out.print("Enter your User ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();

        RideBookingSystem.viewUserBookings(userId);
    }

    private static void cancelBooking() {
        System.out.println("\n========== CANCEL BOOKING ==========");
        System.out.print("Enter your User ID: ");
        int userId = scanner.nextInt();

        System.out.print("Enter Booking ID to cancel: ");
        int bookingId = scanner.nextInt();
        scanner.nextLine();

        RideBookingSystem.cancelBooking(userId, bookingId);
    }
}
