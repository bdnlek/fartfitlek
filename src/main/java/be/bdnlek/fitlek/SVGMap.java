package be.bdnlek.fitlek;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import be.bdnlek.fitlek.model.Results;

public class SVGMap {

	private static Logger LOGGER = Logger.getLogger("be.ons.fit.SVGMap");

	private Results ride;

	private static Double SS_TO_RADIAN_PROPORTION = 180.0 / (2 ^ 31);

	public SVGMap(Results ride) {
		this.ride = ride;
	}

	public String getPoints(Integer width, Integer height) {
		StringBuilder lineValue = new StringBuilder();
		Double minX, maxX, minY, maxY, rangeX, rangeY, range;

		minX = ride.getTimeSeries("LONG").getMin();
		maxX = ride.getTimeSeries("LONG").getMax();
		minY = ride.getTimeSeries("LAT").getMin();
		maxY = ride.getTimeSeries("LAT").getMax();
		rangeX = maxX - minX;
		rangeY = maxY - minY;

		if (rangeX > rangeY) {
			range = rangeX;
		} else {
			range = rangeY;
		}

		Long start = null;
		LOGGER.info("LONG: " + " [" + minX + "," + (maxX) + "]");
		LOGGER.info("LAT: " + " [" + (minY) + "," + (maxY) + "]");

		for (Long ts : ride.getTimeSeries("").getValues().keySet()) {
			if (start == null) {
				start = ts;
			} else {
				lineValue.append(" ");
			}

			Double x = ride.getTimeSeries("LONG").getValue(ts);
			Double y = ride.getTimeSeries("LAT").getValue(ts);

			if (y != null && x != null) {
				x = width - ((x - minX) * width / range);
				y = (height - ((y - minY) * height / range));
				lineValue.append(y + "," + x);
			}
		}

		return lineValue.toString();
	}

	public String getPolyLine(Integer labelHeight, String color, Integer width, Integer height) {
		String style = "fill:none; stroke:" + color + ";stroke-width:1";
		String polyLineElement = "\t<polyline points=\"" + getPoints(width, height) + "\" style=\"" + style
				+ "\"></polyline> \n" + "\t<text x=\"10\" y=\"" + labelHeight + "\" style=\"" + style
				+ "\" >course</text>";
		return polyLineElement;
	}

	public File toFile(String fileName) throws IOException {
		File f = new File(fileName);
		FileWriter fw = new FileWriter(f);
		fw.write("<html><body><div style=\"width:1000px;height:1000pxx;overflow:scroll;border: 3px solid grey;\">"
				+ "<svg version=\"1.1\" style=\"overflow: scroll;\" width=\"1000\" height=\"1000\" id=\"Layer_1\" xmlns=\"http://www.w3.org/2000/svg\">");
		fw.write(getPolyLine(10, "red", 1000, 1000));
		fw.write("</svg></div></body></html>");
		fw.flush();
		fw.close();
		return f;
	}

}
