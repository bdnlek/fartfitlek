package be.bdnlek.fitlek.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FileList {

	private List<String> fileNames = new ArrayList<String>();

	public FileList() {

	}

	public FileList(List<String> fileNames) {
		this.setFileNames(fileNames);
	}

	/**
	 * @return the fileNames
	 */

	@XmlElement(name = "fileNames")
	public List<String> getFileNames() {
		return fileNames;
	}

	/**
	 * @param fileNames
	 *            the fileNames to set
	 */
	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
	}

}
