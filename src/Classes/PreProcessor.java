/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

/**
 *
 * @author Doctormaliko
 */
public class PreProcessor {

    private Document documentToProcess;
    private String[] documentSentences;
    private String query;
    private Initializer initializer;

    public PreProcessor() {
    }

    public PreProcessor(Document documentToProcess, String query, Initializer initializer) {
        this.documentToProcess = documentToProcess;
        this.query = query;
        this.initializer = initializer;
        documentSentences = this.documentToProcess.getDocumentSentences();
    }

    public void setDocumentToProcess(Document documentToProcess) {
        this.documentToProcess = documentToProcess;
        documentSentences = this.documentToProcess.getDocumentSentences();
    }

    public Document getDocumentToProcess() {
        return documentToProcess;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

//    public Document processDocument() {
//        removeQuery();
//        removeTwitterLingo();
//        removeURLs();
//        toLowerCase();
//        removeStopWords();
//        removeNewLines();
//        normalizeTokens();
//        return new Document(documentSentences);
//    }
    public Document stageOneProcessing() {
        removeTwitterLingo();
        removeURLs();
        removeNewLines();
        normalizeTokens();
        this.documentToProcess = new Document(documentSentences);
        return this.documentToProcess;

    }

    public Document stageTwoProcessing() {
        toLowerCase();
        removeQuery();
        removeStopWords();
        this.documentToProcess = new Document(documentSentences);
        return this.documentToProcess;
    }

    private void removeTwitterLingo() {
        int count = documentSentences.length;
        for (int i = 0; i < count; i++) {
            String joinedTokens = "";
            String[] tokens = documentSentences[i].split("\\s* ");
            for (String token : tokens) {
                token = token.trim();
//                if (token.length() > 0 && (!(token.startsWith("#") || token.startsWith("@") || token.equalsIgnoreCase("RT")))) {
                if (token.length() > 0 && (!(token.startsWith("@") || token.equalsIgnoreCase("RT")))) {
                    joinedTokens += token + " ";
                }
            }
            documentSentences[i] = joinedTokens;
        }
    }

    private void removeURLs() {
        int count = documentSentences.length;
        for (int i = 0; i < count; i++) {
            String joinedTokens = "";
            String[] tokens = documentSentences[i].split("\\s* ");
            for (String token : tokens) {
                token = token.trim();
                if (token.length() > 0 && token.startsWith("http://t.co/")) {
                    joinedTokens += "EXT_URL ";
                } else {
                    joinedTokens += token + " ";
                }
            }
            documentSentences[i] = joinedTokens;
        }
    }

    public void normalizeTokens() {
        int count = documentSentences.length;
        for (int i = 0; i < count; i++) {
            String joinedTokens = "";

            String[] tokens = documentSentences[i].split("\\s* ");
            for (String token : tokens) {
                token = Utils.stripPunctuations(token.trim());

                if (!Utils.isNegation(token) && !Utils.isStopWord(token) && !initializer.appSentiWordNet.exists(token)) {
                    int allowedRepition = Initializer.ALLOWED_CHAR_REPITION;
                    do {
                        token = Utils.normalise(token.trim(), allowedRepition);
                        allowedRepition--;
                    } while (!initializer.appSentiWordNet.exists(token) && allowedRepition >= 1);
                }
                joinedTokens += token.trim() + " ";
            }
            documentSentences[i] = joinedTokens;
        }
    }

    private void removeNewLines() {
        int count = documentSentences.length;
        for (int i = 0; i < count; i++) {
            documentSentences[i] = documentSentences[i].replace("\n", " ");
        }
    }

    private void removeStopWords() {
        int count = documentSentences.length;

        for (int i = 0; i < count; i++) {
            String joinedTokens = "";
            String[] tokens = documentSentences[i].split(" ");
            for (String token : tokens) {
                token = Utils.stripPunctuations(token).trim();

                if (token.length() > 0 && !Utils.isStopWord(token)) {
                    joinedTokens += token + " ";
                }
            }
            documentSentences[i] = joinedTokens;
        }
    }

    private void removeQuery() {
        int count = documentSentences.length;
        String queryInLC = this.query.toLowerCase();
        for (int i = 0; i < count; i++) {
            if (documentSentences[i].contains(queryInLC)) {
                documentSentences[i] = documentSentences[i].replaceAll(queryInLC, "");
            }
        }
    }

    private void toLowerCase() {
        int count = documentSentences.length;
        for (int i = 0; i < count; i++) {
            documentSentences[i] = documentSentences[i].toLowerCase();
        }
    }

    private void removeDuplicates() {

    }
}
