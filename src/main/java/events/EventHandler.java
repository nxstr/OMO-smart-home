package events;

import items.ElectricalItem;
import items.Observer;
import items.sensors.Sensor;
import java.util.List;

public class EventHandler {
    private final Event event;

    public EventHandler(Event event) {
        this.event = event;
    }

    public void notifySystem(){
        System.out.println("Event " + event.getType() + " is handled at " + event.getTime());
        List<ElectricalItem> sensors = event.getRoom().getElectricalItems().stream()
                .filter(s -> s.getMainType() == "sensor").toList();
//        ElectricalItem item = sensors.stream().filter(s -> s.getName() == event.getType().toString());
        ElectricalItem sens = null;
        for(ElectricalItem s: sensors){
            if(s.getName() == event.getType().toString()){
                sens = s;
            }
        }
        if(sens!=null){
            sens.usingDevice();
            System.out.println("at time " + event.getTime());
        }else{
            Observer.getInstance().eventHandler();
        }
    }
}
