package be.bdnlek.fitlek;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import be.bdnlek.fitlek.model.FitWrapper;

public class FitDBRepository implements IFitRepository {

	private static Logger LOG = Logger.getLogger("be.bdnlek.fitlek.FitDBRepository");

	private static EntityManagerFactory EMF = null;

	private static void initializeEmf(String databaseUrl, String persistenceUnit) {

		StringTokenizer st = new StringTokenizer(databaseUrl, ":@/");
		String dbVendor = st.nextToken(); // if DATABASE_URL is set
		String userName = st.nextToken();
		String password = st.nextToken();
		String host = st.nextToken();
		String port = st.nextToken();
		String databaseName = st.nextToken();
		LOG.info(String.format("dbvendor:%s;userName:%s;password:%s;host:%s;port:%s;databaseName:%s;", dbVendor,
				userName, password, host, port, databaseName));
		Map<String, String> properties = new HashMap<String, String>();

		switch (dbVendor) {
		case "postgres":
			// String jdbcUrl =
			// String.format("jdbc:postgresql://%s:%s/%s?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",
			// host, port, databaseName);
			String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s?sslmode=require", host, port, databaseName);
			LOG.info("jdbcUrl: " + jdbcUrl);
			properties.put("javax.persistence.jdbc.url", jdbcUrl);
			properties.put("javax.persistence.jdbc.user", userName);
			properties.put("javax.persistence.jdbc.password", password);
			properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
			break;
		case "derby":
			jdbcUrl = "jdbc:derby:/tmp/derbyDB/" + databaseName + ";create=true";
			LOG.info("jdbcUrl: " + jdbcUrl);
			properties.put("javax.persistence.jdbc.url", jdbcUrl);
			properties.put("javax.persistence.jdbc.driver", "org.apache.derby.jdbc.EmbeddedDriver");
			break;

		default:
			break;
		}

		EMF = Persistence.createEntityManagerFactory(persistenceUnit, properties);
	}

	public FitDBRepository(String persistenceUnit) throws FitException {
		String databaseUrl = System.getenv("DATABASE_URL");
		if (databaseUrl == null) {
			throw new FitException("the environment variable DATABASE_URL was not set", null);
		}
		if (EMF == null) {
			initializeEmf(databaseUrl, persistenceUnit);
		}
	}

	@Override
	public void addFitFile(File file, String fileName) throws FitException {
		EntityManager em = EMF.createEntityManager();

		FitWrapper fitWrapper = new FitWrapper();
		try {
			fitWrapper.setFile(file);
			fitWrapper.setName(fileName);
			fitWrapper.setOwner("");
			em.getTransaction().begin();
			em.persist(fitWrapper);
			em.getTransaction().commit();
		} catch (IOException e) {
			throw new FitException(e.getMessage(), e);
		} finally {
			em.close();
		}

	}

	@Override
	public List<String> getFits() {
		List<String> list = new ArrayList<String>();
		EntityManager em = EMF.createEntityManager();

		Query q = em.createQuery("select x from FitWrapper x");
		List<FitWrapper> fitWrappers = q.getResultList();
		for (FitWrapper fw : fitWrappers) {
			list.add(fw.getName() + "");
		}

		em.close();
		return list;

	}

	@Override
	public List<FitWrapper> getFitWrappers() {
		EntityManager em = EMF.createEntityManager();

		Query q = em.createQuery("select x from FitWrapper x");
		List<FitWrapper> fitWrappers = q.getResultList();

		em.close();
		return fitWrappers;

	}

	@Override
	public File getFit(Integer id) throws FitException {

		EntityManager em = EMF.createEntityManager();

		Query q = em.createQuery("select x from FitWrapper x where x.file_id = " + id);
		List<FitWrapper> list = (List<FitWrapper>) q.getResultList();
		int size = list.size();
		LOG.info("There are " + size + " fit-files with id=" + id);
		if (list.size() == 0) {
			em.close();
			return null;
		} else if (list.size() == 1) {
			File tempFile;
			try {
				tempFile = File.createTempFile("tempFile", ".fit");
				FileOutputStream fos = new FileOutputStream(tempFile);
				try {
					fos.write(list.get(0).getFile());
				} finally {
					fos.close();
					em.close();
				}
			} catch (IOException e) {
				throw new FitException("could not create temporary file", e);
			}
			return tempFile;
		} else {
			em.close();
			throw new FitException("at most one fit file with id = " + id + " should exist instead of " + list.size(),
					null);
		}

	}

}
