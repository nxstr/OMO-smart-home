import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.logging.LogManager;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        LogManager.getLogManager().readConfiguration(Main.class.getResourceAsStream("logging.properties"));
        Simulation s = new Simulation();
        switch (args[0]) {
            case "Scenario1" -> s.start("Scenario1.json");
            case "Scenario2" -> s.start("Scenario2.json");
        }
    }
}
