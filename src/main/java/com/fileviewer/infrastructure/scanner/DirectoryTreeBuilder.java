package com.fileviewer.infrastructure.scanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import com.fileviewer.domain.model.DirectoryInfo;
import com.fileviewer.domain.model.FileInfo;
import com.fileviewer.domain.model.Info;
import com.fileviewer.domain.scanner.DirectoryScanner;
import com.fileviewer.domain.scanner.NodeMetricStrategy;
import com.fileviewer.tdas.trees.MultiTree;

/**
 * Escanea un directorio en paralelo (ForkJoinPool), construyendo un MultiTree
 * con tamaños acumulados.
 *
 * <p>
 * Implementa {@link DirectoryScanner} para desacoplar de la capa de aplicación
 * e {@link AutoCloseable} para liberar el pool al terminar.</p>
 */
public class DirectoryTreeBuilder implements DirectoryScanner, AutoCloseable {

    private final ForkJoinPool pool;
    private final NodeMetricStrategy metric;

    public DirectoryTreeBuilder() {
        this(NodeMetricStrategy.fileSize());
    }

    public DirectoryTreeBuilder(NodeMetricStrategy metric) {
        this.pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        this.metric = metric;
    }

    public DirectoryTreeBuilder(NodeMetricStrategy metric, int parallelism) {
        if (parallelism < 1) {
            throw new IllegalArgumentException("El paralelismo debe ser al menos 1");
        }
        this.pool = new ForkJoinPool(parallelism);
        this.metric = metric;
    }

    @Override
    public MultiTree<Info> scan(String directoryPath) {
        return pool.invoke(new DirectoryScanTask(new File(directoryPath), false, metric));
    }

    /**
     * Libera el ForkJoinPool. Llamar al cerrar la aplicación.
     */
    @Override
    public void close() {
        pool.shutdown();
    }

    // -------------------------------------------------------------------------
    // Tarea recursiva interna
    // -------------------------------------------------------------------------
    private static class DirectoryScanTask extends RecursiveTask<MultiTree<Info>> {

        private final File file;
        private final boolean vertical;
        private final NodeMetricStrategy metric;

        DirectoryScanTask(File file, boolean vertical, NodeMetricStrategy metric) {
            this.file = file;
            this.vertical = vertical;
            this.metric = metric;
        }

        @Override
        protected MultiTree<Info> compute() {
            return file.isFile() ? buildFileNode() : buildDirectoryNode();
        }

        private MultiTree<Info> buildFileNode() {
            FileInfo info = new FileInfo(file);
            info.setSize(metric.computeMetric(file));
            return new MultiTree<>(info);
        }

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
                    DirectoryScanTask subtask = new DirectoryScanTask(child, !vertical, metric);
                    subtask.fork();
                    subtasks.add(subtask);
                } else if (child.isFile()) {
                    FileInfo fileInfo = new FileInfo(child);
                    fileInfo.setSize(metric.computeMetric(child));
                    tree.addChild(fileInfo);
                    accumulatedSize += fileInfo.getSize();
                }
            }

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
