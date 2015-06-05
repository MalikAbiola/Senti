package UIs;

import Classes.Document;
import Classes.Initializer;
import Classes.PreProcessor;
import Classes.SpamRemover;
import Classes.Utils;
import Main.SASM;
import gate.util.GateException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
public class Main_backup {

    public static void main(String[] args) {
        try {
            System.out.println("********************LOADING MODULES**********************");
            Initializer systemInitializer = new Initializer();
            systemInitializer.init();

//            systemInitializer.initSpamDetector();
//
            while (systemInitializer.appPOSTagger == null) {

                System.out.println("waiting for module (appPOSTagger) To Be Initialised...");
                Thread.sleep(2000);
            }

            while (systemInitializer.appSentiWordNet == null) {

                System.out.println("waiting for module (appSentiWordNet) To Be Initialised...");
                Thread.sleep(2000);
            }

            while (systemInitializer.appEmotiSentiWordNet == null) {

                System.out.println("waiting for module (appEmotiSentiWordNet) To Be Initialised...");
                Thread.sleep(2000);
            }

            while (systemInitializer.appSpamDetector == null) {

                System.out.println("waiting for module (appSpamDetector) To Be Initialised...");
                Thread.sleep(2000);
            }

            while (systemInitializer.appUrbanRetriever == null) {

                System.out.println("waiting for module (appUrbanRetriever) To Be Initialised...");
                Thread.sleep(2000);
            }

            systemInitializer.appUrbanRetriever.setInitializer(systemInitializer);
            System.out.println("******************DONE LOADING MODULES********************");

            SASM sasm = new SASM(systemInitializer);

            //retrieve tweets
            Document retrievedTweets = new Document(new File("testingtesting.txt"));

            System.out.println("\n\n\n Retrieved Tweets : " + retrievedTweets.getDocumentSentences().length + " / " + Utils.sentencesToArray(retrievedTweets.getDocumentSentencesString()).length + "\n\n\n");

            //remove spam
            Document cleanTweetsDocument = new SpamRemover(systemInitializer.appSpamDetector, retrievedTweets).removeSpam();

            //create array to hold clean tweets
            String savedCleanTweets[] = Utils.sentencesToArray(cleanTweetsDocument.getDocumentSentencesString());

            System.out.println("\n\n\n AfterSpamRemoval Tweets : " + cleanTweetsDocument.getDocumentSentences().length + " / " + Utils.sentencesToArray(cleanTweetsDocument.getDocumentSentencesString()).length + "\n\n\n");

            //create preprocessor
            PreProcessor retrievedTweetsPreProc = new PreProcessor(cleanTweetsDocument, "Edge of Tomorrow", systemInitializer);

            //first stage of preprocessing
            Document preProcessedRetrievedTweets = retrievedTweetsPreProc.stageOneProcessing();

            //retrieved preprocessed clean tweets sentences
            String[] cleanTweets = preProcessedRetrievedTweets.getDocumentSentences();

            //create list to save tagged clean tweets, map to save tagged emoticons, and integer to hold clean tweets count
            ArrayList<String> plainCleanTweets = new ArrayList<>();
            ArrayList<Map<String, String>> taggedCleanTweets = new ArrayList<>();
//            Map<Integer, String> plainCleanTweets = new HashMap<>();
//            Map<Integer, Map<String, String>> taggedCleanTweets = new HashMap<>();
            Map<Integer, Map<String, String>> taggedEmoticons = new HashMap<>();
            int cleanTweetCount = 0;
            int newTweetDocumentCount = 0;

            //for each tweet in the clean tweets - preprocessed clean tweets, tag tweet
            for (String tweet : cleanTweets) {
                //tag tweet, pass tweet tag and clean tweet to see if its interogative, if interogative, do not add to the lists.
                Map<String, String> taggedTweet = systemInitializer.appPOSTagger.tagString(tweet, true);
                if (!Utils.isInterogative(tweet, taggedTweet)) {
                    //check if emoticon exists, if yes, save emoticon details in map
                    Map<String, String> emoticonTag = sasm.detectEmoticon(tweet);
                    if (emoticonTag != null) {
                        //put tagged emoticon in tagged emoticons bag with the index of the tweet which it belongs
                        taggedEmoticons.put(newTweetDocumentCount, emoticonTag);
                    }
                    taggedCleanTweets.add(newTweetDocumentCount, taggedTweet);
//                    plainCleanTweets.put(cleanTweetCount, tweet);
                    plainCleanTweets.add(newTweetDocumentCount, savedCleanTweets[cleanTweetCount]);
                    newTweetDocumentCount++;
                }
                //increase no of cleantweets added
                cleanTweetCount++;
            }

            //do second stage preprocessing for clean tweets and saved each tweet in a string array
            retrievedTweetsPreProc = new PreProcessor(new Document(plainCleanTweets.toArray(new String[plainCleanTweets.size()])), "Edge of Tomorrow", systemInitializer);
            String[] documentStrings = retrievedTweetsPreProc.stageTwoProcessing().getDocumentSentences();

            //get length of tweets in document and the clean tagged strings
            int documentStringsLen = documentStrings.length;
            int taggedCleanTweetsLen = taggedCleanTweets.size();

            //throw exception if length do not match
            if (documentStringsLen != taggedCleanTweetsLen) {
                throw new UnsupportedOperationException("Tagged strings and document strings size not equal");
            }

            //for every tweet
            for (int i = 0; i < documentStringsLen; i++) {

                Map<String, String> taggedEmoticon = null;
//                Map tweetEmoticonScores = null;
                double tweetEmoticonScore = -2.0;

                //check if tweet id exists in tagged emoticon list, if yes, evaluate the emoticon score
                if (taggedEmoticons.containsKey(i)) {
                    taggedEmoticon = taggedEmoticons.get(i);
//                    tweetEmoticonScores = sasm.calculateEmoticonScore(taggedEmoticon);
                    tweetEmoticonScore = sasm.getEmoticonsScores(taggedEmoticon);

                }
                //represent tweet emoticon score in words
                String tEmoSc = (tweetEmoticonScore < -1.0 || tweetEmoticonScore > 1.0) ? "no emoticons" : String.valueOf(tweetEmoticonScore);

                //evaluate score of tweet without emoticons
                double tweetScore = sasm.calculateTweetScore(documentStrings[i], taggedCleanTweets.get(i));

                //aggregate scores
//                System.out.printf("%s: %s - %s\n", cleanTweets[i], String.valueOf(tweetScore), String.valueOf(tweetEmoticonScores));
//                System.out.printf("%s: %s - %s\n", savedCleanTweets[i], String.valueOf(tweetScore), tEmoSc);
                System.out.printf("%s: %s | %s\n", plainCleanTweets.get(i), String.valueOf(tweetScore), tEmoSc);
            }

//        } catch (IOException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
        } catch (InterruptedException | GateException | IOException ex) {
            Logger.getLogger(Main_backup.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
