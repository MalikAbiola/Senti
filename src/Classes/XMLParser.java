package Classes;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Doctormaliko
 */
public class XMLParser {

    private File f;
    private String xml;

    public XMLParser() {
    }

    public XMLParser(File f) {
        this.f = f;
    }

    public XMLParser(String xml) {
        this.xml = xml;
    }

    public void setF(File f) {
        this.f = f;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public void parseFile() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xmlDoc = db.parse(this.f);
            xmlDoc.getDocumentElement();

            NodeList nodeList = xmlDoc.getElementsByTagName("*");
            for (int i = 0; i < nodeList.getLength(); i++) {
                traverse((Element) nodeList.item(i));
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String parseString() {
        String taggedString = "";
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xmlDoc = db.parse(new InputSource(new StringReader(this.xml)));
            xmlDoc.getDocumentElement();

            NodeList nodeList = xmlDoc.getElementsByTagName("Token");
            for (int i = 0; i < nodeList.getLength(); i++) {
                taggedString += traverse((Element) nodeList.item(i)) + "\t";
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return taggedString;
    }

    public Map<String, String> parseString(boolean returnAsMap) {
        Map<String, String> taggedString = new HashMap<>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xmlDoc = db.parse(new InputSource(new StringReader(this.xml)));
            xmlDoc.getDocumentElement();

            NodeList nodeList = xmlDoc.getElementsByTagName("Token");
            for (int i = 0; i < nodeList.getLength(); i++) {
                String[] elementDetails = traverseElement((Element) nodeList.item(i));
                taggedString.put(elementDetails[0], elementDetails[1]);
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return taggedString;
    }

    private String traverse(Element n) {
//        System.out.println(n.getAttribute("string") + ": " + n.getAttribute("category"));
        return n.getAttribute("string") + ": " + Utils.determinePOS(n.getAttribute("category"));
    }

    private String[] traverseElement(Element n) {
        String[] elementDetails = {n.getAttribute("string").trim(), Utils.determinePOS(n.getAttribute("category").trim())};
        return elementDetails;
    }
}
