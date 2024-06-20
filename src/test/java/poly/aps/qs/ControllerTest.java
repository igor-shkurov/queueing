package poly.aps.qs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    final int TOTAL_TASKS_CREATED = 2000;
    Controller controller = new Controller(0, 20, 0.1, 3, TOTAL_TASKS_CREATED, 10, 3);

    @Test
    void checkIfTotalTaskNumberIsRight() {
        controller.executeAuto();
        // Tasks that entered the system i.e. were generated on the sources
        assertEquals(TOTAL_TASKS_CREATED, StatController.instance.getTotalTasksCreated());
        // Tasks that were successfully processed by the devices
        // Not equals, because with given values we are almost always getting a lot of rejected requests
        assertNotEquals(TOTAL_TASKS_CREATED, StatController.instance.getTotalTasksProcessed());
    }


}