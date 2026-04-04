package com.fileviewer.domain.pipeline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fileviewer.domain.model.DirectoryInfo;
import com.fileviewer.domain.model.ExtensionInfo;
import com.fileviewer.domain.model.FileInfo;
import com.fileviewer.domain.model.Info;
import com.fileviewer.tdas.trees.MultiTree;

/**
 * Tests de {@link AccumulatedTransformation}: consolidación de extensiones de
 * todos los niveles en un único nivel bajo la raíz.
 */
class AccumulatedTransformationTest {

    private AccumulatedTransformation transformation;

    @BeforeEach
    void setUp() {
        transformation = new AccumulatedTransformation();
    }

    @Test
    @DisplayName("ID es ACCUMULATED")
    void id_isAccumulated() {
        assertEquals(AccumulatedTransformation.ID, transformation.getId());
    }

    @Test
    @DisplayName("Prerequisito es FILE_EXTENSION")
    void requiredPredecessors_containsFileExtension() {
        assertTrue(transformation.requiredPredecessors()
                .contains(FileExtensionTransformation.ID));
    }

    @Test
    @DisplayName("apply con null devuelve null")
    void apply_null_returnsNull() {
        assertNull(transformation.apply(null));
    }

    @Test
    @DisplayName("Extensiones del mismo tipo en distintos directorios se consolidan")
    void apply_mergesSameExtensionAcrossDirectories() {
        // Árbol ya transformado por FileExtension:
        // root (dir)
        //   subdir1 (dir)
        //     .java (200)
        //   subdir2 (dir)
        //     .java (150)
        MultiTree<Info> tree = buildAlreadyGroupedTree();

        MultiTree<Info> result = transformation.apply(tree);

        ExtensionInfo javaNode = findExtensionNode(result, ".java");
        assertNotNull(javaNode, "Debe existir un nodo .java consolidado");
        assertEquals(350, javaNode.getSize(),
                "El tamaño consolidado de .java debe ser 200 + 150 = 350");
    }

    @Test
    @DisplayName("Extensiones distintas no se mezclan")
    void apply_keepsDifferentExtensionsSeparate() {
        MultiTree<Info> tree = buildAlreadyGroupedTree(); // .java (350) + .txt (50)
        MultiTree<Info> subdirWithTxt = new MultiTree<>(new DirectoryInfo("sub3", 50));
        subdirWithTxt.addChild(new ExtensionInfo(".txt", 50));
        tree.addChild(subdirWithTxt);

        MultiTree<Info> result = transformation.apply(tree);

        assertNotNull(findExtensionNode(result, ".java"));
        assertNotNull(findExtensionNode(result, ".txt"));
    }

    @Test
    @DisplayName("El resultado es plano: solo un nivel de ExtensionInfo bajo la raíz")
    void apply_resultIsFlat() {
        MultiTree<Info> tree = buildAlreadyGroupedTree();
        MultiTree<Info> result = transformation.apply(tree);

        // Todos los hijos directos deben ser ExtensionInfo
        for (MultiTree<Info> child : result.getRoot().getChildren()) {
            assertInstanceOf(ExtensionInfo.class, child.getRoot().getContent());
            assertTrue(child.getRoot().getChildren().isEmpty(),
                    "Los nodos ExtensionInfo no deben tener hijos en el resultado plano");
        }
    }

    @Test
    @DisplayName("Pipeline FILE_EXTENSION + ACCUMULATED sobre árbol raw produce resultado correcto")
    void pipeline_fileExtensionThenAccumulated_endToEnd() {
        TransformationPipeline pipeline = new TransformationPipeline();
        pipeline.register(new FileExtensionTransformation());
        pipeline.register(new AccumulatedTransformation());
        pipeline.enable(AccumulatedTransformation.ID); // activa ambos

        // Árbol raw: root → src/ (Main.java:100, App.java:120), test/ (MyTest.java:60)
        MultiTree<Info> raw = new MultiTree<>(new DirectoryInfo("root", 280));
        MultiTree<Info> src = new MultiTree<>(new DirectoryInfo("src", 220));
        MultiTree<Info> test = new MultiTree<>(new DirectoryInfo("test", 60));
        src.addChild(new FileInfo("Main.java", 100, "/src/Main.java", ".java"));
        src.addChild(new FileInfo("App.java", 120, "/src/App.java", ".java"));
        test.addChild(new FileInfo("MyTest.java", 60, "/test/MyTest.java", ".java"));
        raw.addChild(src);
        raw.addChild(test);

        MultiTree<Info> result = pipeline.apply(raw);

        ExtensionInfo javaNode = findExtensionNode(result, ".java");
        assertNotNull(javaNode);
        assertEquals(280, javaNode.getSize(),
                "Todos los .java (100+120+60) deben consolidarse en un solo nodo");
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    /**
     * Árbol ya transformado por FileExtension (como lo devolvería
     * FileExtensionTransformation): root → subdir1(.java:200),
     * subdir2(.java:150)
     */
    private MultiTree<Info> buildAlreadyGroupedTree() {
        MultiTree<Info> root = new MultiTree<>(new DirectoryInfo("root", 350));
        MultiTree<Info> sub1 = new MultiTree<>(new DirectoryInfo("sub1", 200));
        sub1.addChild(new ExtensionInfo(".java", 200));
        MultiTree<Info> sub2 = new MultiTree<>(new DirectoryInfo("sub2", 150));
        sub2.addChild(new ExtensionInfo(".java", 150));
        root.addChild(sub1);
        root.addChild(sub2);
        return root;
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
