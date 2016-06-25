package main;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ValidationHelper;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        if (args.length!=4) {
            LOG.error("Wrong number of parameters!");
            return;
        }
        if (!Files.exists(Paths.get(args[0]))) {
            LOG.error("File "+args[0]+" was not found!");
            return;
        }
        File testFile = new File(args[0]);

        if (!Files.exists(Paths.get(args[1]))) {
            LOG.error("File "+args[1]+" was not found!");
            return;
        }
        File schemaLocation = new File(args[1]);

        if (!Files.exists(Paths.get(args[2]))) {
            LOG.error("File "+args[2]+" was not found!");
            return;
        }
        File xsltLocation = new File(args[2]);
        File result = new File(args[3]);

        if (!ValidationHelper.isValid(schemaLocation, testFile)) return;

        try {
            LOG.info("Transforming...");

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Source xslt = new StreamSource(xsltLocation);
            Transformer transformer = transformerFactory.newTransformer(xslt);
            Source tested = new StreamSource(testFile);

            File tempFile = Files.createTempFile("transformed", null).toFile();
            tempFile.deleteOnExit();

            transformer.transform(tested, new StreamResult(tempFile));

            LOG.info("Transformation complete.");
            if (ValidationHelper.isValid(schemaLocation, tempFile)){
                LOG.info("Writing data to "+result.getName());
                Files.copy(tempFile.toPath(), result.toPath(), StandardCopyOption.REPLACE_EXISTING);
                LOG.info(result.getName() +" is ready!");
            }
        } catch (TransformerException e) {
            LOG.error("Transforming error: "+e.getMessage());
        } catch (IOException e) {
            LOG.error("Error: "+e.getMessage());
        }

    }
}
