import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.ws.WebServiceProvider;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by antondahlin on 2016-02-12.
 */
@XmlRootElement
public class Airport {

    private ArrayList<Flight> flights = new ArrayList<Flight>();
    private String name = new String();

    public Airport(){
    }

    public Airport(String name, ArrayList<Flight> flights){
        this.name = name;
        this.flights = flights;
    }

    public Flight getFlight(String departureCity, String destinationCity){
        Flight temp = new Flight();
        for(Flight flight: flights){
            if(flight.getDepartureCity().equals(departureCity) && flight.getDestinationCity().equals(destinationCity)){
                temp = flight;
            }
        }
        return temp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Flight> getFlights() {
        return flights;
    }

    public void setFlights(ArrayList<Flight> flights) {
        this.flights = flights;
    }
}
