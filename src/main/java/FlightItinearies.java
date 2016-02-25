//import flightitinearies.ns.AuthService;

import jersey.repackaged.com.google.common.collect.Lists;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBElement;


/**
 * Created by antondahlin on 2016-02-08.
 */


@Path("/flightItinearies")
public class FlightItinearies {



    private static HashMap<Airport, Boolean> visited = new HashMap<Airport, Boolean>();
    private static Airports airports = new Airports();
    private static ArrayList<FlightItinerary> itineraryResult = new ArrayList<>();
    private static HashMap<Integer, Booking> bookedItinearies = new HashMap<>();
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
    public Response getFlightItinearies(@PathParam("token") String token, @PathParam("departureCity") String departureCity, @PathParam("destinationCity") String destinationCity){
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

    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/addFlights")
    public String addFlight(JAXBElement<Flight> flightJAXBElement){

        Flight flight = flightJAXBElement.getValue();
        for(Airport airport : airports.getAirports())
        {
            if(airport.getName().equalsIgnoreCase(flight.getDepartureCity()))
            {
                airport.getFlights().add(flight);
            }
        }
        return "Success.";
    }


    @GET @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/greet")
    public String sayHello() {
        return "Hello RESTful World!";
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("getFlightPrices/{date}/{token}")
    public Response getFlightPrices(@PathParam("date") String date, @PathParam("token") String token)
    {
        ArrayList<Flight> resultList = null;

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
            GenericEntity<ArrayList<Flight>> entity = new GenericEntity<ArrayList<Flight>>(Lists.newArrayList(resultList)){};

            return Response.ok(200).entity(entity).build();
        }else
            throw new NotAuthorizedException("Not authorized");
    }

    @PUT
    @Produces({MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_XML,MediaType.TEXT_PLAIN})
    @Path("bookItinerary/{token}")//{id}&{creditCardNumber}/{token}")
    public Response bookItinerary(JAXBElement<Booking> booking, @PathParam("token") String token){

        Booking newBooking = booking.getValue();

        if(token.equals("ABCJL8769xzvf")){
            authorized = true;
        }

        if(authorized) {

            if(!bookedItinearies.containsKey(newBooking.getCreditCardNumber())){
                bookedItinearies.put(newBooking.getCreditCardNumber(), newBooking);
                return Response.ok(200).build();
            }else{
                throw new BadRequestException();
            }
        }else{
            throw new NotAuthorizedException("Not authorized");
        }
    }

    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/issueTickets/{creditCardNumber}/{token}")
    public String issueTickets(@PathParam("creditCardNumber") int creditCardNumber, @PathParam("token") String token){

        if(token.equals("ABCJL8769xzvf")){
            authorized = true;
        }

        if(authorized)
        {
            if(bookedItinearies.containsKey(creditCardNumber)){

                Booking booking = bookedItinearies.get(creditCardNumber);
                bookedItinearies.remove(creditCardNumber);
                return "Tickets issued for itinerary: "+booking.getItinerary().getId();
            }else{
                return "No booking found with creditcard number: "+creditCardNumber;
            }
        }
        else
            throw new NotAuthorizedException("Not authorized");
    }






    //HELP FUNCTIONS
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

