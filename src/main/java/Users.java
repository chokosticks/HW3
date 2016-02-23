import java.util.HashMap;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;




@Path("users")
public class Users {

	private static HashMap<String, User> users = new HashMap<>();


	
	public Users(){
		User user = new User("Anton","Dahlin");
        User user1 = new User("Rickard","Zwahlen");
        users.put(user.getUsername(), user);
        users.put(user.getUsername(), user);
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
	@Consumes(MediaType.TEXT_PLAIN)
	@Path("create/{username}&{password}")
	public String createUser(@PathParam("username") String username, @PathParam("password") String password){
		if(!users.containsKey(username)){
			User newUser = new User(username, password);
			users.put(username, newUser);
			return "User "+username+" created!";
		}else
			return "User already exists!";
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
