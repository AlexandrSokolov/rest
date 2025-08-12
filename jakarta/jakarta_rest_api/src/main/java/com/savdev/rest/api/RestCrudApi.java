package com.savdev.rest.api;


import com.savdev.rest.dto.Item;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path(RestCrudApi.BASE_URL)
@Produces(MediaType.APPLICATION_JSON)
public interface RestCrudApi {
  String BASE_URL = "/jakarta/rest/test/api/items";

  @GET
  List<Item> getItems();

  @GET
  @Path("/{id : \\d+}") //support digit only
  Item getById(@PathParam("id") Long itemId);


  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  Item newItem(Item item);
}
