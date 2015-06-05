
import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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
                urlReturnString = "HTTP_ERR" + respCode;
            }
        } catch (IOException e) {
            urlReturnString = "IO_ERR";
            try {
                is.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        this.jString.setJsonString(urlReturnString);

//    }
//    public static void main(String[] args) {
//        Future<HttpResponse<JsonNode>> future = Unirest.get("http://api.urbandictionary.com/v0s/define")
//                .field("term", "turnt")
//                .header("accept", "application/json")
//                .asJsonAsync(new Callback<JsonNode>() {
//            @Override
//            public void failed(UnirestException e) {
//                System.out.println("The request has failed");
//                }
//
//            @Override
//            public void completed(HttpResponse<JsonNode> response) {
//                try {
//                    int code = response.getCode();
//                    Headers headers = response.getHeaders();
//                    JsonNode body = response.getBody();
//                    InputStream rawBody = response.getRawBody();
//                    System.out.println("Respn: " + code);
//                    System.out.println(IOUtils.toString(response.getRawBody()));
//                } catch (IOException ex) {
//                    Logger.getLogger(UrbanAPIConnection.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//
//            @Override
//            public void cancelled() {
//                System.out.println("The request has been cancelled");
//            }
//
//        });
//    }
    }
}
