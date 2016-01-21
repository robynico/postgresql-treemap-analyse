package fr.postgresql.treemap.ws;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import fr.postgresql.treemap.api.DatabaseAnalyseService;

@Path("/")
@Produces({ MediaType.APPLICATION_JSON })
public class TreemapWS {

	@Inject
	DatabaseAnalyseService service;

	@GET
	@Path("/analyse")
	public List<Object> analyse() throws Exception {
		return service.analyse();
	}
}
