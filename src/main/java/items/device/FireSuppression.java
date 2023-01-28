package items.device;

import house.Room;
import items.ElectricalItem;
import items.state.ActiveState;
import items.state.FixingState;
import items.state.IdleState;

import java.util.Objects;

public class FireSuppression extends Device{

    private static final int usingHours = 3;
    private static final int electricityInOnState = 1;
    private static final int electricityInOffState = 1;

    public FireSuppression(Room currentRoom) {
        super(DeviceType.FIRE_SUPPRESSION, usingHours, currentRoom, electricityInOnState, electricityInOffState);
    }

    @Override
    public void usingDevice() {
        setCurrentState(new ActiveState(this));
        getCurrentRoom().getElectricalItems().stream().filter(d-> !Objects.equals(d.getName(), DeviceType.FIRE_SUPPRESSION.name())).forEach(ElectricalItem::stopDevice);
        getCurrentRoom().getElectricalItems().stream().filter(d-> !Objects.equals(d.getName(), DeviceType.FIRE_SUPPRESSION.name())).forEach(d->d.setCurrentState(new FixingState(d)));
        setUsedTimes(getUsedTimes()+1);
        System.out.println(this.getName() + " is starting at " + getHouse().getTime());
        generateReportForObserver();
    }

    public void stopDevice(){
        super.stopDevice();
        getCurrentRoom().getElectricalItems().stream().filter(d-> Objects.equals(d.getMainType(), "device")).forEach(d->d.setCurrentState(new IdleState((Device) d)));
        getCurrentRoom().getElectricalItems().stream().filter(d-> Objects.equals(d.getMainType(), "sensor")).forEach(d->d.setCurrentState(new ActiveState(d)));
        generateReportForObserver();
    }
}
