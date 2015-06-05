
import Classes.EmotiSentiWordNet;
import Classes.Initializer;
import Classes.SarcasmDetector;
import Classes.UrbanRetriever;
import Classes.Utils;
import Main.SASM;
import gate.util.GateException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
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
public class Test {

    public static void main(String[] args) {
        try {
            //        try {
//            Scanner s = new Scanner(new File("stopwords.txt"));
//            String line = null;
//            String output = "";
//            while (s.hasNextLine()) {
//                line = s.next().trim();
//                if (!Utils.isNegation(line)) {
//                    output += "-" + s.next().trim() + "-,";
//                }
//            }
//            System.out.println(output);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
//        }

//            EmotiSentiWordNet e = new EmotiSentiWordNet();
//            Utils.printMapContent(e.getESWNDictonary());
//            
            Initializer i = new Initializer();
            i.initPOSTagger();
////            i.initEmotiSentiWordNet();
            i.initSentiWordNet();
            i.initUrbanRetriever();
//
            while (i.appPOSTagger == null) {

                System.out.println("waiting for modules To Be Initialised...");
                Thread.sleep(2000);
            }
            while (i.appSentiWordNet == null) {

                System.out.println("waiting for modules To Be Initialised...");
                Thread.sleep(2000);
            }
            while (i.appUrbanRetriever == null) {

                System.out.println("waiting for modules To Be Initialised...");
                Thread.sleep(2000);
            }
//            while (i.appEmotiSentiWordNet == null) {
//
//                System.out.println("waiting for modules To Be Initialised...");
//                Thread.sleep(2000);
//            }
//            Utils.printMapContent(i.appEmotiSentiWordNet.getESWNDictonary());
//            String[] k = {"thanks Twitter for shortening URLs even tho we didnt ask you to #sarcasm", "great, now i'm sad", "tunde is isn't that good na"};
//            for (String w : k) {
//                SarcasmDetector s = new SarcasmDetector(i, w, i.appPOSTagger.tagString(w, true));
//                System.out.println(w + " | " + s.isSarcastic());
//            }
//            i.initSentiWordNet();
//            while (i.appSentiWordNet == null) {
//
//                System.out.println("waiting for modules To Be Initialised...");
//                Thread.sleep(2000);
//            }
//            UrbanRetriever r = new UrbanRetriever("turnt");
//            r.setInitializer(i);
//            r.getDetails();
//            System.out.println("sentiment scores:" + r.getSentimentScores());
            System.out.println(i.appPOSTagger.tagString("this movie is the most underated artiest", true));
            System.out.println(i.appPOSTagger.tagString("the music spoke to me", true));
            System.out.println(i.appPOSTagger.tagString("i am truly surprised by this movie", true));
            SASM sasm = new SASM(i);
            System.out.println(sasm.calculateTweetScore("i am surprised by this movie", i.appPOSTagger.tagString("i am surprised by this movie", true)));
            
//            System.out.println(i.appPOSTagger.tagString("this movie is underated", true));
//            System.out.println(i.appPOSTagger.tagString("this movie is underated", true));
//        System.out.println(Utils.normalise("that was a facinatinggg gooooooooooooooooooooooooooooooooooooooooooooooooooooooooal!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", 2));
//                        Utils.printMapContent(i.appEmotiSentiWordNet.getESWNDictonary());
//                        Utils.printArrayContent("i. love. good. boys really!".split("\\s* "));
//            String[] ww = {"this movie is an #epic #fail", "this movie is an #epic #fail #sarcasm","this movie is an #epic #fail #failedMovie"};
//            for (String w : ww) {
//                Map c = i.appPOSTagger.tagString(w, true);
//                System.out.println(c);
//                System.out.println(Utils.isInterogative(w, c));
//            }
            //            String tweet = "she is so O:)";
//            Map<String,String> emoticonTag = sasm.detectEmoticon(tweet);
//            System.out.println(emoticonTag);
//            System.out.println(sasm.calculateEmoticonScore(emoticonTag));
//        } catch (InterruptedException | GateException ex) {
//            String w = ", i wanna yeah right go";
//            String a = "yeah right";
//            System.out.println(w.indexOf(a) + " " + w.length()  + " " + a.length());
//            String a = "this is really a sentence this is really a sentence this is really a sentence this is really a sentence this is really a sentence this is really a sentence";
//            String[] b = a.split("\\W* ");
//
//            String[] leftSide = new String[b.length / 2];
//            String[] rightSide = new String[b.length - (b.length / 2)];
//
//            int i = 0, j = leftSide.length, k = 0;
//            for (String word : b) {
//                if (i < leftSide.length && j >= 0) {
//                    leftSide[i] = word;
//                    j--;
//                } else if (k < b.length) {
//                    rightSide[k] = word;
//                    k++;
//                }
//                i++;
//            }
//            Utils.printArrayContent(b);
//            System.out.println("****************************************************");
//            Utils.printArrayContent(leftSide);
//            System.out.println("**************************************************");
//            Utils.printArrayContent(rightSide);
//                        Utils.printArrayContent("web 2.0 isnt't right, but we use it still.".split("\\s*"));
//            try {
//                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                    System.out.println(info.getName());
//                }
//            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
//                java.util.logging.Logger.getLogger(Test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//            }
        } catch (GateException | InterruptedException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
