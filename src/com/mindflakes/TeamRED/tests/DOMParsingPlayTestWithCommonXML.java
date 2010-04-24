package com.mindflakes.TeamRED.tests;


import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DOMParsingPlayTestWithCommonXML {
	private static Document doc;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DocumentBuilderFactory factory = 
            DocumentBuilderFactory.newInstance();

        // Turn off validation, and turn off namespaces
//        factory.setValidating(false);
//        factory.setNamespaceAware(false);

        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.parse(
        		new File("docs/specimens/ortega/-xxxx-tthom-042010-110140.xml"));
	}
	
	@Test
	public void printdoc() {
//		System.out.println(doc);
	}
	
	@Test
	public void showCommonName() {

		doc.toString();
		NodeList nodes = doc.getElementsByTagName("*");
		System.out.println(nodes.getLength());
	}
	
	

}
