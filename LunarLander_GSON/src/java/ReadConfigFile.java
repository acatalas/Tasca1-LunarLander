/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ale
 */
public class ReadConfigFile extends HttpServlet {

    @Override
    //Sends the game's configurations read from the config.xml file to the server.
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Get path to the config.json file
        ServletContext context = getServletContext();
        String fullPath = context.getRealPath("json/config.json");
        File configFile = new File(fullPath);

        try {
            //Creates a new Gson object
            Gson gson = new Gson();
            
            //Reads the configurations from the file into a Configuraciones object.
            Configuraciones configuraciones = gson.fromJson(new FileReader(fullPath), Configuraciones.class);
            String json = gson.toJson(configuraciones);
          
            //Sends the json string to the server
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println(json);
            
        } catch (IOException e) {
            //Send a response to the web page with the failure message
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println("{\"error\":\"Ha sido imposible recuperar los datos\"}");
        }
    }
}
