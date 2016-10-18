package config;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;

public class PropUtil {

    public static HashMap<String, String> defaults;
    public static Properties prop = new Properties();
    public static Boolean firstRun = true;

    private static final String propfFileName = "evidenceserver.properties";

    private static Logger logger = LogManager
            .getLogger(PropUtil.class);

    public static boolean _amServer() {
        StackTraceElement[] elements = new Throwable()
                .getStackTrace();

        for (StackTraceElement element : elements) {
            if (element
                    .getClassName()
                    .equals("org.apache.catalina.core.StandardEngineValve")) {
                return true;
            }
        }

        return false;
    }

    public static Properties getProperties() {
        if (!firstRun) {
            return prop;
        }
        firstRun = false;
        InputStream inputStream = null;
        try {
            // find file
            System.out.println("Loading the properties-check the wiki for useful information: ");
            System.out.println("Trying to find the file in the WEB-INF/classes directory of the tomcat app");
            inputStream = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(propfFileName);
            try {
                prop.load(inputStream);
            } catch (IOException e) {
                System.out.println("Error occured during searching for the the file " + propfFileName + " in the WEB-INF/classes directory of the tomcat app");
            }
        } catch (Exception e1) {
            try {
                //To know where the file is searched, uncomment the text below
                System.out.println("Trying to find the " + propfFileName + " in the current classpath: \n" + Paths.get(".").toAbsolutePath().toString());
                inputStream = new FileInputStream(
                        propfFileName);
                prop.load(inputStream);
            } catch (FileNotFoundException e) {
                try {
                    return findPropFileInAbsolutePaths();
                } catch (Exception e2) {
                    e1.printStackTrace();
                }
            } catch (IOException e) {
                try {
                    return findPropFileInAbsolutePaths();
                } catch (Exception e3) {
                    e1.printStackTrace();
                }
            }
        }

        return prop;


    }

    public static Properties findPropFileInAbsolutePaths() throws Exception {
        String[] pathsToCheck = new String[]{"~/competence-base/" + propfFileName, "C:/Users/dehne/competence-database/" + propfFileName, "/opt/up/competence-database/" + propfFileName, "/opt/up/tomcat/7.0/webapps/competence-database-dev/WEB-INF/classes/" + propfFileName, };
        for (String s : pathsToCheck) {
            try {
                System.out.println("Trying to find the " + propfFileName + " in the current classpath: \n" + s);
                FileInputStream inputStream = new FileInputStream(
                        propfFileName);
                Properties prop = new Properties();
                prop.load(inputStream);
                return prop;
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }
        throw new Exception("could not find properties file in the paths" + pathsToCheck.toString());
    }

    public static String getProp(String key) {
        try {
            return getProperties().getProperty(key)
                    .replaceAll("\"", "");
        } catch (NullPointerException p) {
            logger.error("property " + key
                    + " is not set!!!");
        }
        return null;
    }

    /**
     * adds the rootPath as prefix
     *
     * @param key
     * @return
     */
    public static String getRelativeFileProp(String key) {
        return MagicStrings.ROOTPATH
                + getProperties().getProperty(key)
                .replaceAll("\"", "");
    }


    public static String getRelativeOrAbsoluteFileProp(
            String relativeKey, String absoluteKey) {
        if (getProperties().getProperty(relativeKey) != null) {
            return MagicStrings.ROOTPATH
                    + getProperties().getProperty(
                    relativeKey).replaceAll("\"",
                    "");
        } else {
            return getProperties().getProperty(absoluteKey)
                    .replaceAll("\"", "");
        }
    }


}
