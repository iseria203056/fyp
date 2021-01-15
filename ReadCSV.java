

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

/**
 *
 * @author SatoshiFung
 */
public class GetCSVTesting {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String csvFile
                = Paths.get("").toAbsolutePath()
                + "/references/kln_parking_spaces_eng.csv"; // outside carPark file path
        System.out.println(csvFile);
        final String geocodeKey = "AIzaSyAMuN8OoUPkZU3upTFYNwIPjIMRC5-DCDo";
        String geocodeURL = "https://maps.googleapis.com/maps/api/geocode/json?";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(csvFile)); // read gov api
            
            //console demo
            String testingTable ="Latitude\tLongitude\n";
            
            while ((line = br.readLine()) != null) {
                String[] country = line.split(cvsSplitBy);
                if (country[0].equals("KWUN TONG")) {
                    String district = country[0].replaceAll(" ", "+");
                    String location = country[1].replaceAll(" ", "+");
                    String geocodeURLParameter;

                    geocodeURLParameter = String.format("address=%s&key=%s",
                             location + "," + district,
                             geocodeKey);
                    String readyGeocodeURL = geocodeURL + geocodeURLParameter;
                    // start call Geocoding API
                    // Connect to the URL using java's native library
                    URL url = new URL(readyGeocodeURL);
                    URLConnection request = url.openConnection();
                    request.connect();

                    // Convert to a JSON object to print data
                    JSONParser jp = new JSONParser();
                    // Convert the input stream to a json element
                    JSONObject j =
                            (JSONObject) jp.parse(new InputStreamReader(
                                    (InputStream) request.getContent()
                            ));
                    // Get results array
                    JSONArray resultsArray = (JSONArray) j.get("results");
                    // Get latitude and longitude
                    
                    for(Object results:resultsArray){
                        JSONObject jobj = (JSONObject) results;
                        JSONObject geometry = (JSONObject) jobj.get("geometry");
                        JSONObject jLocation = (JSONObject) geometry.get("location");
                        // Output testing: latitude and longitude
                        testingTable += jLocation.get("lat")+"\t"+jLocation.get("lng")+"\n";
                    }
                    

                }
            }
            
            System.out.println(testingTable);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

}
