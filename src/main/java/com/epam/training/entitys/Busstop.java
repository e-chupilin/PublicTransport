package com.epam.training.entitys;

import static com.epam.training.utilites.Utility.*;
import static com.epam.training.constants.Constants.*;

import com.epam.training.constants.PassengerState;

public class Busstop {
	private final String name;
	private Bus busAtBusStop;
	private volatile int quantityPassengerAtBusStop = 0;
	private boolean isBusAtBusStop = false;
	private final static String WAIT = " wait for bus ...";
	private final static String WAKE_UP = " I am wake UP! Add me at ";
	private final static String BUS_FULL = " bus full or busstop empty and I`m say GO BUS !";
	private final static String BUS_WAIT = " I`m wait and get me passenger.";
	private final static String BUS_GO = " ok, I go ...";
	private final static String BUS_BUSY = " is busy !";
	private final static String NO_PASSENGER = " NO passenger at busstop.";

	public Busstop(String name) {
		super();
		this.name = name;
	}

	public void actionPassengerEnter(Passenger passenger) {
		quantityPassengerAtBusStop++;

		synchronized (this) {
			while (!isBusAtBusStop) {
				try {
					logData(toString(), passenger + WAIT);
					wait();
				} catch (InterruptedException e) {
					logErr(this, ERROR_SET_PASSENGER_WAIT);
					throw new IllegalStateException(ERROR_SET_PASSENGER_WAIT);
				}
			}
			logData(toString(), passenger + WAKE_UP + busAtBusStop);
			passenger.setState(PassengerState.MOVING);
			busAtBusStop.addPassengerAtBus();
			quantityPassengerAtBusStop--;

			if (busAtBusStop.getIsBusBusy() || quantityPassengerAtBusStop == 0) {
				logData(toString(), passenger + BUS_FULL);
				isBusAtBusStop = false;
				notifyAll();
			}
		}
		busAtBusStop.waitPassenger(passenger);
	}

	public void actionBus(Bus bus) {
		isBusAtBusStop = true;
		busAtBusStop = bus;

		if (quantityPassengerAtBusStop != 0) {
			if (!bus.getIsBusBusy()) {
				try {
					logData(toString(), bus + BUS_WAIT);
					notifyAll();
					wait();
				} catch (InterruptedException e) {
					logErr(this, ERROR_SET_BUS_WAIT);
					throw new IllegalStateException(ERROR_SET_BUS_WAIT);				}
				logData(toString(), bus + BUS_GO);

			} else
				logData(toString(), bus + BUS_BUSY);
			isBusAtBusStop = false;

		} else {
			logData(toString(), bus + NO_PASSENGER);
			isBusAtBusStop = false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Busstop other = (Busstop) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public boolean isBusAtBusStop() {
		return isBusAtBusStop;
	}

	public int getQuantityPassengerAtBusStop() {
		return quantityPassengerAtBusStop;
	}

	@Override
	public String toString() {
		return makeAngleHooks(name + makeSquareHooks(quantityPassengerAtBusStop));
	}

}