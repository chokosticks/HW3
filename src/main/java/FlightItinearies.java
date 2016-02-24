//import flightitinearies.ns.AuthService;
import com.google.common.collect.Lists;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.*;


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
    private static Users users = new Users();

    public FlightItinearies(){
        System.out.println("Flight Itinearies Service Started");
        authorized = false;
    }




    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("authorize/{username}&{password}")
    public String authorize(@PathParam("username") String username, @PathParam("password") String password){

        User user = users.getUser(username, password);

        if(user != null)
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
    @Path("/getFlightItinearies/{departureCity}&{destinationCity}/{token}")
    public Response/*List<FlightItinerary>*/ getFlightItinearies(@PathParam("token") String token, @PathParam("departureCity") String departureCity, @PathParam("destinationCity") String destinationCity){
        ArrayList<FlightItinerary> result = null;

        if(token.equals("ABCJL8769xzvf")){
            authorized = true;
        }
        
        if(authorized)
        {
            result = new ArrayList<FlightItinerary>();

            if(!airports.airportExists(destinationCity) || !airports.airportExists(departureCity)) {
                System.out.println("Non existing airport specified");
//                return result;
            }

            Airport finalDestination = airports.getAirport(destinationCity);
            Airport departureAirport = airports.getAirport(departureCity);

            initDFSVisit();

            result = DFS(departureAirport, finalDestination);
            itineraryResult = result;
            setItineraryId();

            GenericEntity<ArrayList<FlightItinerary>> entity = new GenericEntity<ArrayList<FlightItinerary>>(Lists.newArrayList(result)){};

            return Response.ok(200).entity(entity).build();
        }else{
            throw new NotAuthorizedException("Not authorized");
        }


    }

    private void setItineraryId(){
        int id = 0;
        for(FlightItinerary fi: itineraryResult){
            fi.setId(id);
            id++;
        }
    }


    @GET @Produces(MediaType.TEXT_PLAIN) @Consumes(MediaType.TEXT_PLAIN) @Path("/greet")
    public String sayHello() {
        return "Hello RESTful World!";
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("getFlightPrices/{date}/{token}")
    public Response getFlightPrices(@PathParam("date") String date, @PathParam("token") String token)
    {
        List<Flight> resultList = null;

        if(token.equals("ABCJL8769xzvf")){
            authorized = true;
        }

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
            GenericEntity<List<Flight>> entity = new GenericEntity<List<Flight>>(Lists.newArrayList(resultList)){};

            return Response.ok(200).entity(entity).build();
        }else
            throw new NotAuthorizedException("Not authorized");
    }

    @PUT
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("bookItinerary/{id}&{creditCardNumber}/{token}")
    public String bookItinerary(@PathParam("id") int id, @PathParam("creditCardNumber") int creditCardNumber, @PathParam("token") String token){

        if(token.equals("ABCJL8769xzvf")){
            authorized = true;
        }

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
            throw new NotAuthorizedException("Not authorized");
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/issueTickets/{creditCardNumber}/{token}")
    public String issueTickets(@PathParam("creditCardNumber") int creditCardNumber, @PathParam("token") String token){

        if(token.equals("ABCJL8769xzvf")){
            authorized = true;
        }

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
            throw new NotAuthorizedException("Not authorized");
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

