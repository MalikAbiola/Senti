
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
public class SentiClass {

//    protected String pathToSWN = "./lib/SentiWordNet_3.0.0_20130122_mini.txt";
        private final String pathToSWN = "./Resources/SentiScores/SentiWordNet_3.0.0_20130122.txt";

    private final Map<String, HashMap<String, Double>> tempDic;
    private final Map<String, Integer> synsetCount;

    public SentiClass() {
        this.tempDic = new HashMap<>();
        this.synsetCount = new HashMap<>();
        System.out.println("Loading sentiwordnet ....");
        load();
        System.out.println("Done Loading SentiWordNet. :)");

    }

    private void load() {
        BufferedReader csv = null;
        try {
            csv = new BufferedReader(new FileReader(pathToSWN));
            int lineNumber = 0;

            String line;
            while ((line = csv.readLine()) != null) {
                lineNumber++;

                // If it's a comment, skip this line.
                if (!line.trim().startsWith("#")) {
                    // We use tab separation
                    String[] data = line.split("\t");
                    String wordTypeMarker = data[0];

                    if (data.length != 6) {
                        throw new IllegalArgumentException("Incorrect tabulation format in file, line: " + lineNumber);
                    }

                    // Calculate synset score as score = PosS - NegS
                    HashMap<String, Double> termScores = new HashMap<>();
                    termScores.put("pos", Double.parseDouble(data[2]));
                    termScores.put("neg", Double.parseDouble(data[3]));
                    termScores.put("obj", 1 - (Double.parseDouble(data[2]) + Double.parseDouble(data[3])));

                    // Get all Synset terms
                    String[] synTerms = data[4].split(" ");

//                    System.out.printf("line: %s,%s,%s,%s,%s\n", data[0],termScores.get("pos"),termScores.get("neg"),termScores.get("obj"),data[4]);
                    // Go through all terms of current synset.
                    for (String synTerm : synTerms) {
                        //remove sense no
                        String[] termAndSenseNo = synTerm.split("#");

                        //word to add to dictionary
                        String word = termAndSenseNo[0].trim() + "#" + wordTypeMarker.trim();

                        //put word and scores into temp dictionary
                        if (!tempDic.containsKey(word)) {
                            tempDic.put(word, termScores);
                            synsetCount.put(word, 1);

                        } else {
                            HashMap<String, Double> prevScores = tempDic.get(word);
                            double newPosScore = prevScores.get("pos") + Double.parseDouble(data[2]);
                            double newNegScore = prevScores.get("neg") + Double.parseDouble(data[3]);
                            double newObjScore = prevScores.get("obj") + 1 - (Double.parseDouble(data[2]) + Double.parseDouble(data[3]));

                            int prevCount = synsetCount.get(word);
                            HashMap<String, Double> totalScores = new HashMap<>();

                            totalScores.put("pos", newPosScore);
                            totalScores.put("neg", newNegScore);
                            totalScores.put("obj", newObjScore);

//                            System.out.printf("%s: %s, %s, %s, %s, %s, %s\n", word,tempDic.get(word).get("pos"),tempDic.get(word).get("neg"),tempDic.get(word).get("obj"),termScores.get("pos"),termScores.get("neg"),termScores.get("obj"));
//                            termScores.put("pos", (tempDic.get(word).get("pos") + termScores.get("pos")) / 2.0);
//                            termScores.put("neg", (tempDic.get(word).get("neg") + termScores.get("neg")) / 2.0);
//                            termScores.put("obj", (tempDic.get(word).get("obj") + termScores.get("obj")) / 2.0);
//                            termScores.put("pos", (tempDic.get(word).get("pos") + termScores.get("pos")));
//                            termScores.put("neg", (tempDic.get(word).get("neg") + termScores.get("neg")));
//                            termScores.put("obj", (tempDic.get(word).get("obj") + termScores.get("obj")));
                            tempDic.remove(word);
                            synsetCount.remove(word);
                            synsetCount.put(word, prevCount + 1);
                            tempDic.put(word, totalScores);

                        }
                    }

                }
            }
        } catch (IOException | IllegalArgumentException e) {
            Logger.getLogger(SentiClass.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (csv != null) {
                try {
                    csv.close();
                } catch (IOException ex) {
                    Logger.getLogger(SentiClass.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public String extract(String word, String pos) {
        String eWord = word.trim() + "#" + pos.trim();

        HashMap<String, Double> hashExtract = this.tempDic.get(eWord);
        if (hashExtract != null) {
            return "Word: " + eWord + "{ pos: " + hashExtract.get("pos") / synsetCount.get(eWord) + ", neg: " + hashExtract.get("neg") / synsetCount.get(eWord) + ", obj: " + hashExtract.get("obj") / synsetCount.get(eWord) + " }, Synset Count: " + synsetCount.get(eWord);
//            return "Word: " + eWord + "{ pos: " + hashExtract.get("pos") + ", neg: " + hashExtract.get("neg") + ", obj: " + hashExtract.get("obj") + " }, Synset Count: " + synsetCount.get(eWord);
        } else {
            return "Word: " + eWord + "{ pos: null, neg: null , obj: null }";
        }
    }

    public HashMap extract(String word, String pos, boolean returnHashMap) {
        String eWord = word.trim() + "#" + pos.trim();
        HashMap<String, Double> termScores = this.tempDic.get(eWord);
        if (returnHashMap) {

            if (termScores != null) {
                HashMap<String, Double> extractedScores = new HashMap<>();

                extractedScores.put("pos", termScores.get("pos") / synsetCount.get(eWord));
                extractedScores.put("neg", termScores.get("neg") / synsetCount.get(eWord));
                extractedScores.put("obj", termScores.get("obj") / synsetCount.get(eWord));
                return extractedScores;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public boolean exists(String word) {
        return this.tempDic.containsKey(word);
    }

    public static void main(String[] args) {
        SentiClass sentiClass = new SentiClass();
        System.out.println(sentiClass.extract("interest", "n"));
        System.out.println(sentiClass.extract("interest", "a"));
        System.out.println(sentiClass.extract("interest", "r"));
        System.out.println(sentiClass.extract("interest", "v"));
//        System.out.println(sentiClass.extract("very", "n"));
//        System.out.println(sentiClass.extract("very", "a"));
//        System.out.println(sentiClass.extract("very", "r"));
        System.out.println(sentiClass.extract("fragile", "a"));
        System.out.println(sentiClass.extract("fragile", "r"));
        System.out.println(sentiClass.extract("fragile", "n"));
        System.out.println(sentiClass.extract("sex", "v"));
//        System.out.println(sentiClass.extract("phone", "n"));
//        System.out.println(sentiClass.extract("phone", "n"));
//        System.out.println(sentiClass.extract("phone", "n"));
    }
}
