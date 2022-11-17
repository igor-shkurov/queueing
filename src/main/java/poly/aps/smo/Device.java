package poly.aps.smo;

import java.util.Comparator;
import java.util.Random;

public class Device implements Comparable<Device> {
    private double taskStartTime = 0L;
    private final int deviceNumber;
    private Request currentRequest;
    private final Random random = new Random();
    private final long lambda;

    public Device(int deviceNumber, long lambda) {
        this.deviceNumber = deviceNumber;
        this.lambda = lambda;
    };

    public double setNextRequest(Request request, double currentTime) {
        taskStartTime = currentTime;
        currentRequest = request;
        return Math.log(1 - random.nextDouble()) / (- lambda);
    }

    public boolean isBusy() {
        return currentRequest != null;
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

    @Override
    public int compareTo(Device o) {
        return Comparator.comparing(Device::isBusy)
                .thenComparing(Device::getDeviceNumber)
                .compare(this, o);
    }
}
