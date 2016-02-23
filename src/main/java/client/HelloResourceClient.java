package client;

/**
 * Created by Rickard on 2016-02-23.
 */
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
public class HelloResourceClient {
    public static void main(String[] args) {
        Client cln = ClientBuilder.newClient();
        String resp = cln
                .target("http://localhost:8080/HW3/rest/flightItinearies/greet")
                .request("text/plain").get(String.class);
        System.out.println(resp);
    }
}
