package be.bdnlek.fitlek;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FitRepository implements IFitRepository {

	private static Logger LOGGER = Logger.getLogger(FitRepository.class.getName());
	private File repositoryDir = null;

	public FitRepository(String directoryName) {
		File dir = new File(directoryName);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (directoryName != null && dir.exists() && dir.isDirectory() && dir.canWrite()) {
			repositoryDir = dir;
		} else {
			throw new IllegalArgumentException(
					"the directoryName " + dir.getAbsolutePath() + " must be an existing directory with write-access");
		}
	}

	public void addFitFile(File f, String fileName) throws FitException {
		if (f.exists() && f.canRead()) {
			File repoFile = new File(repositoryDir + File.separator + fileName);
			try {
				Files.copy(f.toPath(), new FileOutputStream(repoFile));
			} catch (IOException e) {
				throw new FitException("something went wrong during the copy of the file to the repository", e);
			}
		}
	}

	public List<File> getFitFiles() {
		List<File> files = new ArrayList<File>();
		for (String fileName : repositoryDir.list()) {
			files.add(new File(repositoryDir, fileName));
		}
		files.sort(null);
		return files;
	}

	public List<String> getFits() {
		List<String> fileNames = new ArrayList<String>();
		List<File> files = new ArrayList<File>();
		for (String fileName : repositoryDir.list()) {
			fileNames.add(fileName);
		}
		fileNames.sort(null);
		return fileNames;
	}

	public File getFit(Integer id) {
		List<String> fits = getFits();
		String fitName = fits.get(id);
		if (fitName == null || id > fits.size()) {
			return null;
		}
		File fit = new File(repositoryDir + File.separator + getFits().get(id));
		if (fit.exists() && fit.canRead()) {
			return fit;
		} else {
			return null;
		}
	}

	public File getFit(String fileName) {
		Integer index = getFits().indexOf(fileName);
		if (index < 0) {
			return null;
		}
		File fit = new File(repositoryDir + File.separator + fileName);
		if (fit.exists() && fit.canRead()) {
			return fit;
		}
		return null;
	}

}
