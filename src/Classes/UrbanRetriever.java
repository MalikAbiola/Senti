package Classes;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import gate.util.GateException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Doctormaliko
 */
public class UrbanRetriever {

    private String term;
    private final HashMap<String, String[]> termDetails;
    private HashMap<String, Double> termSentiment;
    private Initializer initializer;

    public UrbanRetriever() {
        this.termDetails = new HashMap<>();
    }

    public UrbanRetriever(String term) {
        this.termDetails = new HashMap<>();
        this.term = term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTerm() {
        return this.term;
    }

    public void setInitializer(Initializer initializer) {
        this.initializer = initializer;
    }

    public Initializer getInitializer() {
        return initializer;
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

    public HashMap getSentimentScores() {
        return this.termSentiment;
    }

    private String termPOSTag() throws GateException {
        String[] example;
        String POSTag = null;
        if ((example = this.termDetails.get("example")) != null) {
//            HashMap<String, String> tags = new HashMap<>();
//            if (this.initializer.appPOSTagger == null) {
//                this.initializer.initPOSTagger();
//            }
//            System.out.println("usage example: " + example[0]);
            if (!example[0].isEmpty()) {
                String[] wordsAndTags = this.initializer.appPOSTagger.tagString(example[0]).split("\t"); //modify with static pos tagger initiated when app starts
                for (String wordAndTag : wordsAndTags) {
//                System.out.println("current taggedwords being checked: " + wordAndTag);
                    if (wordAndTag.startsWith(term)) {
                        String[] wt = wordAndTag.split(":");
                        POSTag = wt[1];
                    }
                }
            }
        }
//        System.out.println("POS tag of tag: " + POSTag);
        return POSTag;
    }

    private void sentimentScore() throws GateException {
        String POSTag;
        if ((POSTag = termPOSTag()) != null) {
            String determinedTag;
            if (this.initializer.appSentiWordNet == null) {
                this.initializer.initSentiWordNet();
            }

            if (!(determinedTag = Utils.determinePOS(POSTag)).isEmpty()) {
//                System.out.println("Determined tag: " + determinedTag);
                String[] termTags = this.termDetails.get("tags");
                HashMap<String, Double> termTagScore = null;
                for (String termTag : termTags) {
//                    System.out.println("term tag being checked: " + termTag);
                    if ((termTagScore = this.initializer.appSentiWordNet.extract(termTag, determinedTag, true)) != null) {
//                        System.out.println(String.valueOf(termTagScore));
                        this.termSentiment = termTagScore;
                    } else {
                        this.termSentiment = this.initializer.appSentiWordNet.extract(term, true);
                    }
                }
            }
        }
    }

    public void getDetails() throws InterruptedException, GateException {
        JsonObject jsonObject = JsonObject.readFrom(this.getJsonString());
        if (!jsonObject.isEmpty()) {
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

                sentimentScore();
            }
        }
    }
}
