import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.ws.WebServiceProvider;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by antondahlin on 2016-02-12.
 */

@XmlRootElement
public class FlightItinerary {

    private int id;
    private ArrayList<Flight> flights = new ArrayList<>();
    private int price = 0;
    private double totalPrice = 0;


    public FlightItinerary(ArrayList<Flight> flights)
    {
        setFlights(flights);
        Random r = new Random();
        id = r.nextInt(9999-1000+1) + 1000;
    }

    public FlightItinerary()
    {

    }


    public void setFlights(ArrayList<Flight> flights) {
        this.flights.clear();
        this.flights.addAll(flights);
        price = 0;
        for(Flight f: flights)
        {
            price += Math.floor(f.getPrice());
        }
    }

    public ArrayList<Flight> getFlights() {
        return flights;
    }

    public void addFlight(Flight flight){
        this.flights.add(flight);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice()
    {
        return price;
    }

    public double getTotalPrice(){
        for(Flight flight: flights){
            totalPrice += flight.getPrice();
        }
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice){
        this.totalPrice = totalPrice;
    }
}
