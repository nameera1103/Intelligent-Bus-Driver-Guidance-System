package com.busdriver;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;


 // Integration tests for DriverRepository.

class DriverIntegrationTest {

    private DriverRepository repo;
    private static final String TEST_FILE = "test_drivers.txt";

    // Valid driver used across tests
    private Driver validDriver() {
        return new Driver("23@#abcdAB", "Alice Smith", 5, "Heavy",
                "10|King St|Melbourne|VIC|Australia", "15-06-1990");
    }

    @BeforeEach
    void setUp() throws IOException {
        // Use a fresh file before each test
        repo = new DriverRepository(TEST_FILE);
        repo.clearAll();
    }

    @AfterAll
    static void teardown() throws IOException {
        // Clean up test file after all tests complete
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }


     // IT-D01 -  Valid driver is stored and retrieved correctly from TXT file.

    @Test
    void itd01_validDriver_isStoredAndRetrievedCorrectly() throws IOException {
        Driver d = validDriver();

        boolean added = repo.add(d);

        assertTrue(added, "Driver should be added successfully");

        Driver retrieved = repo.retrieve("23@#abcdAB");
        assertNotNull(retrieved, "Driver should be retrievable after add");
        assertEquals("Alice Smith", retrieved.getName());
        assertEquals("Heavy", retrieved.getLicenseType());
        assertEquals("15-06-1990", retrieved.getBirthdate());
    }


     // T-D02 -  Driver with duplicate ID is rejected and not stored.

    @Test
    void itd02_duplicateDriverID_isRejected() throws IOException {
        repo.add(validDriver());
        // Try to add same driver again
        Driver duplicate = new Driver("23@#abcdAB", "Bob Jones", 3, "Light",
                "5|Park Rd|Sydney|NSW|Australia", "20-03-1988");
        boolean secondAdd = repo.add(duplicate);

        assertFalse(secondAdd, "Duplicate driverID should be rejected");
        assertEquals(1, repo.count(), "Count should still be 1 after rejected duplicate");
    }


     //  IT-D03 -  Update is persisted correctly to TXT file.

    @Test
    void itd03_update_isPersistedCorrectly() throws IOException {
        repo.add(validDriver());

        // Update address and experience (license stays same since exp will be 6, <=10)
        repo.update("23@#abcdAB", 6, "Heavy",
                "99|New Ave|Canberra|ACT|Australia", "15-06-1990");

        Driver updated = repo.retrieve("23@#abcdAB");
        assertNotNull(updated);
        assertEquals(6, updated.getExperienceYears());
        assertEquals("99|New Ave|Canberra|ACT|Australia", updated.getAddress());
        // D5: Name and ID must not change
        assertEquals("Alice Smith", updated.getName());
        assertEquals("23@#abcdAB", updated.getDriverID());
    }


     //  IT-D04 -  Count reflects correct number of stored drivers after add operations.

    @Test
    void itd04_count_isUpdatedCorrectlyAfterAdds() throws IOException {
        assertEquals(0, repo.count(), "Initial count should be 0");

        repo.add(validDriver());
        assertEquals(1, repo.count());

        Driver second = new Driver("34@#abcdAB", "Bob Lee", 2, "Medium",
                "7|Oak St|Adelaide|SA|Australia", "10-10-1992");
        repo.add(second);
        assertEquals(2, repo.count());
    }
}
