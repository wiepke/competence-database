package uzuzjmd.competence.config;

import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ResourceConfig;
import uzuzjmd.competence.persistence.dao.DBInitializer;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class MyRESTAPIApp extends ResourceConfig {

	private Logger logger = org.apache.log4j.LogManager
			.getLogger(MyRESTAPIApp.class);

	public MyRESTAPIApp() {
		packages("uzuzjmd.competence");
		register(org.glassfish.jersey.filter.LoggingFilter.class);
		property(
				"jersey.config.beanValidation.enableOutputValidationErrorEntity.server",
				"true");

		logger.info("Initiated Logger");
		logger.info("Initiated Server");
		DBInitializer.init();
	}
}