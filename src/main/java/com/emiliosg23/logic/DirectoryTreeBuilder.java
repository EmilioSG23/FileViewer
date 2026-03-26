package com.emiliosg23.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import com.emiliosg23.models.infos.DirectoryInfo;
import com.emiliosg23.models.infos.FileInfo;
import com.emiliosg23.models.infos.Info;
import com.emiliosg23.tdas.trees.MultiTree;

/**
 * Construye un {@link MultiTree} de {@link Info} a partir de un directorio del
 * sistema de archivos, utilizando un {@link ForkJoinPool} para explorar
 * subdirectorios en paralelo y aprovechar todos los núcleos disponibles del
 * procesador.
 *
 * <p>
 * Cada subdirectorio se procesa como una {@link RecursiveTask} independiente.
 * Los archivos dentro de un mismo directorio se procesan secuencialmente, ya
 * que su costo individual (obtener {@code File.length()}) es mínimo y no
 * justifica la sobrecarga de una tarea por archivo.</p>
 *
 * <p>
 * El árbol resultante es seguro para lectura desde cualquier hilo una vez
 * construido, ya que {@link MultiTree} no se comparte entre tareas durante la
 * construcción: cada tarea produce su propio subárbol y el ensamblaje final
 * ocurre después del {@code join()}.</p>
 *
 * <h3>Uso típico:</h3>
 * <pre>
 *   DirectoryTreeBuilder builder = new DirectoryTreeBuilder();
 *   MultiTree&lt;Info&gt; tree = builder.build("C:/Users/proyecto");
 *   builder.shutdown(); // cuando ya no se necesite
 * </pre>
 *
 * @see TreeInfoGenerator
 */
public class DirectoryTreeBuilder {

    private final ForkJoinPool pool;

    /**
     * Crea un builder con un {@link ForkJoinPool} cuyo paralelismo es igual al
     * número de procesadores disponibles en la máquina.
     */
    public DirectoryTreeBuilder() {
        this.pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Crea un builder con un {@link ForkJoinPool} de paralelismo personalizado.
     *
     * @param parallelism cantidad máxima de hilos de trabajo concurrentes
     * @throws IllegalArgumentException si {@code parallelism} es menor a 1
     */
    public DirectoryTreeBuilder(int parallelism) {
        if (parallelism < 1) {
            throw new IllegalArgumentException("El paralelismo debe ser al menos 1");
        }
        this.pool = new ForkJoinPool(parallelism);
    }

    /**
     * Construye el árbol de información del directorio indicado. La raíz del
     * árbol contiene un {@link DirectoryInfo} (o {@link FileInfo} si la ruta es
     * un archivo) con el tamaño acumulado de todo su contenido.
     *
     * @param directoryPath ruta absoluta del directorio a escanear
     * @return árbol completo con tamaños acumulados en cada nodo directorio
     */
    public MultiTree<Info> build(String directoryPath) {
        return pool.invoke(new DirectoryScanTask(new File(directoryPath), false));
    }

    /**
     * Libera los recursos del pool de hilos. Debe llamarse cuando el builder ya
     * no se necesite.
     */
    public void shutdown() {
        pool.shutdown();
    }

    /**
     * Tarea recursiva que escanea un directorio o archivo y retorna el subárbol
     * correspondiente.
     *
     * <p>
     * Para directorios, los subdirectorios se procesan en paralelo mediante
     * {@code fork()}/{@code join()}, mientras que los archivos se procesan
     * secuencialmente en el hilo actual.</p>
     */
    private static class DirectoryScanTask extends RecursiveTask<MultiTree<Info>> {

        private final File file;
        private final boolean vertical;

        /**
         * Crea una tarea de escaneo para la ruta indicada.
         *
         * @param file archivo o directorio a escanear
         * @param vertical orientación para alternancia de layout en la vista
         */
        DirectoryScanTask(File file, boolean vertical) {
            this.file = file;
            this.vertical = vertical;
        }

        @Override
        protected MultiTree<Info> compute() {
            if (file.isFile()) {
                return buildFileNode();
            }
            return buildDirectoryNode();
        }

        /**
         * Construye un nodo hoja a partir de un archivo.
         */
        private MultiTree<Info> buildFileNode() {
            FileInfo info = new FileInfo(file);
            info.setSize(file.length());
            return new MultiTree<>(info);
        }

        /**
         * Construye un nodo directorio, lanzando tareas paralelas para cada
         * subdirectorio hijo y procesando archivos secuencialmente en el hilo
         * actual.
         */
        private MultiTree<Info> buildDirectoryNode() {
            DirectoryInfo info = new DirectoryInfo(file);
            MultiTree<Info> tree = new MultiTree<>(info);

            String[] childNames = file.list();
            if (childNames == null) {
                return tree;
            }

            List<DirectoryScanTask> subtasks = new ArrayList<>();
            long accumulatedSize = 0;

            for (String childName : childNames) {
                File child = new File(file, childName);

                if (child.isDirectory()) {
                    // Fork: escanear subdirectorio en paralelo
                    DirectoryScanTask subtask = new DirectoryScanTask(child, !vertical);
                    subtask.fork();
                    subtasks.add(subtask);
                } else if (child.isFile()) {
                    // Secuencial: archivos dentro del mismo directorio
                    FileInfo fileInfo = new FileInfo(child);
                    fileInfo.setSize(child.length());
                    tree.addChild(fileInfo);
                    accumulatedSize += fileInfo.getSize();
                }
            }

            // Join: recoger resultados de subdirectorios
            for (DirectoryScanTask subtask : subtasks) {
                MultiTree<Info> childTree = subtask.join();
                if (childTree != null) {
                    tree.addChild(childTree);
                    accumulatedSize += childTree.getRoot().getContent().getSize();
                }
            }

            info.setSize(accumulatedSize);
            return tree;
        }
    }
}
