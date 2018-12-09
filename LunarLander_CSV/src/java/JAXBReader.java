
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import generated.Configuraciones;

public class JAXBReader {
    public Configuraciones readXMLConfig(File file) throws JAXBException{
        //Create JAXB context from the classes generated via the XSD
        JAXBContext context = JAXBContext.newInstance(Configuraciones.class);

        //UnMarshalling [Generate JAVA from XML]
        //Instantiate Unmarshaller via context
        Unmarshaller um = context.createUnmarshaller();
        
        //Unmarshall the provided XML into an object
        Configuraciones configuraciones = (Configuraciones)um.unmarshal(file);
        return configuraciones;
    }
    
    public void writeXMLConfig(File file,Configuraciones configuraciones) throws JAXBException{
        // create JAXB context from the classes generated via the XSD
        JAXBContext context = JAXBContext.newInstance(Configuraciones.class);
        
        // Marshalling [Generate XML from JAVA]
        // create Marshaller using JAXB context
        Marshaller m = context.createMarshaller();
        
        // Format the [to be]generated XML output
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        
        // Marshall it and write output to file
        m.marshal(configuraciones, file);
    }
}
