package com.emiliosg23.logic;

import com.emiliosg23.models.infos.DirectoryInfo;
import com.emiliosg23.models.infos.ExtensionInfo;
import com.emiliosg23.models.infos.FileInfo;
import com.emiliosg23.models.infos.Info;
import com.emiliosg23.tdas.trees.MultiTree;

/**
 * Estrategia de transformación por extensión de archivo.
 *
 * <p>
 * Reemplaza los nodos de tipo {@link FileInfo} por nodos {@link ExtensionInfo},
 * agrupando archivos con la misma extensión dentro de cada directorio y sumando
 * sus tamaños. Los subdirectorios se transforman recursivamente.</p>
 *
 * @see AcumulativeTransformerStrategy
 * @see TreeTransformerStrategy
 */
public class FileExtensionTransformerStrategy implements TreeTransformerStrategy {

    /**
     * Transforma el árbol agrupando archivos por extensión dentro de cada
     * directorio.
     *
     * @param tree árbol original con nodos {@link FileInfo}
     * @return nuevo árbol con nodos {@link ExtensionInfo} en lugar de archivos
     * individuales
     */
    @Override
    public MultiTree<Info> transform(MultiTree<Info> tree) {
        if (tree == null) {
            return null;
        }

        Info rootInfo = tree.getRoot().getContent();
        Info newRootInfo = transformRoot(rootInfo);
        MultiTree<Info> transformedTree = new MultiTree<>(newRootInfo);

        for (MultiTree<Info> child : tree.getRoot().getChildren()) {
            Info childInfo = child.getRoot().getContent();

            if (childInfo instanceof DirectoryInfo) {
                MultiTree<Info> transformedChild = transform(child);
                if (transformedChild != null) {
                    transformedTree.addChild(transformedChild);
                }
            } else if (childInfo instanceof FileInfo && childInfo.getSize() > 0) {
                addOrUpdateExtensionNode(transformedTree, (FileInfo) childInfo);
            }
        }

        return transformedTree;
    }

    /**
     * Transforma el nodo raíz al tipo apropiado.
     */
    private Info transformRoot(Info root) {
        if (root instanceof DirectoryInfo) {
            return new DirectoryInfo(root.getName(), root.getSize());
        }
        if (root instanceof FileInfo) {
            return new ExtensionInfo(((FileInfo) root).getExtension(), root.getSize());
        }
        return root;
    }

    /**
     * Añade o actualiza un nodo de extensión en el árbol, acumulando el tamaño.
     */
    private void addOrUpdateExtensionNode(MultiTree<Info> tree, FileInfo fileInfo) {
        String ext = fileInfo.getExtension();
        ExtensionInfo existing = findExtensionInfo(tree, ext);

        if (existing != null) {
            existing.setSize(existing.getSize() + fileInfo.getSize());
        } else {
            ExtensionInfo newExt = new ExtensionInfo(ext, fileInfo.getSize());
            tree.addChild(newExt);
        }
    }

    /**
     * Busca un nodo hijo de tipo {@link ExtensionInfo} con la extensión
     * indicada.
     *
     * @return el nodo encontrado, o {@code null} si no existe
     */
    private ExtensionInfo findExtensionInfo(MultiTree<Info> tree, String ext) {
        for (MultiTree<Info> child : tree.getRoot().getChildren()) {
            Info content = child.getRoot().getContent();
            if (content instanceof ExtensionInfo && content.getName().equals(ext)) {
                return (ExtensionInfo) content;
            }
        }
        return null;
    }
}
