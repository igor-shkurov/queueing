package poly.aps.smo;

import java.util.Random;

public class Device {
    private double taskStartTime = 0L;
    private final int deviceNumber;
    private Request currentRequest;
    private final Random random = new Random();
    private final Long lambda;

    public Device(int deviceNumber, long lambda) {
        this.deviceNumber = deviceNumber;
        this.lambda = lambda;
    };

    public double setNextTask(Request request, Long currentTime) {
        taskStartTime = currentTime;
        currentRequest = request;
        return Math.log(1 - random.nextDouble()) / (- lambda);
    }

    public boolean isAvailable() {
        return currentRequest == null;
    }

    public int getDeviceNumber() {
        return deviceNumber;
    }

    public Request getCurrentRequest() {
        return currentRequest;
    }

    public double getTaskStartTime() {
        return taskStartTime;
    }
}
