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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Doctormaliko
 */
public class POSTagger1 {

    private String string;
    private Corpus posCorpus;
    private CorpusController posApp;
    private Document documentToTag;
    private XMLParser xmlParser;

    public POSTagger1() throws GateException, IOException {
        init();
    }

    public POSTagger1(String string) throws GateException, IOException {
        this.string = string;
        init();
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    private void init() throws GateException, IOException {
        Gate.init();
        this.posCorpus = Factory.newCorpus("SASM Corpus");
        this.posApp = (CorpusController) PersistenceManager.loadObjectFromFile(new File("application.xgapp"));
        this.posApp.setCorpus(this.posCorpus);
        this.xmlParser = new XMLParser();
    }

    private void initDocument() throws GateException {
        if (this.string != null) {
            this.documentToTag = Factory.newDocument(this.string);
            this.posCorpus.add(this.documentToTag);
        } else {
            throw new UnsupportedOperationException("No String To Tag");
        }
    }

    private void clearResources() {
        this.posCorpus.cleanup();
        Factory.deleteResource(this.documentToTag);
        
//        this.posCorpus.clear();
//  Factory.deleteResource(doc);
    }

    public String tagString() throws GateException {
        initDocument();
        this.posApp.execute();

        AnnotationSet tokens = this.documentToTag.getAnnotations().get("Token");
        String xml = this.documentToTag.toXml(tokens);
//        String xmlOpening = "<?xml version=\"1.0\"?>\n<sentence>";
//        String xmlClosing = "\n</sentence>";
//        this.xmlParser.setXml(xmlOpening + xml + xmlClosing);
        this.xmlParser.setXml("<?xml version=\"1.0\"?><sentence>" + xml + "</sentence>");
//        clearResources();
        return this.xmlParser.parseString();
    }

    public String tagString(String stringToTag) throws GateException {
        this.documentToTag = Factory.newDocument(stringToTag);
        this.posCorpus.add(this.documentToTag);
        this.posApp.execute();

        AnnotationSet tokens = this.documentToTag.getAnnotations().get("Token");
        String xml = this.documentToTag.toXml(tokens);
//        String xmlOpening = "<?xml version=\"1.0\"?><sentence>";
//        String xmlClosing = "</sentence>";
        this.xmlParser.setXml("<?xml version=\"1.0\"?><sentence>" + xml + "</sentence>");
        clearResources();
        return this.xmlParser.parseString();
    }

    public Map<String, String> tagString(String stringToTag, boolean returnAsMap) throws GateException {
//        this.documentToTag = Factory.newDocument(stringToTag);
//        this.posCorpus.add(this.documentToTag);
//        this.posApp.execute();
        
        
        
        

        AnnotationSet tokens = this.documentToTag.getAnnotations().get("Token");
        String xml = this.documentToTag.toXml(tokens);
        this.xmlParser.setXml("<?xml version=\"1.0\"?><sentence>" + xml + "</sentence>");
        clearResources();
        return this.xmlParser.parseString(true);
    }
}
