package com.emiliosg23.models.enums;

/**
 * Modos de visualización y transformación disponibles en la aplicación.
 *
 * <ul>
 * <li>{@link #EXECUTABLE} — Habilita la apertura de archivos al hacer clic en
 * sus nodos.</li>
 * <li>{@link #FILE_EXTENSION} — Agrupa archivos por extensión dentro de cada
 * directorio.</li>
 * <li>{@link #ACUMULATIVE} — Combina todas las extensiones en un único nivel
 * (requiere {@link #FILE_EXTENSION} activo).</li>
 * </ul>
 */
public enum Modes {
    /**
     * Modo ejecutable: permite abrir archivos desde la visualización.
     */
    EXECUTABLE,
    /**
     * Modo extensión: agrupa archivos por su extensión en cada directorio.
     */
    FILE_EXTENSION,
    /**
     * Modo acumulativo: consolida todas las extensiones en un solo nivel.
     */
    ACUMULATIVE
}
