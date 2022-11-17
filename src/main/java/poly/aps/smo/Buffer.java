package poly.aps.smo;

import java.util.ArrayList;
import java.util.EmptyStackException;

public class Buffer {
    private final StatController statstics;
    private final ArrayList<Request> requests;
    private int fetchPosition = 1;
    private int placePosition = 1;
    private int size = 0; // occupied with requests
    private final int capacity; // buffer size according to settings

    public Buffer(int capacity) {
        this.capacity = capacity;
        requests = new ArrayList<>(capacity + 1);
        statstics = StatController.instance;
        for (int i = 0; i < capacity + 1; i++) {
            requests.add(i, null);
        }
    }

    public int getFetchPosition() {
        return fetchPosition;
    }

    public int getPlacePosition() {
        return placePosition;
    }

    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addRequest(Request request) {
        if (placePosition == capacity + 1) {
            placePosition = 1;
        }

        if (size == capacity) {
            Request canceledRequest = requests.get(placePosition);
            statstics.taskRejected(canceledRequest.getSourceNumber(), request.getStartTime() - canceledRequest.getStartTime());
        }
        else {
            size++;
        }
        requests.set(placePosition, request);

        placePosition++;
    }

    public Request getRequest() throws Exception {
        if (this.isEmpty()) {
            throw new Exception("Buffer is empty");
        }
        placePosition--;
        if (placePosition == 0) {
            placePosition = capacity;
        }
        Request request = requests.get(placePosition);
        requests.set(placePosition, null);
        size--;
        return request;
    }
}
