package poly.aps.qs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatControllerTest {
    final double PROCESSED_TIME = 5.0;
    final double TOTAL_TIME = 10.0;
    StatController statController = new StatController(1, 1);

    @Test
    void checkTaskFinishedFunction() {
        statController.taskCreated(0);
        statController.taskFinished(0, 0, TOTAL_TIME, PROCESSED_TIME);
        assertEquals(10.0, statController.getAverageTime());
        assertEquals(5.0, statController.getAverageWorkingTime());
        assertEquals(5.0, statController.getAverageBufferedTime());
    }
}