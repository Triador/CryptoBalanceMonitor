package Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Properties {

    private static java.util.Properties properties;

    static {
        InputStream is = null;
        try {
            properties = new java.util.Properties();
            is = ClassLoader.class.getResourceAsStream("credentials.properties");
            properties.load(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPropertyValue(String key) {
        return properties.getProperty(key);
    }

}
