import com.sun.research.ws.wadl.*;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder;


import java.util.HashMap;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;


@Path("users")
public class Users {

	private static HashMap<String, User> users = new HashMap<>();

	public Users(){
		User user = new User("Anton","Dahlin");
        User user1 = new User("Rickard","Zwahlen");
        users.put(user.getUsername(), user);
        users.put(user1.getUsername(), user1);
	}

	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("user/{username}&{password}")
	public User getUser(@PathParam("username") String username, @PathParam("password") String password){
		if(!users.containsKey(username)){
            throw new NotFoundException("user not found");
		}else if(!users.get(username).getPassword().equals(password)){
            throw new NotAuthorizedException("Not authorized!");
        }else {
            return users.get(username);
        }
	}

	@PUT
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_XML)
	@Path("create")///{username}&{password}")
	public Response createUser(JAXBElement<User> user/*@PathParam("username") String username, @PathParam("password") String password*/){

        User newUser = user.getValue();
		if(!users.containsKey(newUser.getUsername())){

            users.put(newUser.getUsername(), newUser);
            return Response.ok(200).build();
		}else
            throw new BadRequestException("User already exists");
	}

    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("delete/{username}&{password}")
    public String deleteUser(@PathParam("username") String username, @PathParam("password") String password){
        if(!users.containsKey(username)){
            return "User doesn't exist!";
        }else if(!users.get(username).getPassword().equals(password)){
            throw new NotAuthorizedException("Wrong password!");
        }else{
            users.remove(username);
            return "Success! User "+username+" deleted!";
        }
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("update/{username}&{password}/password/{newpassword}")
    public String deleteUser(@PathParam("username") String username, @PathParam("password") String password, @PathParam("newpassword") String newPassword){
        if(!users.containsKey(username)){
            throw new NotAuthorizedException("User doesn't exists!");
        }else if(!users.get(username).getPassword().equals(password)){
            throw new NotAuthorizedException("Wrong password!");
        }else{
            users.get(username).setPassword(newPassword);
            return "Success! Password updated";
        }
    }

    public static HashMap<String, User> getUsers() {
        return users;
    }

    public static void setUsers(HashMap<String, User> users) {
        Users.users = users;
    }
}
