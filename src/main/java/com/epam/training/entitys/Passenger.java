package com.epam.training.entitys;

import static com.epam.training.utilites.Utility.*;
import com.epam.training.constants.PassengerState;
import static com.epam.training.constants.Constants.*;

public class Passenger implements Runnable {
	private PassengerState state = PassengerState.WAITING;
	private Busstop startBusStop;
	private Busstop endBusStop;
	private Thread passengerThread;
	private final static String PASSENGER = "Passenger-";
	private final static String ARRIVED = "I arrived at the destination: ";

	public Passenger(Busstop startBusStop, Busstop endBusStop) {
		super();
		this.startBusStop = startBusStop;
		this.endBusStop = endBusStop;
		passengerThread = new Thread(this);
		passengerThread.setName(PASSENGER + passengerThread.getId());
		logData(this, CREATE + startBusStop + COLON + endBusStop);
		passengerThread.start();
	}

	public void run() {
		startBusStop.actionPassengerEnter(this);
		logData(this, ARRIVED + endBusStop);
	}

	public Thread getPassengerThread() {
		return passengerThread;
	}

	public Busstop getEndBusStop() {
		return endBusStop;
	}

	public String getState() {
		return state.toString().toLowerCase();
	}

	public void setState(PassengerState state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return makeSquareHooks(passengerThread.getName() + makeAngleHooks(getState()));
	}
}