package poly.aps.smo;

import java.util.Comparator;

public class SpecialEvent implements Comparable<SpecialEvent> {
    @Override
    public int compareTo(SpecialEvent o) {
        return Comparator.comparing(SpecialEvent::getEventTime)
                .thenComparing(SpecialEvent::getEventTypeOrdinal)
                .compare(this, o);
    }

    public enum EventType
    {
        GenerateRequest,
        RequestUnbuffered,
        RequestCompleted
    };

    private final double eventTime;
    private final EventType eventType;
    private int assignedDevice;

    public SpecialEvent(double eventTime, EventType eventType, int assignedDevice) {
        this.eventTime = eventTime;
        this.eventType = eventType;
        this.assignedDevice = assignedDevice;
    }

    public double getEventTime() {
        return eventTime;
    }

    public int getEventTypeOrdinal() {
        return eventType.ordinal();
    }

    public int getAssignedDevice() {
        return assignedDevice;
    }

    public void setAssignedDevice(int assignedDevice) {
        this.assignedDevice = assignedDevice;
    }
}
