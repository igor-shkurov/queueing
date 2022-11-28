package poly.aps.smo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StatController {
    public static StatController instance;
    private long totalTasksCreated;
    private long totalTasksProcessed;
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
            allProcessTime += source.getTasksTotalTime();
            allBufferedTime += source.getBufferedTime();
        }
        return (allBufferedTime + allProcessTime) / totalTasksCreated;
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

    public void sourceStatTable() {
        int i = 1;
        DecimalFormat df = new DecimalFormat("#.###");
        Path file = Paths.get("source_stats.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Источник;Количество заявок;Вероятность отказа;Сред. время в системе;Время в БП;Время обслуживания;Д(БП);Д(Обсл.)");
        for (StatSource ss: sourcesStats) {
            String str = String.valueOf(i) +
                    ';' +
                    ss.getGeneratedTasksCount() +
                    ';' +
                    df.format((double) ss.getRejectedTasksCount() / ss.getGeneratedTasksCount()) +
                    ';' +
                    df.format((ss.getTasksTotalTime() + ss.getBufferedTime()) / ss.getGeneratedTasksCount()) +
                    ';' +
                    df.format(ss.getBufferedTime() / ss.getGeneratedTasksCount()) +
                    ';' +
                    df.format(ss.getTasksTotalTime() / ss.getGeneratedTasksCount())+
                    ';' +
                    df.format(ss.getBufferedTimeDispersion()) +
                    ';' +
                    df.format(ss.getTotalTimeDispersion());
            i++;
            lines.add(str);
        }
        try {
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deviceStatTable() {
        int i = 1;
        DecimalFormat df = new DecimalFormat("#.###");
        Path file = Paths.get("device_stats.txt");
        List<String> lines = new ArrayList<>();
        lines.add("Прибор;Коэффициент использования");
        for (StatDevice sd: devicesStats) {
            String str = String.valueOf(i) +
                    ';' +
                    df.format(sd.getTotalWorkingTime() / Controller.instance.getCurrentTime());
            i++;
            lines.add(str);
        }
        try {
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
