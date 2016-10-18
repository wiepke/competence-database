package uzuzjmd.competence.main;

import config.MagicStrings;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import uzuzjmd.competence.evidence.service.rest.EvidenceServiceRestServerImpl;
import uzuzjmd.competence.persistence.dao.DBInitializer;
import uzuzjmd.competence.service.rest.*;
import uzuzjmd.competence.util.CrossOriginResourceSharingFilter;

import javax.ws.rs.ProcessingException;
import java.io.IOException;
import java.net.BindException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * starts the rest server as grizzly standalone java program
 */
public class RestServer {
    static Logger logger = LogManager
            .getLogger(RestServer.class.getName());
    private static HttpServer server;

    public static void main(String[] args)
            throws IllegalArgumentException,
            NullPointerException, IOException,
            ProcessingException, URISyntaxException {
        startServer();
    }


    public static void startServer() throws IOException,
            ProcessingException, URISyntaxException, BindException {
        logger.debug("Entering startServer");
        System.out
                .println("usage is java - jar *.version.jar");
        System.out
                .println("plz configure evidenceserver.properties");


        ResourceConfig resourceConfig = new ResourceConfig(
                CompetenceServiceRestJSON.class, RecommenderApiImpl.class,
                EvidenceServiceRestServerImpl.class,
                CrawlerServiceRest.class,
                CompetenceApiImpl.class,
                CourseApiImpl.class,
                EvidenceApiImpl.class,
                LearningTemplateApiImpl.class,
                RecommenderApiImpl.class,
                UserApiImpl.class,
                ProgressApiImpl.class,
                ActivityApiImpl.class);

        // add CORS header filter
        resourceConfig.register(CrossOriginResourceSharingFilter.class);
        //resourceConfig.register(JacksonFeature.class);

        server = GrizzlyHttpServerFactory.createHttpServer(new URI(
                        MagicStrings.RESTURLCompetence),
                resourceConfig);

        // register shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("Stopping server..");
                server.shutdownNow();
            }
        }, "shutdownHook"));


        // run
        try {
            server.start();
            System.out
                    .println("publishing competence server to to "
                            + MagicStrings.RESTURLCompetence);
            System.out
                    .println("Test this with: "
                            + MagicStrings.RESTURLCompetence
                            + "/competences/competencetree/university");
            logger.info("Press CTRL^C to exit..");
            Thread.currentThread().join();
        } catch (Exception e) {
            logger.error(
                    "There was an error while starting Grizzly HTTP server.", e);
        }
        logger.info("Published HTTP Server to "
                + MagicStrings.RESTURLCompetence);
        Logger logJena = Logger
                .getLogger("com.hp.hpl.jena");
        logJena.setLevel(Level.WARN);
        logger.debug("Leaving startServer");

    }

    public static void singleStartServer() throws URISyntaxException {
        logger.debug("Entering singleStartServer");
        System.out
                .println("usage is java - jar *.version.jar");
        System.out
                .println("plz configure evidenceserver.properties");

        ResourceConfig resourceConfig = new ResourceConfig(
                CompetenceServiceRestJSON.class, RecommenderApiImpl.class,
                EvidenceServiceRestServerImpl.class, CrawlerServiceRest.class);


        // add CORS header filter
        resourceConfig.register(CrossOriginResourceSharingFilter.class);

        server = GrizzlyHttpServerFactory.createHttpServer(new URI(
                        MagicStrings.RESTURLCompetence),
                resourceConfig);

        // run
        try {
            server.start();
            //Thread.currentThread().join();
        } catch (Exception e) {
            logger.error(
                    "There was an error while starting Grizzly HTTP server.", e);
        }
        logger.info("Published HTTP Server to "
                + MagicStrings.RESTURLCompetence);
        Logger logJena = Logger
                .getLogger("com.hp.hpl.jena");
        logJena.setLevel(Level.WARN);

        logger.debug("Leaving singleStartServer");
    }

    public static void stopServer() {
        server.shutdownNow();
        logger.debug("Stopped Server.");
    }


}
