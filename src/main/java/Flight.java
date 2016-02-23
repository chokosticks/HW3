import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.ws.WebServiceProvider;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by antondahlin on 2016-02-08.
 */

@XmlRootElement
public class Flight{

    private String departureCity;
    private String destinationCity;
    private Date departureDate;
    private double price;
    private int availableSeats;

    public Flight(){}

    public Flight(String departureCity, String destinationCity, Date departureDate, double price, int availableSeats){
        this.departureCity = departureCity;
        this.destinationCity = destinationCity;
        this.departureDate = departureDate;
        this.price = price;
        this.availableSeats = availableSeats;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        sb.append("From: "+this.getDepartureCity()+"\n").
        append("To: "+this.getDestinationCity()+"\n").
        append("Price: "+this.getPrice()+" \nDeparture date: "+df.format(this.getDepartureDate())+"\n").
        append("Available seats: "+this.getAvailableSeats()+"\n");

        return sb.toString();

    }
}
