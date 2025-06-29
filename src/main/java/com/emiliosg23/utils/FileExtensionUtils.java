package com.emiliosg23.utils;

import java.util.*;

public class FileExtensionUtils {
    private static final Map<String, int[]> EXTENSION_COLORS = new HashMap<>();

    static {
        addColor("",        255, 240, 100);  // Directorio
        addColor(".exe",    125, 125, 125);  // Ejecutable

				// Word
				String[] wordExts = {".doc", ".docx", ".docm", ".dotx", ".dotm"};
				for (String ext : wordExts) addColor(ext, 100, 157, 255);

				// PowerPoint
				String[] pptExts = {".ppt", ".pptx", ".pptm", ".pps", ".ppsx", ".ppsm", ".pot", ".potx", ".potm"};
				for (String ext : pptExts) addColor(ext, 240, 150, 97);

				// Excel
				String[] excelExts = {".xlsx", ".xls", ".xlsm", ".xlsb", ".xlam", ".xltx", ".xltm", ".xlt", ".xl"};
				for (String ext : excelExts) addColor(ext, 90, 205, 0);

				// Otros
				String[] imgExts = {".png", ".jpg", ".jpeg", ".bmp", ".gif", ".tif"};
				for (String ext : imgExts) addColor(ext, 150, 250, 255);

				addColor(".txt", 200, 200, 200);
				addColor(".pdf", 255, 83, 140);

				// Video
				String[] videoExts = {".mov", ".mkv", ".mp4", ".wmv", ".flv", ".avi", ".asf", ".rm", ".rmvb", ".mks", ".3gpp", ".m4v", ".mpg", ".mpeg"};
				for (String ext : videoExts) addColor(ext, 255, 100, 255);

				// Audio
				String[] audioExts = {".m4a", ".aiff", ".au", ".mid", ".midi", ".mp3", ".wav", ".wma"};
				for (String ext : audioExts) addColor(ext, 100, 255, 200);

				// Comprimidos
				String[] zipExts = {".zip", ".rar"};
				for (String ext : zipExts) addColor(ext, 215, 1, 130);

				// Lenguajes
				String[] javaExts = {".java", ".jar"};
				for (String ext : javaExts) addColor(ext, 255, 180, 120);
				
				addColor(".py", 50, 50, 50);
    }

    private static void addColor(String ext, int r, int g, int b) {
      EXTENSION_COLORS.put(ext.toLowerCase(), new int[]{r, g, b});
    }

    public static String getColor(String extension) {
        int[] baseRGB = EXTENSION_COLORS.getOrDefault(extension.toLowerCase(), new int[]{200, 200, 200});
        return generateRGBA(baseRGB[0], baseRGB[1], baseRGB[2]);
    }

    private static String generateRGBA(int r, int g, int b) {
        int delta = getRandomNumber(5); // small random change
        r = clamp(r + delta);
        g = clamp(g + delta);
        b = clamp(b + delta);
        double opacity = 0.7 + Math.random() * 0.3; // between 0.7 and 1.0
        return String.format(Locale.US, "rgba(%d, %d, %d, %.2f)", r, g, b, opacity);
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    private static int getRandomNumber(int range) {
        return (int) (Math.random() * (range * 2 + 1)) - range;
    }
}
