package be.bdnlek.fitlek.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.io.IOUtils;

@XmlRootElement
@Entity
@Table
public class FitWrapper {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int file_id;

	@Lob
	private byte[] file = null;
	private String name = null;
	private String owner = null;

	public FitWrapper() {
		super();
	}

	public int getFile_id() {
		return file_id;
	}

	public void setFile_id(int file_id) {
		this.file_id = file_id;
	}

	@XmlTransient
	public byte[] getFile() {
		return file;
	}

	public void setFile(File file) throws FileNotFoundException, IOException {
		byte[] bytes = IOUtils.toByteArray(new FileInputStream(file));
		this.file = bytes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		Integer length = null;

		if (file != null) {
			length = file.length;
		}

		return "FitWrapper [fit_id=" + file_id + ", name=" + name + ", owner=" + owner + "], " + length + " bytes.";
	}

}
