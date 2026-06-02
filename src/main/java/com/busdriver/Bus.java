package com.busdriver;

/**
 * Represents a bus in the system.
 * Enforces B1 validation on construction.
 */
public class Bus {

    private String busID;
    private int capacity;
    private double fuelLevel;
    private String fuelType; // Diesel, Hybrid, Electricity

    /**
     * Constructor with B1 validation on busID.
     * Throws IllegalArgumentException if busID is invalid.
     */
    public Bus(String busID, int capacity, double fuelLevel, String fuelType) {
        // B1: Validate busID
        if (!ValidationUtils.isValidBusID(busID)) {
            throw new IllegalArgumentException("Invalid busID: " + busID);
        }
        this.busID = busID;
        this.capacity = capacity;
        this.fuelLevel = fuelLevel;
        this.fuelType = fuelType;
    }

    //  Getters
    public String getBusID() { return busID; }
    public int getCapacity() { return capacity; }
    public double getFuelLevel() { return fuelLevel; }
    public String getFuelType() { return fuelType; }

    // Setters

    /**
     * B2: Capacity can only decrease, never increase.
     */
    public void setCapacity(int newCapacity) {
        if (newCapacity > this.capacity) {
            throw new IllegalArgumentException(
                    "Bus capacity cannot be increased. Current: " + this.capacity);
        }
        this.capacity = newCapacity;
    }

    public void setFuelLevel(double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    /**
     * Serialises bus to a comma-delimited string for TXT file storage.
     */
    public String toFileString() {
        return busID + "," + capacity + "," + fuelLevel + "," + fuelType;
    }

    /**
     * Reconstructs a Bus object from a stored file line.
     */
    public static Bus fromFileString(String line) {
        String[] parts = line.split(",", 4);
        if (parts.length != 4) {
            throw new IllegalArgumentException("Malformed bus record: " + line);
        }
        return new Bus(
                parts[0],
                Integer.parseInt(parts[1]),
                Double.parseDouble(parts[2]),
                parts[3]
        );
    }

    @Override
    public String toString() {
        return "Bus{id=" + busID + ", capacity=" + capacity
                + ", fuelLevel=" + fuelLevel + ", fuelType=" + fuelType + "}";
    }
}