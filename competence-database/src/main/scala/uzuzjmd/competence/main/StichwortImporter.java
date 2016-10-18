package uzuzjmd.competence.main;

import com.google.common.collect.Lists;
import mysql.MysqlConnect;
import scala.collection.immutable.List;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by dehne on 12.04.2016.
 */
public class StichwortImporter {

    //INSERT INTO `unidisk`.`elearning_stichwort` (`Id`, `Stichwort`, `Variable`, `Metavariable`) VALUES (NULL, 'Skypekurse', 'elearning', 'delfi2016');


    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

        String[] keywordsInterviews = new String[]{
                "Skypekurse", "digitale Verbreitung", "Internetlehre", "mediengestützte Lehre und Lernen", "Blended-Learning", "virtuelle Lehre", "problemzentriertes E-Learning", "forschungsorientiertes E-Learning", " situiertes E-Learning", " kontextbezogenes E-Learning", " standortübergreifendes Lernen", " E-Learning und Didaktik", " E-Portfolio", " fächerübergreifende Lernszenarien", " Online-Vorlesung", " MOOCs", " Blended-Learning", " Game-Design", " Open Educational Resources", " Lernapps", " Game-Based-Learning", " Mobile Learning", " Personal Learning Environment", " Mediendidaktik", " Digitalisierung der Hochschule", " Web-Based-Training", " Computer-Based-Training", " Web 2.0", " Kollaboratives Lernen", " Personalisierung des Lernens"
        };

        String[] keywordsLiterature = new String[] {
                "Lernen und Lehren mittels elektronischer Medien"," E-Lernen"," electronic learning"," oder eLearning"," computerbasiertes Training"," Adaptive Lernumgebung"," individualized"," taylored"," personalized E-Learning"," Kollaborative Lernumgebungen"," Digitale Lernspiele"," Computer-Based-Training"," Web-Based-Training"," Virtuelle Seminare"," Lernplattforme"," E-Learning-Portal"," MOOCs"," Inverted Classroom"," Game-Based-Learning"," Gamifizierung"," Mobile/Apps"," Social"," Networks/Communities"," Serious Games"," Wikis"," Augmented Reality"," Lernumgebungen in virtuellen 3D-Welten"," Twitter/Micro-Blogging"," Online-Lernen"," E-Teaching"," EEducation"," Internet-basiertes Lernen"," Multimediales Lernen oder Medienbasiertes Lernen"
        };

        ArrayList<String> combinedKeywords = Lists.newArrayList(keywordsInterviews);
        ArrayList<String> keywordsLiteratureList = Lists.newArrayList(keywordsLiterature);
        combinedKeywords.addAll(keywordsLiteratureList);

        String dummy = "";
        PrintWriter writer = new PrintWriter("inserts.sql", "UTF-8");
        CreateTableLine(writer);

        for (String combinedKeyword : combinedKeywords) {
            addLine(writer, combinedKeyword);
        }
        writer.close();
    }

    private static void CreateTableLine(PrintWriter writer) {
        writer.println("--\n" +
                "CREATE TABLE IF NOT EXISTS `elearning_stichwort` (\n" +
                "  `Id` mediumint(9) NOT NULL,\n" +
                "  `Stichwort` varchar(50) DEFAULT NULL,\n" +
                "  `Variable` varchar(50) DEFAULT NULL,\n" +
                "  `Metavariable` varchar(50) DEFAULT NULL\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;\n" +
                "ALTER TABLE `elearning_stichwort`\n" +
                "  ADD PRIMARY KEY (`Id`);\n" +
                "ALTER TABLE `elearning_stichwort`\n" +
                "  MODIFY `Id` mediumint(9) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;\n");
         }

    private static void addLine(PrintWriter writer, String s) {
        writer.println("INSERT INTO `unidisk`.`elearning_stichwort` (`Id`, `Stichwort`, `Variable`, `Metavariable`) VALUES (NULL,'" + s + "', 'elearning', 'delfi2016');");
    }
}
