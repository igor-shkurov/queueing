package poly.aps.smo;

public class StatDevice {
    private double totalWorkingTime = 0;

    public double getTotalWorkingTime() {
        return totalWorkingTime;
    }

    public void addWorkingTime(double time) {
        totalWorkingTime += time;
    }
}
