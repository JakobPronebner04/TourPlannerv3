package at.jp.tourplanner.service;

import at.jp.tourplanner.dataaccess.StateDataAccess;
import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;

public class ExceptionService {
    private final StateDataAccess stateDataAccess;
    private final EventManager eventManager;
    public ExceptionService(StateDataAccess stateDataAccess, EventManager eventManager) {
        this.stateDataAccess = stateDataAccess;
        this.eventManager = eventManager;
    }

    public String getCurrentExceptionMessage(){
        return stateDataAccess.getException();
    }
    public void updateCurrentExceptionMessage(Exception e){
        stateDataAccess.updateException(e.getMessage());
        eventManager.publish(Events.EXCEPTION_THROWN, "NEW_EXCEPTION");
    }

}
