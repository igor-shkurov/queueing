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

     double getCancelProb() {
        double prob = 0;
        for (StatSource source: sourcesStats) {
            prob += (double) source.getRejectedTasksCount() / source.getGeneratedTasksCount();
        }
        return prob / sourceCount;
    }

    public double getAverageTime() {
        double allProcessTime = 0;
        double allBufferedTime = 0;
        for (StatSource source: sourcesStats) {
            System.out.println(source.getTasksTotalTime());
            allProcessTime += source.getTasksTotalTime();
            allBufferedTime += source.getBufferedTime();
        }
        return allBufferedTime / sourceCount + allProcessTime / sourceCount;
    }

    public double getAverageWorkingTime() {
        double allBusiness = 0;
        for (StatDevice device: devicesStats) {
            allBusiness += device.getTotalWorkingTime();
        }
        return allBusiness / deviceCount;
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

    public void taskFinished(int source, int processedBy, double consumptedTime, double processedTime) { // workingTime - time on device; processedTime - time in system
        if (processedBy != -1) {
            devicesStats.get(processedBy).addWorkingTime(processedTime);
        }
        sourcesStats.get(source).addFinishedTaskTime(consumptedTime);
        sourcesStats.get(source).addBufferedTime(consumptedTime - processedTime);
        totalTasksProcessed++;
    }

    public void taskRejected(int source, double workingTime) {
        taskFinished(source, -1, workingTime, 0);
        sourcesStats.get(source).addRejectedTask();
        totalTasksProcessed++;
    }

    public int getRejectedTasks() {
        int rejected = 0;
        for (StatSource ss: sourcesStats) {
            rejected += ss.getRejectedTasksCount();
        }
        return rejected;
    }

    public void setSourceCount(int sourceCount) {
        this.sourceCount = sourceCount;
    }

    public void setDeviceCount(int deviceCount) {
        this.deviceCount = deviceCount;
    }
}
