package com.busdriver;

/**
 * Represents a bus driver in the system.
 * Enforces validation rules D1-D3 on construction.
 */
public class Driver {

    private String driverID;
    private String name;
    private int experienceYears;
    private String licenseType; // Light, Medium, Heavy, PublicTransport
    private String address;
    private String birthdate;   // Format: DD-MM-YYYY

    /**
     * Constructor with full validation.
     * Throws IllegalArgumentException if any field is invalid.
     */
    public Driver(String driverID, String name, int experienceYears,
                  String licenseType, String address, String birthdate) {

        // D1: Validate driverID format
        if (!ValidationUtils.isValidDriverID(driverID)) {
            throw new IllegalArgumentException("Invalid driverID: " + driverID);
        }
        // D2: Validate address format
        if (!ValidationUtils.isValidAddress(address)) {
            throw new IllegalArgumentException("Invalid address format: " + address);
        }
        // D3: Validate birthdate format
        if (!ValidationUtils.isValidBirthdate(birthdate)) {
            throw new IllegalArgumentException("Invalid birthdate format: " + birthdate);
        }

        this.driverID = driverID;
        this.name = name;
        this.experienceYears = experienceYears;
        this.licenseType = licenseType;
        this.address = address;
        this.birthdate = birthdate;
    }

    // --- Getters ---
    public String getDriverID() { return driverID; }
    public String getName() { return name; }
    public int getExperienceYears() { return experienceYears; }
    public String getLicenseType() { return licenseType; }
    public String getAddress() { return address; }
    public String getBirthdate() { return birthdate; }

    // --- Setters with business rule enforcement ---

    /**
     * D5: driverID is immutable — no setter provided.
     * D5: name is immutable — no setter provided.
     */

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    /**
     * D4: License cannot be changed if driver has more than 10 years experience.
     */
    public void setLicenseType(String licenseType) {
        if (this.experienceYears > 10) {
            throw new IllegalStateException(
                    "Cannot change licenseType: driver has more than 10 years of experience.");
        }
        this.licenseType = licenseType;
    }

    public void setAddress(String address) {
        if (!ValidationUtils.isValidAddress(address)) {
            throw new IllegalArgumentException("Invalid address format: " + address);
        }
        this.address = address;
    }

    public void setBirthdate(String birthdate) {
        if (!ValidationUtils.isValidBirthdate(birthdate)) {
            throw new IllegalArgumentException("Invalid birthdate format: " + birthdate);
        }
        this.birthdate = birthdate;
    }

    /**
     * Serialises driver to a pipe-delimited string for TXT file storage.
     * Format: driverID,name,experienceYears,licenseType,address,birthdate
     * Note: address already uses '|' internally, so we use ',' as record delimiter.
     */
    public String toFileString() {
        return driverID + "," + name + "," + experienceYears + ","
                + licenseType + "," + address + "," + birthdate;
    }

    /**
     * Reconstructs a Driver object from a stored file line.
     * Expected format matches toFileString().
     */
    public static Driver fromFileString(String line) {
        // Split only on commas NOT inside the address (address uses '|')
        // Format: id,name,exp,license,streetNum|streetName|city|state|country,DD-MM-YYYY
        // We split on first 4 commas only, then last comma separates address and birthdate
        String[] parts = line.split(",", 6);
        if (parts.length != 6) {
            throw new IllegalArgumentException("Malformed driver record: " + line);
        }
        return new Driver(
                parts[0],               // driverID
                parts[1],               // name
                Integer.parseInt(parts[2]), // experienceYears
                parts[3],               // licenseType
                parts[4],               // address
                parts[5]                // birthdate
        );
    }

    @Override
    public String toString() {
        return "Driver{id=" + driverID + ", name=" + name
                + ", exp=" + experienceYears + ", license=" + licenseType
                + ", address=" + address + ", dob=" + birthdate + "}";
    }
}
