/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.ArrayList;

/**
 *
 * @author Doctormaliko
 */
public class SpamRemover {

    private final SpamDetector spamDetector;
    private final Document document;
    private final ArrayList<String> spam = new ArrayList<>();

    public SpamRemover(SpamDetector spamDetector, Document document) {
        this.spamDetector = spamDetector;
        this.document = document;
    }

    public Document removeSpam() {
        ArrayList<String> notSpam = new ArrayList<>();
        String[] documentSentences = document.getDocumentSentences();
        for (String sentence : documentSentences) {
            if (!spamDetector.isSpam(sentence)) {
                notSpam.add(sentence);
            } else {
                spam.add(sentence);
            }
        }
        return new Document(notSpam.toArray(new String[notSpam.size()]));
    }

    public Document getSpam() {
        return new Document(spam.toArray(new String[spam.size()]));
    }
}
