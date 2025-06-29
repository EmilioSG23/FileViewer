package com.emiliosg23.logic;

import java.io.File;
import java.util.Map;
import com.emiliosg23.models.enums.Modes;
import com.emiliosg23.models.infos.DirectoryInfo;
import com.emiliosg23.models.infos.FileInfo;
import com.emiliosg23.models.infos.Info;
import com.emiliosg23.models.tdas.trees.MultiTree;

public class TreeInfoGenerator {
	private final Map<Modes, TreeTransformerStrategy> transformers;

	public TreeInfoGenerator() {
			this.transformers = Map.of(
					Modes.ACUMULATIVE, new AcumulativeTransformerStrategy(),
					Modes.FILE_EXTENSION, new FileExtensionTransformerStrategy()
			);
	}

	public MultiTree<Info> createTree(String directory){
		return createTree(directory, false);
	}

	public MultiTree<Info> createTree(String directory, boolean vertical) {
    //if (limit <= 0) return null;

    File file = new File(directory);
    Info info = file.isFile() ? new FileInfo(file) : new DirectoryInfo(file);
    MultiTree<Info> tree = new MultiTree<>(info);

    if (file.isFile()) {
        ((FileInfo) info).setSize(file.length());
        return tree;
    }

    String[] children = file.list();
    if (children == null) return tree;

    long accumulatedSize = 0;

    for (String childName : children) {
        File child = new File(file, childName);

        if (child.isDirectory()) {
            MultiTree<Info> childTree = createTree(child.getAbsolutePath(), !vertical);
            if (childTree != null) {
                tree.addChild(childTree);
                accumulatedSize += childTree.getRoot().getContent().getSize();
            }
        } else if (child.isFile()) {
            FileInfo fileInfo = new FileInfo(child);
            fileInfo.setSize(child.length());
            tree.addChild(fileInfo);
            accumulatedSize += fileInfo.getSize();
        }
    }

    info.setSize(accumulatedSize);

    return tree;
	}

	public MultiTree<Info> transformTree(MultiTree<Info> tree, Modes mode) {
    TreeTransformerStrategy strategy = transformers.get(mode);
    if (strategy == null) throw new IllegalArgumentException("Unsupported mode: " + mode);
    return strategy.transform(tree);
	}

	public MultiTree<Info> copyTree(MultiTree<Info> tree) {
    Info originalContent = tree.getRoot().getContent();
    Info copiedContent = copyInfo(originalContent);

    MultiTree<Info> copiedTree = new MultiTree<>(copiedContent);
    for (MultiTree<Info> child : tree.getRoot().getChildren()) {
        copiedTree.addChild(copyTree(child));
    }
    return copiedTree;
	}

  private Info copyInfo(Info info) {
    if (info == null) {
        throw new IllegalArgumentException("Info parameter is null");
    }
    if (info instanceof DirectoryInfo) {
        DirectoryInfo dir = (DirectoryInfo) info;
        return new DirectoryInfo(dir.getName(), dir.getSize()); // o atributos necesarios
    } else if (info instanceof FileInfo) {
        FileInfo file = (FileInfo) info;
        return new FileInfo(file.getName(), file.getSize(), file.getFullPath(), file.getExtension());
    } else {
        throw new IllegalArgumentException("Unsupported Info type: " + info.getClass());
    }
  }
}
