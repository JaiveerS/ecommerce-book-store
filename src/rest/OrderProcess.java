package rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import model.BOOKSTORE;

@Path("order")
public class OrderProcess {
	
	public OrderProcess() {
		
	}
	
	@GET
	@Path("/getOrder/")
	@Produces("text/plain")
	public String getOrdersByPartNumber(@QueryParam("partNumber") String partNumber) throws Exception {
		return BOOKSTORE.getInstance().exportAllPurchase(partNumber);
	}
}
