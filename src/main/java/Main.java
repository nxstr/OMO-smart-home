import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        Simulation s = new Simulation();
        s.start("Scenario1.json");
//        Config config = new Config();
//        config.conf("Scenario1.json");
    }
}
