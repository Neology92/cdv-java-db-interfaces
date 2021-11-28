
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Crunchify.com
 *
 */

public class BaseConfig {
    public static Properties getPropValues() throws IOException {
        Properties props = new Properties();
        InputStream inputStream = null;
        try {
            String propFileName = "config.properties";
            inputStream = BaseConfig.class.getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                props.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            System.out.println("Variables loaded");
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return props;
    }
}