package com.emiliosg23.models.infos;

import java.io.File;

import com.emiliosg23.utils.FileUtils;

public class FileInfo extends Info{
  private final String extension;

	public FileInfo(String name, long size, String extension){
		super(name, size);
		this.extension = extension;
	}
	public FileInfo(String name, String extension){
		super(name);
		this.extension = extension;
	}
	public FileInfo(File file){
		super(file.getName());
		this.extension = FileUtils.lastDirectory(file.toString());
	}

	public String getExtension(){
		return this.extension;
	}
}
