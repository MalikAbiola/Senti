package Classes;

import UIs.Main;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Doctormaliko
 */
public class UrbanAPIConnection implements Runnable {

    private JsonString jString;
    private String urbanTerm;

    public UrbanAPIConnection(String urbanTerm, JsonString jsString) {
        this.jString = jsString;
        this.urbanTerm = urbanTerm;
    }

    public UrbanAPIConnection() {
    }

    public String getUrbanTerm() {
        return urbanTerm;
    }

    public JsonString getjString() {
        return jString;
    }

    public void setUrbanTerm(String urbanTerm) {
        this.urbanTerm = urbanTerm;
    }

    public void setjString(JsonString jString) {
        this.jString = jString;
    }

    @Override
    public void run() {
        StringBuffer sb;
        DataInputStream is = null;
        String urlReturnString = "";
        try {
            URL u = new URL("http://api.urbandictionary.com/v0/define?term=" + this.urbanTerm);
            HttpURLConnection httpConn = (HttpURLConnection) u.openConnection();
            httpConn.connect();
            int respCode = httpConn.getResponseCode();

            if (respCode == HttpURLConnection.HTTP_OK) {
                sb = new StringBuffer();
                is = new DataInputStream(httpConn.getInputStream());
                int chr;
                while ((chr = is.read()) != -1) {
                    sb.append((char) chr);
                }
                is.close();
                urlReturnString = sb.toString();
            } else {
//                urlReturnString = "HTTP_ERR" + respCode;
                urlReturnString = "{}";
            }
        } catch (IOException e) {
            urlReturnString = "{}";

            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                urlReturnString = "{}";

                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.jString.setJsonString(urlReturnString);
    }
}
