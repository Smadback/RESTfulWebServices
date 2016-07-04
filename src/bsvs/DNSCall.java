package bsvs;

import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
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
        System.out.print("Please enter an address: ");
        String input = "";
        boolean again = true;

        do {
            input = scanner.nextLine();
        /*
         * AUFGABE 2
         */
            String str = "";
            JSONObject jsonObject = null;
            String jsonResponse = "";

            String uri_ip = "http://dig.jsondns.org/IN/"+input+"/A";
            try {
                System.out.print("fetching IP address...");
                str = sendRequest(uri_ip);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("We encountered a problem getting the IP address. Please try again or a different one.");
                break;
            }

            try {
                jsonObject = new JSONObject(str);
                jsonResponse = jsonObject.getJSONArray("answer").getJSONObject(0).getString("rdata");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //print result
            System.out.println(jsonResponse);

            /**
             * AUFGABE 3
             */

            // append the fetched ip to the url
            String uri_geo = "http://freegeoip.net/json/" + jsonResponse;
            try {
                System.out.print("searching the server...");
                str = sendRequest(uri_geo);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("We encountered a problem localizing the server. Please try again or a different IP address.");
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
            System.out.println(jsonLatitude + ", " + jsonLongitude);

            /**
             * AUFGABE 4
             */
            String uri_map = "http://staticmap.openstreetmap.de/staticmap.php?center=" + jsonLatitude + "," + jsonLongitude + "&zoom=10&size=512x512&maptype=osmarenderer";
            Image image = null;
            try {
                System.out.println("opening map...");
                image = sendImageRequest(uri_map);
            } catch (IOException e) {
                System.out.println("We encountered a problem fetching the map. Please try again or a different IP address.");
                break;
            }

            JFrame frame = new JFrame();
            JLabel label = new JLabel(new ImageIcon(image));
            frame.getContentPane().add(label, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);

            System.out.print("Enter the next address: ");


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
