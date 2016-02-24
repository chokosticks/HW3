/**
 * Created by Rickard on 2016-02-23.
 */
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.util.JAXBResult;
import javax.xml.registry.infomodel.User;
import java.util.ArrayList;
import java.util.List;


public class HelloResourceClient {
    public static void main(String[] args) {

        String baseURL = "http://localhost:8080/HW3/rest/";

        Client cln = ClientBuilder.newClient();
        try {
            String token = cln
                    .target(baseURL + "flightItinearies/authorize/Anton&Dahlin")
                    .request(MediaType.TEXT_PLAIN).get(String.class);
            System.out.println(token);



            Response res = cln
                    .target(baseURL + "flightItinearies/getFlightItinearies/Stockholm&Moskva/"+token)
                    .request(MediaType.APPLICATION_XML)
                    .get();
            ArrayList<FlightItinerary> response = res.readEntity(new GenericType<ArrayList<FlightItinerary>>(){});
            
            for(FlightItinerary fi: response){
                for(Flight fl: fi.getFlights()){
                    System.out.println(fl.toString());
                }
            }



            Response res1 = cln
                    .target(baseURL + "flightItinearies/getFlightPrices/"+token)
                    .request(MediaType.APPLICATION_XML)
                    .get();

            List<Flight> response1 = res1.readEntity(new GenericType<List<Flight>>(){});



//            String resp2 = cln
//                    .target(baseURL + "users/create/Rickard&Zwahlen")
//                    .request(MediaType.TEXT_PLAIN)
//                    .buildPut(user);
//            System.out.println(resp2);



        }catch(NotAuthorizedException ex){
            System.out.println("Not authorized");
        }

//        try {
//            String resp = cln
//                    .target(baseURL + "flightItinearies/bookItinerary/2"+token)
//                    .request(MediaType.TEXT_PLAIN).get(String.class);
//            System.out.println(resp);
//        }catch(NotAuthorizedException ex){
//            System.out.println("Not authorized");
//        }




    }
}
