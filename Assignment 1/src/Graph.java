import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Graph {
    //Fields
    public static HashMap<String, Stop> stops = new HashMap<>();
    public static HashMap<String, Stop> stopsName =  new HashMap<>();
    public static ArrayList<Connection> connections = new ArrayList<>();
    public static Trie trie = new Trie();

    /**
     * reads a stops file, parses its data, and returns them as a list.
     *
     * @param stopFile the file containing a list of stops
     */
    public static void readStopsFile(File stopFile) {
        try {
            List<String> stopData = Files.readAllLines(Path.of(stopFile.getPath()));
            for (int i = 1; i < stopData.size(); i++) {

                Scanner sc = new Scanner(stopData.get(i));
                sc.useDelimiter("\t"); //Ignores the spaces and only gets the next string separated by tab
                //get the values from the scanner into the appropriate variables
                String stopId = sc.next();
                String name = sc.next();
                double lat = sc.nextDouble();
                double lon = sc.nextDouble();
                Location location = Location.newFromLatLon(lat, lon);
                // creates new stop object and add it to the stops hash map
                Stop tempStop = new Stop(stopId, name, location);
                stops.put(stopId, tempStop);
                stopsName.put(name,tempStop);
                trie.insert(name.toUpperCase(), tempStop);//adds the Stop name into the trie Structure
            }
        } catch (IOException e) {
            System.out.println("File reading failed");
        }


    }

    /**
     * reads a tips file, parses its data, and returns them as a list of
     * connections.
     *
     * @param tripFile the file containing a list of trips
     */
    public static void readTripsFile(File tripFile) {
        FileReader fr = null;
        try {
            fr = new FileReader(tripFile);
            BufferedReader reader = new BufferedReader(fr);
            String Line;
            //ignores the first Line
            reader.readLine();
            while ((Line = reader.readLine()) != null) {
                String[] nextString = Line.split("\t");
                ArrayList<Connection> stopConnections = new ArrayList<>();

                //Get the trip Name/Id
                String tripId = nextString[0];
                Trip trip = new Trip(tripId);
                //loop through the arrayString of the line
                for (int i = 1; i < nextString.length; i++) {
                    //get stops by its id
                    String id = nextString[i];
                    Stop stop = stops.get(id);

                    // check that it is second to last
                    //add Next Connection
                    if (i < nextString.length - 1) {
                        //gets the Next Stop
                        String nextStopId = nextString[i + 1];
                        Stop nextStop = stops.get(nextStopId);
                        //Make new/Outgoing  Connection
                        Connection connection = new Connection(trip, stop, nextStop);
                        stopConnections.add(connection);
                        stop.addNextConnection(connection);
                    }
                    //checks if that i/Stop is not the first one on the List
                    if (i > 1) {
                        //get the previous Stop
                        String preStopId = nextString[i - 1];
                        Stop preStop = stops.get(preStopId);
                        //Make new/Incoming  Connection
                        Connection connection = new Connection(trip,preStop, stop);
                        stop.addPreviousConnection(connection);
                    }

                }
                //add all the connections to the trip object, and to the list of all the connections
                trip.setConnections(stopConnections);
                connections.addAll(stopConnections);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * searches for stops in the trie structure by prefix.
     *
     * @param searchTerm the prefix that will be used to search for stops
     * @return a list of stops that match the prefix
     */
    public static List<Stop> searchStopsByName(String searchTerm) {
        return trie.getAll(searchTerm.toCharArray());
    }

    /**
     * getters
     */
    public static HashMap<String, Stop> getStops() {
        return stops;
    }

    public static HashMap<String, Stop> getStopsName() {
        return stopsName;
    }

    public static ArrayList<Connection> getConnections() {
        return connections;
    }

}
