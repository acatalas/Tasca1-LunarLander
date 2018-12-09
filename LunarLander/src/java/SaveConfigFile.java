/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
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
    /**
     * Handles the HTTP <code>POST</code> method. Saves the game's current
     * configuration
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Read the request's parameters
        String modoDificil = request.getParameter("modoDificil");
        String tipoNave = request.getParameter("nave");

        //Get the path to the config.json file
        ServletContext context = getServletContext();
        String fullPath = context.getRealPath("json/config.json");

        //Prepare the content that will be written into the file
        String fileContent = "{\"modoDificil\":" + modoDificil + ",\"nave\":\""
                + tipoNave + "\"}";

        try {
            //Get the config.json file and write the new configuration into the file
            File f = new File(fullPath);
            FileWriter fw = new FileWriter(f);
            fw.write(fileContent);
            fw.close();

            //Send a response to the web page with the success message
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println("{\"mess\":\"El fichero ha sido guardado correctamente\"}");
            
        } catch (IOException e) {
           //If anything fails, send a JSON object with the error message
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println("{\"error\":\"Ha sido imposible guardar los datos\"}");
        }
    }
}
