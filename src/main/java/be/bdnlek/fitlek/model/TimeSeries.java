package be.bdnlek.fitlek.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import be.bdnlek.fitlek.ListenerException;

@XmlRootElement
public class TimeSeries {

	private String name;
	private String unit;
	private TreeMap<Long, Double> values;
	private Double max = null;
	private Double min = null;
	private Double total = 0.0;

	private static Logger LOGGER = Logger.getLogger("be.ons.fit.TimeSeries");

	public TimeSeries() {

	};

	@XmlElement
	public Double getMax() {
		return max;
	}

	@XmlElement
	public Double getMin() {
		return min;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@XmlTransient
	public TreeMap<Long, Double> getValues() {
		return values;
	}

	@XmlElementWrapper(name = "X")
	public String[] getX() {
		return values.keySet().stream().map(ts -> LocalDateTime.ofEpochSecond(ts + 631065600, 0, ZoneOffset.UTC)
				.atZone(ZoneId.of("Europe/Paris")).toString()).toArray(String[]::new);
	}

	@XmlElement
	public Double[] getY() {
		return values.values().toArray(new Double[] {});
	}

	@XmlElement
	public Double getAvg() {

		Long first = values.firstKey();
		Long last = values.lastKey();

		Double avg = total / (last - first);
		return avg;
	}

	public TimeSeries(String name) {
		this.name = name;

		this.values = new TreeMap<Long, Double>();
	}

	public void addValue(Long msgTs, Double value) throws ListenerException {
		if (value == null) {
			throw new ListenerException(
					"a null-value may not be added to TimeSeries " + name + " at timestamp " + msgTs + ".");
		}
		Long previousMaxTs = null;
		if (!values.isEmpty()) {
			previousMaxTs = values.lastKey();
			if (previousMaxTs > msgTs) {
				throw new ListenerException("adding values for timestamp " + msgTs
						+ " while there were already more recent timestamps registered");
			}
			values.put(msgTs, value);
			if (previousMaxTs != null) {
				total = total + (values.get(previousMaxTs) * (msgTs - previousMaxTs));
			}
		} else {
			total = 0.0;
			values.put(msgTs, value);
		}
		if (min == null || value < min) {
			min = value;
		}

		if (max == null || value > max) {
			max = value;
		}
	}

	@XmlTransient
	@JsonIgnore
	public Double getValue(Long ts) {
		return values.get(ts);
	}

	public void removeValue(Long ts) throws ListenerException {
		if (values.firstKey() != ts) {
			throw new ListenerException("you should only remove the first key of a timeseries.  You wanted to remove "
					+ ts + " while " + values.firstKey() + " exists");
		}
		Double value = values.get(ts);
		values.remove(ts);
		if (value != null && !values.isEmpty()) {
			Long newFirstKey = values.firstKey();
			if (newFirstKey != null) {
				Long delta = newFirstKey - ts;
				total = total - (value * delta);
			}
		} else if (values.isEmpty()) {
			total = 0.0;
		}
	}

	public Double getTotal() {
		return total;
	}

	/**
	 * 
	 * @return a string that looks like: "t1 value1, t2 value2, t3 value3,...
	 * 
	 *         where t1, t2, t3,... are the timestamps of this timeseries and
	 *         value1, value2, value3,... are the corresponding values
	 * 
	 *         this should be the value of the points-attribute in a
	 *         svg:path-element
	 */

	@XmlTransient
	@JsonIgnore
	public String getSvgPath() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Long ts : values.keySet()) {
			if (first) {
				first = false;
			} else {
				sb.append(", ");
			}
			LocalDateTime dateTime = LocalDateTime.ofEpochSecond(ts + 631065600, 0, ZoneOffset.UTC);
			sb.append(dateTime + " " + values.get(ts));
		}
		return sb.toString();
	}
}
