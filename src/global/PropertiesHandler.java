package global;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

public class PropertiesHandler {
    private String dir;

    public PropertiesHandler(String fileName) {
        dir = MessageFormat.format("src/properties/{0}.properties", fileName);
    }    

    public String get(String key){         
         try { 
            InputStream input = new FileInputStream(dir);
            Properties file = new Properties();
            file.load(input); 
            String value = file.getProperty(key);
            input.close();
            return value;
        } 
        catch (Exception ex) { 
            ex.printStackTrace(); 
            return "Error at resolving property";
        }
    }

    public String get(String key, Object[] args) {
        String rawValue = get(key);
        return MessageFormat.format(rawValue, args);
    }
}
