/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UIs;

import Classes.Document;
import Classes.Initializer;
import Classes.PreProcessor;
import Classes.SarcasmDetector;
import Classes.SpamRemover;
import Classes.TwitterPull;
import Classes.Utils;
import Main.SASM;
import ThirdParty.AppLogs;
import com.bluewalrus.pie.PieChart;
import com.bluewalrus.pie.Segment;
import gate.util.GateException;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import twitter4j.TwitterException;

/**
 *
 * @author Doctormaliko
 */
public class Home extends javax.swing.JFrame {

    private static Initializer systemInitializer;
    private int posTweetCount, negTweetCount, objTweetCount, totalTweetCount;
    private ArrayList<String> posTweets, negTweets, objTweets;
    private String[] retrievedSpamTweets;
    private File fileToAnalyse;
    private Document retrievedTweets;
    private static boolean currentlyAnalysing = false;

    /**
     * Creates new form Home
     */
    public Home() {
        initComponents();

        //set App Logging to messages tab
        OutputStream outputSteam = new AppLogs(appLog);
        System.setOut(new PrintStream(outputSteam, true));
        System.setErr(new PrintStream(outputSteam, true));

        //initialise app modules initializers
        Home.systemInitializer = new Initializer();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtTopic = new javax.swing.JTextField();
        chkUseLiveData = new javax.swing.JCheckBox();
        chkUploadTweetsFile = new javax.swing.JCheckBox();
        txtFilePath = new javax.swing.JTextField();
        btnBrowse = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pnlResultView = new javax.swing.JPanel();
        appTab = new javax.swing.JTabbedPane();
        jScrollPane8 = new javax.swing.JScrollPane();
        appLog = new javax.swing.JTextPane();
        jScrollPane7 = new javax.swing.JScrollPane();
        lstRetrievedTweets = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstPositiveTweets = new javax.swing.JList();
        jScrollPane4 = new javax.swing.JScrollPane();
        lstNegativeTweets = new javax.swing.JList();
        jScrollPane5 = new javax.swing.JScrollPane();
        lstObjectiveTweets = new javax.swing.JList();
        jScrollPane6 = new javax.swing.JScrollPane();
        lstSpams = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        btnClear = new javax.swing.JButton();
        btnAnalyse = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Topic");

        chkUseLiveData.setText("Use Live Data");

        chkUploadTweetsFile.setText("Upload Tweets File");

        txtFilePath.setEditable(false);

        btnBrowse.setText("Browse");
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("SM - SAT");

        jLabel3.setText("Social Media Sentiment Analysis Tool");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(21, 21, 21)
                                .addComponent(txtTopic))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtFilePath, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnBrowse))
                                    .addComponent(chkUploadTweetsFile)
                                    .addComponent(chkUseLiveData))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(195, 195, 195)
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(159, 159, 159)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTopic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkUseLiveData)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkUploadTweetsFile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFilePath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBrowse))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlResultView.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Presentation of Results", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri Light", 1, 14))); // NOI18N
        pnlResultView.setLayout(new java.awt.BorderLayout());

        appLog.setEditable(false);
        appLog.setBackground(new java.awt.Color(102, 0, 51));
        appLog.setFont(new java.awt.Font("Gautami", 0, 11)); // NOI18N
        appLog.setForeground(new java.awt.Color(255, 255, 255));
        jScrollPane8.setViewportView(appLog);

        appTab.addTab("App Log", jScrollPane8);

        jScrollPane7.setViewportView(lstRetrievedTweets);

        appTab.addTab("Retrieved Tweets", jScrollPane7);

        jScrollPane3.setViewportView(lstPositiveTweets);

        appTab.addTab("Positive Tweets", jScrollPane3);

        jScrollPane4.setViewportView(lstNegativeTweets);

        appTab.addTab("Negative Tweets", jScrollPane4);

        jScrollPane5.setViewportView(lstObjectiveTweets);

        appTab.addTab("Objective Tweets", jScrollPane5);

        jScrollPane6.setViewportView(lstSpams);

        appTab.addTab("Spam Tweets", jScrollPane6);

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnAnalyse.setText("Generate Topic Analysis");
        btnAnalyse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalyseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(141, 141, 141)
                .addComponent(btnAnalyse)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClear)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAnalyse)
                    .addComponent(btnClear))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(appTab)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlResultView, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlResultView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(appTab, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        // TODO add your handling code here:
//        ArrayList<Segment> values = new ArrayList<>();
//        values.add(new Segment(70, "Positive", Color.GREEN));
//        values.add(new Segment(20, "Negative", Color.RED));
//        values.add(new Segment(10, "Objective", Color.ORANGE));
//
//        PieChart p = new PieChart(values, "Opinion Analysis");
//        System.out.printf("%s, %s \n", p.getHeight(), p.getWidth());
//        pnlResultView.add(p);
//        pnlResultView.revalidate();
//        pnlResultView.repaint();

        fileToAnalyse = selectFile();
        txtFilePath.setText(fileToAnalyse.getAbsolutePath());
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void btnAnalyseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalyseActionPerformed
        // TODO add your handling code here:
        if (!currentlyAnalysing) {
            cleanUp();
            disable(btnAnalyse, btnClear, btnBrowse, txtTopic, chkUploadTweetsFile, chkUseLiveData);

            try {
                //get query topic
                final String query = txtTopic.getText().trim();
                //check if it contains valid characters
                if (query.isEmpty()) {
                    System.out.println("Please Provide the Analysis Topic");
                } else {
                    if ((chkUploadTweetsFile.isSelected() && chkUseLiveData.isSelected()) || (!chkUploadTweetsFile.isSelected() && !chkUseLiveData.isSelected())) {
                        System.out.println("You Must Select Just One Check Box");
                        JOptionPane.showMessageDialog(rootPane, "You Must Select Just One Check Box");
                    } else if (chkUploadTweetsFile.isSelected()) {
                        if (fileToAnalyse.isFile() && fileToAnalyse != null) {
                            retrievedTweets = new Document(fileToAnalyse);
                            if (retrievedTweets.getDocumentSentences().length > 0) {
                                lstRetrievedTweets.setListData(retrievedTweets.getDocumentSentences());
                                System.out.println("Starting Analysis Of Tweets");
                                Thread analyseTweet = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        currentlyAnalysing = true;
                                        analyse(query, retrievedTweets);
                                    }
                                });
                                analyseTweet.start();
                            } else {
                                System.out.println("The File Selected Contains No Sentences");
                                JOptionPane.showMessageDialog(rootPane, "The File Selected COntains No Sentences");
                            }
                        } else {
                            System.out.println("Please Make sure you Select a Valid File");
                            JOptionPane.showMessageDialog(rootPane, "Please Make sure you upload a Valid File");
                        }
                    } else if (chkUseLiveData.isSelected()) {
                        Thread twitterPullThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    System.out.println("Retrieving Tweets");
                                    TwitterPull pullTweets = new TwitterPull(query);
                                    pullTweets.retrieveTweets();
                                    retrievedTweets = pullTweets.getRetrievedTweets();
                                    lstRetrievedTweets.setListData(retrievedTweets.getDocumentSentences());
                                } catch (UnsupportedEncodingException | TwitterException ex) {
                                    Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, "A Twitter Connection Error Occured, Please check your internet connection. " + ex.getMessage());
                                }
                            }
                        });
                        twitterPullThread.start();
                        twitterPullThread.join();
                        System.out.println("Done Retrieving Tweets");
                        int tweetCount;
                        if (retrievedTweets != null && (tweetCount = retrievedTweets.getDocumentSentences().length) > 0) {
                            System.out.println("Total Tweets Retrieved: " + tweetCount);
                            System.out.println("Starting Analysis Of Tweets");
                            Thread analyseTweet = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    currentlyAnalysing = true;
                                    analyse(query, retrievedTweets);
                                }
                            });
                            analyseTweet.start();
                        } else {
                            System.out.println("NO Tweet Was Retrieved");
                            JOptionPane.showMessageDialog(rootPane, "No tweets were retrieved, Please check Network Connection");
                        }
                    }

                }
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Currently Loading and Analysing Tweets, Calm down");
            JOptionPane.showMessageDialog(rootPane, "Currently Loading and Analysing Tweets, Calm down");
        }
    }//GEN-LAST:event_btnAnalyseActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_btnClearActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows Classic".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread displayWindow = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new Home().setVisible(true);
                        }
                    });

                    Thread initPlugins = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("********************LOADING MODULES**********************");
                            //initialize modules
                            systemInitializer.init();
                        }
                    });

                    Thread check = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            check();
                        }
                    });

                    displayWindow.start();
                    displayWindow.join();
                    initPlugins.start();
                    check.start();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void analyse(String query, Document retrievedTweets) {
        try {
            //init stats holders
            initStatsHolders();
            //initialise analyser
            SASM sasm = new SASM(systemInitializer);

            //initialise sarcasm detector
            SarcasmDetector sarcasmDetector = new SarcasmDetector(systemInitializer);

            //initialize spam remover
            SpamRemover spamRemover = new SpamRemover(systemInitializer.appSpamDetector, retrievedTweets);

            //retrieve clean tweets
            Document cleanTweetsDocument = spamRemover.removeSpam();

            //retrieve spams
            retrievedSpamTweets = spamRemover.getSpam().getDocumentSentences();
            System.out.println("Spam Tweets Discovered: " + retrievedSpamTweets.length);
            //create array to hold clean tweets
            String savedCleanTweets[] = Utils.sentencesToArray(cleanTweetsDocument.getDocumentSentencesString());

            //create preprocessor
            PreProcessor retrievedTweetsPreProc = new PreProcessor(cleanTweetsDocument, query, systemInitializer);

            //first stage of preprocessing
            Document preProcessedRetrievedTweets = retrievedTweetsPreProc.stageOneProcessing();

            //retrieved preprocessed clean tweets sentences
            String[] cleanTweets = preProcessedRetrievedTweets.getDocumentSentences();
            System.out.println("Clean Tweets To Analyse: " + cleanTweets.length);
            //create list to save tagged clean tweets, map to save tagged emoticons, and integer to hold clean tweets count
            ArrayList<String> plainCleanTweets = new ArrayList<>();
            ArrayList<Map<String, String>> taggedCleanTweets = new ArrayList<>();
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
                    plainCleanTweets.add(newTweetDocumentCount, savedCleanTweets[cleanTweetCount]);
                    newTweetDocumentCount++;
                }
                //increase no of cleantweets added
                cleanTweetCount++;
            }

            //do second stage preprocessing for clean tweets and saved each tweet in a string array
            retrievedTweetsPreProc = new PreProcessor(new Document(plainCleanTweets.toArray(new String[plainCleanTweets.size()])), query, systemInitializer);
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
                double tweetEmoticonScore = -2.0;

                //check if tweet id exists in tagged emoticon list, if yes, evaluate the emoticon score
                if (taggedEmoticons.containsKey(i)) {
                    taggedEmoticon = taggedEmoticons.get(i);
                    tweetEmoticonScore = sasm.getEmoticonsScores(taggedEmoticon);
                }

                //evaluate score of tweet without emoticons
                double tweetScore = sasm.calculateTweetScore(documentStrings[i], taggedCleanTweets.get(i));
                //check if tweet is sarcastic
                boolean isSarcastic = sarcasmDetector.setParams(documentStrings[i], taggedCleanTweets.get(i)).isSarcastic();
                //aggregate scores
                int finalPolarity = sasm.determineTweetPolarity(isSarcastic, tweetEmoticonScore, tweetScore);

                if (finalPolarity == 1) {
                    posTweetCount++;
                    posTweets.add(plainCleanTweets.get(i));
                } else if (finalPolarity == -1) {
                    negTweetCount++;
                    negTweets.add(plainCleanTweets.get(i));
                } else if (finalPolarity == 0) {
                    objTweetCount++;
                    objTweets.add(plainCleanTweets.get(i));
                }
                totalTweetCount++;
            }
            presentResults();
            System.out.println("Analysis Done, Please Check The Tabs for more information");
            currentlyAnalysing = false;
            enable(btnAnalyse, btnClear, btnBrowse, txtTopic, chkUploadTweetsFile, chkUseLiveData);

        } catch (InterruptedException | GateException | IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void check() {
        while (systemInitializer.appPOSTagger == null) {
            try {
                System.out.println("waiting for module (appPOSTagger) To Be Initialised...");
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        while (systemInitializer.appSentiWordNet == null) {
            try {
                System.out.println("waiting for module (appSentiWordNet) To Be Initialised...");
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        while (systemInitializer.appEmotiSentiWordNet == null) {
            try {
                System.out.println("waiting for module (appEmotiSentiWordNet) To Be Initialised...");
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        while (systemInitializer.appSpamDetector == null) {
            try {
                System.out.println("waiting for module (appSpamDetector) To Be Initialised...");
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        while (systemInitializer.appUrbanRetriever == null) {
            try {
                System.out.println("waiting for module (appUrbanRetriever) To Be Initialised...");
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        systemInitializer.appUrbanRetriever.setInitializer(systemInitializer);
        System.out.println("******************DONE LOADING MODULES********************");
    }

    private void initStatsHolders() {
        posTweetCount = 0;
        negTweetCount = 0;
        objTweetCount = 0;
        totalTweetCount = 0;
        posTweets = new ArrayList<>();
        negTweets = new ArrayList<>();
        objTweets = new ArrayList<>();
    }

    private void presentResults() {
        lstPositiveTweets.setListData(posTweets.toArray());
        lstNegativeTweets.setListData(negTweets.toArray());
        lstObjectiveTweets.setListData(objTweets.toArray());
        lstSpams.setListData(retrievedSpamTweets);

        String[] headings = {"Retrieved Tweets", "Positive Tweets", "Negative Tweets", "Objective Tweets", "Spam Tweets"};
        int[] resultCounts = {retrievedTweets.getDocumentSentencesCount(), posTweets.size(), negTweets.size(), objTweets.size(), retrievedSpamTweets.length};
        int i = 1;
        for (int c : resultCounts) {
            appTab.setTitleAt(i, headings[i - 1] + " (" + c + ")");
            i++;
        }
    }

    private File selectFile() {
        File file = null;

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {//if open is selected
            file = fileChooser.getSelectedFile(); // gets selected file
            if ((file == null) || (file.getName().equals(""))) {
                // display error if invalid
                JOptionPane.showMessageDialog(null, "Invalid File Name", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
            }
        } // if user clicked Cancel button on dialog, return
        else if (result == JFileChooser.CANCEL_OPTION) {
            fileChooser.cancelSelection();
        }
        return file;
    }

    private void reset() {
        String[] empty = {};
        fileToAnalyse = null;
        retrievedTweets = null;

        lstRetrievedTweets.setListData(empty);
        lstSpams.setListData(empty);
        lstPositiveTweets.setListData(empty);
        lstNegativeTweets.setListData(empty);
        lstObjectiveTweets.setListData(empty);

        txtFilePath.setText("");
        txtTopic.setText("");

        String[] headings = {"Retrieved Tweets", "Positive Tweets", "Negative Tweets", "Objective Tweets", "Spam Tweets"};
        int i = 1;
        for (String title : headings) {
            appTab.setTitleAt(i, title);
            i++;
        }
    }

    public void cleanUp() {
        System.out.println("-------------------------------------------------------------------------------------------");
        String[] empty = {};

        lstRetrievedTweets.setListData(empty);
        lstSpams.setListData(empty);
        lstPositiveTweets.setListData(empty);
        lstNegativeTweets.setListData(empty);
        lstObjectiveTweets.setListData(empty);

        String[] headings = {"Retrieved Tweets", "Positive Tweets", "Negative Tweets", "Objective Tweets", "Spam Tweets"};
        int i = 1;
        for (String title : headings) {
            appTab.setTitleAt(i, title);
            i++;
        }
    }

    private void disable(JComponent... component) {
        for (JComponent c : component) {
            c.setEnabled(false);
        }
    }

    private void enable(JComponent... component) {
        for (JComponent c : component) {
            c.setEnabled(true);
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane appLog;
    private javax.swing.JTabbedPane appTab;
    private javax.swing.JButton btnAnalyse;
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnClear;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBox chkUploadTweetsFile;
    private javax.swing.JCheckBox chkUseLiveData;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JList lstNegativeTweets;
    private javax.swing.JList lstObjectiveTweets;
    private javax.swing.JList lstPositiveTweets;
    private javax.swing.JList lstRetrievedTweets;
    private javax.swing.JList lstSpams;
    private javax.swing.JPanel pnlResultView;
    private javax.swing.JTextField txtFilePath;
    private javax.swing.JTextField txtTopic;
    // End of variables declaration//GEN-END:variables
}
