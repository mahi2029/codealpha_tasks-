import java.io.*;
import java.util.*;

public class HotelReservationSystem {

    // Room categories
    enum Category {
        STANDARD, DELUXE, SUITE
    }

    // Room class
    static class Room {
        int roomNumber;
        Category category;
        boolean isBooked;

        Room(int roomNumber, Category category) {
            this.roomNumber = roomNumber;
            this.category = category;
            this.isBooked = false;
        }

        @Override
        public String toString() {
            return "Room " + roomNumber + " (" + category + ") - " + (isBooked ? "Booked" : "Available");
        }
    }

    // Reservation class
    static class Reservation implements Serializable {
        String guestName;
        int roomNumber;
        Category category;

        Reservation(String guestName, int roomNumber, Category category) {
            this.guestName = guestName;
            this.roomNumber = roomNumber;
            this.category = category;
        }

        @Override
        public String toString() {
            return "Reservation: " + guestName + " in Room " + roomNumber + " (" + category + ")";
        }
    }

    // Hotel class
    static class Hotel {
        List<Room> rooms = new ArrayList<>();
        List<Reservation> reservations = new ArrayList<>();
        final String dataFile = "reservations.dat";

        Hotel() {
            // Initialize rooms (example)
            for (int i = 1; i <= 5; i++) rooms.add(new Room(i, Category.STANDARD));
            for (int i = 6; i <= 8; i++) rooms.add(new Room(i, Category.DELUXE));
            for (int i = 9; i <= 10; i++) rooms.add(new Room(i, Category.SUITE));
            loadReservations();
            updateRoomStatus();
        }

        void updateRoomStatus() {
            // Mark booked rooms based on reservations
            for (Room room : rooms) {
                room.isBooked = false;
                for (Reservation res : reservations) {
                    if (res.roomNumber == room.roomNumber) {
                        room.isBooked = true;
                        break;
                    }
                }
            }
        }

        void loadReservations() {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
                reservations = (List<Reservation>) ois.readObject();
            } catch (Exception e) {
                reservations = new ArrayList<>();
            }
        }

        void saveReservations() {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
                oos.writeObject(reservations);
            } catch (IOException e) {
                System.out.println("Error saving reservations: " + e.getMessage());
            }
        }

        void showRooms() {
            System.out.println("Rooms:");
            for (Room room : rooms) {
                System.out.println(room);
            }
        }

        void searchRooms(Category category) {
            System.out.println("Available " + category + " rooms:");
            boolean found = false;
            for (Room room : rooms) {
                if (room.category == category && !room.isBooked) {
                    System.out.println(room);
                    found = true;
                }
            }
            if (!found) System.out.println("No available rooms found.");
        }

        boolean bookRoom(String guestName, int roomNumber) {
            Room room = findRoom(roomNumber);
            if (room == null) {
                System.out.println("Room does not exist.");
                return false;
            }
            if (room.isBooked) {
                System.out.println("Room already booked.");
                return false;
            }
            // Simulate payment
            if (!simulatePayment()) {
                System.out.println("Payment failed. Booking cancelled.");
                return false;
            }
            // Book room
            Reservation reservation = new Reservation(guestName, roomNumber, room.category);
            reservations.add(reservation);
            room.isBooked = true;
            saveReservations();
            System.out.println("Booking successful!");
            System.out.println(reservation);
            return true;
        }

        boolean cancelReservation(String guestName, int roomNumber) {
            Iterator<Reservation> iterator = reservations.iterator();
            while (iterator.hasNext()) {
                Reservation res = iterator.next();
                if (res.guestName.equalsIgnoreCase(guestName) && res.roomNumber == roomNumber) {
                    iterator.remove();
                    updateRoomStatus();
                    saveReservations();
                    System.out.println("Reservation cancelled.");
                    return true;
                }
            }
            System.out.println("Reservation not found.");
            return false;
        }

        void viewBookings() {
            if (reservations.isEmpty()) {
                System.out.println("No bookings found.");
                return;
            }
            System.out.println("All Bookings:");
            for (Reservation res : reservations) {
                System.out.println(res);
            }
        }

        Room findRoom(int roomNumber) {
            for (Room room : rooms) {
                if (room.roomNumber == roomNumber) return room;
            }
            return null;
        }

        boolean simulatePayment() {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Simulate payment (enter 'yes' to confirm): ");
            String input = scanner.nextLine().trim().toLowerCase();
            return input.equals("yes");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Hotel hotel = new Hotel();

        while (true) {
            System.out.println("\n--- Hotel Reservation System ---");
            System.out.println("1. Show all rooms");
            System.out.println("2. Search rooms by category");
            System.out.println("3. Book a room");
            System.out.println("4. Cancel a reservation");
            System.out.println("5. View all bookings");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> hotel.showRooms();
                case "2" -> {
                    System.out.print("Enter category (STANDARD, DELUXE, SUITE): ");
                    String cat = scanner.nextLine().toUpperCase();
                    try {
                        Category category = Category.valueOf(cat);
                        hotel.searchRooms(category);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid category.");
                    }
                }
                case "3" -> {
                    System.out.print("Enter your name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter room number to book: ");
                    int roomNum;
                    try {
                        roomNum = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid room number.");
                        break;
                    }
                    hotel.bookRoom(name, roomNum);
                }
                case "4" -> {
                    System.out.print("Enter your name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter room number to cancel: ");
                    int roomNum;
                    try {
                        roomNum = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid room number.");
                        break;
                    }
                    hotel.cancelReservation(name, roomNum);
                }
                case "5" -> hotel.viewBookings();
                case "6" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
