package be.bdnlek.fitlek.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * root object of what is the subject of the fit-file
 * 
 * @author bruno
 *
 */

@XmlRootElement
public class Activity {

	private String activity = "unknown";
	private String serial_number = "unknown";
	private Date recordingTimeStamp = null;
	private Map<String, Object> summary = new HashMap<String, Object>();
	private List<Device> devices = new ArrayList<Device>();
	private List<Event> events = new ArrayList<Event>();

	private Results results = new Results();

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getSerial_number() {
		return serial_number;
	}

	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}

	public Date getRecordingTimeStamp() {
		return recordingTimeStamp;
	}

	public void setRecordingTimeStamp(Date recordingTimeStamp) {
		this.recordingTimeStamp = recordingTimeStamp;
	}

	@XmlElementWrapper(name = "devices")
	@XmlElement(name = "device")
	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	@XmlElementWrapper(name = "events")
	@XmlElement(name = "event")
	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public Results getResults() {
		return results;
	}

	public void setResults(Results results) {
		this.results = results;
	}

	public void setSummaryProperty(String propName, Object propValue) {
		summary.put(propName, propValue);
	}

}
