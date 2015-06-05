/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Interfaces.Constants;
import gate.util.GateException;
import java.util.Map;

/**
 *
 * @author Doctormaliko
 */
public class SarcasmDetector implements Constants {

    private String stringToCheck;
    private Map<String, String> sentenceTags;
    private String[] sentenceTokens;
    private String[] leftSide;
    private String[] rightSide;
    private final Initializer appInitializer;
//    private int sarcasmScore = 0;

    public SarcasmDetector(Initializer appInitializer) {
        this.appInitializer = appInitializer;
    }

    public SarcasmDetector(Initializer appInitializer, String stringToCheck, Map<String, String> sentenceTags) {
        this.appInitializer = appInitializer;
        this.stringToCheck = stringToCheck;
        this.sentenceTags = sentenceTags;
        splitSentence();
    }

    public SarcasmDetector setParams(String stringToCheck, Map<String, String> sentenceTags) {
        if (appInitializer != null) {
            this.stringToCheck = stringToCheck;
            this.sentenceTags = sentenceTags;
            splitSentence();
            return this;
        } else {
            throw new UnsupportedOperationException("Please Run App Initializer First");
        }
    }

    private void splitSentence() {
        sentenceTokens = stringToCheck.split("\\s+");
        leftSide = new String[sentenceTokens.length / 2];
        rightSide = new String[sentenceTokens.length - (sentenceTokens.length / 2)];

        int i = 0, j = leftSide.length, k = 0;
        for (String word : sentenceTokens) {
            if (i < leftSide.length && j >= 0) {
                leftSide[i] = word;
                j--;
            } else if (k < sentenceTokens.length) {
                rightSide[k] = word;
                k++;
            }
            i++;
        }
    }

    public boolean isSarcastic() throws InterruptedException, GateException {
        return polaritiesDiffers() || checkIndicators();
    }

    private void checkHashtags() {

    }

    private boolean polaritiesDiffers() throws InterruptedException, GateException {
        //left side
        double leftSideScore = 0;
        double rightSideScore = 0;
        int lopWordCount = 0;
        int ropWordCount = 0;

//        Utils.printArrayContent(this.leftSide);
//        System.out.println("-----------------------------------------------------------");
//        Utils.printArrayContent(this.rightSide);
//        System.out.println("-----------------------------------------------------------");
        for (String word : this.leftSide) {
            String tag = "";
            Double polarity = null;
            word = Utils.stripPunctuations(Utils.autoCorrect(word.trim()));
            if (word.length() > 0) {
                String wordTag = null;
                if ((wordTag = this.sentenceTags.get(word)) != null) {
                    tag = Utils.determinePOS(wordTag);
                }

                //check if word is an opinion word
                if (tag != null && tag.length() == 1) {
                    Map<String, Double> wordScore = appInitializer.appSentiWordNet.extract(word, tag, true);
                    polarity = (wordScore != null) ? Utils.determinePolarity(wordScore.get("pos"), wordScore.get("neg"), wordScore.get("obj")) : null;

                    //if polarity of word is null, use urban dictionary to fix it
                    if (polarity == null && ("r".equalsIgnoreCase(tag) || "a".equalsIgnoreCase(tag))) {
                        appInitializer.appUrbanRetriever.setTerm(word);
                        appInitializer.appUrbanRetriever.getDetails();
                        wordScore = appInitializer.appUrbanRetriever.getSentimentScores();
                        polarity = (wordScore != null) ? Utils.determinePolarity(wordScore.get("pos"), wordScore.get("neg"), wordScore.get("obj")) : null;
                    }
                    if ((polarity != null) && polarity != 0.0) {
                        lopWordCount++;
                        leftSideScore = (leftSideScore + polarity) / lopWordCount;
                    }
                }
            }

        }

        for (String word : this.rightSide) {
            String tag = "";
            Double polarity = null;
            word = Utils.stripPunctuations(Utils.autoCorrect(word.trim()));
            if (word.length() > 0) {
                String wordTag = null;
                if ((wordTag = this.sentenceTags.get(word)) != null) {
                    tag = Utils.determinePOS(wordTag);
                }

                //check if word is an opinion word
                if (tag != null && tag.length() == 1) {
                    Map<String, Double> wordScore = appInitializer.appSentiWordNet.extract(word, tag, true);
                    polarity = (wordScore != null) ? Utils.determinePolarity(wordScore.get("pos"), wordScore.get("neg"), wordScore.get("obj")) : null;

                    //if polarity of word is null, use urban dictionary to fix it
                    if (polarity == null && ("r".equalsIgnoreCase(tag) || "a".equalsIgnoreCase(tag))) {
                        appInitializer.appUrbanRetriever.setTerm(word);
                        appInitializer.appUrbanRetriever.getDetails();
                        wordScore = appInitializer.appUrbanRetriever.getSentimentScores();
                        polarity = (wordScore != null) ? Utils.determinePolarity(wordScore.get("pos"), wordScore.get("neg"), wordScore.get("obj")) : null;
                    }
                    if ((polarity != null) && polarity != 0.0) {
                        ropWordCount++;

                        rightSideScore = (rightSideScore + polarity) / ropWordCount;
                    }
                }
            }

        }
//        System.out.println("right side: " + rightSideScore);
//        System.out.println("left side: " + leftSideScore);

        if (rightSideScore != 0.0 && leftSideScore != 0.0) {
            return (-1 * rightSideScore == leftSideScore);
        } else {
            return false;
        }
    }

    private boolean checkIndicators() {
        int sarcasmScore = 0;
        for (String indicator : sarcasmIndicators) {
            if (stringToCheck.contains(indicator)) {
                sarcasmScore += 10;
                int indicatorPosition = stringToCheck.indexOf(indicator);
                if (indicatorPosition > -1 && indicatorPosition < 2) {
                    sarcasmScore += 10;
                } else if ((indicatorPosition + indicator.length()) == stringToCheck.length()) {
                    sarcasmScore += 10;
                } else if ((stringToCheck.length() - indicatorPosition) > 0) {
                    sarcasmScore += indicatorPosition - stringToCheck.length() - (indicatorPosition + indicator.length());
                }

            }

            if (stringToCheck.startsWith(indicator)) {
                sarcasmScore += 10;
            }
            if (stringToCheck.endsWith(indicator)) {
                sarcasmScore += 10;
            }
        }
        return sarcasmScore >= 30;
    }
}
