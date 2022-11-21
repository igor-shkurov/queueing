package poly.aps.smo;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.*;

public class Buffer {
    private final StatController statistics;
    private final ArrayList<Request> requests;
    private long fetchPosition = 0;
    private long placePosition = 0;
    private long cancelPosition = 0;
    private long size = 0; // occupied with requests
    private final int capacity; // buffer size according to settings

    public Buffer(int capacity) {
        this.capacity = capacity;
        requests = new ArrayList<>(capacity);
        statistics = StatController.instance;
        for (int i = 0; i < capacity; i++) {
            requests.add(i, null);
        }
    }

    public long getFetchPosition() {
        return fetchPosition;
    }

    public long getPlacePosition() {
        return placePosition;
    }

    public long getSize() {
        return size;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean posBusy(long i) {
        return requests.get((int) i) != null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Request findOldest() {
        Optional<Request> opt = requests.stream().min(Comparator.comparing(Request::getStartTime));
        return opt.orElse(null);
    }

    public void addRequest(Request request) throws IOException {
        if (size == capacity) {
            Request canceledRequest = findOldest();
            System.out.println("Отказ с " + requests.indexOf(canceledRequest) + " источника");
            requests.set(requests.indexOf(canceledRequest), request);
            statistics.taskRejected(canceledRequest.getSourceNumber(), request.getStartTime() - canceledRequest.getStartTime());
        }
        else {
            while (posBusy(placePosition)) {;
                System.out.println("size: " + size + " capacity: " + capacity + " placePos " + placePosition);
                System.out.println(requests.size());

                placePosition++;
                if (placePosition == capacity) {
                    placePosition = 0;
                }
            }
            requests.set((int) placePosition, request);
            size = requests.stream().filter(Objects::nonNull).count();
        }
        for (Request r: requests) {
            System.out.println(r);
        }
    }

    public Request getRequest() {
        fetchPosition = placePosition;
        Request request = requests.get((int) fetchPosition);
        while (request == null) {
            if (fetchPosition == 0) {
                fetchPosition = capacity;
            }
            else {
                fetchPosition--;
            }
            request = requests.get((int) fetchPosition);
        }
        requests.set((int) placePosition, null);
        size = requests.stream().filter(Objects::nonNull).count();
        for (Request r: requests) {
            System.out.println(r);
        }
        System.out.println("size: " + size + " capacity: " + capacity + " placePos: " + placePosition + " fetchPos: " + fetchPosition);
        return request;
    }
}
