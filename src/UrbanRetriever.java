
import Classes.POSTagger;
import Classes.Utils;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import gate.util.GateException;
import java.io.IOException;
import java.util.HashMap;
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
public class UrbanRetriever {

    private final String term;
    private final HashMap<String, String[]> termDetails;

    public UrbanRetriever(String term) {
        this.termDetails = new HashMap<>();
        this.term = term;
    }

    private String getJsonString() throws InterruptedException {
        JsonString jsString = new JsonString();
        Thread t = new Thread(new UrbanAPIConnection(this.term, jsString));
        t.start();
        t.join();
        return jsString.getJsonString();
    }

    public String[] getTermTags() {
        return this.termDetails.get("tags");
    }

    public String[] getTermExample() {
        return this.termDetails.get("example");
    }

    public void getScore() {

    }

    public void getDetails() throws InterruptedException {
        JsonObject jsonObject = JsonObject.readFrom(this.getJsonString());

        if (jsonObject.get("result_type").asString().equalsIgnoreCase("exact")) {
            JsonArray jsonArray = jsonObject.get("tags").asArray();
            String termTags[] = new String[jsonArray.size()];
            int i = 0;
            for (JsonValue tag : jsonArray) {
                termTags[i++] = tag.asString();
            }

            String termExample[] = {jsonObject.get("list").asArray().get(0).asObject().get("example").asString()};

            this.termDetails.put("tags", termTags);
            this.termDetails.put("example", termExample);
        }
    }

    private String termTag() {
        String[] example;
        String tag = null;
        if ((example = this.termDetails.get("example")) != null) {
            try {
                HashMap<String, String> tags = new HashMap<>();
                String[] wordsAndTags = new POSTagger().tagString(example[0]).split("\t"); //modify with static pos tagger initiated when app starts
                for (String wordAndTag : wordsAndTags) {
                    if (wordAndTag.startsWith(term)) {
                        String[] wt = wordAndTag.split(":");
                        tag = wt[1];
                    }
                }
            } catch (IOException | GateException ex) {
                Logger.getLogger(UrbanRetriever.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return tag;
    }
    
    private void sentimentScore() {
        //calculate sentiment of term
        String tag;
        if((tag = termTag())!= null) {
            String determinedTag;
//            if(!(determinedTag = Utils.determinePOS(tag)).isEmpty()) {
//                new SentiWor
//            }
        }
    }
    
    public static void main(String[] args) {
        try {
            UrbanRetriever u = new UrbanRetriever("turnt");
            u.getDetails();
//            System.out.println(u.termDetails);
            Utils.printArrayContent(u.termDetails.get("tags"));
            Utils.printArrayContent(u.termDetails.get("example"));
        } catch (InterruptedException ex) {
            Logger.getLogger(UrbanRetriever.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
