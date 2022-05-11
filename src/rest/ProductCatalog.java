package rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import model.BOOKSTORE;

@Path("product")
public class ProductCatalog{
	
	@GET
	@Path("/read/")
	@Produces("text/plain")
	public String getProductInfo(@QueryParam("productId")String productId) throws Exception {
		BOOKSTORE instance = BOOKSTORE.getInstance();
		String n = instance.exportBookJson(productId);
		return n;
	}
}
