package com.emiliosg23.models.infos;

import java.io.File;

public class DirectoryInfo extends Info{
	public DirectoryInfo(String name, long size){
		super(name, size);
	}
	public DirectoryInfo(String name){
		super(name);
	}
	public DirectoryInfo(File file){
		super(file.getName());
	}
}
