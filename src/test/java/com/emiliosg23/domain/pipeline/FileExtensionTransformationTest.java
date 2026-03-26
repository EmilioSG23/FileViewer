package com.emiliosg23.domain.pipeline;

import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.emiliosg23.domain.model.DirectoryInfo;
import com.emiliosg23.domain.model.ExtensionInfo;
import com.emiliosg23.domain.model.FileInfo;
import com.emiliosg23.domain.model.Info;
import com.emiliosg23.tdas.trees.MultiTree;

/**
 * Tests de {@link FileExtensionTransformation}: agrupación de archivos por
 * extensión dentro de cada directorio.
 */
class FileExtensionTransformationTest {

    private FileExtensionTransformation transformation;

    @BeforeEach
    void setUp() {
        transformation = new FileExtensionTransformation();
    }

    @Test
    @DisplayName("ID es FILE_EXTENSION")
    void id_isFileExtension() {
        assertEquals(FileExtensionTransformation.ID, transformation.getId());
    }

    @Test
    @DisplayName("apply con null devuelve null")
    void apply_null_returnsNull() {
        assertNull(transformation.apply(null));
    }

    @Test
    @DisplayName("Archivos con la misma extensión se agrupan en un nodo ExtensionInfo")
    void apply_groupsFilesByExtension() {
        MultiTree<Info> tree = buildTreeWithTwoJavaFiles();

        MultiTree<Info> result = transformation.apply(tree);

        long javaCount = StreamSupport.stream(result.getRoot().getChildren().spliterator(), false)
                .filter(c -> c.getRoot().getContent() instanceof ExtensionInfo ext
                && ext.getName().equals(".java"))
                .count();
        assertEquals(1, javaCount, "Debería haber exactamente un nodo .java");
    }

    @Test
    @DisplayName("El tamaño del nodo ExtensionInfo es la suma de los archivos agrupados")
    void apply_accumulatesSizePerExtension() {
        MultiTree<Info> tree = buildTreeWithTwoJavaFiles();  // 100 + 120 = 220

        MultiTree<Info> result = transformation.apply(tree);

        ExtensionInfo javaNode = findExtensionNode(result, ".java");
        assertNotNull(javaNode);
        assertEquals(220, javaNode.getSize());
    }

    @Test
    @DisplayName("Extensiones distintas generan nodos distintos")
    void apply_differentExtensionsSeparateNodes() {
        MultiTree<Info> tree = TransformationPipelineTest.buildSimpleTree(); // .java x2, .txt x1

        MultiTree<Info> result = transformation.apply(tree);

        assertEquals(2, StreamSupport.stream(result.getRoot().getChildren().spliterator(), false)
                .filter(c -> c.getRoot().getContent() instanceof ExtensionInfo).count(),
                "Deben existir 2 nodos ExtensionInfo (.java y .txt)");
    }

    @Test
    @DisplayName("Archivos con tamaño cero son ignorados")
    void apply_zeroSizeFilesAreIgnored() {
        DirectoryInfo root = new DirectoryInfo("root", 0);
        MultiTree<Info> tree = new MultiTree<>(root);
        tree.addChild(new FileInfo("empty.java", 0, "/empty.java", ".java"));

        MultiTree<Info> result = transformation.apply(tree);

        assertEquals(0, StreamSupport.stream(result.getRoot().getChildren().spliterator(), false).count(),
                "Los archivos de tamaño 0 no deben generar nodos de extensión");
    }

    @Test
    @DisplayName("Los subdirectorios se transforman recursivamente")
    void apply_recursivelyTransformsSubdirectories() {
        MultiTree<Info> tree = new MultiTree<>(new DirectoryInfo("root", 200));
        MultiTree<Info> subdir = new MultiTree<>(new DirectoryInfo("lib", 200));
        subdir.addChild(new FileInfo("Util.java", 100, "/lib/Util.java", ".java"));
        subdir.addChild(new FileInfo("Base.java", 100, "/lib/Base.java", ".java"));
        tree.addChild(subdir);

        MultiTree<Info> result = transformation.apply(tree);

        MultiTree<Info> resultSubdir = result.getRoot().getChildren().iterator().next();
        ExtensionInfo javaInSubdir = findExtensionNode(resultSubdir, ".java");
        assertNotNull(javaInSubdir, "Debe haber un nodo .java en el subdirectorio transformado");
        assertEquals(200, javaInSubdir.getSize());
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private MultiTree<Info> buildTreeWithTwoJavaFiles() {
        DirectoryInfo root = new DirectoryInfo("root", 220);
        MultiTree<Info> tree = new MultiTree<>(root);
        tree.addChild(new FileInfo("Main.java", 100, "/Main.java", ".java"));
        tree.addChild(new FileInfo("App.java", 120, "/App.java", ".java"));
        return tree;
    }

    private ExtensionInfo findExtensionNode(MultiTree<Info> tree, String ext) {
        for (MultiTree<Info> child : tree.getRoot().getChildren()) {
            Info content = child.getRoot().getContent();
            if (content instanceof ExtensionInfo extInfo && extInfo.getName().equals(ext)) {
                return extInfo;
            }
        }
        return null;
    }
}
