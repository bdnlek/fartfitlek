package be.bdnlek.fitlek;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import be.bdnlek.fitlek.model.Results;

public class SVGTimeLine {

	private static Logger LOGGER = Logger.getLogger("be.ons.fit.SVGTimeLine");

	private Results ride;

	public SVGTimeLine(Results ride) {
		this.ride = ride;
	}

	public String getLine(String metric, Integer width, Integer height) {
		StringBuilder lineValue = new StringBuilder();
		Double min, max, range;

		if (metric.substring(0, 1).equals("P")) {
			min = new Double(ride.getTimeSeries("P").getMin());
			max = new Double(ride.getTimeSeries("P").getMax());
		} else {
			min = ride.getTimeSeries(metric).getMin();
			max = ride.getTimeSeries(metric).getMax();
		}
		range = max - min;
		Long start = null;
		LOGGER.info(metric + " [" + min + "," + max + "]");

		for (Long ts : ride.getTimeSeries(metric).getValues().keySet()) {
			if (start == null) {
				start = ts;
			} else {
				lineValue.append(" ");
			}
			Long x = ts - start;
			Double y = ride.getTimeSeries(metric).getValue(ts);
			if (y != null) {
				y = ((y - min) / range) * height;
				lineValue.append(x + "," + (height - y));
			}
		}

		return lineValue.toString();
	}

	public String getPolyLine(String metric, String color, Integer labelHeight, Integer height, Integer width) {
		String style = "fill:none; stroke:" + color + ";stroke-width:1";
		String polyLineElement = "\t<polyline points=\"" + getLine(metric, height, width) + "\" style=\"" + style
				+ "\"></polyline> \n" + "\t<text x=\"10\" y=\"" + labelHeight + "\" style=\"" + style + "\" >" + metric
				+ "</text>";
		return polyLineElement;
	}

	public File toFile(String fileName) throws IOException {
		File f = new File(fileName);
		FileWriter fw = new FileWriter(f);
		Long width = ride.getTimeSeries("").getValues().lastKey() - ride.getTimeSeries("").getValues().firstKey();
		fw.write("<svg version=\"1.1\" style=\"overflow: scroll;\" width=\"" + width
				+ "\" height=\"1000\" id=\"Layer_1\" viewport=\"0 0 " + width
				+ " 1000\" xmlns=\"http://www.w3.org/2000/svg\">");
		fw.write(getPolyLine("HR", "red", 10, 1000, 1000));
		fw.write(getPolyLine("P", "yellow", 30, 1000, 1000));
		fw.write(getPolyLine("Pavg10", "blue", 50, 1000, 1000));
		fw.write(getPolyLine("Pavg300", "black", 70, 1000, 1000));
		fw.write(getPolyLine("A", "brown", 90, 1000, 1000));
		fw.write(getPolyLine("V", "pink", 110, 1000, 1000));
		fw.write("</svg>");
		fw.flush();
		fw.close();
		return f;
	}

}
