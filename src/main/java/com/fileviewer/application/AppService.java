package com.fileviewer.application;

import com.fileviewer.domain.interaction.InteractionOptions;
import com.fileviewer.domain.model.Info;
import com.fileviewer.domain.pipeline.AccumulatedTransformation;
import com.fileviewer.domain.pipeline.FileExtensionTransformation;
import com.fileviewer.domain.pipeline.TransformationPipeline;
import com.fileviewer.infrastructure.interaction.OpenFileInteractionPolicy;
import com.fileviewer.infrastructure.scanner.DirectoryTreeBuilder;
import com.fileviewer.tdas.trees.MultiTree;
import com.fileviewer.view.render.RenderConfiguration;

/**
 * Fachada de servicios — punto de entrada único para la capa de presentación.
 *
 * <p>
 * Es el único lugar donde se ensamblan las dependencias concretas:
 * {@link DirectoryTreeBuilder} (scanner) y el pipeline de transformaciones por
 * defecto. {@link AppLogic} solo conoce las interfaces del dominio.</p>
 *
 * <p>
 * Además de delegar operaciones de árbol a {@link AppLogic}, gestiona el modo
 * de interacción ({@link InteractionOptions}).</p>
 */
public class AppService {

    private final AppLogic app;
    private InteractionOptions interactionOptions = InteractionOptions.none();

    public AppService() {
        TransformationPipeline pipeline = new TransformationPipeline();
        pipeline.register(new FileExtensionTransformation());
        pipeline.register(new AccumulatedTransformation());
        this.app = new AppLogic(new DirectoryTreeBuilder(), pipeline);
    }

    public MultiTree<Info> initDirectory(String directory) {
        app.changeDirectory(directory);
        app.createTreeDirectory();
        return app.transformTreeDirectory();
    }

    public MultiTree<Info> initDirectory() {
        app.createTreeDirectory();
        return app.transformTreeDirectory();
    }

    public MultiTree<Info> update() {
        return app.transformTreeDirectory();
    }

    public void reset() {
        app.reset();
        interactionOptions = InteractionOptions.none();
    }

    public boolean toggleTransformation(String id) {
        return app.toggleTransformation(id);
    }

    public boolean enableTransformation(String id) {
        return app.enableTransformation(id);
    }

    public boolean disableTransformation(String id) {
        return app.disableTransformation(id);
    }

    public boolean isTransformationActive(String id) {
        return app.isTransformationActive(id);
    }

    public boolean toggleExecutableMode() {
        if (interactionOptions.isActive()) {
            interactionOptions = InteractionOptions.none();
        } else {
            interactionOptions = InteractionOptions.of(OpenFileInteractionPolicy.INSTANCE);
        }
        return interactionOptions.isActive();
    }

    public boolean isExecutableMode() {
        return interactionOptions.isActive();
    }

    public InteractionOptions getInteractionOptions() {
        return interactionOptions;
    }

    public boolean showFilenames() {
        return app.toggleShowFilenames();
    }

    public int incrementLevel() {
        return app.increaseLimitLevel();
    }

    public int decrementLevel() {
        return app.decreaseLimitLevel();
    }

    public int incrementLevelTitle() {
        return app.increaseLimitTitleLevel();
    }

    public int decrementLevelTitle() {
        return app.decreaseLimitTitleLevel();
    }

    public RenderConfiguration getRenderConfiguration() {
        return app.getRenderConfiguration();
    }

    public String getDirectory() {
        return app.getDirectory();
    }

    public void shutdown() {
        app.shutdown();
    }
}
