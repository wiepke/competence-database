package uzuzjmd.competence.service.rest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;

public class RestUtil {
	/**
	 * utilityfunction to add browser caches
	 * 
	 * @param result
	 * @param cache
	 * @return
	 */
	public static Response buildCachedResponse(Object result, Boolean cache) {
		if (cache) {
			CacheControl control = new CacheControl();
			control.setMaxAge(3);
			Response response = Response.status(200).entity(result).cacheControl(control).build();
			return response;
		} else {
			Response response = Response.status(200).entity(result).build();
			return response;
		}

	}

	/**
	 * utility to convert booleans given as path parameters
	 * 
	 * @param compulsory
	 * @return
	 */
	public static Boolean convertCompulsory(String compulsory) {
		if (compulsory == null) {
			throw new WebApplicationException(new Exception("compulsory query param not optional"));
		}

		Boolean compulsoryBoolean = false;
		if (compulsory.equals("true")) {
			compulsoryBoolean = true;
		}
		return compulsoryBoolean;
	}
}
