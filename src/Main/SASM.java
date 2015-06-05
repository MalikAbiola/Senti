/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Classes.Initializer;
import Classes.Utils;
import gate.util.GateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Doctormaliko
 */
public class SASM {

    private final Initializer appInitializer;

    public SASM() {
        appInitializer = new Initializer();

    }

    public SASM(Initializer appInitializer) {
        this.appInitializer = appInitializer;
    }

    public double calculateTweetScore(String taggedTweet) {
        Double sentencePosScore = 0.0;
        Double sentenceNegScore = 0.0;
        Double sentenceObjScore = 0.0;

        String[] wordsAndTag = taggedTweet.split("\t");

        boolean flipNext = false;
        boolean concentrateNext = false;
        boolean dialateNext = false;
        int i = 0;
        int totalWordTagCount = wordsAndTag.length;
        int count = 0;

        for (String wordAndTag : wordsAndTag) {
            String[] breakdown = wordAndTag.split(":");
            String word = Utils.autoCorrect(breakdown[0].trim());
            String tag = Utils.determinePOS(breakdown[1].trim());

            //check if word is a but so that the values can be reset thus effective ignoring weverything that has happened before the but
            if (word.equalsIgnoreCase("but") | word.equalsIgnoreCase("bt")) {
                flipNext = false;
                concentrateNext = false;
                dialateNext = false;
                i = 0;
                count = 0;
            }

            //check if word is a negation
            if (Utils.isNegation(word)) {
                flipNext = true;
                continue;
            }

            //check if word is an opinion word
            if (tag.length() == 1) {
                //check if word is an adverb
                if (tag.equals(Initializer.CAT_ADVERB)) {
                    //check if next word is an adjective or verb
                    if (((i + 1) < totalWordTagCount) && (wordsAndTag[(i + 1)].contains(":a") || wordsAndTag[(i + 1)].contains(":v"))) {
                        Map<String, Double> wordScore = appInitializer.appSentiWordNet.extract(word, tag, true);
                        if (wordScore != null) {
                            Double max = Utils.determinePolarity(wordScore.get("pos"), wordScore.get("neg"), wordScore.get("obj"));
                            if (max > 0.0) {
                                concentrateNext = true;
                            } else if (max < 0.0) {
                                dialateNext = true;
                            }
                        } else {
                            continue;
                        }
                    }
                }

                double wordPosScore = 0.0;
                double wordNegScore = 0.0;
                double wordObjScore = 0.0;

                //get word sentiment
                Map<String, Double> wordScore = appInitializer.appSentiWordNet.extract(word, tag, true);

                if (wordScore != null) {
                    if (dialateNext) {
                        wordPosScore = 1 - Math.pow((1 - wordScore.get("pos")), 2.0);
                        wordNegScore = 1 - Math.pow((1 - wordScore.get("neg")), 2.0);
                        wordObjScore = 1 - Math.pow((1 - wordScore.get("obj")), 2.0);
                    } else if (concentrateNext) {
                        wordPosScore = 1 - Math.pow((1 - wordScore.get("pos")), 0.5);
                        wordNegScore = 1 - Math.pow((1 - wordScore.get("neg")), 0.5);
                        wordObjScore = 1 - Math.pow((1 - wordScore.get("obj")), 0.5);
                    } else {
                        wordPosScore = wordScore.get("pos");
                        wordNegScore = wordScore.get("neg");
                        wordObjScore = wordScore.get("obj");
                    }
                    concentrateNext = false;
                    dialateNext = false;
                } else {
                    //use urban api to get value
                }
                System.out.printf("Word Set for %s: {pos: %s, neg: %s, obj: %s}\n", word + "#" + tag, String.valueOf(wordPosScore), String.valueOf(wordNegScore), String.valueOf(wordObjScore));
                if (flipNext && ("r".equals(tag) || "a".equals(tag) || "v".equals(tag))) {
                    sentencePosScore += wordNegScore;
                    sentenceNegScore += wordPosScore;
                    sentenceObjScore += wordObjScore;
                    flipNext = false;
                    count++;
                } else {
                    sentencePosScore += wordPosScore;
                    sentenceNegScore += wordNegScore;
                    sentenceObjScore += wordObjScore;
                    count++;
                }
                System.out.printf("Sentence Set for %s: {pos: %s, neg: %s, obj: %s} %s\n", taggedTweet, String.valueOf(sentencePosScore), String.valueOf(sentenceNegScore), String.valueOf(sentenceObjScore), String.valueOf(count));

            }
        }
        return Utils.determinePolarity(sentencePosScore / totalWordTagCount, sentenceNegScore / totalWordTagCount, sentenceObjScore / totalWordTagCount);
    }

    public double calculateTweetScore(String tweet, Map<String, String> taggedTweet) throws InterruptedException, GateException {
        double sentencePosScore = 0.0;
        double sentenceNegScore = 0.0;
        double sentenceObjScore = 0.0;

        String[] words = tweet.split("\\W* ");

        boolean flipNext = false;
        boolean concentrateNext = false;
        boolean dilateNext = false;
        int i = 0;
        int totalWordCount = words.length;
        int count = 0;
        int nextToFuzzify = -1;
        int nextToFlip = -1;

        for (String word : words) {
//            String[] breakdown = wordAndTag.split(":");'
//            System.out.println("sdfdfdsf " + word );
            String tag = null;
            word = Utils.stripPunctuations(Utils.autoCorrect(word.trim()));

            //check if word is a but so that the values can be reset thus effective ignoring weverything that has happened before the but
            if (word.equalsIgnoreCase("but") | word.equalsIgnoreCase("bt")) {
                flipNext = false;
                concentrateNext = false;
                dilateNext = false;
//                i = 0;
                count = 0;
            }

            //check if word is a negation
            if (Utils.isNegation(word)) {
                flipNext = (flipNext == false);
                continue;
            }

//            word = Utils.stripPunctuations(word);
            if (word.length() > 0) {
                String wordTag = null;
                if ((wordTag = taggedTweet.get(word)) != null) {
                    tag = Utils.determinePOS(wordTag);
                    if ("VBN".equals(tag) || tag.equals("VBD")) {
                        tag = "v";
                        word = Utils.flipWord(word);
                    }
                }

                //check if word is an opinion word
                if (tag != null && tag.length() == 1) {
                    //calculate word score
                    Map<String, Double> wordScore = appInitializer.appSentiWordNet.extract(word, tag, true);
                    Double polarity = (wordScore != null) ? Utils.determinePolarity(wordScore.get("pos"), wordScore.get("neg"), wordScore.get("obj")) : null;

                    //if polarity of word is null, use urban dictionary to fix it
                    if (polarity == null) {
                        appInitializer.appUrbanRetriever.setTerm(word);
                        appInitializer.appUrbanRetriever.getDetails();
                        wordScore = appInitializer.appUrbanRetriever.getSentimentScores();
                        polarity = (wordScore != null) ? Utils.determinePolarity(wordScore.get("pos"), wordScore.get("neg"), wordScore.get("obj")) : null;
                    }
                    //check if word is an adverb and is not the last word
                    if (tag.equals(Initializer.CAT_ADVERB) && i < totalWordCount - 1 && polarity != null) {
                        //while there is a next word that is an adverb, verb, or adjective
                        int currentWordLocation = i;
                        while (currentWordLocation < totalWordCount - 1) {
                            String nextWordInQ = Utils.stripPunctuations(Utils.autoCorrect(words[(currentWordLocation + 1)]));
                            String nextWordInQTag = null;
                            if (!Utils.isNegation(nextWordInQ) && (nextWordInQTag = taggedTweet.get(nextWordInQ)) != null) {
                                //check if word is an adjective or a verb
                                if ((nextWordInQTag.equals(Initializer.CAT_ADJECTIVE) || nextWordInQTag.equals(Initializer.CAT_VERB))) {
                                    if (polarity > 0.0) {
                                        concentrateNext = true;
                                    } else if (polarity < 0.0) {
                                        dilateNext = true;
                                    }
                                    nextToFuzzify = currentWordLocation;
                                    break;
                                }
                            }
                            currentWordLocation++;
                        }
                        if (dilateNext || concentrateNext) {
                            continue;
                        }
                    }

                    //if flipnext and this word is noot an adjective 
                    if (flipNext) {
                        //if this word is an adjective or verb, flip it
                        if ((tag.equals(Initializer.CAT_ADJECTIVE) || tag.equals(Initializer.CAT_VERB))) {
                            nextToFlip = i;
                            flipNext = false;

                        } else {
                            //is this the last word on the queue
                            if (i < totalWordCount - 1) {
                                nextToFlip = i;
                                flipNext = false;

                            } else { //find if the next words on the queue are verbs or adjectives
                                int currentWordLocation = i;
                                while (currentWordLocation < totalWordCount - 1) {
                                    String nextWordInQ = Utils.stripPunctuations(Utils.autoCorrect(words[(currentWordLocation + 1)]));
                                    String nextWordInQTag = null;
                                    if (!Utils.isNegation(nextWordInQ) && (nextWordInQTag = taggedTweet.get(nextWordInQ)) != null) {
                                        //check if word is an adjective or a verb
                                        if ((nextWordInQTag.equals(Initializer.CAT_ADJECTIVE) || nextWordInQTag.equals(Initializer.CAT_VERB))) {
                                            nextToFlip = currentWordLocation + 1;
                                            flipNext = false;
                                            break;
                                        }
                                    }
                                    currentWordLocation++;
                                }
                            }
                        }
                    }

                    double wordPosScore = 0.0;
                    double wordNegScore = 0.0;
                    double wordObjScore = 0.0;

                    //check if word is valid 
                    if (wordScore != null) {
                        //check if to upgrade or degrade score based on the previously encountered adverb
                        if (i == nextToFuzzify) {
                            if (concentrateNext) {
                                wordPosScore = 1 - Math.pow((1 - wordScore.get("pos")), 2.0);
                                wordNegScore = 1 - Math.pow((1 - wordScore.get("neg")), 2.0);
                                wordObjScore = 1 - Math.pow((1 - wordScore.get("obj")), 2.0);
                            } else if (dilateNext) {
                                wordPosScore = 1 - Math.pow((1 - wordScore.get("pos")), 0.5);
                                wordNegScore = 1 - Math.pow((1 - wordScore.get("neg")), 0.5);
                                wordObjScore = 1 - Math.pow((1 - wordScore.get("obj")), 0.5);
                            }
                            concentrateNext = false;
                            dilateNext = false;
                        } else {
                            wordPosScore = wordScore.get("pos");
                            wordNegScore = wordScore.get("neg");
                            wordObjScore = wordScore.get("obj");
                        }

                        //check what to flip
                        if (i == nextToFlip) {
                            sentencePosScore += wordNegScore;
                            sentenceNegScore += wordPosScore;
                            sentenceObjScore += wordObjScore;
                            flipNext = false;
                            nextToFlip = -1;
                        } else if (flipNext && nextToFlip == -1 && i == totalWordCount - 1) {
                            sentencePosScore = sentenceNegScore;
                            sentenceNegScore = sentencePosScore;
                            flipNext = false;
                            nextToFlip = -1;
                        } else {
                            sentencePosScore += wordPosScore;
                            sentenceNegScore += wordNegScore;
                            sentenceObjScore += wordObjScore;
                        }
                        //if is noun and just basically objective i.e. pos score is 0, and neg score is 0

                        if (!(tag.equals(Initializer.CAT_NOUN) && wordPosScore == 0.0 && wordNegScore == 0.0)) {
                            count++;
                        }
                    }
                    System.out.printf("Word Set for %s: {pos: %s, neg: %s, obj: %s}\n", word + "#" + tag, String.valueOf(wordPosScore), String.valueOf(wordNegScore), String.valueOf(wordObjScore));

//                    System.out.printf("Sentence Set for %s: {pos: %s, neg: %s, obj: %s} %s\n", taggedTweet, String.valueOf(sentencePosScore), String.valueOf(sentenceNegScore), String.valueOf(sentenceObjScore), String.valueOf(count));
                }
            }
            i++;
        }

        return flipNext ? Utils.determinePolarity(sentenceNegScore / count, sentencePosScore / count, sentenceObjScore / count)
                : Utils.determinePolarity(sentencePosScore / count, sentenceNegScore / count, sentenceObjScore / count);
    }

    public Map detectEmoticon(String tweet) throws GateException {
        Map<String, String> emoticonAndPOS = new HashMap<>();
        boolean emoticonExists = false;
        String[] reconstructedTokens = null;
        ArrayList<String> reconstructedArrayList = new ArrayList<>();

        String tweetTokens[] = tweet.split(" ");
        String reconstructed = "";
        int i = 0;
        ArrayList<Integer> emoticonPositions = new ArrayList<>();

        for (String tweetToken : tweetTokens) {
            String emoticonInWord = null;
            if ((emoticonInWord = appInitializer.appEmotiSentiWordNet.find(tweetToken)) != null) {
                emoticonPositions.add(i);
                emoticonExists = true;
            }
            String wordToAdd = (emoticonInWord != null) ? emoticonInWord + " " : tweetToken + " ";
            reconstructed += wordToAdd;
            reconstructedArrayList.add(wordToAdd.trim());
            i++;
        }

        if (emoticonExists) {
            reconstructedTokens = reconstructedArrayList.toArray(new String[reconstructedArrayList.size()]);

            Map<String, String> wordsAndTags = appInitializer.appPOSTagger.tagString(reconstructed, true);
            int emoticonPositionsLength = emoticonPositions.size();

            for (int j = 0; j < emoticonPositionsLength; j++) {
                emoticonAndPOS.put(tweetTokens[emoticonPositions.get(j)], wordsAndTags.get(reconstructedTokens[emoticonPositions.get(j)]));
            }

            return emoticonAndPOS;
        } else {
            return null;
        }
    }

    public Map calculateEmoticonScore(Map taggedEmoticons) {
        double emoticonPosScore = 0.0;
        double emoticonNegScore = 0.0;
        double emoticonObjScore = 0.0;
        int count = 0;

        Iterator<Map.Entry> i = taggedEmoticons.entrySet().iterator();
        while (i.hasNext()) {
            Object key = i.next().getKey();
//            System.out.println(key + " : " + taggedEmoticons.get(key));

            Map<String, Double> emoticonScores = (appInitializer.appEmotiSentiWordNet.extract(String.valueOf(key), String.valueOf(taggedEmoticons.get(key)), true) != null)
                    ? appInitializer.appEmotiSentiWordNet.extract(String.valueOf(key), String.valueOf(taggedEmoticons.get(key)), true)
                    : appInitializer.appEmotiSentiWordNet.extract(String.valueOf(key), true);

            if (emoticonScores != null) {
                count++;
                emoticonPosScore += emoticonScores.get("pos");
                emoticonNegScore += emoticonScores.get("neg");
                emoticonObjScore += emoticonScores.get("obj");
            }
        }

        if (count > 0) {
            Map<String, Double> emoticonsScore = new HashMap<>();
            emoticonsScore.put("pos", emoticonPosScore / count);
            emoticonsScore.put("neg", emoticonNegScore / count);
            emoticonsScore.put("obj", emoticonObjScore / count);
            return emoticonsScore;
        } else {
            return null;
        }
    }

    public double getEmoticonsScores(Map taggedEmoticons) {
        double emoticonsPosScore = 0.0;
        double emoticonsNegScore = 0.0;
        double emoticonsObjScore = 0.0;
        int count = 0;

        Iterator<Map.Entry> i = taggedEmoticons.entrySet().iterator();
        while (i.hasNext()) {
            Object key = i.next().getKey();
            Map<String, Double> emoticonScores = (appInitializer.appEmotiSentiWordNet.extract(String.valueOf(key), String.valueOf(taggedEmoticons.get(key)), true) != null)
                    ? appInitializer.appEmotiSentiWordNet.extract(String.valueOf(key), String.valueOf(taggedEmoticons.get(key)), true)
                    : appInitializer.appEmotiSentiWordNet.extract(String.valueOf(key), true);

            if (emoticonScores != null) {
                count++;
                emoticonsPosScore += emoticonScores.get("pos");
                emoticonsNegScore += emoticonScores.get("neg");
                emoticonsObjScore += emoticonScores.get("obj");
            }
        }

        if (count > 0) {
            return Utils.determinePolarity(emoticonsPosScore / count, emoticonsNegScore / count, emoticonsObjScore / count);
        } else {
            return -2;
        }
    }

    public int determineTweetPolarity(boolean isSarcastic, double eAnalysis, double wAnalysis) {
        if (isSarcastic && eAnalysis < -1.0 && wAnalysis > 0) {
            return -1;
        } else if (isSarcastic && eAnalysis < -1.0 && wAnalysis < 0) {
            return 1;
        } else if (isSarcastic && eAnalysis < -1.0 && wAnalysis == 0) {
            return 0;
        } else if (!isSarcastic && eAnalysis < -1.0 && wAnalysis > 0) {
            return 1;
        } else if (!isSarcastic && eAnalysis < -1.0 && wAnalysis < 0) {
            return -1;
        } else if (!isSarcastic && eAnalysis < -1.0 && wAnalysis == 0) {
            return 0;
        } else if (isSarcastic && eAnalysis > 0 && wAnalysis > 0) {
            return -1;
        } else if (isSarcastic && eAnalysis > 0 && wAnalysis < 0) {
            return -1;
        } else if (isSarcastic && eAnalysis < 0 && wAnalysis > 0) {
            return 1;
        } else if (isSarcastic && eAnalysis < 0 && wAnalysis < 0) {
            return 1;
        } else if (!isSarcastic && eAnalysis > 0 && wAnalysis > 0) {
            return 1;
        } else if (!isSarcastic && eAnalysis > 0 && wAnalysis < 0) {
            return 1;
        } else if (!isSarcastic && eAnalysis < 0 && wAnalysis > 0) {
            return -1;
        } else if (!isSarcastic && eAnalysis < 0 && wAnalysis < 0) {
            return -1;
        } else if (isSarcastic && eAnalysis > 0 && wAnalysis == 0) {
            return -1;
        } else if (isSarcastic && eAnalysis < 0 && wAnalysis == 0) {
            return 1;
        } else if (!isSarcastic && eAnalysis > 0 && wAnalysis == 0) {
            return 1;
        } else if (!isSarcastic && eAnalysis < 0 && wAnalysis == 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
