/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
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
    /**
     * Handles the HTTP <code>GET</code> method. Reads the config.json file and
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
            //Get path to the config.json file
            ServletContext context = getServletContext();
            String fullPath = context.getRealPath("json/config.json");
            File configFile = new File(fullPath);
            
            try {
            //Create RandomAccessFile to read the configuration file
            RandomAccessFile frar = new RandomAccessFile(configFile, "r");

            //Save the file's contents
            String jsonString = frar.readLine();
            frar.close();

            //Send the file's contents (JSON object)
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println(jsonString);

        } catch (IOException e) {

            //If anything fails, send a JSON object with the error message
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println("{\"error\":\"Ha sido imposible recuperar los datos\"}");
        }
    }

}
