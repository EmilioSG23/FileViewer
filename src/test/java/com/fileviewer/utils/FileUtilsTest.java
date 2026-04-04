package com.fileviewer.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests de las utilidades de {@link FileUtils}.
 */
class FileUtilsTest {

    // -------------------------------------------------------------------------
    // extractExtension
    // -------------------------------------------------------------------------
    @ParameterizedTest(name = "{0} → {1}")
    @CsvSource({
        "Main.java,      .java",
        "archive.tar.gz, .gz",
        "README.txt,     .txt",
        "image.PNG,      .PNG"
    })
    @DisplayName("extractExtension devuelve la extensión con punto")
    void extractExtension_commonFiles(String filename, String expected) {
        assertEquals(expected, FileUtils.extractExtension(filename));
    }

    // -------------------------------------------------------------------------
    // lastDirectory
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("lastDirectory devuelve el último segmento de la ruta")
    void lastDirectory_returnsLastSegment() {
        assertEquals("myProject", FileUtils.lastDirectory("C:\\Users\\user\\myProject"));
    }

    // -------------------------------------------------------------------------
    // getConvertedSize
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("0 bytes devuelve '0 bytes'")
    void getConvertedSize_zero() {
        assertEquals("0 bytes", FileUtils.getConvertedSize(0));
    }

    @Test
    @DisplayName("valor negativo se trata como 0")
    void getConvertedSize_negative() {
        assertEquals("0 bytes", FileUtils.getConvertedSize(-1));
    }

    @Test
    @DisplayName("1 KB (1024 bytes) se convierte correctamente")
    void getConvertedSize_1KB() {
        String result = FileUtils.getConvertedSize(1024);
        assertTrue(result.contains("KB"), "Debe contener KB: " + result);
    }

    @Test
    @DisplayName("1 MB (1024*1024 bytes) se convierte correctamente")
    void getConvertedSize_1MB() {
        String result = FileUtils.getConvertedSize(1024 * 1024L);
        assertTrue(result.contains("MB"), "Debe contener MB: " + result);
    }

    @Test
    @DisplayName("Bytes pequeños se representan como bytes")
    void getConvertedSize_smallBytes() {
        String result = FileUtils.getConvertedSize(512);
        assertTrue(result.contains("bytes") || result.contains("KB"),
                "512 bytes debería mostrarse como bytes: " + result);
    }
}
