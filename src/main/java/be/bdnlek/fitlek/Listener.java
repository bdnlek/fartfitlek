package be.bdnlek.fitlek;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.garmin.fit.ActivityMesg;
import com.garmin.fit.AntplusDeviceType;
import com.garmin.fit.CapabilitiesMesg;
import com.garmin.fit.DateTime;
import com.garmin.fit.DeveloperField;
import com.garmin.fit.DeveloperFieldDescription;
import com.garmin.fit.DeveloperFieldDescriptionListener;
import com.garmin.fit.DeviceInfoMesg;
import com.garmin.fit.EventMesg;
import com.garmin.fit.Field;
import com.garmin.fit.FieldBase;
import com.garmin.fit.FileIdMesg;
import com.garmin.fit.Mesg;
import com.garmin.fit.MesgListener;
import com.garmin.fit.MesgNum;
import com.garmin.fit.RecordMesg;
import com.garmin.fit.SourceType;
import com.garmin.fit.UserProfileMesg;

import be.bdnlek.fitlek.model.Activity;
import be.bdnlek.fitlek.model.Device;
import be.bdnlek.fitlek.model.Event;
import be.bdnlek.fitlek.model.TimeSeries;

public class Listener implements MesgListener, DeveloperFieldDescriptionListener {

	private static Logger LOGGER = Logger.getLogger("be.ons.fit.Listener");
	private Map<String, Integer> metrics;
	private Integer[] intervals;
	private List<Class<? extends Mesg>> supportedMessages;
	public static Class[] SUPPORTED_CLASSES = new Class[] { ActivityMesg.class, CapabilitiesMesg.class,
			CapabilitiesMesg.class, DeviceInfoMesg.class, EventMesg.class, FileIdMesg.class, RecordMesg.class,
			UserProfileMesg.class };

	private Activity activity = new Activity();

	// while progressing through the fit file, we update - for every second -
	// the relative timeSeries spanning the previous 10, 60, 120, ... seconds
	private Map<Integer, TimeSeries> runningTimeSeries = new HashMap<Integer, TimeSeries>();

	public Activity getActivity() {
		return activity;
	}

	public Listener(Map<String, Integer> metrics, Integer[] intervals, List<Class<? extends Mesg>> messagesList)
			throws ListenerException {
		super();

		this.metrics = metrics;
		this.intervals = intervals;
		this.supportedMessages = messagesList;

		// the metric defined in the static method above (see METRICS)
		// give result in a number of timeseries
		for (String metric : metrics.keySet()) {
			TimeSeries s = new TimeSeries((metric));
			activity.getResults().addSeries(s);
		}

		// in order to calculate CPs and NPs, we need to create new
		// timeseries for each interval. Each such TimeSeries which will change
		// as we run through the fit file
		for (int interval : intervals) {
			runningTimeSeries.put(interval, new TimeSeries(interval + ""));

			TimeSeries s = new TimeSeries("Pavg" + interval);
			activity.getResults().addSeries(s);
		}
	}

	public void onCapabilitiesMesg(CapabilitiesMesg mesg) {
		Iterable<DeveloperField> fields = mesg.getDeveloperFields();
		for (DeveloperField field : fields) {
			System.out.println("Name: " + field.getName());
			System.out.println("Type: " + field.getType());
			System.out.println("Units: " + field.getUnits());
		}

	}

	public void onActivityMesg(ActivityMesg mesg) {
		for (Field field : mesg.getFields()) {
			activity.setSummaryProperty(field.getName(), field.getValue());
		}
	}

	public void onFileIdMesg(FileIdMesg mesg) {
		activity.setSerial_number(mesg.getSerialNumber().toString());
	}

	public void onDeviceInfoMesg(DeviceInfoMesg mesg) {
		Device device = extractDevice(mesg);
		activity.getDevices().add(device);
		LOGGER.info(device.toString());

	}

	@Override
	public void onMesg(Mesg mesg) {
		if (mesg.getName().equals("event")) {
			System.out.println(mesg.getName() + ": " + mesg.getNum());
		}
		if (supportedMessages.contains(ActivityMesg.class) && mesg.getNum() == MesgNum.EVENT) {
			onActivityMesg(new ActivityMesg(mesg));
		} else if (supportedMessages.contains(CapabilitiesMesg.class) && mesg.getNum() == MesgNum.CAPABILITIES) {
			onCapabilitiesMesg(new CapabilitiesMesg(mesg));
		} else if (supportedMessages.contains(DeviceInfoMesg.class) && mesg.getNum() == MesgNum.DEVICE_INFO) {
			onDeviceInfoMesg(new DeviceInfoMesg(mesg));
		} else if (supportedMessages.contains(EventMesg.class) && mesg.getNum() == MesgNum.EVENT) {
			onEventMesg(new EventMesg(mesg));
		} else if (supportedMessages.contains(FileIdMesg.class) && mesg.getNum() == MesgNum.FILE_ID) {
			onFileIdMesg(new FileIdMesg(mesg));
		} else if (supportedMessages.contains(UserProfileMesg.class) && mesg.getNum() == MesgNum.USER_PROFILE) {
			onUserProfileMesg(new UserProfileMesg(mesg));
		} else if (supportedMessages.contains(RecordMesg.class) && mesg.getNum() == MesgNum.RECORD) {
			onRecordMesg(new RecordMesg(mesg));
		}
		/*
		 * String name = mesg.getName(); Integer num = mesg.getNum(); String
		 * msgNumString = MesgNum.getStringFromValue(num);
		 * activity.getResults().getCapabilities().add(name); if (!name.equals("record")
		 * && !name.equals("hrv") && !name.equals("unknown")) { StringBuilder sb = new
		 * StringBuilder(name + " (" + msgNumString + ")" + ": "); for (Field f :
		 * mesg.getFields()) { sb.append(f.getName() + "=" + f.getValue() + " " +
		 * f.getUnits() + ";"); } System.out.println(sb); }
		 */
	}

	/**
	 * generates the timeseries for the metrics defined in global variable metrics
	 */
	public void onRecordMesg(RecordMesg mesg) {

		DateTime dt = mesg.getTimestamp();
		// the unit of a fit-timestamp is "second" and NOT "millisecond"
		Long msgTs = dt.getTimestamp();
		// every RecordMesg corresponds to an Observation
		// Observation observation = new Observation();

		// we'll loop over all METRICS we're interested in
		// (that's the what we've put in METRICS)
		for (String metric : metrics.keySet()) {
			Double value = getValue(mesg, metric);

			if (value != null || metric.equals("")) {
				if ((metric.equals("LAT") || metric.equals("LONG"))) {
					// if (metric.equals("LONG")) {
					value = -value;
					// }
					// semicircle to degree conversion
					value = (value * 180) / (1024 * 1024 * 1024 * 2);
				}
				if (value == null) {
					// if metric="", we just put value 0. It is not used...
					value = 0.0;
				}
				try {
					activity.getResults().getTimeSeries(metric).addValue(msgTs, value);
				} catch (ListenerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// if the power is not null, we should proceed to adapt the relative
		// timeseries which we use to calculate the Pavg<interval> and NP<interval>
		Double power = activity.getResults().getTimeSeries("P").getValue(msgTs);
		if (power != null) {
			try {
				calculatePowerMetrics(msgTs, power);
			} catch (ListenerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void calculatePowerMetrics(Long msgTs, Double power) throws ListenerException {
		// we'll recalculate all the metrics that are derived
		// from the registrations of the garmin:
		for (Integer interval : intervals) {
			TimeSeries series = runningTimeSeries.get(interval);
			List<Long> timestampsToBeRemoved = new ArrayList<Long>();

			// select the timestamps that lay outside the interval
			for (Long t : series.getValues().keySet()) {
				if ((msgTs - t) > interval) {
					timestampsToBeRemoved.add(t);
				} else {
					break;
				}
			}

			// remove the values which are outside of the interval
			for (Long t : timestampsToBeRemoved) {
				series.removeValue(t);
			}

			// now add the new value:
			series.addValue(msgTs, power);

			// the relative timeseries has been regenerated. Now let's calculate
			// the averages.
			Double average = series.getTotal() / interval;

			try {
				activity.getResults().getTimeSeries("Pavg" + interval).addValue(msgTs, average);
			} catch (ListenerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void printDeveloperData(Mesg mesg) {
		for (DeveloperField field : mesg.getDeveloperFields()) {
			if (field.getNumValues() < 1) {
				continue;
			}

			if (field.isDefined()) {
				System.out.print("   " + field.getName());

				if (field.getUnits() != null) {
					System.out.print(" [" + field.getUnits() + "]");
				}

				System.out.print(": ");
			} else {
				System.out.print("   Undefined Field: ");
			}

			System.out.print(field.getValue(0));
			for (int i = 1; i < field.getNumValues(); i++) {
				System.out.print("," + field.getValue(i));
			}

			System.out.println();
		}
	}

	public void onDescription(DeveloperFieldDescription desc) {
		LOGGER.info("New Developer Field Description");
		LOGGER.info("   App Id: " + desc.getApplicationId());
		LOGGER.info("   App Version: " + desc.getApplicationVersion());
		LOGGER.info("   Field Num: " + desc.getFieldDefinitionNumber());
	}

	private Double getValue(Mesg mesg, String metric) {
		Integer fieldInt = metrics.get(metric);
		if (fieldInt == null) {
			return null;
		}
		int fieldNum = fieldInt;
		Iterable<FieldBase> fields = mesg.getOverrideField((short) fieldNum);

		for (FieldBase field : fields) {

			if (field instanceof Field) {
				Double value = field.getDoubleValue();
				return value;
			}
		}
		return null;
	}

	private Device extractDevice(Mesg mesg) {
		Device device = new Device();

		Iterable<Field> fields = mesg.getFields();
		for (Field field : fields) {
			device.set(field.getName(), field.getValue().toString());
		}
		String source_type_String = device.get("source_type");
		String device_type_String = device.get("device_type");

		if (source_type_String != null) {
			Short source_type_Int = Short.parseShort(device.get("source_type"));
			if (source_type_Int == SourceType.ANTPLUS.getValue()) {
				device_type_String = AntplusDeviceType.getStringFromValue(Short.parseShort(device_type_String));
				device.set("device_type", device_type_String);
			}

		}
		device.resolve();
		return device;
	}

	public <T extends MesgListener> T as(Class<T> T) throws FitServiceException {
		if (T.isInstance(this)) {
			return (T) this;
		} else {
			throw new FitServiceException("cannot cast listener to " + T.getName());
		}
	}

	public void onUserProfileMesg(UserProfileMesg mesg) {
		// TODO:???

	}

	public void onEventMesg(EventMesg mesg) {
		Event event = new Event();
		event.setData(mesg.getData().toString());
		event.setEvent(mesg.getEvent().toString());
		if (mesg.getEventGroup() != null) {
			event.setEvent_group(mesg.getEventGroup().toString());
		}
		event.setEvent_type(mesg.getEventType().toString());
		event.setTimestamp(mesg.getTimestamp());
		activity.getEvents().add(event);
		LOGGER.info(event.toString());
	}

}
