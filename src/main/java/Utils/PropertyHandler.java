package Utils;

import java.io.*;
import java.util.Properties;

public class PropertyHandler {

    private static PropertyHandler instance = null;

    private Properties props;

    private PropertyHandler() {

        try {

            FileInputStream in = new FileInputStream("credentials.properties");
            this.props = new Properties();
            this.props.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized PropertyHandler getInstance() {
        if (instance == null)
            instance = new PropertyHandler();
        return instance;
    }

    public String getValue(String propKey){
        return this.props.getProperty(propKey);
    }
}