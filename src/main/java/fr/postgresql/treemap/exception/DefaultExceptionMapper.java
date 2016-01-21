package fr.postgresql.treemap.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class DefaultExceptionMapper implements ExceptionMapper<Exception> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionMapper.class);

	public Response toResponse(Exception ex) {
		LOGGER.error(ex.getMessage(), ex);
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).type(MediaType.TEXT_HTML_TYPE).build();
	}

}