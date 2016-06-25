package util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class ValidationHelper {
    private static final Logger LOG = LoggerFactory.getLogger(ValidationHelper.class);

    public static boolean isValid(File schemaLocation, File testFile){
        LOG.info("Validating "+testFile.getName()+"...");
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        try {
            Schema schema = factory.newSchema(schemaLocation);
            Validator validator = schema.newValidator();
            Source source = new StreamSource(testFile);
            validator.validate(source);
            LOG.info("File is valid!");
            return true;
        } catch (SAXException e) {
            LOG.error("File is not valid because of the error: "+e.getMessage());
            return false;
        } catch (IOException e) {
            LOG.error("Error: "+e.getMessage());
            return false;
        }
    }
}
