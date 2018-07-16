package be.bdnlek.fitlek;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import be.bdnlek.fitlek.model.Activity;

public class ActivityTest {

	@Test
	public void toXml() throws JAXBException, URISyntaxException, ActivityException, JsonGenerationException,
			JsonMappingException, IOException {
		URL url = this.getClass().getClassLoader().getResource("2016-01-01-12-38-21.fit");
		if (url == null) {
			fail("fitfile could not be found");
		}
		File file = new File(new URI(url.toString()));
		ActivityFactory svc = new ActivityFactory(file);
		XmlMapper mapper = new XmlMapper();
		Activity a = svc.getActivity();
		mapper.writeValue(System.out, a);
		/*
		 * JAXBContext ctx = JAXBContextFactory.createContext(new Class[] {
		 * FileList.class, Activity.class }, null); Marshaller m =
		 * ctx.createMarshaller(); m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
		 * true); m.marshal(a, System.out);
		 */ }

	@Test
	public void toJson() throws JAXBException, URISyntaxException, ActivityException, JsonGenerationException,
			JsonMappingException, IOException {
		URL url = this.getClass().getClassLoader().getResource("2016-01-01-12-38-21.fit");
		if (url == null) {
			fail("fitfile could not be found");
		}
		File file = new File(new URI(url.toString()));
		ActivityFactory svc = new ActivityFactory(file);
		ObjectMapper mapper = new ObjectMapper();
		Activity a = svc.getActivity(Listener.SUPPORTED_CLASSES);
		mapper.writeValue(System.out, a);
		/*
		 * JAXBContext ctx = JAXBContextFactory.createContext(new Class[] {
		 * FileList.class, Activity.class }, null); Marshaller m =
		 * ctx.createMarshaller(); m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
		 * true); m.setProperty(MarshallerProperties., "application/json");
		 * m.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false); Activity a =
		 * svc.getActivity(); m.marshal(a, System.out);
		 */ }

}
