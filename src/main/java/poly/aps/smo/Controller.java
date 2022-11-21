package poly.aps.smo;

import java.util.ArrayList;
import java.util.TreeSet;

public class Controller {
    public static Controller instance;
    private final StatController statistics;
    private double currentTime = 0.0;
    int totalTasksRequired;
    private final Buffer buffer;
    private final int sourceCount;
    private final int deviceCount;
    private final ArrayList<Source> sources;
    private final ArrayList<Device> devices;
    private final TreeSet<SpecialEvent> eventSet;
    private int currentDeviceNum = 0;

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

    public ArrayList<Device> getDevices() {
        return devices;
    }

    public ArrayList<Source> getSources() {
        return sources;
    }

    public Controller(int alpha, int beta, int lambda, int bufferSize, int totalTasksRequired, int sourceCount, int deviceCount) {
        this.sourceCount = sourceCount;
        this.deviceCount = deviceCount;
        this.totalTasksRequired = totalTasksRequired;
        eventSet = new TreeSet<>();

        statistics = new StatController(sourceCount, deviceCount);
        buffer = new Buffer(bufferSize);
        sources = new ArrayList<>(sourceCount);
        devices = new ArrayList<>(deviceCount);

        initSources(alpha, beta);
        initDevices(lambda);
        initEventSet();
        instance = this;
    }

    public SpecialEvent getFirstEvent() {
        return eventSet.first();
    }

    public TreeSet<SpecialEvent> goToNextState() {
        if (!eventSet.isEmpty()) {
            try {
                doStep();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return eventSet;
    }

    public void doStep() throws Exception {
        SpecialEvent specialEvent = eventSet.first();
        eventSet.remove(eventSet.first());
        currentTime = specialEvent.getEventTime();
        int deviceId = specialEvent.getAssignedDevice();
        Device currentDevice;
        switch (specialEvent.getEventTypeOrdinal()) {
            case 0:
                if (!buffer.isEmpty()) {
                    if (!devices.stream().allMatch(Device::isBusy)) {
                        Request request = buffer.getRequest();
                        currentDevice = devices.stream().filter(device -> !device.isBusy()).findAny().get();
                        eventSet.add(new SpecialEvent(currentTime + currentDevice.setNextRequest(request, currentTime),
                                SpecialEvent.EventType.RequestCompleted, currentDevice.getDeviceNumber()));
                    }
                }
                if (statistics.getTotalTasksCreated() < totalTasksRequired) {
                    buffer.addRequest(sources.get(deviceId).generateNewRequest(currentTime));
                    eventSet.add(new SpecialEvent(currentTime + sources.get(deviceId).nextRequestGenerationTime(),
                                                    SpecialEvent.EventType.GenerateRequest, deviceId));
                    eventSet.add(new SpecialEvent(currentTime, SpecialEvent.EventType.RequestUnbuffered, -1));
                    statistics.taskCreated(deviceId);
                }
                break;
            case 1:
                if (devices.stream().allMatch(Device::isBusy)) {
                    break;
                }
                else {
                    do {
                        currentDevice = devices.get(currentDeviceNum);
                        currentDeviceNum++;
                        if (currentDeviceNum == deviceCount) {
                            currentDeviceNum = 0;
                        }
                    }
                    while (currentDevice.isBusy());
                }
                Request request = buffer.getRequest();
                eventSet.add(new SpecialEvent(currentTime + currentDevice.setNextRequest(request, currentTime),
                        SpecialEvent.EventType.RequestCompleted, currentDevice.getDeviceNumber()));
                break;
            case 2:
//                if (!buffer.isEmpty()) {
//                    if (!devices.stream().allMatch(Device::isBusy)) {
//                        request = buffer.getRequest();
//                        currentDevice = devices.stream().filter(device -> !device.isBusy()).findAny().get();
//                        eventSet.add(new SpecialEvent(currentTime + currentDevice.setNextRequest(request, currentTime),
//                                SpecialEvent.EventType.RequestCompleted, currentDevice.getDeviceNumber()));
//                    }
//                }
                currentDevice = devices.get(deviceId);
                statistics.taskFinished(currentDevice.getCurrentRequest().getSourceNumber(), deviceId,
                         currentTime - currentDevice.getCurrentRequest().getStartTime(),
                         currentTime - currentDevice.getTaskStartTime());
                currentDevice.setNextRequest(null, currentTime);
//                if (!buffer.isEmpty()) {
//                    eventSet.add(new SpecialEvent(currentTime, SpecialEvent.EventType.RequestUnbuffered, -1));
//                }
//                devices.add(currentDevice);
//                if (devices.stream().filter(Device::isBusy).count() == deviceCount - 1) {
//                    eventSet.add(new SpecialEvent(currentTime, SpecialEvent.EventType.RequestUnbuffered, -1));
//                }
                break;
            default:
                break;
        }
    }

    public void executeAuto() throws Exception {
        while (!eventSet.isEmpty()) {
            doStep();
        }
    }
}
