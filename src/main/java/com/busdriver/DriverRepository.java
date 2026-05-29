// Reviewed by Anusha: verified add, update, retrieve and count operations for DriverRepository
// Reviewed by Tejas: verified add, update, retrieve and count operations for DriverRespository
package com.busdriver;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Handles persistence of Driver records using a TXT file.
 * Each line in the file represents one driver record.
 * File format per line: driverID,name,experienceYears,licenseType,address,birthdate
 */
public class DriverRepository {

    private final String filePath;

    /**
     * Constructor — accepts file path so tests can use isolated temp files.
     */
    public DriverRepository(String filePath) {
        this.filePath = filePath;
        // Create file if it doesn't exist
        try {
            File f = new File(filePath);
            if (!f.exists()) f.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("Cannot initialise driver storage file: " + filePath, e);
        }
    }

    /**
     * Adds a new driver to the TXT file.
     * D1: Rejects duplicate driverIDs.
     * Returns true if added, false if duplicate.
     */
    public boolean add(Driver driver) throws IOException {
        List<Driver> existing = retrieveAll();
        // D1: Check for duplicate ID
        for (Driver d : existing) {
            if (d.getDriverID().equals(driver.getDriverID())) {
                return false; // Duplicate — reject
            }
        }
        // Append new record to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(driver.toFileString());
            writer.newLine();
        }
        return true;
    }

    /**
     * Retrieves a driver by driverID.
     * Returns null if not found.
     */
    public Driver retrieve(String driverID) throws IOException {
        for (Driver d : retrieveAll()) {
            if (d.getDriverID().equals(driverID)) return d;
        }
        return null;
    }

    /**
     * Retrieves all drivers from the TXT file.
     */
    public List<Driver> retrieveAll() throws IOException {
        List<Driver> drivers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    drivers.add(Driver.fromFileString(line));
                }
            }
        }
        return drivers;
    }

    /**
     * Updates an existing driver's mutable fields.
     * D5: Cannot update driverID or name.
     * D4: Cannot update licenseType if experienceYears > 10.
     * Returns true if updated, false if driver not found.
     */
    public boolean update(String driverID, int newExperience, String newLicenseType,
                          String newAddress, String newBirthdate) throws IOException {
        List<Driver> all = retrieveAll();
        boolean found = false;

        for (Driver d : all) {
            if (d.getDriverID().equals(driverID)) {
                found = true;
                // D5: driverID and name are never changed
                d.setExperienceYears(newExperience);

                // D4: only set license if experience <= 10
                if (!newLicenseType.equals(d.getLicenseType())) {
                    d.setLicenseType(newLicenseType); // throws if exp > 10
                }

                d.setAddress(newAddress);
                d.setBirthdate(newBirthdate);
                break;
            }
        }

        if (!found) return false;

        // Rewrite entire file with updated data
        rewriteFile(all);
        return true;
    }

    /**
     * Returns the number of driver records stored.
     */
    public int count() throws IOException {
        return retrieveAll().size();
    }

    /**
     * Rewrites the entire TXT file with the given list.
     * Used internally after updates.
     */
    private void rewriteFile(List<Driver> drivers) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (Driver d : drivers) {
                writer.write(d.toFileString());
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