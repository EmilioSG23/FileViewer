package com.fileviewer.domain.pipeline;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fileviewer.domain.model.DirectoryInfo;
import com.fileviewer.domain.model.FileInfo;
import com.fileviewer.domain.model.Info;
import com.fileviewer.tdas.trees.MultiTree;

/**
 * Tests del {@link TransformationPipeline}: ciclo de vida de registros,
 * activación/desactivación con cascadas, orden topológico y deep copy.
 */
class TransformationPipelineTest {

    private TransformationPipeline pipeline;
    private FileExtensionTransformation extTransform;
    private AccumulatedTransformation accTransform;

    @BeforeEach
    void setUp() {
        pipeline = new TransformationPipeline();
        extTransform = new FileExtensionTransformation();
        accTransform = new AccumulatedTransformation();
        pipeline.register(extTransform);
        pipeline.register(accTransform);
    }

    // -------------------------------------------------------------------------
    // Registro y estado básico
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("Sin transformaciones activas, la pipeline devuelve una copia del árbol original")
    void apply_noActiveTransformations_returnsDeepCopy() {
        MultiTree<Info> source = buildSimpleTree();
        MultiTree<Info> result = pipeline.apply(source);

        assertNotSame(source, result);
        assertEquals(source.getRoot().getContent().getName(),
                result.getRoot().getContent().getName());
        assertEquals(source.getRoot().getContent().getSize(),
                result.getRoot().getContent().getSize());
    }

    @Test
    @DisplayName("apply con null devuelve null")
    void apply_null_returnsNull() {
        assertNull(pipeline.apply(null));
    }

    @Test
    @DisplayName("isActive es false para transformación no activada")
    void isActive_notEnabled_returnsFalse() {
        assertFalse(pipeline.isActive(FileExtensionTransformation.ID));
        assertFalse(pipeline.isActive(AccumulatedTransformation.ID));
    }

    @Test
    @DisplayName("isActive es true tras enable")
    void enable_setsActiveTrue() {
        pipeline.enable(FileExtensionTransformation.ID);
        assertTrue(pipeline.isActive(FileExtensionTransformation.ID));
    }

    @Test
    @DisplayName("enable con ID desconocido lanza excepción")
    void enable_unknownId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> pipeline.enable("UNKNOWN_MODE"));
    }

    // -------------------------------------------------------------------------
    // Prerequisitos y cascada
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("Activar ACCUMULATED también activa su prerequisito FILE_EXTENSION")
    void enable_accumulated_autoenablesPrerequisite() {
        pipeline.enable(AccumulatedTransformation.ID);
        assertTrue(pipeline.isActive(FileExtensionTransformation.ID));
        assertTrue(pipeline.isActive(AccumulatedTransformation.ID));
    }

    @Test
    @DisplayName("Desactivar FILE_EXTENSION también desactiva ACCUMULATED en cascada")
    void disable_fileExtension_cascadesDisableToAccumulated() {
        pipeline.enable(AccumulatedTransformation.ID);   // activa ambos
        pipeline.disable(FileExtensionTransformation.ID);
        assertFalse(pipeline.isActive(FileExtensionTransformation.ID));
        assertFalse(pipeline.isActive(AccumulatedTransformation.ID));
    }

    @Test
    @DisplayName("Desactivar transformación no activa devuelve false sin error")
    void disable_notActive_returnsFalse() {
        assertFalse(pipeline.disable(FileExtensionTransformation.ID));
    }

    // -------------------------------------------------------------------------
    // toggle
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("toggle activa e inactiva alternativamente")
    void toggle_alternatesState() {
        assertTrue(pipeline.toggle(FileExtensionTransformation.ID));   // activa
        assertFalse(pipeline.toggle(FileExtensionTransformation.ID));  // desactiva
        assertTrue(pipeline.toggle(FileExtensionTransformation.ID));   // activa de nuevo
    }

    // -------------------------------------------------------------------------
    // reset
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("reset desactiva todas las transformaciones")
    void reset_clearsAllActive() {
        pipeline.enable(FileExtensionTransformation.ID);
        pipeline.reset();
        assertFalse(pipeline.isActive(FileExtensionTransformation.ID));
        assertFalse(pipeline.isActive(AccumulatedTransformation.ID));
    }

    // -------------------------------------------------------------------------
    // Deep copy (la fuente no debe modificarse por transformaciones)
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("apply no modifica el árbol fuente")
    void apply_doesNotMutateSource() {
        MultiTree<Info> source = buildSimpleTree();
        long originalSize = source.getRoot().getContent().getSize();

        pipeline.enable(FileExtensionTransformation.ID);
        pipeline.apply(source);

        assertEquals(originalSize, source.getRoot().getContent().getSize(),
                "El árbol original no debe verse afectado por la transformación");
    }

    @Test
    @DisplayName("Requiredpredecessors de AccumulatedTransformation contiene FILE_EXTENSION")
    void accumulatedHasCorrectPrerequisites() {
        Set<String> prereqs = accTransform.requiredPredecessors();
        assertTrue(prereqs.contains(FileExtensionTransformation.ID));
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    /**
     * Árbol simple: directorio raíz con 2 archivos java y 1 archivo txt.
     */
    static MultiTree<Info> buildSimpleTree() {
        DirectoryInfo root = new DirectoryInfo("root", 300);
        MultiTree<Info> tree = new MultiTree<>(root);

        FileInfo file1 = new FileInfo("Main.java", 100, "/root/Main.java", ".java");
        FileInfo file2 = new FileInfo("App.java", 120, "/root/App.java", ".java");
        FileInfo file3 = new FileInfo("README.txt", 80, "/root/README.txt", ".txt");

        tree.addChild(file1);
        tree.addChild(file2);
        tree.addChild(file3);
        return tree;
    }
}
