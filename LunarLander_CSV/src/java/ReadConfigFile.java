/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import csvParser.CSVToXMLConverter;
import generated.Configuraciones;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;

/**
 * @author Ale
 */
public class ReadConfigFile extends HttpServlet {
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //Get CSV file location
            ServletContext context = getServletContext();
            String fullPathCSV = context.getRealPath("csv/config.csv");
            String fullPathXML = context.getRealPath("xml/config.xml");
            File csvFile = new File(fullPathCSV);
            File xmlFile = new File(fullPathXML);
            
            //Transform CSV file to XML file using the CSVToXMLConverter class
            CSVToXMLConverter converter = new CSVToXMLConverter();
            converter.convertCSVtoXML(csvFile, xmlFile);
            
            //Obtain the JAXBReader instance
            JAXBReader reader = new JAXBReader();
            
            //Generate "Configuraciones" object from the config.xml file
            Configuraciones configuraciones = reader.readXMLConfig(xmlFile);
            
            //marshall Configuraciones object to StringWriter
            StringWriter sw = new StringWriter();
            JAXB.marshal(configuraciones, sw);
            String xmlString = sw.toString();
            
            //set the response to send the xml configuration file.
            response.setContentType("text/xml");
            PrintWriter pw = response.getWriter();
            pw.println(xmlString);
            
        } catch (IOException | JAXBException e) {
            //Sends message informing of a possible error to the client
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("text/html");
            PrintWriter pw = response.getWriter();
            pw.println(e.toString());
        }
    }
}
