package com.busdriver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for validating Driver and Bus fields.
 * Centralises all validation logic to keep model classes clean.
 */
public class ValidationUtils {

    /**
     * D1: Validates driverID format.
     * - Exactly 10 characters
     * - First 2 chars: digits 2-9
     * - Characters 3-8: must contain at least 2 special characters
     * - Last 2 chars: uppercase A-Z
     */
    public static boolean isValidDriverID(String id) {
        if (id == null || id.length() != 10) return false;

        // First two chars must be digits 2-9
        if (!Character.isDigit(id.charAt(0)) || !Character.isDigit(id.charAt(1))) return false;
        if (id.charAt(0) < '2' || id.charAt(0) > '9') return false;
        if (id.charAt(1) < '2' || id.charAt(1) > '9') return false;

        // Characters at index 2-7 (positions 3-8): count special characters
        int specialCount = 0;
        for (int i = 2; i <= 7; i++) {
            char c = id.charAt(i);
            if (!Character.isLetterOrDigit(c)) specialCount++;
        }
        if (specialCount < 2) return false;

        // Last two characters must be uppercase A-Z
        char ninth = id.charAt(8);
        char tenth = id.charAt(9);
        if (!Character.isUpperCase(ninth) || !Character.isLetter(ninth)) return false;
        if (!Character.isUpperCase(tenth) || !Character.isLetter(tenth)) return false;

        return true;
    }

    /**
     * D2: Validates address format.
     * Expected: StreetNumber|StreetName|City|State|Country
     * Must have exactly 5 parts separated by '|'
     */
    public static boolean isValidAddress(String address) {
        if (address == null) return false;
        String[] parts = address.split("\\|", -1);
        if (parts.length != 5) return false;
        for (String part : parts) {
            if (part.trim().isEmpty()) return false;
        }
        return true;
    }

    /**
     * D3: Validates birthdate format DD-MM-YYYY.
     * Also checks it is a real calendar date.
     */
    public static boolean isValidBirthdate(String birthdate) {
        if (birthdate == null) return false;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    .withResolverStyle(java.time.format.ResolverStyle.STRICT);
            // STRICT mode requires uuuu (week-based year) not yyyy
            DateTimeFormatter strictFormatter = DateTimeFormatter
                    .ofPattern("dd-MM-uuuu")
                    .withResolverStyle(java.time.format.ResolverStyle.STRICT);
            java.time.LocalDate.parse(birthdate, strictFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Calculates age in years from a DD-MM-YYYY birthdate string.
     * Returns -1 if birthdate is invalid.
     */
    public static int calculateAge(String birthdate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate dob = LocalDate.parse(birthdate, formatter);
            return LocalDate.now().getYear() - dob.getYear()
                    - (LocalDate.now().getDayOfYear() < dob.getDayOfYear() ? 1 : 0);
        } catch (DateTimeParseException e) {
            return -1;
        }
    }

    /**
     * B1: Validates busID format.
     * - Exactly 8 characters
     * - All characters must be digits
     */
    public static boolean isValidBusID(String id) {
        if (id == null || id.length() != 8) return false;
        for (char c : id.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    /**
     * B5: Checks if license type is allowed for electric or hybrid buses.
     * Only Heavy or PublicTransport licenses are permitted.
     */
    public static boolean isLicenseValidForBusType(String licenseType, String fuelType) {
        if (fuelType.equalsIgnoreCase("Electricity") || fuelType.equalsIgnoreCase("Hybrid")) {
            return licenseType.equalsIgnoreCase("Heavy")
                    || licenseType.equalsIgnoreCase("PublicTransport");
        }
        return true;
    }
}