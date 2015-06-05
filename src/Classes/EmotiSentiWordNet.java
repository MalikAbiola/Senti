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

/**
 *
 * @author Doctormaliko
 */
public class EmotiSentiWordNet {

    private final String pathToESWN = "./Resources/SentiScores/emoticonsentiwordnet.txt";
    private final HashMap<String, String> emoticonWordSet;
    private final HashMap<String, HashMap<String, HashMap<String, Double>>> ESWNDictonary;

    public EmotiSentiWordNet() {
        this.emoticonWordSet = new HashMap<>();
        this.ESWNDictonary = new HashMap<>();
        System.out.println("Loading Emoti-sentiwordnet ....");
        load();
        System.out.println("Done Loading Emoti-SentiWordNet. :)");
    }

    public HashMap<String, String> getEmoticonWordSet() {
        return emoticonWordSet;
    }

    public HashMap<String, HashMap<String, HashMap<String, Double>>> getESWNDictonary() {
        return ESWNDictonary;
    }

    private void load() {
        BufferedReader csv = null;
        try {
            csv = new BufferedReader(new FileReader(pathToESWN));
            int lineNumber = 0;

            String line;
            while ((line = csv.readLine()) != null) {
                lineNumber++;

                // If it's a comment, skip this line.
                if (!line.trim().startsWith("#")) {
                    // We use tab separation
                    String[] data = line.split("\t");

                    String emoticonPOS = data[0];
                   
//                    System.out.println(lineNumber + ":\t" + line + " \tlen: " + data.length);

                    if (data.length != 6) {
                        throw new IllegalArgumentException("Incorrect tabulation format in file, line: " + lineNumber);
                    }
                    
                    emoticonWordSet.put(data[1], data[4]);

                    HashMap<String, Double> termScores = new HashMap<>(3);
                    termScores.put("pos", Double.parseDouble(data[2]));
                    termScores.put("neg", Double.parseDouble(data[3]));
                    termScores.put("obj", 1 - (Double.parseDouble(data[2]) + Double.parseDouble(data[3])));

                    HashMap<String, HashMap<String, Double>> tempEmoScores = new HashMap<>(1);
                    tempEmoScores.put(emoticonPOS, termScores);

                    ESWNDictonary.put(data[1], tempEmoScores);
                }
            }
        } catch (IOException | IllegalArgumentException e) {
        } finally {
            if (csv != null) {
                try {
                    csv.close();
                } catch (IOException ex) {
                    System.out.println("An Error Occured While Closing File");
                }
            }
        }
    }

    public String extract(String emoticon, String pos) {
        emoticon = emoticon.trim();
        pos = pos.trim();

        return "Emoticon: " + emoticon + "{pos: " + this.ESWNDictonary.get(emoticon).get(pos).get("pos")
                + ", neg: " + this.ESWNDictonary.get(emoticon).get(pos).get("neg") + ", obj: " + this.ESWNDictonary.get(emoticon).get(pos).get("obj") + "}";

    }

    public HashMap extract(String emoticon, String pos, boolean returnAsHashMap) {
        emoticon = emoticon.trim();
        pos = pos.trim();
        return this.ESWNDictonary.get(emoticon).get(pos);
    }

    public String extract(String emoticon) {
        emoticon = emoticon.trim();
        double posScore = 0.0;
        double negScore = 0.0;
        double objScore = 0.0;

        String[] poses = {"r", "a", "v", "n"};
        int count = 0;
        for (String pos : poses) {
            HashMap<String, Double> EPosScore = null;
            if ((EPosScore = this.ESWNDictonary.get(emoticon).get(pos)) != null) {
                posScore += EPosScore.get("pos");
                negScore += EPosScore.get("neg");
                objScore += EPosScore.get("obj");
                count++;
            }
        }
        return "Emoticon: " + emoticon + "{pos: " + posScore / count + ", neg: " + negScore / count + ", obj: " + objScore / count + "}";
    }

    public HashMap extract(String emoticon, boolean returnAsHashMap) {
        emoticon = emoticon.trim();
        double posScore = 0.0;
        double negScore = 0.0;
        double objScore = 0.0;

        String[] poses = {"r", "a", "v", "n"};
        int count = 0;
        for (String pos : poses) {
            HashMap<String, Double> EPosScore = null;
            if ((EPosScore = this.ESWNDictonary.get(emoticon).get(pos)) != null) {
                posScore += EPosScore.get("pos");
                negScore += EPosScore.get("neg");
                objScore += EPosScore.get("obj");
                count++;
            }
        }
        if (count > 0) {
            HashMap<String, Double> emoticonScore = new HashMap<>();
            emoticonScore.put("pos", posScore / count);
            emoticonScore.put("neg", negScore / count);
            emoticonScore.put("obj", objScore / count);
            return emoticonScore;
        } else {
            return null;
        }
    }

    public String find(String emoticon) {
        return this.emoticonWordSet.get(emoticon);
    }
}
