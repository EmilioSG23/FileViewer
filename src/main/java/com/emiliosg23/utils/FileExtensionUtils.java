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

				// Web y frontend
				addColor(".html", 255, 153, 0);
				addColor(".htm", 255, 153, 0);
				addColor(".css", 86, 156, 214);
				addColor(".scss", 204, 102, 153);
				addColor(".sass", 204, 102, 153);
				addColor(".js", 255, 215, 0);
				addColor(".jsx", 0, 200, 255);
				addColor(".ts", 0, 122, 204);
				addColor(".tsx", 0, 180, 255);
				addColor(".json", 255, 255, 255);
				addColor(".xml", 200, 200, 0);
				addColor(".yml", 255, 200, 0);
				addColor(".yaml", 255, 200, 0);
				addColor(".astro", 255, 150, 255);

				// Backend / scripts
				addColor(".sh", 0, 255, 0);
				addColor(".bat", 100, 100, 100);
				addColor(".cmd", 100, 100, 100);
				addColor(".ps1", 0, 100, 255);
				addColor(".rb", 255, 0, 128);         // Ruby
				addColor(".rs", 255, 64, 64);         // Rust
				addColor(".go", 0, 173, 216);         // Golang
				addColor(".c", 0, 255, 255);
				addColor(".h", 100, 255, 255);
				addColor(".cpp", 0, 128, 255);
				addColor(".hpp", 100, 200, 255);
				addColor(".cs", 104, 33, 122);        // C#
				addColor(".kt", 255, 135, 0);         // Kotlin
				addColor(".kts", 255, 135, 0);
				addColor(".swift", 255, 69, 0);
				addColor(".dart", 0, 180, 180);
				addColor(".php", 79, 93, 149);
				addColor(".sql", 255, 255, 100);
				addColor(".pl", 200, 100, 255);       // Perl

				// Audio
				addColor(".aac", 120, 255, 210);
				addColor(".ogg", 140, 255, 180);
				addColor(".flac", 180, 255, 180);
				addColor(".aif", 100, 255, 200);
				addColor(".aiff", 100, 255, 200);
				addColor(".opus", 80, 230, 190);

				// Video
				addColor(".webm", 255, 110, 240);
				addColor(".m4v", 255, 100, 255);
				addColor(".3gp", 255, 120, 255);
				addColor(".vob", 255, 60, 255);
				addColor(".ts", 255, 80, 255);
				addColor(".f4v", 255, 90, 255);

				// Imágenes
				addColor(".webp", 150, 250, 255);
				addColor(".svg", 100, 200, 255);
				addColor(".ico", 255, 255, 100);
				addColor(".tiff", 180, 250, 255);

				// Documentos / libros
				addColor(".epub", 255, 215, 180);
				addColor(".mobi", 255, 190, 140);
				addColor(".odt", 100, 157, 255);      // OpenDocument Word
				addColor(".odp", 240, 150, 97);       // OpenDocument PowerPoint
				addColor(".ods", 90, 205, 0);         // OpenDocument Spreadsheet

				// Comprimidos
				addColor(".7z", 215, 1, 130);
				addColor(".tar", 215, 1, 130);
				addColor(".gz", 215, 1, 130);
				addColor(".xz", 215, 1, 130);
				addColor(".bz2", 215, 1, 130);

				// Sistema y varios
				addColor(".log", 200, 200, 200);
				addColor(".bak", 160, 160, 160);
				addColor(".tmp", 150, 150, 150);
				addColor(".ini", 100, 255, 255);
				addColor(".cfg", 100, 255, 255);
				addColor(".db", 120, 120, 255);
				addColor(".sqlite", 120, 120, 255);
				addColor(".md", 150, 200, 255);       // Markdown
				addColor(".csv", 90, 200, 90);
				addColor(".tsv", 100, 210, 100);
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
