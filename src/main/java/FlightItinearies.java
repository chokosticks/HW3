//import flightitinearies.ns.AuthService;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


/**
 * Created by antondahlin on 2016-02-08.
 */


@Path("/flightItinearies")
public class FlightItinearies {
	
    private static HashMap<Airport, Boolean> visited = new HashMap<Airport, Boolean>();
    private static Airports airports = new Airports();
    private static ArrayList<FlightItinerary> itineraryResult = new ArrayList<>();
    private static ArrayList<Booking> bookedItinearies = new ArrayList<>();
    private static String username = "webservice";
    private static String password = "password";
    private static boolean authorized = false;

    public FlightItinearies(){
        System.out.println("Flight Itinearies Service Started");
        authorized = true;
    }
    
    
    


    public String authorize(String username, String password){
        if(this.username.equals(username) && this.password.equals(password))
        {
            authorized = true;
            return "ABCJL8769xzvf";
        }
        else
            authorized = false;
            return "Wrong credentials";
    }

    
    @GET
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/getFlightItinearies/{departureCity}&{destinationCity}")
    public List<FlightItinerary> getFlightItinearies(@PathParam("departureCity") String departureCity, @PathParam("destinationCity") String destinationCity){
        ArrayList<FlightItinerary> result = null;
        
        if(authorized)
        {
            result = new ArrayList<FlightItinerary>();

            if(!airports.airportExists(destinationCity) || !airports.airportExists(departureCity)) {
                System.out.println("Non existing airport specified");
                return result;
            }

            Airport finalDestination = airports.getAirport(destinationCity);
            Airport departureAirport = airports.getAirport(departureCity);

            initDFSVisit();

            result = DFS(departureAirport, finalDestination);
            itineraryResult = result;
            setItineraryId();
            }
        return result;
    }

    private void setItineraryId(){
        int id = 0;
        for(FlightItinerary fi: itineraryResult){
            fi.setId(id);
            id++;
        }
    }


    @GET @Produces("text/plain") @Path("/greet")
    public String sayHello() {
        return "Hello RESTful World!";
    }

    public List<Flight> getFlightPrices(String date)
    {
        List<Flight> resultList = null;
        if(authorized)
        {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date d = new Date();
            try
            {
                d = df.parse(date);
            } catch (ParseException e)
            {
                e.printStackTrace();
            }
            resultList = new ArrayList<Flight>();
            for (Airport a : airports.getAirports())
            {
                for (Flight f : a.getFlights())
                {
                    if (df.format(d).equals(df.format(f.getDepartureDate())))
                    {
                        resultList.add(f);
                        System.out.println(f.toString());
                    }
                }
            }
        }
        return resultList;
    }


    public String bookItinerary(int id, int creditCardNumber){

        if(authorized)
        {
            if (id < itineraryResult.size())
            {
                bookedItinearies.add(new Booking(creditCardNumber, itineraryResult.get(id)));
                String from, to = "";
                Booking booking = bookedItinearies.get(bookedItinearies.size() - 1);
                from = booking.getItinerary().getFlights().get(0).getDepartureCity();
                to = booking.getItinerary().getFlights().get(booking.getItinerary().getFlights().size() - 1).getDestinationCity();
                return "Booking succeeded for flight itinerary id: " + id + " from " + from + " to " + to + " for " + booking.getItinerary().getPrice();
            } else
                return "No itinerary with that id exists";
        }
        else
        {
            return "User is not authorized";
        }
    }


    public String issueTickets(int creditCardNumber){
        if(authorized)
        {
            int itineraryID = 0;
            for (Booking booking : bookedItinearies)
            {
                if (booking.getCreditCardNumber() == creditCardNumber)
                {
                    return "Tickets issued for itinerary id: " + booking.getItinerary().getId();

                }
            }
            return "No booking found for your creditcard number";
        }
        else
            return "User is not authorized";
    }

    private void initDFSVisit(){
        for(Airport airport: airports.getAirports()){
            visited.put(airport,false);
        }
    }


    private ArrayList<FlightItinerary> DFS(Airport currentAirport, Airport finalDestination){
        ArrayList<Flight> resultFlights = new ArrayList<>();
        ArrayList<FlightItinerary> allItinerary = new ArrayList<>();
        DFSRec(currentAirport, finalDestination, resultFlights, allItinerary);
        return allItinerary;
    }

    private void DFSRec(Airport currentAirport, Airport finalDestination, ArrayList<Flight> resultFlights, ArrayList<FlightItinerary> allItinerary){

        if(visited.get(currentAirport)){
            return;
        }

        visited.put(currentAirport, true);

        if(currentAirport.equals(finalDestination)){
            System.out.println("New Itinerary");
            FlightItinerary newFlightItinerary = new FlightItinerary();
            newFlightItinerary.setFlights(resultFlights);
            allItinerary.add(newFlightItinerary);
            return;
        }

        for(Flight flight: currentAirport.getFlights()){
            System.out.println("DFS-From: "+currentAirport.getName()+" : " +" DFS-To: "+ airports.getAirport(flight.getDestinationCity()).getName());

            resultFlights.add(flight);
            DFSRec(airports.getAirport(flight.getDestinationCity()), finalDestination, resultFlights, allItinerary);

            resultFlights.remove(resultFlights.size()-1);
            visited.put(airports.getAirport(flight.getDestinationCity()), false);
        }
    }


    public static void main(String[] args){
//        Endpoint.publish("http://0.0.0.0:1337/FlightItinearies", new FlightItinearies());
        //        Endpoint.publish("http://0.0.0.0:1337/AuthService", new AuthServiceImpl());

    }
}

