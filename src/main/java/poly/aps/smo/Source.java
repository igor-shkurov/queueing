package poly.aps.smo;

import java.util.Random;

public class Source {
    private final int deviceNumber;
    private Long requestGenerated = 0L;
    private Random random = new Random();
    private Long alpha;
    private Long beta;

    public Source(int deviceNumber, Long alpha, Long beta) {
        this.deviceNumber = deviceNumber;
        this.alpha = alpha;
        this.beta = beta;
    }

    public double nextRequestGenerationTime() {
        return random.nextDouble() * (alpha - beta) - alpha;
    }

    public Request generateNewRequest(Long currentTime) {
        requestGenerated++;
        return Request.generateNewRequest(currentTime, deviceNumber, deviceNumber + "." + requestGenerated);
    }
}
