import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by antondahlin on 2016-02-15.
 */

@XmlRootElement

public class Booking {

    private int creditCardNumber = 0;
    private FlightItinerary itinerary = new FlightItinerary();

    public Booking(){}

    public Booking(int creditCardNumber, FlightItinerary flightItinerary) {
        this.creditCardNumber = creditCardNumber;
        this.itinerary = flightItinerary;
    }

    public FlightItinerary getItinerary() {
        return itinerary;
    }

    public void setItinerary(FlightItinerary itinerary) {
        this.itinerary = itinerary;
    }

    public int getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(int creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }
}
