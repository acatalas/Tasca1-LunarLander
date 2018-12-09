/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
 *
 * @author Ale
 */
public class ReadConfigFile extends HttpServlet {
    
    @Override
    //Sends the game's configurations read from the config.xml file to the server.
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //Get XML file location
            ServletContext context = getServletContext();
            String fullPath = context.getRealPath("xml/config.xml");
            File configFile = new File(fullPath);
            
            JAXBReader reader = new JAXBReader();
            Configuraciones configuraciones = reader.readXMLConfig(configFile);
            
            //marshall object to string xml
            StringWriter sw = new StringWriter();
            JAXB.marshal(configuraciones, sw);
            String xmlString = sw.toString();
            
            //return XML
            response.setContentType("text/xml");
            PrintWriter pw = response.getWriter();
            pw.println(xmlString);
            
        } catch (IOException | JAXBException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println("{\"error\":\"Ha sido leer el fichero de configuracion\"}");
        }
    }
}
