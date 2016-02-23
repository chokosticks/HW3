import java.util.HashMap;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;




@Path("users")
public class Users {
	
	HashMap<String, User> users = new HashMap<>();


	
	public Users(){
		User user = new User("Anton","Lugnet");
		users.put(user.getUsername(), user);
	}

	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("user/{username}&{password}")
	public User getUser(@PathParam("username") String username, @PathParam("password") String password){
		if(!users.containsKey(username)){
			throw new NotFoundException("user not found");
		}
		return users.get(username);
	}

	@PUT
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	@Path("create/{username}&{password}")
	public String putUser(@PathParam("username") String username, @PathParam("password") String password){
		if(users.containsKey(username)){
			User newUser = new User(username, password);
			users.put(username, newUser);
			return "User; "+username+" created.";
		}else
			return "naaaH";
	}
	
//	@PUT("create/{username}&{password}")
//	public createUser(@PathParam String username, @PathParam String password){
//		
//	}
	
	
	
	
	
	
}
