package com.epam.training.entitys;

import static com.epam.training.utilites.Utility.*;
import static com.epam.training.constants.Constants.*;

import java.util.Collection;
import java.util.Map;

import com.epam.training.constants.PassengerState;
import com.epam.training.utilites.Utility;

public class Bus implements Runnable {
	private Map<Integer, Busstop> route;
	private Busstop currentBusStop;
	private int busSpeed;
	private int distance;
	private final int busCapasity;
	private int passengerAtBus = 0;
	private boolean isBusBusy = false;
	private Thread busThread;
	private final static String BUS = "Bus-";
	private final static String DRIVE = "drive ";
	private final static String KM = " km ...";
	private final static String PASSANGER_WAIT = "I`m wait my end BusStop ";
	private final static String ANYBODY_EXIT = " Anybody exit ?";
	private final static String AT = "at ";
	private final static String ANY_BUS_AT = "any bus at ";
	private final static String WAIT_FOR_FREE = " i`m wait for busstop stay free...";
	private final static String IS_FREE = " is free, move...";
	private final static String NEXT_ROUND = "Passenger at route, I'll go for another round!";
	private final static String BUS_NOT_FREE = "Round is end but bus not free.";
	private final static String WORK_IS_END = "No passenger at route. My work is end!";

	public Bus(Map<Integer, Busstop> route, int busSpeed, int busCapasity) {
		super();
		this.busCapasity = busCapasity;
		this.busSpeed = busSpeed;
		this.route = route;
		busThread = new Thread(this);
		busThread.setName(BUS + busThread.getId());
		busThread.start();
		logData(this, CREATE);
	}

	public void run() {
		boolean isPassengerAtRoute = true;
		while (isPassengerAtRoute) {
			Utility.sleep(busSpeed);
			distance++;
			logData(this, DRIVE + distance + KM);

			if (route.containsKey(distance)) {
				currentBusStop = route.get(distance);

				synchronized (currentBusStop) {
					while (currentBusStop.isBusAtBusStop()) {
						try {
							logData(this, WAIT_FOR_FREE);
							currentBusStop.wait();
							logData(this, currentBusStop + IS_FREE);
						} catch (InterruptedException e) {
							logErr(this, ERROR_SET_BUS_WAIT);
							throw new IllegalStateException(ERROR_SET_BUS_WAIT);
						}
					}

					synchronized (this) {
						logData(this, AT + currentBusStop + ANYBODY_EXIT);
						notifyAll();
					}

					logData(this, ANY_BUS_AT + currentBusStop + COLON + currentBusStop.isBusAtBusStop());

					if (currentBusStop.isBusAtBusStop()) {
						logErr(this, ERROR_BUSSTOP_BUSY);
						throw new IllegalStateException(ERROR_BUSSTOP_BUSY);
					}
					currentBusStop.actionBus(this);
				}
			}

			if (distance >= ROUTE_LENGHT) {
				distance = 0;
				isPassengerAtRoute = false;
				if (passengerAtBus != 0) {
					logData(this, BUS_NOT_FREE);
					isPassengerAtRoute = true;
				} else {
					Collection<Busstop> busstops = route.values();
					for (Busstop busstop : busstops) {
						if (busstop.getQuantityPassengerAtBusStop() != 0) {
							isPassengerAtRoute = true;
						}
					}
				}
				logData(this, isPassengerAtRoute ? NEXT_ROUND : WORK_IS_END);
			}
		}
		logData(this, END);
	}

	public int getQuantityPassengerAtBus() {
		return passengerAtBus;
	}

	public void addPassengerAtBus() {
		if (passengerAtBus == busCapasity) {
			logErr(this, ERROR_QUANTITY_PLACES);
			throw new IllegalArgumentException(ERROR_QUANTITY_PLACES);
		}
		passengerAtBus++;
		if (passengerAtBus == busCapasity) {
			isBusBusy = true;
		}
	}

	public synchronized void waitPassenger(Passenger passenger) {
		try {
			while (!currentBusStop.equals(passenger.getEndBusStop())) {
				logData(passenger, PASSANGER_WAIT + passenger.getEndBusStop());
				wait();
			}
		} catch (InterruptedException e) {
			logErr(this, ERROR_SET_PASSENGER_WAIT);
			throw new IllegalStateException(ERROR_SET_PASSENGER_WAIT);
		}
		removePassenger();
		passenger.setState(PassengerState.END);
	}

	public boolean getIsBusBusy() {
		return isBusBusy;
	}

	public void removePassenger() {
		if (passengerAtBus == 0) {
			logErr(this, ERROR_QUANTITY_PLACES);
			throw new IllegalArgumentException(ERROR_QUANTITY_PLACES);
		}
		isBusBusy = false;
		passengerAtBus--;
	}

	@Override
	public String toString() {
		return makeSquareHooks(busThread.getName() + makeSquareHooks(passengerAtBus + DASH + busCapasity));
	}
}