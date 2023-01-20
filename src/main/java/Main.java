import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
        Simulation s = new Simulation(200, LocalTime.of(0, 0));
        s.start();
    }
}
