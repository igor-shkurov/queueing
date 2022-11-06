package poly.aps.smo;

public class SpecialEvent {
    public enum EventType
    {
        GenerateTask,
        TaskUnbuffered,
        TaskCompleted
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

    public EventType getEventType() {
        return eventType;
    }

    public int getAssignedDevice() {
        return assignedDevice;
    }

    public void setAssignedDevice(int assignedDevice) {
        this.assignedDevice = assignedDevice;
    }

    static boolean compare(SpecialEvent l, SpecialEvent r) {
        if (l.getEventTime() < r.getEventTime()) {
            return true;
        } else if (l.getEventTime() > r.getEventTime()) {
            return false;
        } else {
            return l.getEventType().ordinal() > r.getEventType().ordinal();
        }
    }
}
