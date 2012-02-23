package poker.server.service.player;

import javax.ejb.Remote;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import poker.server.model.player.Player;

@Remote
@Path("playerService")
public interface PlayerServiceRemote {

	@GET
	@Path("/player")
	public Player authentificate(String name, String pwd);

	@GET
	@Path("/player")
	public Player createUser(String name, String pwd);

	public Player loadPlayer(String name);

	 @GET
	 @Path("/{param}")
	 public Response testMessage(@PathParam("param") String message);
}
