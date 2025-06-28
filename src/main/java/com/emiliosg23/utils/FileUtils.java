package com.emiliosg23.utils;

public class FileUtils {
	public static String lastDirectory(String directorio){
		String[] tokens=directorio.split("\\\\");
		return tokens[tokens.length-1];
	}
	public static String getConvertedSize(long size) {
		if (size <= 0) return "0 bytes";

		final String[] units = { "bytes", "KB", "MB", "GB", "TB", "PB", "EB" };
		final int base = 1024;

		int unitIndex = 0;
		double readableSize = size;

		while (readableSize >= base && unitIndex < units.length - 1) {
			readableSize /= base;
			unitIndex++;
		}

		return String.format("%.2f %s", readableSize, units[unitIndex]);
	}
}
