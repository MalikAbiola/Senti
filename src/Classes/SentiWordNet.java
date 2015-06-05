/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Doctormaliko
 */
public class SentiWordNet {

    private final String pathToSWN = "./Resources/SentiScores/SentiWordNet_3.0.0_20130122.txt";
    private final Map<String, HashMap<String, Double>> tempDic;
    private final Map<String, Integer> synsetCount;

    public SentiWordNet() {
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

                            tempDic.remove(word);
                            synsetCount.remove(word);
                            synsetCount.put(word, prevCount + 1);
                            tempDic.put(word, totalScores);

                        }
                    }

                }
            }
        } catch (IOException | IllegalArgumentException e) {
        } finally {
            if (csv != null) {
                try {
                    csv.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    public String extract(String word, String pos) {
        String eWord = word.trim() + "#" + pos.trim();

        HashMap<String, Double> hashExtract = this.tempDic.get(eWord);
        if (hashExtract != null) {
            return "Word: " + eWord + "{ pos: " + hashExtract.get("pos") / synsetCount.get(eWord) + ", neg: " + hashExtract.get("neg") / synsetCount.get(eWord) + ", obj: " + hashExtract.get("obj") / synsetCount.get(eWord) + " }, Synset Count: " + synsetCount.get(eWord);
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

    public String extract(String word) {
        String eWordNoun = word.trim() + "#n";
        String eWordVerb = word.trim() + "#v";
        String eWordAdverb = word.trim() + "#r";
        String eWordAdject = word.trim() + "#a";

        double positive = 0.0;
        double negative = 0.0;
        double objective = 0.0;

        int count = 0;

        HashMap<String, Double> nounHashExtract, verbHashExtract, adverbHashExtract, adjectHashExtract;

        if ((nounHashExtract = this.tempDic.get(eWordNoun)) != null) {
            positive += nounHashExtract.get("pos") / synsetCount.get(eWordNoun);
            negative += nounHashExtract.get("neg") / synsetCount.get(eWordNoun);
            objective += nounHashExtract.get("obj") / synsetCount.get(eWordNoun);
            count++;
        }
        if ((verbHashExtract = this.tempDic.get(eWordVerb)) != null) {
            positive += verbHashExtract.get("pos") / synsetCount.get(eWordVerb);
            negative += verbHashExtract.get("neg") / synsetCount.get(eWordVerb);
            objective += verbHashExtract.get("obj") / synsetCount.get(eWordVerb);
            count++;
        }
        if ((adverbHashExtract = this.tempDic.get(eWordAdverb)) != null) {
            positive += adverbHashExtract.get("pos") / synsetCount.get(eWordAdverb);
            negative += adverbHashExtract.get("neg") / synsetCount.get(eWordAdverb);
            objective += adverbHashExtract.get("obj") / synsetCount.get(eWordAdverb);
            count++;
        }
        if ((adjectHashExtract = this.tempDic.get(eWordAdject)) != null) {
            positive += adjectHashExtract.get("pos") / synsetCount.get(eWordAdject);
            negative += adjectHashExtract.get("neg") / synsetCount.get(eWordAdject);
            objective += adjectHashExtract.get("obj") / synsetCount.get(eWordAdject);
            count++;
        }

        if (count > 0) {
            return "Word: " + word + "{ pos: " + positive / count + ", neg: " + negative / count + ", obj: " + objective / count;
        } else {
            return "Word: " + word + "{ pos: null, neg: null , obj: null }";
        }
    }

    public HashMap extract(String word, boolean returnHashMap) {
        String eWordNoun = word.trim() + "#n";
        String eWordVerb = word.trim() + "#v";
        String eWordAdverb = word.trim() + "#r";
        String eWordAdject = word.trim() + "#a";

        double positive = 0.0;
        double negative = 0.0;
        double objective = 0.0;

        int count = 0;

        HashMap<String, Double> nounHashExtract, verbHashExtract, adverbHashExtract, adjectHashExtract;

        if ((nounHashExtract = this.tempDic.get(eWordNoun)) != null) {
            positive += nounHashExtract.get("pos") / synsetCount.get(eWordNoun);
            negative += nounHashExtract.get("neg") / synsetCount.get(eWordNoun);
            objective += nounHashExtract.get("obj") / synsetCount.get(eWordNoun);
            count++;
        }
        if ((verbHashExtract = this.tempDic.get(eWordVerb)) != null) {
            positive += verbHashExtract.get("pos") / synsetCount.get(eWordVerb);
            negative += verbHashExtract.get("neg") / synsetCount.get(eWordVerb);
            objective += verbHashExtract.get("obj") / synsetCount.get(eWordVerb);
            count++;
        }
        if ((adverbHashExtract = this.tempDic.get(eWordAdverb)) != null) {
            positive += adverbHashExtract.get("pos") / synsetCount.get(eWordAdverb);
            negative += adverbHashExtract.get("neg") / synsetCount.get(eWordAdverb);
            objective += adverbHashExtract.get("obj") / synsetCount.get(eWordAdverb);
            count++;
        }
        if ((adjectHashExtract = this.tempDic.get(eWordAdject)) != null) {
            positive += adjectHashExtract.get("pos") / synsetCount.get(eWordAdject);
            negative += adjectHashExtract.get("neg") / synsetCount.get(eWordAdject);
            objective += adjectHashExtract.get("obj") / synsetCount.get(eWordAdject);
            count++;
        }

        if (count > 0) {
            HashMap<String, Double> extractedScores = new HashMap<>();

            extractedScores.put("pos", positive / count);
            extractedScores.put("neg", negative / count);
            extractedScores.put("obj", objective / count);
            return extractedScores;
        } else {
            return null;
        }
    }

    public boolean exists(String word) {
        String eWordNoun = word.trim() + "#n";
        String eWordVerb = word.trim() + "#v";
        String eWordAdverb = word.trim() + "#r";
        String eWordAdject = word.trim() + "#a";
        
        return this.tempDic.containsKey(eWordAdject) || this.tempDic.containsKey(eWordVerb)|| this.tempDic.containsKey(eWordAdverb)|| this.tempDic.containsKey(eWordNoun);
    }
}
