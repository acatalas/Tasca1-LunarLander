/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ale
 */
public class SaveConfigFile extends HttpServlet {

     //Saves the new configuration's parameters and adds it to the config.json
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Read the configuration's attributes
        String nombre = request.getParameter("nombre");
        String modoDificil = request.getParameter("modoDificil");
        String tipoNave = request.getParameter("nave");

        try {
            //Obtains the configuration file's real location
            ServletContext context = getServletContext();
            String fullPath = context.getRealPath("json/config.json");
            File configFile = new File(fullPath);

            //Creates a new Gson object
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            //Obtain the configurations already in the configuration file
            Configuraciones configuraciones = gson.fromJson(
                    new FileReader(configFile), Configuraciones.class);

            //Adds the new configuration to the configurations
            configuraciones.addConfiguracion(
                    new Configuracion(nombre, tipoNave, Boolean.valueOf(modoDificil)));

            //Writes the configurations to the config.json file
            FileWriter fw = new FileWriter(configFile);
            gson.toJson(configuraciones, fw);
            fw.close();

            //Send a response to the web page with the success message
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println("{\"mess\":\"El fichero ha sido guardado correctamente\"}");

        } catch (IOException e) {

            //Send a response to the web page with the failure message
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println("{\"error\":\"Ha sido imposible guardar los datos\"}");
        }
    }
}
