package be.bdnlek.fitlek;

import java.io.File;
import java.util.List;

import be.bdnlek.fitlek.model.FitWrapper;

public interface IFitRepository {

	public void addFitFile(File f, String fileName) throws FitException;

	public List<String> getFits();

	public List<FitWrapper> getFitWrappers() throws FitException;

	public File getFit(Integer id) throws FitException;

}
