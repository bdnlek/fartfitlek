package be.bdnlek.fitlek.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.garmin.fit.ActivityType;

import be.bdnlek.fitlek.ActivityException;
import be.bdnlek.fitlek.ActivityFactory;
import be.bdnlek.fitlek.FitDBRepository;
import be.bdnlek.fitlek.FitException;
import be.bdnlek.fitlek.IFitRepository;
import be.bdnlek.fitlek.Listener;
import be.bdnlek.fitlek.model.Activity;
import be.bdnlek.fitlek.model.FitWrapper;
import be.bdnlek.fitlek.model.TimeSeries;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@Path("/activities")
public class ActivityResource {

	private static Logger LOGGER = Logger.getLogger(ActivityResource.class.getName());
	private IFitRepository repo;
	private ActivityFactory activityFac;

	public ActivityResource() throws ActivityException {
		try {
			repo = new FitDBRepository("fartfitlek");
		} catch (FitException e) {
			throw new ActivityException(e);
		}
	}

	@ApiOperation(value = "Finds all Activities")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
	public List<FitWrapper> getActivities() throws FitException {
		LOGGER.info("-->");
		List<FitWrapper> fits = repo.getFitWrappers();
		LOGGER.info("<--");

		return fits;
	}

	@Path("/{id}/{timeSeries}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, MediaType.TEXT_XML })
	public TimeSeries getTimeSeries(@PathParam("id") Integer id, @PathParam("timeSeries") String timeSeries)
			throws ActivityException {

		try {
			activityFac = new ActivityFactory(repo.getFit(id));
		} catch (FitException e) {
			throw new ActivityException(e);
		}
		TimeSeries ts = activityFac.getActivity(Listener.SUPPORTED_CLASSES).getResults().getTimeSeries(timeSeries);
		if (ts != null) {
			return ts;
		} else {
			throw new NotFoundException();
		}
	}

	@Path("/{id}")
	@DELETE
	public void deleteFit(@PathParam("id") Integer id) throws ActivityException {
		try {
			repo.deleteFit(id);
		} catch (FitException e) {
			throw new ActivityException("Could not delete Fit-file with id=" + id, e);
		}
	}

	@Path("/{id}/metrics")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public String[] getMetrics(@PathParam("id") Integer id) throws ActivityException {
		ActivityFactory svc;
		try {
			svc = new ActivityFactory(repo.getFit(id));
		} catch (FitException e) {
			throw new ActivityException(e);
		}
		return svc.getActivity(Listener.SUPPORTED_CLASSES).getResults().getTimeSeries();
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public void addActivity(@FormDataParam("file") InputStream is,
			@FormDataParam("file") FormDataContentDisposition fileDisposition) throws FitException {
		File f;
		try {
			String fileName = fileDisposition.getFileName();
			f = File.createTempFile(fileName, "");
			try {
				OutputStream out = null;
				int read = 0;
				byte[] bytes = new byte[1024];

				out = new FileOutputStream(f);
				while ((read = is.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				out.flush();
				out.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
			ActivityFactory svc = new ActivityFactory(f);
			Activity activity = svc.getActivity();
			if (activity == null
					|| activity.getActivity().equals(ActivityType.getStringFromValue(ActivityType.CYCLING))) {
				throw new ActivityException("activity is null or not cycling ");
			} else {
				repo.addFit(f, fileName);
			}

		} catch (IOException | ActivityException e) {
			throw new FitException("error at upload", e);
		}

	}

}
