package poly.aps.smo;

public class StatSource {
    private int generatedTasksCount = 0;
    private int rejectedTasksCount = 0;
    private double tasksTotalTime = 0;
    private double taskTotalSquaredTime = 0;
    private double bufferedTime = 0;
    private double bufferedSquaredTime = 0;

    public int getGeneratedTasksCount() {
        return generatedTasksCount;
    }

    public int getRejectedTasksCount() {
        return rejectedTasksCount;
    }

    public double getTasksTotalTime() {
        return tasksTotalTime;
    }

    public double getBufferedTime() {
        return bufferedTime;
    }

    public double getTotalTimeDispersion() {
        return (taskTotalSquaredTime / generatedTasksCount) -
                (tasksTotalTime / generatedTasksCount) * (tasksTotalTime / generatedTasksCount);
    }

    public double getBufferedTimeDispersion() {
        return (bufferedSquaredTime / generatedTasksCount) -
                (bufferedTime / generatedTasksCount) * (bufferedTime / generatedTasksCount);
    }

    public void addGeneratedTask() {
        generatedTasksCount++;
    }

    public void addRejectedTask() {
        rejectedTasksCount++;
    }

    public void addFinishedTaskTime(double time)
    {
        taskTotalSquaredTime += time * time;
        tasksTotalTime += time;
    }

    public void addBufferedTime(double value)
    {
        bufferedSquaredTime += value * value;
        bufferedTime += value;
    }
}
