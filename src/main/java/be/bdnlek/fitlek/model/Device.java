package be.bdnlek.fitlek.model;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.garmin.fit.Manufacturer;

@XmlRootElement
public class Device {

	private static Map<String, Map<Integer, String>> PROPS_MAP = new HashMap<String, Map<Integer, String>>();

	private Map<String, String> properties = new HashMap<String, String>();
	private String manufacturer;
	private String source_type;
	private String product;
	private String device_type;
	private String garmin_product;
	private String antplus_device_type;

	static {
		Map<Integer, String> manufacturer_map = new HashMap<Integer, String>();
		manufacturer_map.put(1, "garmin");
		manufacturer_map.put(2, "garmin_fr405_antfs");
		manufacturer_map.put(3, "zephyr");
		manufacturer_map.put(4, "dayton");
		manufacturer_map.put(5, "idt");
		manufacturer_map.put(6, "srm");
		manufacturer_map.put(7, "quarq");
		manufacturer_map.put(8, "ibike");
		manufacturer_map.put(9, "saris");
		manufacturer_map.put(10, "spark_hk");
		manufacturer_map.put(11, "tanita");
		manufacturer_map.put(12, "echowell");
		manufacturer_map.put(13, "dynastream_oem");
		manufacturer_map.put(14, "nautilus");
		manufacturer_map.put(15, "dynastream");
		manufacturer_map.put(16, "timex");
		manufacturer_map.put(17, "metrigear");
		manufacturer_map.put(18, "xelic");
		manufacturer_map.put(19, "beurer");
		manufacturer_map.put(20, "cardiosport");
		manufacturer_map.put(21, "a_and_d");
		manufacturer_map.put(22, "hmm");
		manufacturer_map.put(23, "suunto");
		manufacturer_map.put(24, "thita_elektronik");
		manufacturer_map.put(25, "gpulse");
		manufacturer_map.put(26, "clean_mobile");
		manufacturer_map.put(27, "pedal_brain");
		manufacturer_map.put(28, "peaksware");
		manufacturer_map.put(29, "saxonar");
		manufacturer_map.put(30, "lemond_fitness");
		manufacturer_map.put(31, "dexcom");
		manufacturer_map.put(32, "wahoo_fitness");
		manufacturer_map.put(33, "octane_fitness");
		manufacturer_map.put(34, "archinoetics");
		manufacturer_map.put(35, "the_hurt_box");
		manufacturer_map.put(36, "citizen_systems");
		manufacturer_map.put(37, "magellan");
		manufacturer_map.put(38, "osynce");
		manufacturer_map.put(39, "holux");
		manufacturer_map.put(40, "concept2");
		manufacturer_map.put(42, "one_giant_leap");
		manufacturer_map.put(43, "ace_sensor");
		manufacturer_map.put(44, "brim_brothers");
		manufacturer_map.put(45, "xplova");
		manufacturer_map.put(46, "perception_digital");
		manufacturer_map.put(47, "bf1systems");
		manufacturer_map.put(48, "pioneer");
		manufacturer_map.put(49, "spantec");
		manufacturer_map.put(50, "metalogics");
		manufacturer_map.put(51, "4iiiis");
		manufacturer_map.put(52, "seiko_epson");
		manufacturer_map.put(53, "seiko_epson_oem");
		manufacturer_map.put(54, "ifor_powell");
		manufacturer_map.put(55, "maxwell_guider");
		manufacturer_map.put(56, "star_trac");
		manufacturer_map.put(57, "breakaway");
		manufacturer_map.put(58, "alatech_technology_ltd");
		manufacturer_map.put(59, "mio_technology_europe");
		manufacturer_map.put(60, "rotor");
		manufacturer_map.put(61, "geonaute");
		manufacturer_map.put(62, "id_bike");
		manufacturer_map.put(63, "specialized");
		manufacturer_map.put(64, "wtek");
		manufacturer_map.put(65, "physical_enterprises");
		manufacturer_map.put(66, "north_pole_engineering");
		manufacturer_map.put(67, "bkool");
		manufacturer_map.put(68, "cateye");
		manufacturer_map.put(69, "stages_cycling");
		manufacturer_map.put(70, "sigmasport");
		manufacturer_map.put(71, "tomtom");
		manufacturer_map.put(72, "peripedal");
		manufacturer_map.put(73, "wattbike");
		manufacturer_map.put(76, "moxy");
		manufacturer_map.put(77, "ciclosport");
		manufacturer_map.put(78, "powerbahn");
		manufacturer_map.put(79, "acorn_projects_aps");
		manufacturer_map.put(80, "lifebeam");
		manufacturer_map.put(81, "bontrager");
		manufacturer_map.put(82, "wellgo");
		manufacturer_map.put(83, "scosche");
		manufacturer_map.put(84, "magura");
		manufacturer_map.put(85, "woodway");
		manufacturer_map.put(86, "elite");
		manufacturer_map.put(87, "nielsen_kellerman");
		manufacturer_map.put(88, "dk_city");
		manufacturer_map.put(89, "tacx");
		manufacturer_map.put(90, "direction_technology");
		manufacturer_map.put(91, "magtonic");
		manufacturer_map.put(92, "1partcarbon");
		manufacturer_map.put(93, "inside_ride_technologies");
		manufacturer_map.put(94, "sound_of_motion");
		manufacturer_map.put(95, "stryd");
		manufacturer_map.put(96, "icg");
		manufacturer_map.put(97, "MiPulse");
		manufacturer_map.put(98, "bsx_athletics");
		manufacturer_map.put(99, "look");
		manufacturer_map.put(100, "campagnolo_srl");
		manufacturer_map.put(101, "body_bike_smart");
		manufacturer_map.put(102, "praxisworks");
		manufacturer_map.put(103, "limits_technology");
		manufacturer_map.put(104, "topaction_technology");
		manufacturer_map.put(105, "cosinuss");
		manufacturer_map.put(106, "fitcare");
		manufacturer_map.put(107, "magene");
		manufacturer_map.put(108, "giant_manufacturing_co");
		manufacturer_map.put(109, "tigrasport");
		manufacturer_map.put(110, "salutron");
		manufacturer_map.put(111, "technogym");
		manufacturer_map.put(112, "bryton_sensors");
		manufacturer_map.put(113, "latitude_limited");
		manufacturer_map.put(114, "soaring_technology");
		manufacturer_map.put(115, "igpsport");
		manufacturer_map.put(255, "development");
		manufacturer_map.put(257, "healthandlife");
		manufacturer_map.put(258, "lezyne");
		manufacturer_map.put(259, "scribe_labs");
		manufacturer_map.put(260, "zwift");
		manufacturer_map.put(261, "watteam");
		manufacturer_map.put(262, "recon");
		manufacturer_map.put(263, "favero_electronics");
		manufacturer_map.put(264, "dynovelo");
		manufacturer_map.put(265, "strava");
		manufacturer_map.put(266, "precor");
		manufacturer_map.put(267, "bryton");
		manufacturer_map.put(268, "sram");
		manufacturer_map.put(269, "navman");
		manufacturer_map.put(270, "cobi");
		manufacturer_map.put(271, "spivi");
		manufacturer_map.put(272, "mio_magellan");
		manufacturer_map.put(273, "evesports");
		manufacturer_map.put(274, "sensitivus_gauge");
		manufacturer_map.put(275, "podoon");
		manufacturer_map.put(276, "life_time_fitness");
		manufacturer_map.put(277, "falco_e_motors");
		manufacturer_map.put(278, "minoura");
		manufacturer_map.put(279, "cycliq");
		manufacturer_map.put(280, "luxottica");
		manufacturer_map.put(281, "trainer_road");
		manufacturer_map.put(282, "the_sufferfest");
		manufacturer_map.put(283, "fullspeedahead");
		manufacturer_map.put(5759, "actigraphcorp");

		Map<Integer, String> garmin_product_map = new HashMap<Integer, String>();

		garmin_product_map.put(1, "hrm1");
		garmin_product_map.put(2, "axh01");
		garmin_product_map.put(3, "axb01");
		garmin_product_map.put(4, "axb02");
		garmin_product_map.put(5, "hrm2ss");
		garmin_product_map.put(6, "dsi_alf02");
		garmin_product_map.put(7, "hrm3ss");
		garmin_product_map.put(8, "hrm_run_single_byte_product_id");
		garmin_product_map.put(9, "bsm");
		garmin_product_map.put(10, "bcm");
		garmin_product_map.put(11, "axs01");
		garmin_product_map.put(12, "hrm_tri_single_byte_product_id");
		garmin_product_map.put(14, "fr225_single_byte_product_id");
		garmin_product_map.put(473, "fr301_china");
		garmin_product_map.put(474, "fr301_japan");
		garmin_product_map.put(475, "fr301_korea");
		garmin_product_map.put(494, "fr301_taiwan");
		garmin_product_map.put(717, "fr405");
		garmin_product_map.put(782, "fr50");
		garmin_product_map.put(987, "fr405_japan");
		garmin_product_map.put(988, "fr60");
		garmin_product_map.put(1011, "dsi_alf01");
		garmin_product_map.put(1018, "fr310xt");
		garmin_product_map.put(1036, "edge500");
		garmin_product_map.put(1124, "fr110");
		garmin_product_map.put(1169, "edge800");
		garmin_product_map.put(1199, "edge500_taiwan");
		garmin_product_map.put(1213, "edge500_japan");
		garmin_product_map.put(1253, "chirp");
		garmin_product_map.put(1274, "fr110_japan");
		garmin_product_map.put(1325, "edge200");
		garmin_product_map.put(1328, "fr910xt");
		garmin_product_map.put(1333, "edge800_taiwan");
		garmin_product_map.put(1334, "edge800_japan");
		garmin_product_map.put(1341, "alf04");
		garmin_product_map.put(1345, "fr610");
		garmin_product_map.put(1360, "fr210_japan");
		garmin_product_map.put(1380, "vector_ss");
		garmin_product_map.put(1381, "vector_cp");
		garmin_product_map.put(1386, "edge800_china");
		garmin_product_map.put(1387, "edge500_china");
		garmin_product_map.put(1410, "fr610_japan");
		garmin_product_map.put(1422, "edge500_korea");
		garmin_product_map.put(1436, "fr70");
		garmin_product_map.put(1446, "fr310xt_4t");
		garmin_product_map.put(1461, "amx");
		garmin_product_map.put(1482, "fr10");
		garmin_product_map.put(1497, "edge800_korea");
		garmin_product_map.put(1499, "swim");
		garmin_product_map.put(1537, "fr910xt_china");
		garmin_product_map.put(1551, "fenix");
		garmin_product_map.put(1555, "edge200_taiwan");
		garmin_product_map.put(1561, "edge510");
		garmin_product_map.put(1567, "edge810");
		garmin_product_map.put(1570, "tempe");
		garmin_product_map.put(1600, "fr910xt_japan");
		garmin_product_map.put(1623, "fr620");
		garmin_product_map.put(1632, "fr220");
		garmin_product_map.put(1664, "fr910xt_korea");
		garmin_product_map.put(1688, "fr10_japan");
		garmin_product_map.put(1721, "edge810_japan");
		garmin_product_map.put(1735, "virb_elite");
		garmin_product_map.put(1736, "edge_touring");
		garmin_product_map.put(1742, "edge510_japan");
		garmin_product_map.put(1743, "hrm_tri");
		garmin_product_map.put(1752, "hrm_run");
		garmin_product_map.put(1765, "fr920xt");
		garmin_product_map.put(1821, "edge510_asia");
		garmin_product_map.put(1822, "edge810_china");
		garmin_product_map.put(1823, "edge810_taiwan");
		garmin_product_map.put(1836, "edge1000");
		garmin_product_map.put(1837, "vivo_fit");
		garmin_product_map.put(1853, "virb_remote");
		garmin_product_map.put(1885, "vivo_ki");
		garmin_product_map.put(1903, "fr15");
		garmin_product_map.put(1907, "vivo_active");
		garmin_product_map.put(1918, "edge510_korea");
		garmin_product_map.put(1928, "fr620_japan");
		garmin_product_map.put(1929, "fr620_china");
		garmin_product_map.put(1930, "fr220_japan");
		garmin_product_map.put(1931, "fr220_china");
		garmin_product_map.put(1936, "approach_s6");
		garmin_product_map.put(1956, "vivo_smart");
		garmin_product_map.put(1967, "fenix2");
		garmin_product_map.put(1988, "epix");
		garmin_product_map.put(2050, "fenix3");
		garmin_product_map.put(2052, "edge1000_taiwan");
		garmin_product_map.put(2053, "edge1000_japan");
		garmin_product_map.put(2061, "fr15_japan");
		garmin_product_map.put(2067, "edge520");
		garmin_product_map.put(2070, "edge1000_china");
		garmin_product_map.put(2072, "fr620_russia");
		garmin_product_map.put(2073, "fr220_russia");
		garmin_product_map.put(2079, "vector_s");
		garmin_product_map.put(2100, "edge1000_korea");
		garmin_product_map.put(2130, "fr920xt_taiwan");
		garmin_product_map.put(2131, "fr920xt_china");
		garmin_product_map.put(2132, "fr920xt_japan");
		garmin_product_map.put(2134, "virbx");
		garmin_product_map.put(2135, "vivo_smart_apac");
		garmin_product_map.put(2140, "etrex_touch");
		garmin_product_map.put(2147, "edge25");
		garmin_product_map.put(2148, "fr25");
		garmin_product_map.put(2150, "vivo_fit2");
		garmin_product_map.put(2153, "fr225");
		garmin_product_map.put(2156, "fr630");
		garmin_product_map.put(2157, "fr230");
		garmin_product_map.put(2160, "vivo_active_apac");
		garmin_product_map.put(2161, "vector_2");
		garmin_product_map.put(2162, "vector_2s");
		garmin_product_map.put(2172, "virbxe");
		garmin_product_map.put(2173, "fr620_taiwan");
		garmin_product_map.put(2174, "fr220_taiwan");
		garmin_product_map.put(2175, "truswing");
		garmin_product_map.put(2188, "fenix3_china");
		garmin_product_map.put(2189, "fenix3_twn");
		garmin_product_map.put(2192, "varia_headlight");
		garmin_product_map.put(2193, "varia_taillight_old");
		garmin_product_map.put(2204, "edge_explore_1000");
		garmin_product_map.put(2219, "fr225_asia");
		garmin_product_map.put(2225, "varia_radar_taillight");
		garmin_product_map.put(2226, "varia_radar_display");
		garmin_product_map.put(2238, "edge20");
		garmin_product_map.put(2262, "d2_bravo");
		garmin_product_map.put(2266, "approach_s20");
		garmin_product_map.put(2276, "varia_remote");
		garmin_product_map.put(2327, "hrm4_run");
		garmin_product_map.put(2337, "vivo_active_hr");
		garmin_product_map.put(2347, "vivo_smart_gps_hr");
		garmin_product_map.put(2348, "vivo_smart_hr");
		garmin_product_map.put(2368, "vivo_move");
		garmin_product_map.put(2398, "varia_vision");
		garmin_product_map.put(2406, "vivo_fit3");
		garmin_product_map.put(2413, "fenix3_hr");
		garmin_product_map.put(2417, "virb_ultra_30");
		garmin_product_map.put(2429, "index_smart_scale");
		garmin_product_map.put(2431, "fr235");
		garmin_product_map.put(2432, "fenix3_chronos");
		garmin_product_map.put(2441, "oregon7xx");
		garmin_product_map.put(2444, "rino7xx");
		garmin_product_map.put(2496, "nautix");
		garmin_product_map.put(2530, "edge_820");
		garmin_product_map.put(2531, "edge_explore_820");
		garmin_product_map.put(2544, "fenix5s");
		garmin_product_map.put(2547, "d2_bravo_titanium");
		garmin_product_map.put(2593, "running_dynamics_pod");
		garmin_product_map.put(2604, "fenix5x");
		garmin_product_map.put(2606, "vivo_fit_jr");
		garmin_product_map.put(2691, "fr935");
		garmin_product_map.put(2697, "fenix5");
		garmin_product_map.put(10007, "sdm4");
		garmin_product_map.put(10014, "edge_remote");
		garmin_product_map.put(20119, "training_center");
		garmin_product_map.put(65531, "connectiq_simulator");
		garmin_product_map.put(65532, "android_antplus_plugin");
		garmin_product_map.put(65534, "connect");

		Map<Integer, String> battery_status_map = new HashMap<Integer, String>();

		garmin_product_map.put(1, "new");
		garmin_product_map.put(2, "good");
		garmin_product_map.put(3, "ok");
		garmin_product_map.put(4, "low");
		garmin_product_map.put(5, "critical");
		garmin_product_map.put(6, "charging");
		garmin_product_map.put(7, "unknown");

		Map<Integer, String> source_type_map = new HashMap<Integer, String>();

		source_type_map.put(0, "ant");
		source_type_map.put(1, "antplus");
		source_type_map.put(2, "bluetooth");
		source_type_map.put(3, "bluetooth_low_energy");
		source_type_map.put(4, "wifi");
		source_type_map.put(5, "local");

		Map<Integer, String> antplus_device_type_map = new HashMap<Integer, String>();

		antplus_device_type_map.put(1, "antfs");
		antplus_device_type_map.put(11, "bike_power");
		antplus_device_type_map.put(12, "environment_sensor_legacy");
		antplus_device_type_map.put(15, "multi_sport_speed_distance");
		antplus_device_type_map.put(16, "control");
		antplus_device_type_map.put(17, "fitness_equipment");
		antplus_device_type_map.put(18, "blood_pressure");
		antplus_device_type_map.put(19, "geocache_node");
		antplus_device_type_map.put(20, "light_electric_vehicle");
		antplus_device_type_map.put(25, "env_sensor");
		antplus_device_type_map.put(26, "racquet");
		antplus_device_type_map.put(27, "control_hub");
		antplus_device_type_map.put(31, "muscle_oxygen");
		antplus_device_type_map.put(35, "bike_light_main");
		antplus_device_type_map.put(36, "bike_light_shared");
		antplus_device_type_map.put(38, "exd");
		antplus_device_type_map.put(40, "bike_radar");
		antplus_device_type_map.put(119, "weight_scale");
		antplus_device_type_map.put(120, "heart_rate");
		antplus_device_type_map.put(121, "bike_speed_cadence");
		antplus_device_type_map.put(122, "bike_cadence");
		antplus_device_type_map.put(123, "bike_speed");
		antplus_device_type_map.put(124, "stride_speed_distance");

		PROPS_MAP.put("manufacturer", manufacturer_map);
		PROPS_MAP.put("garmin_product", garmin_product_map);
		PROPS_MAP.put("battery_status", battery_status_map);
		PROPS_MAP.put("source_type", source_type_map);
		PROPS_MAP.put("antplus_device_type", antplus_device_type_map);
	}

	public void set(String propName, String propValue) {

		properties.put(propName, propValue);

	}

	public void resolve() {
		product = properties.get("product");
		manufacturer = properties.get("manufacturer");
		source_type = properties.get("source_type");
		device_type = properties.get("device_type");
		if ((manufacturer != null) && (manufacturer.equals(Manufacturer.GARMIN + "")) && (product != null)) {
			garmin_product = product;
			product = product + " (see garmin_product)";
		}
		if ((source_type != null) && (source_type.equals("1")) && device_type != null) {
			antplus_device_type = device_type;
			device_type = device_type + " (see antplus_device_type)";
		}
		for (String propName : properties.keySet()) {
			Map<Integer, String> map = PROPS_MAP.get(propName);
			if (map != null) {
				String propValue = properties.get(propName);
				Integer propInt = Integer.valueOf(propValue);
				properties.put(propName, map.get(propInt));
			}
		}
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getSource_type() {
		return source_type;
	}

	public void setSource_type(String source_type) {
		this.source_type = source_type;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	public String getGarmin_product() {
		return garmin_product;
	}

	public void setGarmin_product(String garmin_product) {
		this.garmin_product = garmin_product;
	}

	public String getAntplus_device_type() {
		return antplus_device_type;
	}

	public void setAntplus_device_type(String antplus_device_type) {
		this.antplus_device_type = antplus_device_type;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	@XmlTransient
	public String get(String propName) {
		return properties.get(propName);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("Device:\n");
		for (String propName : properties.keySet()) {
			sb.append("\t" + propName + " = " + properties.get(propName) + "\n");
		}
		return sb.toString();
	}

}
