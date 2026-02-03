// RentWise - Vehicle Rental Management System (Core Java, OOP)
// Single-file version for easy compilation & learning
// Compile: javac RentWise.java
// Run:     java RentWise

import java.util.*;

public class RentVehicles {

    /* ============================
       DOMAIN MODELS (OOP CORE)
       ============================ */

    // Abstraction + Inheritance base class
    static abstract class Person {
        protected int id;
        protected String name;
        protected String phone;

        public Person(int id, String name, String phone) {
            this.id = id;
            this.name = name;
            this.phone = phone;
        }

        public abstract String getRole();
    }

    // Customer class
    static class Customer extends Person {
        public Customer(int id, String name, String phone) {
            super(id, name, phone);
        }

        @Override
        public String getRole() {
            return "Customer";
        }
    }

    // Abstraction for vehicles
    static abstract class Vehicle {
        protected int vehicleId;
        protected String brand;
        protected double pricePerDay;
        protected boolean available = true;

        public Vehicle(int vehicleId, String brand, double pricePerDay) {
            this.vehicleId = vehicleId;
            this.brand = brand;
            this.pricePerDay = pricePerDay;
        }

        public abstract String getType();

        public double calculateRent(int days) {
            return pricePerDay * days;
        }

        public boolean isAvailable() {
            return available;
        }

        public void rentOut() {
            available = false;
        }

        public void returnBack() {
            available = true;
        }
    }

    // Car specialization
    static class Car extends Vehicle {
        private int seats;

        public Car(int id, String brand, double pricePerDay, int seats) {
            super(id, brand, pricePerDay);
            this.seats = seats;
        }

        @Override
        public String getType() {
            return "Car";
        }
    }

    // Bike specialization
    static class Bike extends Vehicle {
        private boolean gear;

        public Bike(int id, String brand, double pricePerDay, boolean gear) {
            super(id, brand, pricePerDay);
            this.gear = gear;
        }

        @Override
        public String getType() {
            return "Bike";
        }
    }

    // Rental entity
    static class Rental {
        private Customer customer;
        private Vehicle vehicle;
        private int days;
        private double totalCost;

        public Rental(Customer customer, Vehicle vehicle, int days) {
            this.customer = customer;
            this.vehicle = vehicle;
            this.days = days;
            this.totalCost = vehicle.calculateRent(days);
        }

        public double getTotalCost() {
            return totalCost;
        }

        public Vehicle getVehicle() {
            return vehicle;
        }

        public void printInvoice() {
            System.out.println("\n----- Rental Invoice -----");
            System.out.println("Customer : " + customer.name);
            System.out.println("Vehicle  : " + vehicle.getType() + " - " + vehicle.brand);
            System.out.println("Days     : " + days);
            System.out.println("Total    : ₹" + totalCost);
            System.out.println("Thank you for renting with us!");
        }
    }

    // Composition: RentalStore HAS vehicles and rentals
    static class RentalStore {
        private List<Vehicle> vehicles = new ArrayList<>();
        private List<Rental> activeRentals = new ArrayList<>();

        public void addVehicle(Vehicle v) {
            vehicles.add(v);
        }

        public void showAvailableVehicles() {
            System.out.println("\nAvailable Vehicles:");
            for (Vehicle v : vehicles) {
                if (v.isAvailable()) {
                    System.out.println(v.vehicleId + " - " + v.getType() + " - " + v.brand + " - ₹" + v.pricePerDay + "/day");
                }
            }
        }

        public Vehicle findVehicle(int id) {
            for (Vehicle v : vehicles) {
                if (v.vehicleId == id && v.isAvailable()) {
                    return v;
                }
            }
            return null;
        }

        public Rental rentVehicle(Customer customer, int vehicleId, int days) {
            Vehicle v = findVehicle(vehicleId);
            if (v == null) {
                return null;
            }

            v.rentOut();
            Rental rental = new Rental(customer, v, days);
            activeRentals.add(rental);
            return rental;
        }

        public boolean returnVehicle(int vehicleId) {
            for (Rental r : activeRentals) {
                if (r.getVehicle().vehicleId == vehicleId) {
                    r.getVehicle().returnBack();
                    activeRentals.remove(r);
                    return true;
                }
            }
            return false;
        }
    }

    /* ============================
       APPLICATION UI (Console)
       ============================ */

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        RentalStore store = new RentalStore();

        // Sample vehicles
        store.addVehicle(new Car(1, "Toyota", 1500, 5));
        store.addVehicle(new Car(2, "Hyundai", 1300, 5));
        store.addVehicle(new Bike(3, "Yamaha", 500, true));
        store.addVehicle(new Bike(4, "Honda", 400, false));

        System.out.println("=== RentWise Vehicle Rental System ===");

        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        System.out.print("Enter phone: ");
        String phone = sc.nextLine();

        Customer customer = new Customer(101, name, phone);

        while (true) {
            System.out.println("\n1. View Available Vehicles");
            System.out.println("2. Rent Vehicle");
            System.out.println("3. Return Vehicle");
            System.out.println("4. Exit");
            System.out.print("Choose: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    store.showAvailableVehicles();
                    break;

                case 2:
                    store.showAvailableVehicles();
                    System.out.print("Enter vehicle ID: ");
                    int vid = sc.nextInt();
                    System.out.print("Enter days: ");
                    int days = sc.nextInt();

                    Rental rental = store.rentVehicle(customer, vid, days);
                    if (rental == null) {
                        System.out.println("Vehicle not available.");
                    } else {
                        rental.printInvoice();
                    }
                    break;

                case 3:
                    System.out.print("Enter vehicle ID to return: ");
                    int rid = sc.nextInt();
                    boolean returned = store.returnVehicle(rid);
                    if (returned) {
                        System.out.println("Vehicle returned successfully.");
                    } else {
                        System.out.println("Invalid vehicle ID or not rented.");
                    }
                    break;

                case 4:
                    System.out.println("Thank you for using RentWise.");
                    return;

                default:
                    System.out.println("Invalid option.");
            }
            // sc.close();
        }
    }
}
