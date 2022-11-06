package poly.aps.smo;

import java.util.ArrayList;
import java.util.EmptyStackException;

public class Buffer {
    private final ArrayList<Request> requests;
    private int fetchPosition = 0;
    private int placePosition = 0;
    private int cancelPosition = 0;
    private int size = 0; // occupied with requests
    private final int capacity; // buffer size according to settings

    public Buffer(int capacity) {
        this.capacity = capacity;
        requests = new ArrayList<>(capacity);
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
        size++;
        if (size == capacity) {
            Request canceledRequest = requests.get(cancelPosition);
            cancelPosition++;
        }

        requests.set(placePosition, request);
        fetchPosition = placePosition++;

        if (size == placePosition) {
            placePosition = 0;
        }
    }

    public void getRequest() {
        if (this.isEmpty()) {

        }

    }
}
