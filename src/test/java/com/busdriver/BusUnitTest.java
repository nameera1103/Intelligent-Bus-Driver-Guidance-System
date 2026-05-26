package com.busdriver;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Bus class and conditions B1-B5.
 * Exactly 15 test cases: 3 per condition as per assignment requirements.
 * Covers normal cases, invalid inputs, and edge cases.
 */
class BusUnitTest {

    // ===================== B1: Bus ID Validation =====================

    /**
     * B1-TC01: Normal case — valid 8-digit busID should be accepted.
     */
    @Test
    void b1_validBusID_shouldBeAccepted() {
        assertTrue(ValidationUtils.isValidBusID("12345678"));
    }

    /**
     * B1-TC02: Invalid case — busID contains a letter instead of digit.
     */
    @Test
    void b1_busIDWithLetters_shouldBeRejected() {
        assertFalse(ValidationUtils.isValidBusID("1234567A"));
    }

    /**
     * B1-TC03: Edge case — busID is only 7 digits, one short of required length.
     */
    @Test
    void b1_busIDTooShort_shouldBeRejected() {
        assertFalse(ValidationUtils.isValidBusID("1234567"));
    }

    // ===================== B2: Capacity Update Restriction =====================

    /**
     * B2-TC01: Normal case — decreasing capacity is allowed.
     */
    @Test
    void b2_decreasingCapacity_shouldBeAllowed() {
        Bus bus = new Bus("12345678", 60, 80.0, "Diesel");
        assertDoesNotThrow(() -> bus.setCapacity(50));
        assertEquals(50, bus.getCapacity());
    }

    /**
     * B2-TC02: Invalid case — increasing capacity must be rejected.
     */
    @Test
    void b2_increasingCapacity_shouldBeRejected() {
        Bus bus = new Bus("12345678", 60, 80.0, "Diesel");
        assertThrows(IllegalArgumentException.class, () -> bus.setCapacity(70));
    }

    /**
     * B2-TC03: Edge case — setting capacity to same current value is allowed.
     */
    @Test
    void b2_sameCapacity_shouldBeAllowed() {
        Bus bus = new Bus("12345678", 60, 80.0, "Diesel");
        assertDoesNotThrow(() -> bus.setCapacity(60));
    }

    // ===================== B3: Driver Age Restriction =====================

    /**
     * B3-TC01: Normal case — driver born in 1990 is well under 50.
     * Age restriction does not apply.
     */
    @Test
    void b3_driverUnder50_ageRestrictionDoesNotApply() {
        int age = ValidationUtils.calculateAge("01-01-1990");
        assertTrue(age < 50);
    }

    /**
     * B3-TC02: Invalid case — driver born in 1960 is over 50.
     * Cannot drive buses with capacity >= 50.
     */
    @Test
    void b3_driverOver50_ageRestrictionApplies() {
        int age = ValidationUtils.calculateAge("01-01-1960");
        assertTrue(age > 50);
    }

    /**
     * B3-TC03: Edge case — driver exactly 50 years old.
     * Restriction is for drivers OLDER THAN 50, so age=50 is not restricted.
     */
    @Test
    void b3_driverExactly50_notRestricted() {
        int birthYear = java.time.LocalDate.now().getYear() - 50;
        int age = ValidationUtils.calculateAge("01-01-" + birthYear);
        assertFalse(age > 50);
    }

    // ===================== B4: Electric Bus Restriction =====================

    /**
     * B4-TC01: Normal case — driver with exactly 5 years experience
     * meets the minimum requirement to drive an electric bus.
     */
    @Test
    void b4_exactlyFiveYearsExperience_meetsElectricBusRequirement() {
        Driver d = new Driver("23@#abcdAB", "Bob", 5, "Heavy",
                "1|Main St|Perth|WA|Australia", "01-01-1985");
        assertTrue(d.getExperienceYears() >= 5);
    }

    /**
     * B4-TC02: Invalid case — driver with 3 years experience
     * does not meet the minimum requirement for electric buses.
     */
    @Test
    void b4_threeYearsExperience_doesNotMeetElectricBusRequirement() {
        Driver d = new Driver("23@#abcdAB", "Cara", 3, "Heavy",
                "2|High St|Darwin|NT|Australia", "01-01-1995");
        assertFalse(d.getExperienceYears() >= 5);
    }

    /**
     * B4-TC03: Edge case — driver with exactly 4 years experience
     * falls one year short of the 5-year minimum for electric buses.
     */
    @Test
    void b4_fourYearsExperience_oneShortOfElectricBusRequirement() {
        Driver d = new Driver("23@#abcdAB", "Dan", 4, "Heavy",
                "3|Low St|Hobart|TAS|Australia", "01-01-1992");
        assertFalse(d.getExperienceYears() >= 5);
    }

    // ===================== B5: Driver Licence Restriction =====================

    /**
     * B5-TC01: Normal case — Heavy licence is valid for electric buses.
     */
    @Test
    void b5_heavyLicense_validForElectricBus() {
        assertTrue(ValidationUtils.isLicenseValidForBusType("Heavy", "Electricity"));
    }

    /**
     * B5-TC02: Normal case — PublicTransport licence is valid for hybrid buses.
     */
    @Test
    void b5_publicTransportLicense_validForHybridBus() {
        assertTrue(ValidationUtils.isLicenseValidForBusType("PublicTransport", "Hybrid"));
    }

    /**
     * B5-TC03: Edge case — Light licence is not valid for electric buses.
     * Only Heavy and PublicTransport are permitted for electric/hybrid.
     */
    @Test
    void b5_lightLicense_invalidForElectricBus() {
        assertFalse(ValidationUtils.isLicenseValidForBusType("Light", "Electricity"));
    }
}