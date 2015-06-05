/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import gate.AnnotationSet;
import gate.Corpus;
import gate.CorpusController;
import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author Doctormaliko
 */
public class POSTagger {

    private Corpus posCorpus;
    private CorpusController posApp;
//    private Document documentToTag;
    private XMLParser xmlParser;

    public POSTagger() throws GateException, IOException {
        init();
    }

    private void init() throws GateException, IOException {
        Gate.init();
        this.posCorpus = Factory.newCorpus("SASM Corpus");
        this.posApp = (CorpusController) PersistenceManager.loadObjectFromFile(new File("application.xgapp"));
        this.posApp.setCorpus(this.posCorpus);
        this.xmlParser = new XMLParser();
    }

    public String tagString(String stringToTag) throws GateException {
        Document documentToTag = (Document) Factory.createResource("gate.corpora.DocumentImpl", gate.Utils.featureMap("stringContent", stringToTag));

        this.posCorpus.add(documentToTag);
        this.posApp.execute();
        String xml = documentToTag.toXml(documentToTag.getAnnotations().get("Token"));
        this.xmlParser.setXml("<?xml version=\"1.0\"?><sentence>" + xml + "</sentence>");
        this.posCorpus.clear();
        Factory.deleteResource(documentToTag);
        return this.xmlParser.parseString();
    }

    public Map<String, String> tagString(String stringToTag, boolean returnAsMap) throws GateException {
        Document documentToTag = (Document) Factory.createResource("gate.corpora.DocumentImpl", gate.Utils.featureMap("stringContent", stringToTag));

        this.posCorpus.add(documentToTag);
        this.posApp.execute();
        String xml = documentToTag.toXml(documentToTag.getAnnotations().get("Token"));
        this.xmlParser.setXml("<?xml version=\"1.0\"?><sentence>" + xml + "</sentence>");
        this.posCorpus.clear();
        Factory.deleteResource(documentToTag);
        return this.xmlParser.parseString(true);
    }

    private void cleanUp() {
        Corpus corp = this.posApp.getCorpus();
        if (!corp.isEmpty()) {
            for (int i = 0; i < corp.size(); i++) {
                Document doc1 = (Document) corp.remove(i);
                corp.unloadDocument(doc1);
                Factory.deleteResource(corp);
                Factory.deleteResource(doc1);
            }
        }
    }
}
