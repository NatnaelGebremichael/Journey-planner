import java.util.ArrayList;
import java.util.List;

public class Trip {
    String tripId;
    List<Connection> connections = new ArrayList<>();

    public Trip(String tripId) {
        this.tripId = tripId;
    }

    /**
     * Getters
     */

    public String getTripId() {
        return tripId;
    }


    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }
}
