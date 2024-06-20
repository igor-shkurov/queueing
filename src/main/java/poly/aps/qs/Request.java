package poly.aps.qs;

public class Request {
    private final int sourceNumber;
    private int processedBy = -1;
    private final double startTime;
    private final String requestId;

    private Request(int sourceNumber, double startTime, String requestId) {
        this.sourceNumber = sourceNumber;
        this.startTime = startTime;
        this.requestId = requestId;
    }

    public int getSourceNumber() {
        return sourceNumber;
    }

    public int getProcessedBy() {
        return processedBy;
    }

    public double getStartTime() {
        return startTime;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setProcessedBy(int processedBy) {
        this.processedBy = processedBy;
    }

    public static Request generateNewRequest(double startTime, int sourceNumber, String requestId) {
        return new Request(sourceNumber, startTime, requestId);
    }
}
