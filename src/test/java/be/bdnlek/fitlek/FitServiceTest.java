package be.bdnlek.fitlek;

import java.io.File;

import org.junit.Test;

public class FitServiceTest {

	@Test
	public void test() {
		// File fitFile = new File("src/test/resources/2016-01-01-12-38-21.fit");
		// File fitFile = new File("src/test/resources/2016-01-09-10-19-25.fit");
		File fitFile = new File("src/test/resources/2016-01-10-08-56-45.fit");

		try {
			FitService service = new FitService(fitFile);
			System.out.println(service.getActivity(Listener.SUPPORTED_CLASSES).getResults().toString());
		} catch (FitServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
