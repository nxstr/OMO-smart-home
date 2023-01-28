import events.*;
import house.House;
import items.Observer;
import items.ReportGenerator;
import items.device.Device;
import items.device.DeviceType;
import items.state.IdleState;
import items.state.StateType;
import livingEntities.*;
import org.json.simple.parser.ParseException;
import strategy.*;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private int days = 0;
    private LocalTime time;
    private final EventGenerator offEventGenerator = WaterAndElectricityEventGenerator.getInstance();
    private final EventGenerator onEventGenerator = FireAndTemperatureEventGenerator.getInstance();
    private ReportGenerator reportGenerator = new ReportGenerator();

    private Strategy strategy;
    private final Observer observer = Observer.getInstance();
    public Simulation() {
    }

    public void start(String file) throws IOException, ParseException {
        Config config = new Config();
        config.configure(file);
        List<Integer> setupList = config.configureSimulation(file);
        //1 = 10 min
        int interactionCount = setupList.get(0);
        time = LocalTime.of(setupList.get(1), 0);
        House house = House.getInstance();
        reportGenerator.houseConfigurationReport();
        checkStrategy();
        for(int i = 0; i< interactionCount; i++){
            time = time.plusMinutes(10);
            house.setTime(time);
            List<LivingEntity> entities = house.getLivingEntities();
            if(time.getMinute()==0){
                int hours = time.getHour();
                Observer.getInstance().logAction(hours + " hours\n");
                checkStrategy();
                house.setDay(days);
            }
            offEventGenerator.generateEvent(time, observer);
            onEventGenerator.generateEvent(time, observer);
            if(strategy!=null) {
                if (!strategy.getActiveDevices().isEmpty()) {
                    List<Device> devices = new ArrayList<>(strategy.getActiveDevices());
                    for (Device d : devices) {
                        if (strategy.getCurrentBackActionProgress() == d.getUsingHours()) {
                            if(d.getCurrentState().getType()==StateType.BROKEN || d.getCurrentState().getType()==StateType.FIXING){
                                strategy.removeActiveDevice(d);
                            }
                            d.stopDevice();
                            reportGenerator.strategyReport(d, time);
                            if(d.getType()== DeviceType.DISHWASHER || d.getType()== DeviceType.WASHING_MACHINE){
                                Adult.addTask(d);
                            }
                            strategy.removeActiveDevice(d);
                        }
                        if(d.getCurrentState().getType()==StateType.BROKEN || d.getCurrentState().getType()==StateType.FIXING || d.getCurrentState().getType()==StateType.OFF){
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
                if(!e.isAlarmMode()) {
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
                                e.getCurrentEq().stopEquipment();
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

    public void dayStrategySetup() throws IOException {
        if(days==0){
            days++;
            Observer.getInstance().logAction(days + " day\n");
            reportGenerator.writeEventDay(days);
            reportGenerator.writeActivityDay(days);
            reportGenerator.writeConsumptionDay(days);
        }
        strategy.setup();
        reportGenerator.strategySetupReport(strategy);
        for(Device d: strategy.getActiveDevices()) {
            reportGenerator.strategyReport(d, time);
        }
        observer.setStrategy(strategy);
    }

    public void nightStrategySetup(){
        strategy.setup();
        reportGenerator.strategySetupReport(strategy);
        for(Device d: strategy.getActiveDevices()) {
            reportGenerator.strategyReport(d, time);
        }
        observer.setStrategy(strategy);
    }

    public void checkStrategy() throws IOException {
        if((time.isAfter(LocalTime.of(8, 0))&&time.isBefore(LocalTime.of(14,0))&&strategy==null)||time.equals(LocalTime.of(8,0))){
            strategy = new Morning();
            dayStrategySetup();
        }
        if((time.isAfter(LocalTime.of(14, 0))&&time.isBefore(LocalTime.of(19,0))&&strategy==null)||time.equals(LocalTime.of(14,0))){
            strategy = new Afternoon();
            dayStrategySetup();
        }
        if((time.isAfter(LocalTime.of(19, 0))&&time.isBefore(LocalTime.of(0,0))&&strategy==null)||time.equals(LocalTime.of(19,0))){
            strategy = new Evening();
            dayStrategySetup();
        }
        if((time.isAfter(LocalTime.of(0, 0))&&time.isBefore(LocalTime.of(8,0))&&strategy==null)||time.equals(LocalTime.of(0,0))){
            strategy = new Night();
            nightStrategySetup();
            days++;
            Observer.getInstance().logAction(days + " day\n");
            reportGenerator.writeEventDay(days);
            reportGenerator.writeActivityDay(days);
            reportGenerator.writeConsumptionDay(days);

        }
    }
}
