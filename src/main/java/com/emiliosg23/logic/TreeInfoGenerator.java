package com.emiliosg23.logic;

import java.util.Map;

import com.emiliosg23.models.enums.Modes;
import com.emiliosg23.models.infos.DirectoryInfo;
import com.emiliosg23.models.infos.FileInfo;
import com.emiliosg23.models.infos.Info;
import com.emiliosg23.tdas.trees.MultiTree;

/**
 * Coordina la creación, transformación y copia de árboles de información de
 * archivos.
 *
 * <p>
 * Delega la construcción del árbol a {@link DirectoryTreeBuilder}, que usa un
 * {@link java.util.concurrent.ForkJoinPool} para paralelizar el escaneo de
 * subdirectorios.</p>
 *
 * <p>
 * Las transformaciones disponibles (por extensión, acumulativa) se resuelven
 * mediante el patrón Strategy a través de {@link TreeTransformerStrategy}.</p>
 *
 * @see DirectoryTreeBuilder
 * @see TreeTransformerStrategy
 */
public class TreeInfoGenerator {

    private final DirectoryTreeBuilder treeBuilder;
    private final Map<Modes, TreeTransformerStrategy> transformers;

    /**
     * Crea un generador con un {@link DirectoryTreeBuilder} por defecto
     * (paralelismo = número de procesadores disponibles).
     */
    public TreeInfoGenerator() {
        this.treeBuilder = new DirectoryTreeBuilder();
        this.transformers = Map.of(
                Modes.ACUMULATIVE, new AcumulativeTransformerStrategy(),
                Modes.FILE_EXTENSION, new FileExtensionTransformerStrategy()
        );
    }

    /**
     * Construye un árbol de información a partir de la ruta de un directorio,
     * utilizando múltiples hilos para recorrer subdirectorios en paralelo.
     *
     * @param directory ruta absoluta del directorio a escanear
     * @return árbol con tamaños acumulados; nunca {@code null}
     */
    public MultiTree<Info> createTree(String directory) {
        return treeBuilder.build(directory);
    }

    /**
     * Aplica una transformación al árbol según el modo indicado.
     *
     * @param tree árbol original (no se modifica)
     * @param mode modo de transformación ({@link Modes#FILE_EXTENSION} o
     * {@link Modes#ACUMULATIVE})
     * @return nuevo árbol transformado
     * @throws IllegalArgumentException si el modo no es soportado
     */
    public MultiTree<Info> transformTree(MultiTree<Info> tree, Modes mode) {
        TreeTransformerStrategy strategy = transformers.get(mode);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported mode: " + mode);
        }
        return strategy.transform(tree);
    }

    /**
     * Crea una copia profunda (deep copy) del árbol, duplicando cada nodo
     * {@link Info}.
     *
     * @param tree árbol a copiar
     * @return nuevo árbol idéntico pero con instancias independientes
     */
    public MultiTree<Info> copyTree(MultiTree<Info> tree) {
        Info originalContent = tree.getRoot().getContent();
        Info copiedContent = copyInfo(originalContent);

        MultiTree<Info> copiedTree = new MultiTree<>(copiedContent);
        for (MultiTree<Info> child : tree.getRoot().getChildren()) {
            copiedTree.addChild(copyTree(child));
        }
        return copiedTree;
    }

    /**
     * Libera los recursos del pool de hilos interno. Llamar cuando el generador
     * ya no se necesite (por ejemplo, al cerrar la aplicación).
     */
    public void shutdown() {
        treeBuilder.shutdown();
    }

    /**
     * Copia una instancia de {@link Info} creando un nuevo objeto del mismo
     * subtipo.
     *
     * @param info nodo a copiar
     * @return nueva instancia con los mismos datos
     * @throws IllegalArgumentException si {@code info} es {@code null} o de un
     * tipo no soportado
     */
    private Info copyInfo(Info info) {
        if (info == null) {
            throw new IllegalArgumentException("Info parameter is null");
        }
        if (info instanceof DirectoryInfo) {
            DirectoryInfo dir = (DirectoryInfo) info;
            return new DirectoryInfo(dir.getName(), dir.getSize());
        } else if (info instanceof FileInfo) {
            FileInfo file = (FileInfo) info;
            return new FileInfo(file.getName(), file.getSize(), file.getFullPath(), file.getExtension());
        } else {
            throw new IllegalArgumentException("Unsupported Info type: " + info.getClass());
        }
    }
}
