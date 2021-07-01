package com.repo.readersAndWriters;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XMLReader
{
    public static Document readXML(File xmlFile) throws ParserConfigurationException, IOException, SAXException
    {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
    }
}
