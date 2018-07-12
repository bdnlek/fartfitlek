package be.bdnlek.fitlek.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.garmin.fit.DateTime;

@XmlRootElement
public class Event {

	private DateTime timestamp = null;
	private String data = "unknown";
	private String event = "unknown";
	private String event_type = "unknown";
	private String event_group = "unknown";

	@XmlTransient
	public DateTime getTimestamp() {
		return timestamp;
	}

	@XmlElement(name = "time")
	public Long getTimeStampAsInteger() {
		return timestamp.getTimestamp();
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEvent_type() {
		return event_type;
	}

	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}

	public String getEvent_group() {
		return event_group;
	}

	public void setEvent_group(String event_group) {
		this.event_group = event_group;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("Event at " + timestamp + ":\n");
		sb.append("\tdata = " + data + "\n");
		sb.append("\tevent = " + event + "\n");
		sb.append("\teventy_type = " + event_type + "\n");
		sb.append("\tevent_group = " + event_group + "\n");
		return sb.toString();
	}

}
