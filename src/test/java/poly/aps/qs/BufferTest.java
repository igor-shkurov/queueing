package poly.aps.qs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BufferTest {
    @Test
    void testSizeAndCapacity() {
        Buffer buffer = new Buffer(10, false);
        buffer.addRequest(Request.generateNewRequest(0.0, 1,  ""));
        assertEquals(10, buffer.getCapacity());
        assertEquals(1, buffer.getSize());
    }

    @Test
    void testRejectRequest() {
        Buffer buffer = new Buffer(2, false);
        buffer.addRequest(Request.generateNewRequest(0.0, 1,  ""));
        buffer.addRequest(Request.generateNewRequest(0.0, 1,  ""));
        buffer.addRequest(Request.generateNewRequest(0.0, 1,  ""));
        assertEquals(2, buffer.getSize());
    }
}