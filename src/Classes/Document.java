/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Doctormaliko
 */
public class Document {

    private String documentSentencesString = "";
    private String[] documentSentences;

    protected BufferedReader scanner;

    public Document(File file) throws FileNotFoundException, IOException {
        scanner = new BufferedReader(new FileReader(file));
        this.DocumentTokenizer();
    }

    public Document(String[] documentSentences) {
        this.documentSentences = documentSentences;
        this.DocumentStringCreator();
    }

    public String[] getDocumentSentences() {
        return documentSentences;
    }

    public String getDocumentSentencesString() {
        return documentSentencesString;
    }
    
    public int getDocumentSentencesCount() {
        return documentSentences.length;
    }

    private void DocumentTokenizer() throws IOException {
        ArrayList<String> documentContent = new ArrayList<>();
//        StringTokenizer sTokenizer;
        String line;
        while ((line = scanner.readLine()) != null) {
//            sTokenizer = new StringTokenizer(line, ".");
//            String sentence;
//            while (sTokenizer.hasMoreTokens()) {
//                sentence = sTokenizer.nextToken();
//                documentContent.add(sentence);
//                this.documentSentencesString += sentence + "\n";
            documentContent.add(line);
            this.documentSentencesString += line + "\n";
//        }
        }

        this.documentSentences = new String[documentContent.size()];
        documentContent.toArray(this.documentSentences);
    }

    private void DocumentStringCreator() {
        for (String documentSentence : documentSentences) {
            this.documentSentencesString += documentSentence + "\n";
        }
    }
}
