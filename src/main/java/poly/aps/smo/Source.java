package poly.aps.smo;

import java.util.Random;

public class Source {
    private final int deviceNumber;
    private long requestGenerated = 0L;
    private Random random = new Random();
    private long alpha;
    private long beta;

    public Source(int deviceNumber, long alpha, long beta) {
        this.deviceNumber = deviceNumber;
        this.alpha = alpha;
        this.beta = beta;
    }

    public double nextRequestGenerationTime() {
        return random.nextDouble() * (beta - alpha) + alpha;
    }

    public Request generateNewRequest(double currentTime) {
        requestGenerated++;
        return Request.generateNewRequest(currentTime, deviceNumber, deviceNumber + "." + requestGenerated);
    }
}
