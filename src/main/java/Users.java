import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;



@Path("users")
public class Users {
	
	HashMap<String, User> users = new HashMap<>();
	
	public Users(){
		User user = new User("Anton","Lugnet");
		users.put(user.getUsername(), user);
	}
	
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_XML)
	@Path("/getUser/{username}&{password}")
	public User getUser(@PathParam("username") String username, @PathParam("password") String password){
		User user = new User("Anton", "Lugnet");
		return user;
	}
	
	@PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
	@Path("/create/{username}&{password}")
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
