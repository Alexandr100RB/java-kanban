package controllers;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ManagersTest {
    @Test
    public void testDefaultManagerNotNull() {
        assertNotNull(Managers.getDefault(), "default history manager should not be null");
        assertNotNull(Managers.getDefaultHistory(), "default history manager should not be null");
    }
}