package be.bdnlek.fitlek;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.garmin.fit.Decode;
import com.garmin.fit.Mesg;
import com.garmin.fit.MesgBroadcaster;
import com.garmin.fit.MesgListener;
import com.garmin.fit.RecordMesg;

import be.bdnlek.fitlek.model.Activity;
import be.bdnlek.fitlek.model.Results;

/**
 * For a given File that follows the fit-specification, this service constructs an Activity-instance.
 * 
 * @author bdenys
 *
 */

public class ActivityFactory {

	public static Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	private File fitFile = null;

	// the metrics which are recorded in the Garmin file
	public final static Map<String, Integer> METRICS = new HashMap<String, Integer>();

	// intervals to calculate Pavg (and later NP). Expressed in seconds. These
	// metrics (Pavg and NP) are not recorded in the fit-file but are rather to be
	// calculated afterwards. They're derived from the recorded values.
	public final static Integer[] INTERVALS = new Integer[] { 10, 60, 120, 300, 36000 }; // , 60, 300, 720, 1800 };

	private Results results = null;

	static {
		// just the list of all timestamps.
		METRICS.put("", null);

		// HeartRate (bpm)
		METRICS.put("HR", RecordMesg.HeartRateFieldNum);

		// Power (Watt)
		METRICS.put("P", RecordMesg.PowerFieldNum);

		// Altitude (meter)
		METRICS.put("A", RecordMesg.AltitudeFieldNum);

		// Speed (m/s ?)
		METRICS.put("V", RecordMesg.SpeedFieldNum);

		// Lattitude, originally semicircles, stored as radians
		METRICS.put("LAT", RecordMesg.PositionLongFieldNum);

		// Longitude, originally semicircles stored as radians
		METRICS.put("LONG", RecordMesg.PositionLatFieldNum);
	}

	/**
	 * 
	 * @param fitFile
	 * 			a file that is supposed to adhere to the fit-specification (having an extension ".fit").
	 * 
	 * @throws ActivityException
	 */
	
	public ActivityFactory(File fitFile) throws ActivityException {
		if (fitFile == null || !fitFile.exists() || !fitFile.canRead()) {
			throw new ActivityException("no fitFile specified (null)");
		}
		LOGGER.info("file: " + fitFile.getAbsolutePath());
		if (!fitFile.exists() || !fitFile.canRead()) {
			throw new ActivityException("fitFile does not exist: " + fitFile.getAbsolutePath());
		}
		if (!fitFile.canRead()) {
			throw new ActivityException("fitFile is not Readable: " + fitFile.getAbsolutePath());
		}

		this.fitFile = fitFile;

	}

	private Activity parseFile(MesgBroadcaster broadcaster, Decode decoder, Listener listener)
			throws ActivityException {
		FileInputStream fis;
		try {
			fis = new FileInputStream(fitFile);
		} catch (FileNotFoundException e) {
			throw new ActivityException(e);
		}

		Boolean successfull = false;
		long start = System.currentTimeMillis();
		LOGGER.info("decoding the fit-file");
		successfull = decoder.read(fis, broadcaster, broadcaster);
		try {
			fis.close();
		} catch (IOException e) {
			// if it was not possible to close the stream we won't be able tofix it
			// so we might as well continue as if nothing happened...
			e.printStackTrace();
		}
		LOGGER.info("decoded the fit-file in " + (System.currentTimeMillis() - start) + "ms.");

		if (successfull) {
			return listener.getActivity();
		} else {
			throw new ActivityException("parsing the fitFile was NOT successfull");
		}

	}

	public Activity getActivity(Class<? extends Mesg>... messages) throws ActivityException {

		List<Class<? extends Mesg>> messagesList = Arrays.asList(messages);

		Listener listener;
		try {
			listener = new Listener(METRICS, INTERVALS, messagesList);
		} catch (ListenerException e) {
			throw new ActivityException(e);
		}

		Decode decoder;
		MesgBroadcaster broadcaster;
		Activity activity;

		decoder = new Decode();
		broadcaster = new MesgBroadcaster(decoder);
		broadcaster.addListener((MesgListener) listener);

		activity = parseFile(broadcaster, decoder, listener);

		return activity;

	}

}
