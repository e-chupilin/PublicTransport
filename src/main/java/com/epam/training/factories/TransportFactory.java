package com.epam.training.factories;

import static com.epam.training.constants.Constants.*;
import static com.epam.training.utilites.Utility.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import com.epam.training.constants.AppMode;
import com.epam.training.entitys.Bus;
import com.epam.training.entitys.Busstop;
import com.epam.training.entitys.Passenger;

public class TransportFactory {
	private final String name;
	private final static String[] busstopNames = { "Kypalavskaja", "60-god bssr", "Mironova", "Rechitcki prospekt",
			"Vakzal", "Park Jubileini", "50-god bssr", "Savetckaja", "Leninskaja", "Perchamaiskaja", "Teatr", "Cirk" };
	private Integer[] distances;
	private Map<Integer, Busstop> route = new HashMap<Integer, Busstop>();
	private AppMode mode;
	private int busstopQuantity;
	private int passengerQuantity;
	private int busQuantity;
	private int busCapasity;
	private int timeInterval;
	private int busSpeed;

	public TransportFactory(String propFileName) throws IOException {
		name = makeSquareHooks(this.getClass().getSimpleName());
		Properties prop = new Properties();
		InputStream input = null;
		input = new FileInputStream(propFileName);
		prop.load(input);
		logLine();
		mode = AppMode.valueOf(prop.getProperty(MODE).toUpperCase());
		logData(name, MODE + COLON + mode.toString().toLowerCase());
		busstopQuantity = getValueLogData(prop, BUSSTOP_QUANTITY);
		passengerQuantity = getValueLogData(prop, PASSENGER_QUANTITY);
		busQuantity = getValueLogData(prop, BUS_QUANTITY);
		busCapasity = getValueLogData(prop, BUS_CAPASITY);
		timeInterval = getValueLogData(prop, TIME_INTERVAL);
		busSpeed = getValueLogData(prop, BUS_SPEED);

		makeRoute();

		logLine();
		Collection<Integer> dist = route.keySet();
		distances = new Integer[dist.size()];
		distances = dist.toArray(distances);

		if (input != null)
			input.close();
	}

	public Passenger getPassengerFromFactory() {
		Random r = new Random();
		return new Passenger(route.get(distances[r.nextInt(busstopQuantity)]),
				route.get(distances[r.nextInt(busstopQuantity)]));
	}

	public Bus getBusFromFactory() {
		Bus bus = new Bus(route, busSpeed, busCapasity);
		return bus;
	}

	public Map<Integer, Busstop> getRoute() {
		return route;
	}

	public AppMode getMode() {
		return mode;
	}

	public int getBusSpeed() {
		return busSpeed;
	}

	public void setBusSpeed(int busSpeed) {
		this.busSpeed = busSpeed;
	}

	public int getBusstopQuantity() {
		return busstopQuantity;
	}

	public int getPassengerQuantity() {
		return passengerQuantity;
	}

	public int getBusQuantity() {
		return busQuantity;
	}

	public int getBusCapasity() {
		return busCapasity;
	}

	public int getTimeInterval() {
		return timeInterval;
	}

	private int getValueLogData(Properties prop, String key) {
		int i = Integer.parseInt(prop.getProperty(key));
		logData(name, key + COLON + i);
		return i;
	}

	private void makeRoute() {
		Random r = new Random();
		Integer distance;
		for (int i = 0; i < busstopQuantity; i++) {
			do {
				distance = r.nextInt(ROUTE_LENGHT);
			} while (route.containsKey(distance) || distance.intValue() == 0);
			int pos = i < busstopNames.length ? i : busstopNames.length - 1;
			route.put(distance, new Busstop(busstopNames[pos]));
		}
		logData(name, CREATE_ROUTE);
		logData(name, route.toString());
	}

	@Override
	public String toString() {
		return name;
	}
}