package com.busdriver;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

//Integration tests for BusRepository using real TXT files.
class BusIntegrationTest {

    private BusRepository repo;
    private static final String TEST_FILE = "test_buses.txt";

    private Bus validBus() {

        return new Bus("12345678", 60, 85.0, "Diesel");
    }

    @BeforeEach
    void setUp() throws IOException {
        repo = new BusRepository(TEST_FILE);
        repo.clearAll();
    }

    @AfterAll
    static void teardown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

     //IT-B01 -  Valid bus is stored correctly and retrieved from TXT file.

    @Test
    void itb01_validBus_isStoredAndRetrievedCorrectly() throws IOException {
        boolean added = repo.add(validBus());
        assertTrue(added, "Bus should be added successfully");

        Bus retrieved = repo.retrieve("12345678");

        assertNotNull(retrieved, "Bus should be retrievable after add");
        assertEquals(60, retrieved.getCapacity());
        assertEquals("Diesel", retrieved.getFuelType());
        assertEquals(85.0, retrieved.getFuelLevel(), 0.01);
    }

     // IT-B02 -  Bus with duplicate busID is rejected and not stored.

    @Test
    void itb02_duplicateBusID_isRejected() throws IOException {
        repo.add(validBus());
        Bus duplicate = new Bus("12345678", 40, 50.0, "Hybrid");
        boolean secondAdd = repo.add(duplicate);

        assertFalse(secondAdd, "Duplicate busID should be rejected");
        assertEquals(1, repo.count(), "Count must remain 1 after rejected duplicate");
    }

     // IT-B03 -  Valid capacity decrease is persisted correctly to TXT file.

    @Test
    void itb03_capacityDecrease_isPersistedCorrectly() throws IOException {
        repo.add(validBus());
        repo.update("12345678", 50, 70.0, "Diesel");

        Bus updated = repo.retrieve("12345678");
        assertNotNull(updated);
        assertEquals(50, updated.getCapacity(), "Capacity should be reduced to 50");
        assertEquals(70.0, updated.getFuelLevel(), 0.01);
    }


     // IT-B04 -  Record count updates correctly after multiple successful add operations.

    @Test
    void itb04_count_updatesCorrectlyAfterAdds() throws IOException {
        assertEquals(0, repo.count(), "Initial count should be 0");

        repo.add(validBus());
        assertEquals(1, repo.count());

        Bus second = new Bus("87654321", 40, 60.0, "Hybrid");
        repo.add(second);
        assertEquals(2, repo.count());
    }
}