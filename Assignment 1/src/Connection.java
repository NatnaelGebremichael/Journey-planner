import java.awt.*;

public class Connection {


    private final Trip trip;
    public Stop origin; // fromNode
    public Stop destination; // ToNode
    public boolean highlighted = false;
    //Connection constructor
    //it takes in a trip and fromNode and to node stop
    public Connection(Trip trip, Stop Origin, Stop Destination) {
        this.origin = Origin;
        this.destination = Destination;
        this.trip = trip;

    }

    /**
     * Getters
     */
    public Stop getOrigin() {
        return origin;
    }

    public Stop getDestination() {
        return destination;
    }

    public Trip getTrip() {
        return trip;
    }

    /**
     * Setter
     */
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    /**
     * draw Method
     */
    public void draw(Graphics g) {
        //changes the color to red if the Connection is highLighted
        //else it sets it to Blue
        if (highlighted) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLUE);

        }
        g.drawLine(getOrigin().u, getOrigin().v, destination.u, getDestination().v);

    }


}
