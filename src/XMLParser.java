
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
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

    public XMLParser(File f) {
        this.f = f;
    }

    public XMLParser(String xml) {
        this.xml = xml;
    }

    public void parse() {
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

    private String traverse(Element n) {
//        System.out.println(n.getAttribute("string") + ": " + n.getAttribute("category"));
        return n.getAttribute("string") + ": " + n.getAttribute("category");
    }

}
