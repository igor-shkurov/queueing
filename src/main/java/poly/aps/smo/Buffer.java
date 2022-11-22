package poly.aps.smo;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.*;

public class Buffer {
    private final StatController statistics;
    private final ArrayList<Request> requests;
    private long fetchPosition = 0;
    private long placePosition = -1;
    private long cancelPosition = 0;
    private boolean flagCancel;
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

    public long getCancelPosition() {
        return cancelPosition;
    }

    public boolean isFlagCancel() {
        return flagCancel;
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
        return requests.stream().allMatch(Objects::isNull);
    }

    public Request findOldest() {
        Optional<Request> opt = requests.stream().min(Comparator.comparing(Request::getStartTime));
        return opt.orElse(null);
    }

    public void addRequest(Request request) {
        if (getSize() == capacity) {
            flagCancel = true;
            Request canceledRequest = findOldest();
            cancelPosition = requests.indexOf(canceledRequest);
            requests.set((int) cancelPosition, request);
            statistics.taskRejected(canceledRequest.getSourceNumber(), request.getStartTime() - canceledRequest.getStartTime());
        }
        else {
            flagCancel = false;
            do {
                placePosition++;
                if (placePosition == capacity) {
                    placePosition = 0;
                }
            } while(posBusy(placePosition));
            requests.set((int) placePosition, request);
            size = requests.stream().filter(Objects::nonNull).count();
        }
    }

    public Request getRequest() {
        flagCancel = false;
        fetchPosition = placePosition;
        Request request = requests.get((int) fetchPosition);
        while (request == null) {
            if (fetchPosition == 0) {
                fetchPosition = capacity - 1;
            }
            else {
                fetchPosition--;
            }
            request = requests.get((int) fetchPosition);
        }
        requests.set((int) fetchPosition, null);
        size = requests.stream().filter(Objects::nonNull).count();
        return request;
    }
}
