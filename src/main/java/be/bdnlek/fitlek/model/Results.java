package be.bdnlek.fitlek.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import be.bdnlek.fitlek.ListenerException;

@XmlRootElement
public class Results {

	@XmlElementWrapper(name = "timeSeriesList")
	@XmlElement(name = "timeseries")
	private Set<TimeSeries> timeSeries = new HashSet<TimeSeries>();

	@XmlElementWrapper(name = "capabilities")
	@XmlElement(name = "capability")
	private Set<String> capabilities = new HashSet<String>();

	public TimeSeries getTimeSeries(String name) {
		for (TimeSeries series : timeSeries) {
			if (series.getName().equals(name)) {
				return series;
			}
		}
		return null;
	}

	public String[] getTimeSeries() {
		List<String> tsList = new ArrayList<String>();
		for (TimeSeries ts : timeSeries) {
			Collection<Double> values = ts.getValues().values();
			if (values != null && values.size() > 0) {
				tsList.add(ts.getName());
			}
		}
		return tsList.toArray(new String[] {});
	}

	public void addSeries(TimeSeries series) throws ListenerException {
		if (getTimeSeries(series.getName()) == null) {
			timeSeries.add(series);
		} else {
			throw new ListenerException("could not add timeSeries " + series.getName() + " because it already exists");
		}
	}

	/**
	 * @return the capabilities
	 */
	public Set<String> getCapabilities() {
		return capabilities;
	}

	/**
	 * @param capabilities
	 *            the capabilities to set
	 */
	public void setCapabilities(Set<String> capabilities) {
		this.capabilities = capabilities;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("summary of the ride:");
		for (TimeSeries ts : timeSeries) {
			sb.append("\n\t" + ts.getName() + ": [" + ts.getMin() + " | " + ts.getAvg() + " | " + ts.getMax() + "]");
		}
		return sb.toString();
	}

}
