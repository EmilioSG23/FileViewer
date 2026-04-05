package com.fileviewer.domain.pipeline;

import com.fileviewer.domain.model.DirectoryInfo;
import com.fileviewer.domain.model.ExtensionInfo;
import com.fileviewer.domain.model.FileInfo;
import com.fileviewer.domain.model.Info;
import com.fileviewer.tdas.trees.MultiTree;

/**
 * Transformación que agrupa los archivos de cada directorio por extensión.
 *
 * <p>
 * Reemplaza los nodos {@link FileInfo} por nodos {@link ExtensionInfo}, sumando
 * el tamaño de todos los archivos con la misma extensión dentro de cada
 * directorio. Los subdirectorios se transforman recursivamente.</p>
 *
 * @see AccumulatedTransformation
 * @see TransformationPipeline
 */
public class FileExtensionTransformation implements TreeTransformation {

    /**
     * ID estable de esta transformación.
     */
    public static final String ID = "FILE_EXTENSION";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public MultiTree<Info> apply(MultiTree<Info> tree) {
        if (tree == null) {
            return null;
        }
        Info newRoot = transformRootInfo(tree.getRoot().getContent());
        MultiTree<Info> result = new MultiTree<>(newRoot);

        for (MultiTree<Info> child : tree.getRoot().getChildren()) {
            Info childInfo = child.getRoot().getContent();
            if (childInfo instanceof DirectoryInfo) {
                MultiTree<Info> transformedChild = apply(child);
                if (transformedChild != null) {
                    result.addChild(transformedChild);
                }
            } else if (childInfo instanceof FileInfo fileInfo && fileInfo.getSize() > 0) {
                mergeIntoExtensionNode(result, fileInfo);
            }
        }
        return result;
    }

    private Info transformRootInfo(Info root) {
        if (root instanceof DirectoryInfo) {
            return new DirectoryInfo(root.getName(), root.getSize());
        }
        if (root instanceof FileInfo fileInfo) {
            return new ExtensionInfo(fileInfo.getExtension(), root.getSize());
        }
        return root;
    }

    private void mergeIntoExtensionNode(MultiTree<Info> tree, FileInfo fileInfo) {
        String ext = fileInfo.getExtension();
        ExtensionInfo existing = findExtensionNode(tree, ext);
        if (existing != null) {
            existing.setSize(existing.getSize() + fileInfo.getSize());
        } else {
            tree.addChild(new ExtensionInfo(ext, fileInfo.getSize()));
        }
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
