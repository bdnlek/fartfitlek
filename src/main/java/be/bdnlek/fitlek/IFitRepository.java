package be.bdnlek.fitlek;

import java.io.File;
import java.util.List;

public interface IFitRepository {

	public void addFitFile(File f, String fileName) throws FitException;

	public List<String> getFits();

	public List<File> getFitFiles();

	public File getFit(Integer id);

}
