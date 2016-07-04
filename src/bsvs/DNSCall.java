package bsvs;

import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Maik on 30/06/16.
 */
public class DNSCall {

    private static final String USER_AGENT = "Mozilla/5.0";


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Bitte URI eingeben: ");
        String input = "";
        boolean again = true;

        do {
            input = scanner.nextLine();
        /*
         * AUFGABE 2
         */
            String str = null;
            JSONObject jsonObject = null;
            String jsonResponse = "";

            String uri_ip = "http://dig.jsondns.org/IN/"+input+"/A";
            try {
                str = sendRequest(uri_ip);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
                System.out.println("Please try a different URI and run the program again.");
                break;
            }

            try {
                jsonObject = new JSONObject(str);
                jsonResponse = jsonObject.getJSONArray("answer").getJSONObject(0).getString("rdata");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //print result
            System.out.println("IP: " + jsonResponse);

            /**
             * AUFGABE 3
             */

            // append the fetched ip to the url
            String uri_geo = "http://freegeoip.net/json/" + jsonResponse;
            try {
                str = sendRequest(uri_geo);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
                System.out.println("Please try a different URI and run the program again.");
                break;
            }

            String jsonLatitude = "";
            String jsonLongitude = "";
            try {
                jsonObject = new JSONObject(str);
                jsonLatitude = jsonObject.getString("latitude");
                jsonLongitude = jsonObject.getString("longitude");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //print result
            System.out.println("LONGITUDE: " + jsonLongitude);
            System.out.println("LATITUDE: " + jsonLatitude);

            /**
             * AUFGABE 4
             */
            String uri_map = "http://staticmap.openstreetmap.de/staticmap.php?center=" + jsonLatitude + "," + jsonLongitude + "&zoom=13&size=512x512&maptype=osmarenderer";
            Image image = null;
            try {
                image = sendImageRequest(uri_map);
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
                System.out.println("Please try a different URI and run the program again.");
                break;
            }

            JFrame frame = new JFrame();
            JLabel label = new JLabel(new ImageIcon(image));
            frame.getContentPane().add(label, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);

            System.out.print("Eine weitere Landkarte anzeigen? Wenn ja, bitte URI eingeben: ");


        } while (again);
    }

    private static String sendRequest(String uri) throws IOException {
        URL obj = new URL(uri);

        HttpURLConnection con = null;
        BufferedReader in = null;
        String inputLine;
        StringBuffer response = new StringBuffer();

        con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("User-Agent", USER_AGENT);

        in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private static Image sendImageRequest(String uri) throws IOException {
        URL obj = new URL(uri);

        HttpURLConnection con = null;
        Image image = null;

        con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("User-Agent", USER_AGENT);

        image = ImageIO.read(con.getInputStream());

        return image;
    }
}
