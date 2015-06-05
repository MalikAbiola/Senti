/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import com.datumbox.opensource.classifiers.NaiveBayes;
//import com.datumbox.opensource.dataobjects.NaiveBayesKnowledgeBase;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Doctormaliko
 */
public class SpamDetector {

    private NaiveBayes nb;
//    private NaiveBayesKnowledgeBase knowledgeBase;

    public SpamDetector() throws IOException {
        init();
    }

    private void init() throws IOException {
        //loading examples in memory
        Map<String, String[]> trainingExamples = new HashMap<>();

        trainingExamples.put("ham", readLines(SpamDetector.class.getResource("/datasets/training.ham.txt")));
        trainingExamples.put("spam", readLines(SpamDetector.class.getResource("/datasets/training.spam.txt")));

        //train classifier
        NaiveBayes trainingNB = new NaiveBayes();
        trainingNB.setChisquareCriticalValue(6.63); //0.01 pvalue
        trainingNB.train(trainingExamples);

        //get trained classifier knowledgeBase
//        this.knowledgeBase = trainingNB.getKnowledgeBase();

//        trainingNB = null;
//        trainingExamples = null;

        //Use classifier
        this.nb = new NaiveBayes(trainingNB.getKnowledgeBase());
        trainingNB = null;
        trainingExamples = null;
    }

    /**
     * Reads the all lines from a file and places it a String array. In each
     * record in the String array we store a training example text.
     *
     * @param url
     * @return
     * @throws IOException
     */
    private String[] readLines(URL url) throws IOException {

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

    public boolean isSpam(String string) {
//        System.out.println(this.nb.predict(string));
        return this.nb.predict(string).equalsIgnoreCase("spam");
    }
}
