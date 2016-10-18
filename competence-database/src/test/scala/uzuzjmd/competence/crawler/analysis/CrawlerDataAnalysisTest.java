package uzuzjmd.competence.crawler.analysis;

//import config.MagicStrings;
//import datastructures.Pair;
//import mysql.VereinfachtesResultSet;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang.ArrayUtils;
//import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
//import org.junit.Test;
//
//import scala.Predef;
//import scala.collection.immutable.List;
//import uzuzjmd.competence.crawler.mysql.MysqlConnector;
//
import java.util.*;

import static org.junit.Assert.assertFalse;

/**
 * Created by dehne on 07.04.2016.
 */
public class CrawlerDataAnalysisTest {

    /*
    static private final Logger logger = LogManager.getLogger(CrawlerDataAnalysisTest.class.getName());
    @Test
    public void testSelectRelevantDataForPlotting() throws Exception {
        String databaseName = "unidisk";
        String tableName = "UnitTest_varMeta";
        Integer min = 10;
        Integer max = 25;


        CrawlerDataAnalysis cda = new CrawlerDataAnalysis(min, max, "UnitTest");
        cda.prepareHochschuleSolrAnalyse("var6");



        double[] values = ArrayUtils.toPrimitive(cda.inputData.keySet().toArray(new Double[0]));

        logger.debug("There are " + values.length + " elements in the table");
        if (values.length > max) {
            Collection<String> result2 = cda.selectRelevantDataForPlotting();
            cda.deleteInDatabase(result2, "var6");
            assertFalse(result2.isEmpty());
            logger.debug(StringUtils.join(result2.toArray(), ", "));
        } else {
            logger.debug("The results in the table don't exceed the maximum limit.");
        }

    }
    */
}