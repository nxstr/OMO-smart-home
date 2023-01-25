import events.Event;
import events.EventHandler;
import events.EventType;
import house.House;
import items.Observer;
import items.device.Device;
import items.device.DeviceType;
import items.state.IdleState;
import items.state.StateType;
import livingEntities.*;
import strategy.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private int hours = 0;
    private int days = 0;
    private final int interactionCount; //1 = 10 min
    private LocalTime time;

    private Strategy strategy;
    private Observer observer = Observer.getInstance();
    public Simulation(int interactionCount, LocalTime time) {
        this.interactionCount = interactionCount;
        this.time = time;
    }

    public void start(){
        Config config = new Config();
        config.configure();
        House house = House.getInstance();

        for(int i=0; i<interactionCount; i++){
            time = time.plusMinutes(10);
            house.setTime(time);
            List<LivingEntity> entities = house.getLivingEntities();
            if(time.getMinute()==0){
                hours = time.getHour();
                System.out.println(hours + " hours");
                checkStrategy(house);
            }
            if(strategy!=null) {
                if (!strategy.getActiveDevices().isEmpty()) {
                    List <Device> devices = new ArrayList<>();
                    for(Device d:strategy.getActiveDevices()){
                        devices.add(d);
                    }
                    for (Device d : devices) {
                        if (strategy.getCurrentBackActionProgress() == d.getUsingHours()) {
                            if(d.getCurrentState().getType()==StateType.BROKEN || d.getCurrentState().getType()==StateType.FIXING){
                                strategy.removeActiveDevice(d);
                            }
                            d.setCurrentState(new IdleState(d));
                            if(d.getType()== DeviceType.DISHWASHER || d.getType()== DeviceType.WASHING_MACHINE){
                                Adult.addTask(d);
                            }
                            System.out.println(d.getType() + " switched off by system at " + time);
                            strategy.removeActiveDevice(d);
                        }
                        if(d.getCurrentState().getType()==StateType.BROKEN || d.getCurrentState().getType()==StateType.FIXING){
                            strategy.removeActiveDevice(d);
                        }
                    }
                    devices.clear();
                    strategy.increaseCBAP();
                }
                if (strategy.getActiveDevices().isEmpty() && strategy.getCurrentBackActionProgress()!=0) {
                    strategy.stopBackAction();
                }
            }

            for(LivingEntity e: entities) {
                if (time.isBefore(LocalTime.of(8, 0))) {
                    e.sleeping();
                } else {
                    if (e.getCurrentDevice() == null && e.getCurrentEq() == null) {
                        e.findActivity();
                    }
                    if (e.getCurrentDevice() != null) {
                        if (e.getCurrentBackActionProgress() == e.getCurrentDevice().getUsingHours()) {
                            e.stopCurrentActivity();
                        } else {
                            e.increaseCBAP();
                        }
                    } else if (e.getCurrentEq() != null) {
                        if (e.getCurrentBackActionProgress() == e.getCurrentEq().getUsingHours()) {
                            e.getCurrentEq().setCurrentState(new IdleState(e.getCurrentEq()));
                            System.out.println(e.getCurrentEq().getType() + " switched off at " + time);
                            e.stopCurrentActivity();
                        } else {
                            e.increaseCBAP();
                        }
                    }
                    e.increaseHunger();
                }
            }
        }
    }

    public void dayStrategySetup(){
        strategy.setup();
        observer.setStrategy(strategy);
    }

    public void nightStrategySetup(){
        strategy.setup();
        observer.setStrategy(strategy);
    }

    public void checkStrategy(House house){
        if(time.equals(LocalTime.of(8, 0))){
            strategy = new Morning();
            dayStrategySetup();
        }
        if(time.equals(LocalTime.of(14, 0))){
            strategy = new Afternoon();
            dayStrategySetup();
        }
        if(time.equals(LocalTime.of(19, 0))){
            strategy = new Evening();
            dayStrategySetup();
        }
        if(time.equals(LocalTime.of(0, 0))){
            days++;
            System.out.println(days + " days");
            strategy = new Night();
            nightStrategySetup();

            Event event = new Event(EventType.TEMPERATURE, house.getFloors().get(0).getRooms().get(0), time);
            EventHandler e = new EventHandler(event);
            e.notifySystem();
        }
    }
}
