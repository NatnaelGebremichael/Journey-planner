import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.*;
import java.util.List;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;


public class journeyPlanner extends GUI {
    //Fields
    private Point mousePressed;
    public HashMap<String, Stop> stops = Graph.getStops();
    public HashMap<String, Stop> stopsName = Graph.getStopsName();
    //Stores the Connections
    public ArrayList<Connection> connections = Graph.getConnections();
    public ArrayList<Connection> highLightedConnections = new ArrayList<>();

    Location origin = Location.newFromLatLon(-12.499435, 130.94329);
    public double width = getDrawingAreaDimension().getWidth();
    public double scale = width / 25;
    public double zoomFactor = 1.1;


    @Override
    protected void redraw(Graphics g) {

        for (Map.Entry stop : stops.entrySet()) { //call the stops drawing method for all the Stops
            Stop s = (Stop) stop.getValue();
            s.draw(g, origin, scale);
        }
        for (Connection connection : connections) {//call the Connections drawing method for all the Connections
            connection.draw(g);
        }
        for (Connection connection : highLightedConnections) {//call the Connections drawing method for highLighted  Connections
            connection.draw(g);
        }

    }

    @Override
    protected void onClick(MouseEvent e) {
        Stop finalStop = null; //to hold the final Stop
        double distance = Double.MAX_VALUE; //to hold the closest stop Distance to a click for comparison
        Object[] stopIds = stops.keySet().toArray(); //changes the stops into Array of objects
        Point mousePosition = e.getPoint(); //gets the mouse click/position/point
        Location mouseLocation = Location.newFromPoint(mousePosition, origin, scale);

        // used to check if a point has been highlighted
        boolean highlight = true;
        for (int i = stopIds.length - 1; i >= 0; i--) {
            Stop currentStop = stops.get(stopIds[i].toString());


            // Gets the distance of the current Stop
            double currentStopDistance = mouseLocation.distance(currentStop.getLocation());
            //Checks if the current Stop distance is less than the previous Stop Distance
            //If it is then set the Distance to currentStopDistance and the final Stop to Current Stop
            //distance is the distance from the Stop Position to the clicked Position
            if (currentStopDistance < distance && (mouseLocation.isClose(currentStop.location, 0.2))) {
                finalStop = currentStop;
                distance = currentStopDistance;
                highlight = false;

            }
        }
        //if there is a stop in rage
        if (finalStop != null) {
            resetStopHighlight(); // reset the All Stop highlighted boolean to false
            resetConnectionHighlight();//rest the Connections highlighted before highlighting the Stop
            finalStop.setHighlighted(true);
            printStopInfo(finalStop,finalStop.stop_name);
        }
    }

    @Override
    protected void onSearch() {
         // search for stops by prefix
        String searchTerm = getSearchBox().getText();
        highlightSearch(searchTerm);
    }

    @Override
    public void comboBox() {
        String name = (String) newSuggestionBox.getSelectedItem();
        findExactStop(name); //Calls the search method to find Find the exact match
    }

    @Override
    protected void onMouseWheelMoved(MouseWheelEvent e) {
        Point newOriginPoint = new Point();
        //if mouse rotation is > 0 performs zoom in action
        //else Zoom out Action
        if (e.getWheelRotation() > 0) {
            //sets the New Origin Point
            newOriginPoint.setLocation((int) (getDrawingAreaDimension().width - getDrawingAreaDimension().width * zoomFactor) / 2
                    , ((int) (getDrawingAreaDimension().height - getDrawingAreaDimension().height * zoomFactor) / 2));

            origin = Location.newFromPoint(newOriginPoint, origin, scale);
            scale /= zoomFactor;

        } else {
            newOriginPoint.setLocation((int) (getDrawingAreaDimension().width - getDrawingAreaDimension().width / zoomFactor) / 2
                    , ((int) (getDrawingAreaDimension().height - getDrawingAreaDimension().height / zoomFactor) / 2));

            origin = Location.newFromPoint(newOriginPoint, origin, scale);
            scale *= zoomFactor;
        }

    }

    @Override
    protected void onMousePressed(MouseEvent e) {
        // update the mouse's press position
        mousePressed = e.getPoint();
    }

    @Override
    protected void onDragged(MouseEvent e) {
        //makes new Location for current mouse point from a Point On the Screen , the origin And Scale
        Location newPos = Location.newFromPoint(e.getPoint(), origin, scale);
        //makes new Location for pressed from a Point On the Screen , the origin And Scale
        Location prePos = Location.newFromPoint(mousePressed, origin, scale);
        //gets the Difference from pressed to current location
        double xNew = prePos.x - newPos.x;
        double yNew = prePos.y - newPos.y;
        xNew = xNew / scale; //ADJUST THE DRAG Speed or amount based on the scale
        yNew = yNew / scale;
        origin = origin.moveBy(xNew, yNew);


    }

    @Override
    protected void onMove(Move m) {
        String move = m.name();

        //
        //as point converts location into screen space(Pixels)
        //origin.x/y is already in kms
        Point newOriginPoint = new Point();

        switch (move) {
            case "ZOOM_OUT":
                newOriginPoint.setLocation((int) (getDrawingAreaDimension().width - getDrawingAreaDimension().width * zoomFactor) / 2
                        , ((int) (getDrawingAreaDimension().height - getDrawingAreaDimension().height * zoomFactor) / 2));

                origin = Location.newFromPoint(newOriginPoint, origin, scale);
                scale /= zoomFactor;
                break;
            case "ZOOM_IN":
                newOriginPoint.setLocation((int) (getDrawingAreaDimension().width - getDrawingAreaDimension().width / zoomFactor) / 2
                        , ((int) (getDrawingAreaDimension().height - getDrawingAreaDimension().height / zoomFactor) / 2));

                origin = Location.newFromPoint(newOriginPoint, origin, scale);
                scale *= zoomFactor;
                break;
            case "SOUTH":
                origin = origin.moveBy(0, -1);
                break;
            case "EAST":
                origin = origin.moveBy(1, 0);
                break;
            case "NORTH":
                origin = origin.moveBy(0, 1);
                break;
            case "WEST":
                origin = origin.moveBy(-1, 0);
                break;
            default:
                break;

        }


    }

    @Override
    protected void onLoad(File stopFile, File tripFile) {
        Graph.readStopsFile(stopFile);
        Graph.readTripsFile(tripFile);

        boolean hasReadFiles = true;
    }

    /**
     * Methods to reset Connections and Stops
     */
    public void resetConnectionHighlight() {
        for (Connection connection : connections) {
            connection.setHighlighted(false);
        }
    }

    public void resetStopHighlight() {
        for (Map.Entry stop : stops.entrySet()) { //call the stops drawing method for all the Stops
            Stop s = (Stop) stop.getValue();
            s.setHighlighted(false);
        }
    }
    /**
     * Searching Methods
     */
    public void highlightSearch(String searchTerm) {
        List<Stop> suggestions = new ArrayList<>();
        newSuggestionBox.removeAllItems();

        // verify that the HashMap of All stops isn't empty
        // verify that suggestions is NotNull
        if (stops.keySet().size() != 0) {
            if (searchTerm != null) {
                suggestions = Graph.searchStopsByName(searchTerm.toUpperCase());
                if (suggestions != null) {
                    for (Stop current : suggestions) {
                        newSuggestionBox.addItem(current.getStop_name());
                    }
                } else
                    return;
            } else
                return;

        } else
            return;

        String stopNames = "";//= "Search results:\n";
        int check = 0;
        Stop finalStop = null;
        // go through all the stops, and only highlight the ones from suggestions, all
        // the other stops will be unHighlighted
        for (String stopId : stops.keySet()) {
            Stop stop = stops.get(stopId);
            resetConnectionHighlight();
            if (suggestions.contains(stop)) {
                finalStop = stop;
                stop.setHighlighted(true);
                check++;
                stopNames += stop.getStop_name() + "\n";
            } else {
                stop.setHighlighted(false);
            }

            //checks that there is only one stop left in the Suggestion and highlight the Stop
            if (check == 1) {
                //gets all the Connection of a Stop
                HashMap<String, Connection> connection = finalStop.getAllConnections();
                for (String key : connection.keySet()) {
                    Connection tempCon = connection.get(key);
                    //gets the trips of a Stop
                    Trip trip = tempCon.getTrip();
                    List<Connection> tripConnection = trip.getConnections();
                    for (Connection allTip : tripConnection) {
                        allTip.setHighlighted(true);
                        // highLightedConnections.clear();
                        highLightedConnections.add(allTip);
                    }
                }
            }
            //getTextOutputArea().setText(stopNames);
            //calls PrintStop info and Highlight the Stop
            if (finalStop != null) {
                finalStop.setHighlighted(true);
                printStopInfo(finalStop,stopNames);
            }


        }
    }

    public void findExactStop(String searchTerm) {
        //Reset the Connection and Stops HigLighted Boolean
        resetConnectionHighlight();
        resetStopHighlight();
        if (searchTerm != null) {
            Stop finalStop = stopsName.get(searchTerm);
            if (finalStop != null) { //verify that Stop is Not Null
                finalStop.setHighlighted(true);
                //gets all the connection of a Stop
                HashMap<String, Connection> connection = finalStop.getAllConnections();
                for (String key : connection.keySet()) {
                    Connection tempCon = connection.get(key);
                    //gets the Trips of a Connection
                    Trip trip = tempCon.getTrip();
                    List<Connection> tripConnection = trip.getConnections();
                    for (Connection tripCon : tripConnection) {
                        tripCon.setHighlighted(true);
                        highLightedConnections.clear();
                        highLightedConnections.add(tripCon);
                    }
                }
                //getTextOutputArea().setText(finalStop.getStop_name());
                if (finalStop != null) {
                    finalStop.setHighlighted(true);
                    printStopInfo(finalStop,finalStop.getStop_name());
                }

            }
        }
    }
    /**
     * Prints info about a Given Stop
     */
    public void printStopInfo(Stop s , String stopInfo) {
        String stopName = s.getStop_name();
        HashMap<String, Connection> connections = s.getAllConnections();
        HashMap<String, Integer> tripIds = new HashMap<>();

        // get the name of the stop
        stopInfo += "\n" + "Stop Name: " + "\n" + stopName + "\n\n";

        // get the IDs of all the trips that go through the stop
        stopInfo += "Trip IDs:\n";
        for (String key : connections.keySet()) {
            Connection connection = connections.get(key);
            String currentTripId = connection.getTrip().getTripId();
            // check if the trip's id was already added to the set
            if (tripIds.get(currentTripId) == null) {
                stopInfo += currentTripId + "\n";
                if (currentTripId != null) {
                    tripIds.put(currentTripId, 1);
                }
            }
        }

        getTextOutputArea().setText(stopInfo);
    }

    public static void main(String[] args) {
        new journeyPlanner();


    }

}

