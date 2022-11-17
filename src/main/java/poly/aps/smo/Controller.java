package poly.aps.smo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.TreeSet;

public class Controller {
    private final StatController statistics;
    private double currentTime = 0.0;
    int totalTasksRequired;
    private final Buffer buffer;
    private final int sourceCount;
    private final int deviceCount;
    private final ArrayList<Source> sources;
    private final TreeSet<Device> devices;
    private final TreeSet<SpecialEvent> eventSet;

    private void initSources(long alpha, long beta) {
        for (int i = 0; i < sourceCount; i++) {
            sources.add(new Source(i, alpha, beta));
        }
    }

    private void initDevices(long lambda) {
        for (int i = 0; i < deviceCount; i++) {
            devices.add(new Device(i, lambda));
        }
    }

    public void initEventSet() {
        for (int i = 0; i < sourceCount; i++) {
            SpecialEvent event = new SpecialEvent(sources.get(i).nextRequestGenerationTime(), SpecialEvent.EventType.GenerateRequest, i);
            eventSet.add(event);
        }
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public Controller(int alpha, int beta, int lambda, int bufferSize, int totalTasksRequired, int sourceCount, int deviceCount) {
        this.sourceCount = sourceCount;
        this.deviceCount = deviceCount;
        this.totalTasksRequired = totalTasksRequired;
        eventSet = new TreeSet<>();

        statistics = new StatController(sourceCount, deviceCount);
        buffer = new Buffer(bufferSize);
        sources = new ArrayList<>(sourceCount);
        devices = new TreeSet<>();

        initSources(alpha, beta);
        initDevices(lambda);
        initEventSet();
    }

    public void doStep() throws Exception {
        SpecialEvent specialEvent = eventSet.first();
        eventSet.remove(eventSet.first());
        currentTime = specialEvent.getEventTime();
        int deviceId = specialEvent.getAssignedDevice();
        switch (specialEvent.getEventTypeOrdinal()) {
            case 0:
                if (statistics.getTotalTasksCreated() < totalTasksRequired) {
                    buffer.addRequest(sources.get(deviceId).generateNewRequest(currentTime));
                    eventSet.add(new SpecialEvent(currentTime + sources.get(deviceId).nextRequestGenerationTime(),
                                                    SpecialEvent.EventType.GenerateRequest, deviceId));
                    eventSet.add(new SpecialEvent(currentTime, SpecialEvent.EventType.RequestUnbuffered, -1));
                    statistics.taskCreated(deviceId);
                }
                break;
            case 1:
                if (!devices.first().isBusy()) {
                    Device currentDevice = devices.first();
                    devices.remove(devices.first());
                    if (buffer.isEmpty() || currentDevice.isBusy()) {
                        devices.add(currentDevice);
                        break;
                    }
                    Request request = buffer.getRequest();
                    eventSet.add(new SpecialEvent(currentTime + currentDevice.setNextRequest(request, currentTime),
                            SpecialEvent.EventType.RequestCompleted, currentDevice.getDeviceNumber()));
                    devices.add(currentDevice);
                }
                break;
            case 2:
                Device currentDevice = devices.stream().filter(device -> device.getDeviceNumber() == deviceId).findAny().get();
                devices.remove(currentDevice);

                 statistics.taskFinished(currentDevice.getCurrentRequest().getSourceNumber(), deviceId,
                         currentTime - currentDevice.getCurrentRequest().getStartTime(),
                         currentTime - currentDevice.getTaskStartTime());
                currentDevice.setNextRequest(null, currentTime);
                devices.add(currentDevice);
                eventSet.add(new SpecialEvent(currentTime, SpecialEvent.EventType.RequestUnbuffered, -1));
                break;
            default:
                break;
        }
    }

    public void executeAuto() throws Exception {
        while (!eventSet.isEmpty()) {
            doStep();
        }
        System.out.println(statistics.getTotalTasksCreated());
    }
}
