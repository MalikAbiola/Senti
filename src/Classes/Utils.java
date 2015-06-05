/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import simplenlg.features.Feature;
import simplenlg.features.Tense;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.XMLLexicon;
import simplenlg.realiser.english.Realiser;

/**
 *
 * @author Doctormaliko
 */
public class Utils {

    public static String determinePOS(String category) {
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
            case "VBD":
                return "VBD";
            case "VBG":
                return "v";
            case "VBN":
                return "VBN";
            case "VBP":
                return "v";
            case "VB":
                return "v";
            case "VBZ":
                return "v";
            case "WDT":
                return "IMW";
            case "WP$":
                return "IMW";
            case "WP":
                return "IMW";
            case "WRB":
                return "IMW";
            case ",":
                return "COMMA";
            case ".":
                return "FULLSTOP";
            default:
                return category;
        }
    }

    public static boolean isInterogative(String tweet, Map taggedTweet) {
        String[] words = tweet.split("\\s* ");
        String tag = "";
        int wordCount = 0;
        boolean startsWithModelWord = false;
        boolean auxiliaryVerbPresent = false;
        boolean questionPresent = false;

        for (String word : words) {
            word = solveNTIssue(word);

            if (word.length() > 0) {
                String[] w = null;
                if ((w = word.split("\\s* ")).length > 0) {
                    for (String ww : w) {
                        String wordTag = null;
                        if ((wordTag = String.valueOf(taggedTweet.get(ww))) != null) {
                            tag = Utils.determinePOS(wordTag);
                        }

                        //check if word is an opinion word
                        if (tag != null && tag.equalsIgnoreCase("IMW") && wordCount < 1) {
                            startsWithModelWord = true;
                        }

                        if (tag != null && startsWithModelWord && (tag.equalsIgnoreCase("v") | tag.equalsIgnoreCase("v")) && wordCount >= 1 && wordCount < 3) {
                            auxiliaryVerbPresent = true;
                        }

                        if (tag != null && (tag.equalsIgnoreCase("v") | tag.equalsIgnoreCase("v")) && wordCount < 1) {
                            auxiliaryVerbPresent = true;
                        }

                        questionPresent = word.endsWith("?");

                        wordCount++;
                    }
                }
            }
        }
        return (startsWithModelWord && auxiliaryVerbPresent && questionPresent) || ((startsWithModelWord && auxiliaryVerbPresent) || questionPresent)
                || (auxiliaryVerbPresent && questionPresent);
    }

    public static double determinePolarity(double a, double b, double c) {
        double max = 0.0;
//        if (a >= b && a >= c) {
//            max = 1.0;
//        } else if (b >= a && b >= c) {
//            max = -1.0;
//        } else if (c >= b && c >= a) {
//            max = 0.0;
//        }
        if (a >= b && a > 0.09) {
            max = 1.0;
        } else if (b > a && b > 0.09) {
            max = -1.0;
        }
        return max;
    }

    public static String autoCorrect(String string) {
        if (string.equalsIgnoreCase("n't")) {
            return "not";
        } else if (string.equalsIgnoreCase("'s")) {
            return "is";
        } else {
            return string;
        }
    }

    public static String solveNTIssue(String string) {
        return string.endsWith("n't") ? string.substring(0, string.length() - 2) + " n't" : string;
    }

    public static boolean isNegation(String string) {
        boolean isNegation = false;
        for (String negation : Initializer.negations) {
            if (string.equalsIgnoreCase(negation)) {
                isNegation = true;
                break;
            }
        }
        return isNegation;
    }

    public static boolean isStopWord(String string) {
        boolean isStopWord = false;
        for (String stopWord : Initializer.stopWords) {
            if (string.equalsIgnoreCase(stopWord)) {
                isStopWord = true;
                break;
            }
        }
        return isStopWord;
    }

    public static String normalise(String text, int allowedRepition) {
        if (allowedRepition < 1) {
            throw new UnsupportedOperationException("Allowed repition can't be less than 1");
        }

        String normalisedText = "";
        String[] textTokens = text.split(" ");

        for (String token : textTokens) {
            String outt = "";

            if (token.length() > allowedRepition) {
                for (int i = 0; i < token.length(); i++) {
                    int iAllowedRepition = allowedRepition;
                    int count = 1;
                    Character c = token.charAt(i);

                    int nextCharPos = i + 1;
                    if (((i - 1) < 0) || !c.equals(token.charAt(i - 1))) {
                        while ((nextCharPos < token.length()) && c.equals(token.charAt(nextCharPos))) {
                            count++;
                            nextCharPos++;
                        }
                        if (count >= iAllowedRepition) {
                            if (c == '.' || c == ',' | c == '!' | c == '?' | c == '>' | c == '<') {
                                iAllowedRepition = 1;
                            }
                            for (int j = 0; j < iAllowedRepition; j++) {
                                outt += String.valueOf(c);
                            }
                        } else {
                            outt += c;
                        }
                    }
                }

                normalisedText += outt + " ";

            } else {
                normalisedText += token + " ";

            }
        }
        return normalisedText;
    }

    public static String stripPunctuations(String string) {
        string = string.replace("#", "");
        if (string.endsWith(",") || string.endsWith(".") || string.endsWith("!") || string.endsWith("?") || string.endsWith(";")) {
            return string.substring(0, string.length() - 1);
        } else {
            return string;
        }
    }

    public static String[] sentencesToArray(String sentences) throws IOException {
        BufferedReader csv = null;
        ArrayList<String> sentencesList = new ArrayList<>();
        csv = new BufferedReader(new CharArrayReader(sentences.toCharArray()));// new FileReader(pathToESWN));
        String line;
        while ((line = csv.readLine()) != null) {
            sentencesList.add(line);
        }
        return sentencesList.toArray(new String[sentencesList.size()]);
    }

    public static void printMapContent(Map map) {
        Iterator<Map.Entry> i = map.entrySet().iterator();
        int c = 0;
        while (i.hasNext()) {
            Object key = i.next().getKey();
            System.out.println("SN: " + ++c + "; \t " + key + " ## " + map.get(key));
        }
    }

    public static void printArrayContent(String[] array) {
        for (String s : array) {
            System.out.println(s);
        }
    }

    public static int max(double... args) {
        double max = 0;
        int maxIndex = 0;
        int i = 0;
        for (double a : args) {
            if (a > max) {
                max = a;
                maxIndex = i;
            }
            i++;
        }
        return maxIndex;
    }
    
    public static String[] readLines(URL url) throws IOException {

        Reader fileReader = new InputStreamReader(url.openStream(), Charset.forName("UTF-8"));
        List<String> lines;
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            lines = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines.toArray(new String[lines.size()]);
    }
    
    public static String flipWord(String wordToFlip) {
        XMLLexicon lexicon = new XMLLexicon("Resources\\simpleNLG\\default-lexicon.xml");
        WordElement word = lexicon.getWordFromVariant(wordToFlip, LexicalCategory.VERB);
        InflectedWordElement infl = new InflectedWordElement(word);
        infl.setFeature(Feature.TENSE, Tense.FUTURE);
        return  new Realiser(lexicon).realise(infl).getRealisation();
    }
}

