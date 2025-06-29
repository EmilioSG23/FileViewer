package com.emiliosg23.models.infos;

import java.io.File;

import com.emiliosg23.utils.FileUtils;

public class FileInfo extends Info{
	private final String fullPath;
  private final String extension;

	public FileInfo(String name, long size, String fullPath, String extension){
		super(name, size);
		this.fullPath = fullPath;
		this.extension = extension;
	}
	public FileInfo(String name, String fullPath, String extension){
		super(name);
		this.fullPath = fullPath;
		this.extension = extension;
	}
	public FileInfo(File file){
		super(file.getName());
		this.fullPath = file.getAbsolutePath();
		this.extension = FileUtils.extractExtension(file.toString());
	}

	public String getExtension() {return this.extension;}
	public String getFullPath() {return this.fullPath;}
}
