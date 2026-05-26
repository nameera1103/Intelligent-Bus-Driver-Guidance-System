package com.busdriver;

import java.io.*;
import java.util.*;

/**
 * Handles persistence of Bus records using a TXT file.
 * Each line in the file represents one bus record.
 * File format per line: busID,capacity,fuelLevel,fuelType
 */
public class BusRepository {

    private final String filePath;

    /**
     * Constructor — accepts file path so tests can use isolated temp files.
     */
    public BusRepository(String filePath) {
        this.filePath = filePath;
        try {
            File f = new File(filePath);
            if (!f.exists()) f.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("Cannot initialise bus storage file: " + filePath, e);
        }
    }

    /**
     * Adds a new bus to the TXT file.
     * B1: Rejects duplicate busIDs.
     * Returns true if added, false if duplicate.
     */
    public boolean add(Bus bus) throws IOException {
        List<Bus> existing = retrieveAll();
        // B1: Check for duplicate ID
        for (Bus b : existing) {
            if (b.getBusID().equals(bus.getBusID())) {
                return false; // Duplicate — reject
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(bus.toFileString());
            writer.newLine();
        }
        return true;
    }

    /**
     * Retrieves a bus by busID.
     * Returns null if not found.
     */
    public Bus retrieve(String busID) throws IOException {
        for (Bus b : retrieveAll()) {
            if (b.getBusID().equals(busID)) return b;
        }
        return null;
    }

    /**
     * Retrieves all buses from the TXT file.
     */
    public List<Bus> retrieveAll() throws IOException {
        List<Bus> buses = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    buses.add(Bus.fromFileString(line));
                }
            }
        }
        return buses;
    }

    /**
     * Updates an existing bus's mutable fields.
     * B2: Capacity cannot increase.
     * Returns true if updated, false if not found.
     */
    public boolean update(String busID, int newCapacity,
                          double newFuelLevel, String newFuelType) throws IOException {
        List<Bus> all = retrieveAll();
        boolean found = false;

        for (Bus b : all) {
            if (b.getBusID().equals(busID)) {
                found = true;
                b.setCapacity(newCapacity); // B2 enforced inside setter
                b.setFuelLevel(newFuelLevel);
                b.setFuelType(newFuelType);
                break;
            }
        }

        if (!found) return false;

        rewriteFile(all);
        return true;
    }

    /**
     * Returns the number of bus records stored.
     */
    public int count() throws IOException {
        return retrieveAll().size();
    }

    /**
     * Rewrites the entire TXT file with the given list.
     */
    private void rewriteFile(List<Bus> buses) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (Bus b : buses) {
                writer.write(b.toFileString());
                writer.newLine();
            }
        }
    }

    /**
     * Clears all records — used in tests for clean state.
     */
    public void clearAll() throws IOException {
        new FileWriter(filePath, false).close();
    }
}
