package pool;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import global.PropertiesHandler;

public class AccessLogger {
    String dir;

    public AccessLogger() {
        PropertiesHandler props = new PropertiesHandler("pool");
        this.dir = props.get("log-file");
    }

    public synchronized void log(int id, String action) {
        try (FileWriter file = new FileWriter(this.dir, true);
            BufferedWriter buffer = new BufferedWriter(file);) {
            
            DateTimeFormatter format = DateTimeFormatter
                .ofPattern("dd/MM/yyyy HH:mm:ss");

            buffer.write(
                String.format("%19s - { ID: %04d ACTION: %10s }\n"
                , LocalDateTime.now().format(format), id, action)
            );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void clearLog() {
        try { 
            FileWriter file = new FileWriter(this.dir, false);
            file.close(); 
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
