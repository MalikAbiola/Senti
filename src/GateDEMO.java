
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
import java.util.StringTokenizer;
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
public class GateDEMO {

    public static void main(String[] args) {
        try {
            Gate.init();
            Document doc = Factory.newDocument("That was a fantasticcccc goooooooooooaaaaaaaaaaaaaallllllll\nI downloaded some great Pr0n last night from Kazaa\ni am not going to  use the #Z10\nThe Z10 is fucked\nThe #Z10 is a good  one from #blackberry :)\nZ10! Dopeeeeeeeeee!!!\nI don't hate the Z10\nThe Z10 Feels out of place to me");
//            Document doc = Factory.newDocument(" BlackBerry #Z10 is Crushing iPhone 5, Samsung Galaxy SIII and Note II in Customer Satisfaction!");

            Corpus c = Factory.newCorpus("SASM Corpus");
            CorpusController app = (CorpusController) PersistenceManager.loadObjectFromFile(new File("application.xgapp"));
            app.setCorpus(c);
            c.add(doc);
            app.execute();

            AnnotationSet tokens = doc.getAnnotations().get("Token");
//            AnnotationSet token = doc.getAnnotations();

//            for(Annotation t: tokens) {
////                System.out.println(gate.Utils.stringFor(doc, token));
////                FeatureMap fm = t.getFeatures();
////                System.out.println(fm);     
//            }
            String xml = doc.toXml(tokens);
//            System.out.println(xml);
//            File f = File.createTempFile("tweets", "tmp");
//            FileWriter fw = null;
//            fw = new FileWriter(f);
//            try {
            String xmlOpening = "<?xml version=\"1.0\"?>\n<tweets>";
            String xmlClosing = "\n</tweets>";
//                fw.write(xmlOpening + xml + xmlClosing);
//                System.out.println(xmlOpening + xml + xmlClosing);
//                System.out.println("--------------------------------------------------------------------------------------------");
//
//            } catch (IOException e) {
//                Logger.getLogger(GateDEMO.class.getName()).log(Level.SEVERE, null, e);
//            } finally {
//                fw.close();
//            }

            XMLParser xmlParser = new XMLParser(xmlOpening + xml + xmlClosing);
            System.out.println(xmlParser.parseString());

        } catch (GateException | IOException ex) {
            Logger.getLogger(GateDEMO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String TagStrings(String string) {
        String output = "";
        try {
            Gate.init();

            Corpus c = Factory.newCorpus("SASM Corpus");
            CorpusController app = (CorpusController) PersistenceManager.loadObjectFromFile(new File("application.xgapp"));
            app.setCorpus(c);

            StringTokenizer sT = new StringTokenizer(string, "\n");
            while (sT.hasMoreTokens()) {
                String currentString = sT.nextToken();
                Document doc = Factory.newDocument(currentString);
                c.add(doc);
                app.execute();
                AnnotationSet tokens = doc.getAnnotations().get("Token");
                String xml = doc.toXml(tokens);
                String xmlOpening = "<?xml version=\"1.0\"?>\n<sentence>";
                String xmlClosing = "\n</sentence>";
                XMLParser xmlParser = new XMLParser(xmlOpening + xml + xmlClosing);

//                output += currentString + ": " + xmlParser.parseString() + "\n";
                output += xmlParser.parseString() + "\n";

                c.clear();
                Factory.deleteResource(doc);
            }
            return output;

//            Document doc = Factory.newDocument(string);
//
//            c.add(doc);
//            app.execute();
//
//            AnnotationSet tokens = doc.getAnnotations().get("Token");
//            String xml = doc.toXml(tokens);
//            String xmlOpening = "<?xml version=\"1.0\"?>\n<tweets>";
//            String xmlClosing = "\n</tweets>";
//            XMLParser xmlParser = new XMLParser(xmlOpening + xml + xmlClosing);
//            return xmlParser.parseString();
        } catch (GateException | IOException ex) {
            Logger.getLogger(GateDEMO.class.getName()).log(Level.SEVERE, null, ex);
            return "Error";
        }
    }
}
