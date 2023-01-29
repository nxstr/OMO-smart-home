import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        Simulation s = new Simulation();
        s.start("Scenario1.json");
    }
}
