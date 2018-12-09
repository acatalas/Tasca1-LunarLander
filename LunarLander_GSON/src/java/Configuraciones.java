
import java.util.ArrayList;
import java.util.List;

//Class representation of the configuration array container
public class Configuraciones {

    private List<Configuracion> configuraciones;

    //Default constructor
    public Configuraciones() {
        configuraciones = new ArrayList<>();
    }

    //adds a Configuration object to the list od Configurations
    public void addConfiguracion(Configuracion configuracion) {
        configuraciones.add(configuracion);
    }

    //returns the list of configurations
    public List<Configuracion> getConfiguraciones() {
        return configuraciones;
    }

    //sets the list of configurations to the one passed by paramater
    public void setConfiguraciones(List<Configuracion> configuraciones) {
        this.configuraciones = configuraciones;
    }

    @Override
    public String toString() {
        String result = "Configuraciones{[\n";
        for (Configuracion configuracion : configuraciones) {
            result += configuracion.toString() + "\n";
        }
        result += "]}";
        return result;
    }
}
