package bsvs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 */
public class Main {

    public static void main(String[] args) {

        String url = "https://api.ipify.org/?format=json";

        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection con = null;
        BufferedReader in = null;
        String inputLine;
        StringBuffer response = new StringBuffer();

        try {
            con = (HttpURLConnection) obj.openConnection();

            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String str = response.toString();
        JSONObject jsonObject = null;
        String ip = "";
        try {
            jsonObject = new JSONObject(str);
            ip = jsonObject.getString("ip");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //print result
        System.out.println("IP: " + ip);
    }
}
