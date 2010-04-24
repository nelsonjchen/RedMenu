package com.mindflakes.TeamRED.tests;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

// JAXP
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

// DOM
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TestDOMParsing {

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                System.err.println ("Usage: java TestDOMParsing " +
                                    "[filename]");
                System.exit (1);
            }

            // Get Document Builder Factory
            DocumentBuilderFactory factory = 
                DocumentBuilderFactory.newInstance();

            // Turn on validation, and turn off namespaces
            factory.setValidating(true);
            factory.setNamespaceAware(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(args[0]));

            // Print the document from the DOM tree and
            //   feed it an initial indentation of nothing
            printNode(doc, "");

        } catch (ParserConfigurationException e) {
            System.out.println("The underlying parser does not " +
                               "support the requested features.");
        } catch (FactoryConfigurationError e) {
            System.out.println("Error occurred obtaining Document " +
                               "Builder Factory.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printNode(Node node, String indent)  {
        // print the DOM tree
    }

}