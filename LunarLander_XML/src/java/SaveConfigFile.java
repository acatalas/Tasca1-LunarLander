/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import generated.Configuraciones;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author Ale
 */
public class SaveConfigFile extends HttpServlet {
    //Saves the user's configuration to the config.xml file without overwriting
    //the existing configurations
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Obtain the configuration's parameters sent by POST
        String nombre = request.getParameter("nombre");
        String nave = request.getParameter("nave");
        String modoDificil = request.getParameter("modoDificil");

        //Obtains the configuration file's real location
        ServletContext context = getServletContext();
        String fullPath = context.getRealPath("xml/config.xml");
        File configFile = new File(fullPath);
        try {
            //Reads the stored configurations so as to not overwrite
            JAXBReader reader = new JAXBReader();
            Configuraciones configuraciones = reader.readXMLConfig(configFile);

            //Creates a new Configuracion object and adds its values
            Configuraciones.Configuracion configuracion = new Configuraciones.Configuracion();
            configuracion.setNombre(nombre);
            configuracion.setNave(nave);
            configuracion.setModoDificil(Boolean.parseBoolean(modoDificil));

            //Adds the configuration to the confgiurations read from the config file
            configuraciones.getConfiguracion().add(configuracion);

            //Writes all the configurations to the file
            reader.writeXMLConfig(configFile, configuraciones);

            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println("{\"mess\":\"Se ha guardado correctamente\"}");
        } catch (IOException | JAXBException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println("{\"error\":\"Ha sido imposible guardar los datos\"}");
        }
    }
}
