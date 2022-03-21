import java.awt.*;
import java.util.*;

public class Stop {
    int u, v;
    String stop_id, stop_name;
    Location location;
    public HashMap<String, Connection> nextConnection = new HashMap<>();
    public HashMap<String, Connection> previousConnections = new HashMap<>();
    private boolean highlighted = false;


    public Stop(String stop_id, String stop_name, Location loc) {
        this.stop_id = stop_id;
        this.stop_name = stop_name;
        this.location = loc;
        this.u = -1;
        this.v = -1;

    }

    /**
     *getter
     */
    public String getId() {
        return stop_id;
    }

    public String getStop_name() {
        return stop_name;
    }

    public Location getLocation() {
        return location;
    }

    public HashMap<String, Connection> getAllConnections() {
        HashMap<String, Connection> connections = new HashMap<>();
        connections.putAll(nextConnection);
        connections.putAll(previousConnections);
        return connections;
    }

    /**
     * Setters and Setters
     */
    public void addNextConnection(Connection c) {
        String key = c.getTrip().getTripId() + "_" + c.getOrigin().getId();
        nextConnection.put(key, c);
    }

    public void addPreviousConnection(Connection c) {
        String key = c.getTrip().getTripId() + "_" + c.getOrigin().getId();
        previousConnections.put(key, c);
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    /**
     *
     * draws the Stops
     */
    public void draw(Graphics g, Location origin, double scale) {
        if (highlighted) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLUE);

        }

        //changes the Origin from Location to a point and creates dpx and dpy
        double dpx = origin.asPoint(origin, scale).x;
        double dpy = origin.asPoint(origin, scale).y;
        //changes the location of the stop from a Location to a point and add the dpx and dpy of origin
        // to create the x and y point of the stop in the screen
        this.u = this.location.asPoint(origin, scale).x + (int) dpx;
        this.v = this.location.asPoint(origin, scale).y + (int) dpy;

        g.fillRect(u - 2, v - 2, 5, 5);
    }


}
