package poly.aps.smo;

import java.util.ArrayList;

public class StatController {
    public static StatController instance;
    private long totalTasksCreated;
    public long totalTasksProcessed;
    private int sourceCount;
    private int deviceCount;
    private final ArrayList<StatSource> sourcesStats;
    private final ArrayList<StatDevice> devicesStats;

    public StatController(int sourceCount, int deviceCount) {
        this.sourceCount = sourceCount;
        this.deviceCount = deviceCount;
        sourcesStats = new ArrayList<>(sourceCount);
        devicesStats = new ArrayList<>(deviceCount);

        for (int i = 0; i < sourceCount; i++) {
            sourcesStats.add(i, new StatSource());
        }
        for (int i = 0; i < deviceCount; i++) {
            devicesStats.add(i, new StatDevice());
        }
        instance = this;
    }

    public long getTotalTasksCreated() {
        return totalTasksCreated;
    }

    public long getTotalTasksProcessed() {
        return totalTasksProcessed;
    }

    public int getSourceCount() {
        return sourceCount;
    }

    public int getDeviceCount() {
        return deviceCount;
    }

    public ArrayList<StatSource> getSourcesArray() {
        return sourcesStats;
    }

    public ArrayList<StatDevice> getDevicesArray() {
        return devicesStats;
    }

    public void taskCreated(int source) {
        totalTasksCreated++;
        sourcesStats.get(source).addGeneratedTask();
    }

    public void taskFinished(int source, int processedBy, double workingTime, double processedTime) { // workingTime - time on device; processedTime - time in system
        if (processedBy != -1) {
            devicesStats.get(processedBy).addWorkingTime(workingTime);
        }
        sourcesStats.get(source).addFinishedTaskTime(processedTime);
        sourcesStats.get(source).addBufferedTime(processedTime - workingTime);
        totalTasksProcessed++;
    }

    public void taskRejected(int source, double workingTime) {
        taskFinished(source, -1, workingTime, 0);
        sourcesStats.get(source).addRejectedTask();
        totalTasksProcessed++;
    }

    public void setSourceCount(int sourceCount) {
        this.sourceCount = sourceCount;
    }

    public void setDeviceCount(int deviceCount) {
        this.deviceCount = deviceCount;
    }
}
