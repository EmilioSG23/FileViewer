package com.emiliosg23.models.infos;

import com.emiliosg23.utils.FileUtils;

public class Info {
	private final String name;
	private long size;

	public Info(String name, long size){
		this.name = name;
		this.size = size;
	}

	public Info(String name){
		this.name = name;
		this.size = 0;
	}

	public String getName(){
		return this.name;
	}
	public long getSize(){
		return this.size;
	}
	public void setSize(long size){
		this.size = size;
	}

	@Override
	public String toString(){
			return getName()+" ("+FileUtils.getConvertedSize(this.size)+")";
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Info)
				return this.equals((Info) o);
		return false;
	}
	public boolean equals(Info fi){
		return getName().equals(fi.getName());
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}
}
