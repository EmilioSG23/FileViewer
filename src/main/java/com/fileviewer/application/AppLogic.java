package com.fileviewer.application;

import com.fileviewer.domain.model.Info;
import com.fileviewer.domain.pipeline.TransformationPipeline;
import com.fileviewer.domain.scanner.DirectoryScanner;
import com.fileviewer.tdas.trees.MultiTree;

/**
 * Núcleo lógico de la aplicación. Coordina tres responsabilidades: 1. Escaneo —
 * {@link DirectoryScanner} (inyectable) 2. Transformación —
 * {@link TransformationPipeline} activo 3. Configuración de render —
 * {@link RenderConfiguration}
 *
 * <p>
 * La composición concreta (qué scanner y pipeline usar) la realiza
 * {@link AppService} en su constructor, manteniendo esta clase libre de
 * dependencias de infraestructura.</p>
 */
public class AppLogic {

    private String directory;
    private final DirectoryScanner scanner;
    private final TransformationPipeline pipeline;
    private final RenderConfiguration renderConfig;
    private MultiTree<Info> directoryTree;

    public AppLogic(DirectoryScanner scanner, TransformationPipeline pipeline) {
        this.directory = Consts.DEFAULT_DIRECTORY;
        this.scanner = scanner;
        this.pipeline = pipeline;
        this.renderConfig = new RenderConfiguration();
    }

    public void changeDirectory(String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }

    public MultiTree<Info> createTreeDirectory() {
        this.directoryTree = scanner.scan(directory);
        return this.directoryTree;
    }

    public MultiTree<Info> transformTreeDirectory() {
        return pipeline.apply(directoryTree);
    }

    public boolean enableTransformation(String id) {
        return pipeline.enable(id);
    }

    public boolean disableTransformation(String id) {
        return pipeline.disable(id);
    }

    public boolean toggleTransformation(String id) {
        return pipeline.toggle(id);
    }

    public boolean isTransformationActive(String id) {
        return pipeline.isActive(id);
    }

    public boolean toggleShowFilenames() {
        return renderConfig.toggleShowFilenames();
    }

    public int increaseLimitLevel() {
        return renderConfig.increaseLimitLevel();
    }

    public int decreaseLimitLevel() {
        return renderConfig.decreaseLimitLevel();
    }

    public int increaseLimitTitleLevel() {
        return renderConfig.increaseTitleLimitLevel();
    }

    public int decreaseLimitTitleLevel() {
        return renderConfig.decreaseTitleLimitLevel();
    }

    public RenderConfiguration getRenderConfiguration() {
        return renderConfig;
    }

    public void reset() {
        this.directory = Consts.DEFAULT_DIRECTORY;
        this.pipeline.reset();
        this.renderConfig.reset();
        this.directoryTree = null;
    }

    /**
     * Libera recursos del scanner si implementa {@link AutoCloseable}. No lanza
     * excepción comprobada.
     */
    public void shutdown() {
        if (scanner instanceof AutoCloseable c) {
            try {
                c.close();
            } catch (Exception e) {
                System.err.println("Error shutting down scanner: " + e.getMessage());
            }
        }
    }
}
