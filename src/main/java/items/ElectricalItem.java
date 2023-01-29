package items;

import house.Room;
import items.device.Manual;
import items.state.ObjectState;

public interface ElectricalItem {

    Observer observer = Observer.getInstance();

    String getName();

    String getMainType();

    ObjectState getCurrentState();

    void setCurrentState(ObjectState currentState);

    int getUsingHours();

    Room getCurrentRoom();

    int getElectricityUsed();

    void setElectricityUsed(int electricityUsed);

    int getElectricityInOnState();

    int getElectricityInBrokeState();

    int getBrokenTimes();

    void setBrokenTimes(int brokenTimes);

    void resetElectricity();

    void addUsedElectricity(int electricity);

    void breakingItem();

    void fixingItem();

    Manual getManual();

    void generateReportForObserver();

    void breakingEvent();

    void usingDevice();

    void stopDevice();
}
