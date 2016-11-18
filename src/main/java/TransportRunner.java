import java.io.IOException;
import java.util.Random;

import com.epam.training.factories.TransportFactory;
import static com.epam.training.constants.Constants.*;
import static com.epam.training.utilites.Utility.*;

public class TransportRunner {

	public static void main(String[] args) {
		logData(MAIN, START);
		try {
			TransportFactory factory = new TransportFactory(PROP_FILENAME);

			for (int i = 0; i < factory.getPassengerQuantity(); i++) {
				factory.getPassengerFromFactory();
			}

			for (int i = 0; i < factory.getBusQuantity(); i++) {
				factory.getBusFromFactory();
				sleep(getRandomTimeInterval(factory.getTimeInterval()));
			}
			logData(MAIN, END);

		} catch (IOException ex) {
			logErr(MAIN, ERROR_FILENOTFOUND + COLON + PROP_FILENAME);
		} catch (NumberFormatException ex) {
			logErr(MAIN, ERROR_CONFIG);
		}
	}

	private static int getRandomTimeInterval(int time) {
		Random r = new Random();
		int i;
		do {
			i = r.nextInt(time);
		} while (i == 0);
		return i * 10;
	}
}