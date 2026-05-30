package com.busdriver;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Driver class and conditions D1-D5.
 * Exactly 15 test cases: 3 per condition as per assignment requirements.
 * Covers normal cases, invalid inputs, and edge cases.
 */
class DriverUnitTest {

    // ===================== D1: Driver ID Validation =====================

    /**
     * D1-TC01: Normal case — valid driverID should be accepted.
     * Format: 2 digits(2-9), 2+ special chars in pos 3-8, 2 uppercase last.
     */
    @Test
    void d1_validDriverID_shouldBeAccepted() {
        assertTrue(ValidationUtils.isValidDriverID("23@#abcdAB"));
    }

    /**
     * D1-TC02: Invalid case — driverID shorter than 10 characters.
     */
    @Test
    void d1_idTooShort_shouldBeRejected() {

        assertFalse(ValidationUtils.isValidDriverID("23@@AB"));
    }

    /**
     * D1-TC03: Edge case — only 1 special character in positions 3-8
     * (minimum required is 2).
     */
    @Test
    void d1_onlyOneSpecialChar_shouldBeRejected() {

        assertFalse(ValidationUtils.isValidDriverID("23@abcdeAB"));
    }

    // ===================== D2: Address Format =====================

    /**
     * D2-TC01: Normal case — valid address with 5 pipe-separated parts.
     */
    @Test
    void d2_validAddress_shouldBeAccepted() {
        assertTrue(ValidationUtils.isValidAddress("12|Main Street|Melbourne|VIC|Australia"));
    }

    /**
     * D2-TC02: Invalid case — only 4 parts, country field missing.
     */
    @Test
    void d2_fourPartAddress_shouldBeRejected() {
        assertFalse(ValidationUtils.isValidAddress("12|Main Street|Melbourne|VIC"));
    }

    /**
     * D2-TC03: Edge case — one part is empty (blank city field).
     */
    @Test
    void d2_emptyPartInAddress_shouldBeRejected() {
        assertFalse(ValidationUtils.isValidAddress("12|Main Street||VIC|Australia"));
    }

    // ===================== D3: Birthdate Format =====================

    /**
     * D3-TC01: Normal case — valid birthdate in DD-MM-YYYY format.
     */
    @Test
    void d3_validBirthdate_shouldBeAccepted() {

        assertTrue(ValidationUtils.isValidBirthdate("15-06-1990"));
    }

    /**
     * D3-TC02: Invalid case — wrong format YYYY-MM-DD used instead.
     */
    @Test
    void d3_wrongFormatBirthdate_shouldBeRejected() {

        assertFalse(ValidationUtils.isValidBirthdate("1990-06-15"));
    }

    /**
     * D3-TC03: Edge case — non-existent date (31st February) with
     * strict parsing enabled to catch invalid calendar dates.
     */
    @Test
    void d3_nonExistentDate_shouldBeRejected() {

        assertFalse(ValidationUtils.isValidBirthdate("31-02-1990"));
    }

    // ===================== D4: License Update Restriction =====================

    /**
     * D4-TC01: Normal case — driver with 5 years experience can change licence.
     */
    @Test
    void d4_underTenYearsExperience_canChangeLicense() {
        Driver d = new Driver("23@#abcdAB", "John", 5, "Light",
                "10|Park Rd|Sydney|NSW|Australia", "01-01-1985");
        assertDoesNotThrow(() -> d.setLicenseType("Heavy"));
    }

    /**
     * D4-TC02: Invalid case — driver with 15 years experience cannot change licence.
     */
    @Test
    void d4_moreThanTenYearsExperience_cannotChangeLicense() {
        Driver d = new Driver("23@#abcdAB", "Jane", 15, "Heavy",
                "10|Park Rd|Sydney|NSW|Australia", "01-01-1975");
        assertThrows(IllegalStateException.class, () -> d.setLicenseType("Light"));
    }

    /**
     * D4-TC03: Edge case — driver with exactly 10 years experience
     * can still change licence (restriction is >10, not >=10).
     */
    @Test
    void d4_exactlyTenYearsExperience_canChangeLicense() {
        Driver d = new Driver("23@#abcdAB", "Tom", 10, "Medium",
                "10|Park Rd|Sydney|NSW|Australia", "01-01-1980");
        assertDoesNotThrow(() -> d.setLicenseType("Heavy"));
    }

    // ===================== D5: Immutable Fields =====================

    /**
     * D5-TC01: Normal case — driverID getter returns correct value.
     * No setDriverID method exists — immutability enforced by design.
     */
    @Test
    void d5_driverID_isImmutable() {
        Driver d = new Driver("23@#abcdAB", "Alice", 3, "Light",
                "1|Queen St|Brisbane|QLD|Australia", "10-05-1992");
        assertEquals("23@#abcdAB", d.getDriverID());
    }

    /**
     * D5-TC02: Normal case — name getter returns correct value.
     * No setName method exists — immutability enforced by design.
     */
    @Test
    void d5_name_isImmutable() {
        Driver d = new Driver("23@#abcdAB", "Alice", 3, "Light",
                "1|Queen St|Brisbane|QLD|Australia", "10-05-1992");
        assertEquals("Alice", d.getName());
    }

    /**
     * D5-TC03: Edge case — updating address field does not affect
     * name or driverID, confirming those fields remain immutable.
     */
    @Test
    void d5_updatingAddress_doesNotAffectNameOrID() {
        Driver d = new Driver("23@#abcdAB", "Alice", 3, "Light",
                "1|Queen St|Brisbane|QLD|Australia", "10-05-1992");
        d.setAddress("5|King St|Adelaide|SA|Australia");
        assertEquals("23@#abcdAB", d.getDriverID());
        assertEquals("Alice", d.getName());
    }
}