import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("rest")
public class ApplicationPath extends Application {

	public java.util.Set<java.lang.Class<?>> getClasses() {
		return new HashSet<>(Arrays.asList(FlightItinearies.class, Flight.class, Airports.class, Airport.class, Booking.class, FlightItinerary.class, Users.class, User.class));
	};
}
