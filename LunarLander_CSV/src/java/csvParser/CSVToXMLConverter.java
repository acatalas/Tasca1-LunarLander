/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Ale
 */
public class CSVToXMLConverter {

    private final int NUMBER_OF_ATTRIBUTES = 3;
    private DocumentBuilderFactory domFactory = null;
    private DocumentBuilder domBuilder = null;
    private BufferedReader br = null;
    private StringTokenizer st = null;

    /*
    Constructor that instantiates the DocumentBuilder
     */
    public CSVToXMLConverter() {
        //Obtaining instance of class DocumentBuilderFactory
        domFactory = DocumentBuilderFactory.newInstance();
        try {
            /* Obtaining instance of class DocumentBuilder API 
            to obtain DOM Document instances from an XML document.*/
            domBuilder = domFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            System.err.println(ex.toString());
        }
    }

    /*
    Method that converts the game configurations from a csv format
    to an xml format
     */
    public void convertCSVtoXML(File csvFile, File xmlFile) {
        /* Initializing the DOM structure */
        Document document = domBuilder.newDocument();
        document.setXmlStandalone(true);

        /* Creating the root element in the DOM structure and appending it*/
        Element rootElement = document.createElement("configuraciones");
        document.appendChild(rootElement);

        try {
            //Read the csv file
            br = new BufferedReader(new FileReader(csvFile));
            String row = null;
            
            //Read the file, line by line, since each line contains one configuration node
            while ((row = br.readLine()) != null) {
                //Splits the values at the comma
               
                String[] values = row.split(",");
                /*Checks that the csv row has the correct number of values to create
                the configuration node*/
                
                if (values.length == NUMBER_OF_ATTRIBUTES) {
                    //Creates the "configuracion" element and affs the "nombre" attribute
                    Element configElement = document.createElement("configuracion");
                    configElement.setAttribute("nombre", values[0]);

                    //Creates the "nave" element and sets its value
                    Element naveElement = document.createElement("nave");
                    naveElement.appendChild(document.createTextNode(values[1]));

                    //Creates the "modoDificil" element and sets its value
                    Element modoElement = document.createElement("modoDificil");
                    modoElement.appendChild(document.createTextNode(values[2]));

                    //Appends the "nave" and the "modoDificil" element to the "configuracion" element
                    configElement.appendChild(naveElement);
                    configElement.appendChild(modoElement);

                    //Appends the "configuracion" element to the root "configuraciones" element
                    rootElement.appendChild(configElement);
                }
            }
        } catch (FileNotFoundException ex) {
            System.err.println(ex.toString());
        } catch (IOException ex) {
            System.err.println(ex.toString());
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                System.err.println(ex.toString());
            }
        }

        //Obtains an instance of TransformerFactory to instantiate the Transformer
        TransformerFactory tranFactory = TransformerFactory.newInstance();
        Transformer aTransformer = null;
        try {
            /*Obtains an instance of Transformer, used to transform the source tree
            created from the CSV document to a result tree*/
            aTransformer = tranFactory.newTransformer();

            //Sets the transformer's parameters
            aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");   //allow indenting

            //method that should be used for outputting the result tree
            aTransformer.setOutputProperty(OutputKeys.METHOD, "xml");

            //sets the indent amount
            aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            
            //sets standalone to "yes"
            aTransformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

            /*Creates the source destination of the transformation
            In this case, the document created with the xml structure using the
            information stored in the csv file*/
            Source src = new DOMSource(document);

            //Creates result destination of the transformation
            Result result = new StreamResult(xmlFile);

            //Transforms the xml Document into the xml file
            aTransformer.transform(src, result);
        } catch (TransformerException ex) {
            System.err.println(ex.toString());
        }

    }

}
