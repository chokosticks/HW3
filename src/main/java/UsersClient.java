import javax.print.attribute.standard.Media;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBElement;

/**
 * Created by Rickard on 2016-02-25.
 */
public class UsersClient
{
    public static void main(String args[])
    {
        String baseURL = "http://localhost:8080/HW3/rest/";

        Client cln = ClientBuilder.newClient();

        //GET USER (GET)--------------------------------------------------------------------
        GenericType<JAXBElement<User>> erasure = new GenericType<JAXBElement<User>>(){};

        JAXBElement<User> responseUser = cln
                .target(baseURL+"users/user/Anton&Dahlin")
                .request(MediaType.APPLICATION_XML)
                .get(erasure);

        System.out.println(responseUser.getValue().getUsername());
        System.out.println(responseUser.getValue().getPassword());
        System.out.println("\n\n");



        //NEW USER (PUT)---------------------------------------------------------------------
        User newUser = new User("Kalle", "Anka");

        Response newUserResponse = cln
                .target(baseURL + "users/create")
                .request(MediaType.TEXT_PLAIN)
                .put(Entity.entity(newUser, MediaType.APPLICATION_XML));

        if(newUserResponse.getStatusInfo().getReasonPhrase().equals("Bad Request")){
            System.out.println("User already exists");
        }else{
            System.out.println("User successfully created");
        }

        JAXBElement<User> responseNewUser = cln
                .target(baseURL+"users/user/"+newUser.getUsername()+"&"+newUser.getPassword())
                .request(MediaType.APPLICATION_XML)
                .get(erasure);



        System.out.println(responseNewUser.getValue().getUsername());
        System.out.println(responseNewUser.getValue().getPassword());
        System.out.println("\n\n");


        //UPDATE USER (POST)------------------------------------------------------------------
        String newPassword = "Collary";

        User updatedUser = new User("Kalle", newPassword);

        Response updatedUserResponse = cln
                .target(baseURL+"users/update/"+newUser.getPassword())
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.entity(updatedUser, MediaType.APPLICATION_XML));


        JAXBElement<User> responseUpdatedUser = cln
                .target(baseURL+"users/user/"+updatedUser.getUsername()+"&"+updatedUser.getPassword())
                .request(MediaType.APPLICATION_XML)
                .get(erasure);



        System.out.println(responseUpdatedUser.getValue().getUsername());
        System.out.println(responseUpdatedUser.getValue().getPassword());
        System.out.println("\n\n");



        //DELETE USER (DELETE)-----------------------------------------------------------------
        String deleteResponse = cln
                .target(baseURL+"users/delete/"+updatedUser.getUsername()+"&"+updatedUser.getPassword())
                .request(MediaType.TEXT_PLAIN)
                .delete(String.class);

        System.out.println(deleteResponse);

        try {
            JAXBElement<User> responseDeletedUser = cln
                    .target(baseURL + "users/user/" + updatedUser.getUsername() + "&" + updatedUser.getPassword())
                    .request(MediaType.APPLICATION_XML)
                    .get(erasure);

            System.out.println(responseDeletedUser.getValue().getUsername());
            System.out.println(responseDeletedUser.getValue().getPassword());
            System.out.println("\n\n");

        }catch(NotFoundException ex){
            System.out.println("User not found");
        }








    }
}
