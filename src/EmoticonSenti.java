
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;
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
public class EmoticonSenti {

    private final SentiClass sentiwordnet;

    public EmoticonSenti() {
        sentiwordnet = new SentiClass();

    }

    public String loadEmoticonSet() throws FileNotFoundException {
        String emoticonSet = "";
        Scanner scanner = new Scanner(new File("emoticon set.csv"));
        while (scanner.hasNextLine()) {
//            emoticonSet += scanner.nextLine() + "\n";
            String[] tokens = scanner.nextLine().split(",");
            emoticonSet += tokens[1] + "\n";

        }
        return emoticonSet;
    }

    public String loadFile(String filePath) throws FileNotFoundException {
        String fileAsText = "";
        Scanner scanner = new Scanner(new File("testtweets.txt"));
        while (scanner.hasNextLine()) {
            fileAsText += scanner.nextLine() + "\n";
        }
        return fileAsText;
    }

    public String tagEmoticons() throws FileNotFoundException {
        return new GateDEMO().TagStrings(loadEmoticonSet());
    }

    public String tagEmoticons(String stringToTag) throws FileNotFoundException {
        return new GateDEMO().TagStrings(stringToTag);
    }

    public void getSentiValue(String taggedStrings) throws IOException {
        int posV = 0;
        int negV = 0;
        int objV = 0;
        int sentenceCount = 0;

        StringTokenizer stringTokenizer = new StringTokenizer(taggedStrings, "\n");

//        SentiWordNetDemoCode sentiwordnet = new SentiWordNetDemoCode("./lib/SentiWordNet_3.0.0_20130122.txt");
        while (stringTokenizer.hasMoreTokens()) {
            String currentString = stringTokenizer.nextToken();
            String[] tokens = currentString.split("\t");
            double count = 0.0;
            double posS = 0.0;
            double negS = 0.0;
            double objS = 0.0;

            for (String token : tokens) {
                String[] stringAndTag = token.split(":");
                String pos = determinePOS(stringAndTag[1].trim());
                if (pos.length() > 0) {
                    HashMap<String, Double> sentiScores = sentiwordnet.extract(autoCorrect(stringAndTag[0]).toLowerCase().trim(), pos, true);
                    if (sentiScores != null) {
                        if (sentiScores.get("pos") > 0 || sentiScores.get("neg") > 0 || sentiScores.get("obj") > 0) {
                            count++;
                            posS += sentiScores.get("pos");
                            negS += sentiScores.get("neg");
                            objS += sentiScores.get("obj");
                        }
                    }

                }

            }

            if (calcMax(posS / count, negS / count, objS / count) == 1.0) {
                posV++;
            } else if (calcMax(posS / count, negS / count, objS / count) == -1.0) {
                negV++;
            } else if (calcMax(posS / count, negS / count, objS / count) == 0) {
                objV++;
            }
            sentenceCount++;

//            System.out.printf("Sentiment Set for %s: {pos: %s, neg: %s, obj: %s} %s\n", currentString, String.valueOf(posS), String.valueOf(negS), String.valueOf(objS), String.valueOf(count));
            System.out.printf("Sentiment Set for %s: {pos: %s, neg: %s, obj: %s} %s\n", currentString, String.valueOf(posS / count), String.valueOf(negS / count), String.valueOf(objS / count), String.valueOf(count));
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("Sentiment Set: {pos: %s, neg: %s, obj: %s} %s\n", String.valueOf((posV * 100) / sentenceCount), String.valueOf((negV * 100) / sentenceCount), String.valueOf((objV * 100) / sentenceCount), String.valueOf(sentenceCount));

    }

    private String determinePOS(String category) {
        switch (category.trim()) {
            case "JJ":
                return "a";
            case "JJR":
                return "a";
            case "JJS":
                return "a";
            case "JJSS":
                return "a";
            case "NN":
                return "n";
            case "NNP":
                return "n";
            case "NNPS":
                return "n";
            case "NNS":
                return "n";
            case "NP":
                return "n";
            case "NPS":
                return "n";
            case "POS":
                return "n";
            case "PP":
                return "n";
            case "RB":
                return "r";
            case "RBR":
                return "r";
            case "RBS":
                return "r";
            case "RP":
                return "r";
            case "WRB":
                return "r";
            case "VBD":
                return "v";
            case "VBG":
                return "v";
            case "VBN":
                return "v";
            case "VBP":
                return "v";
            case "VB":
                return "v";
            case "VBZ":
                return "v";
            default:
                return "";
        }
    }

    private static double calcMax(double a, double b, double c) {
        double max = 0.0;
        if (a >= b && a >= c) {
            max = 1.0;
        } else if (b >= a && b >= c) {
            max = -1.0;
        } else if (c >= b && c >= a) {
            max = 0.0;
        }
        return max;
    }

    private String autoCorrect(String string) {
        if (string.equalsIgnoreCase("n't")) {
            return "not";
        } else {
            return string;
        }
    }

    public static void main(String[] args) {
        try {
            EmoticonSenti e = new EmoticonSenti();
            String tagged = e.tagEmoticons(e.loadFile("testtweets.txt"));
//            System.out.println(tagged);
            System.out.println("________________________________________________________________________________________");
            e.getSentiValue(tagged);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EmoticonSenti.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EmoticonSenti.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
