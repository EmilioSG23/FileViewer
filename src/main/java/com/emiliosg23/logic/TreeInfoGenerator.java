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

	public MultiTree<Info> createTree(String directory, boolean vertical, int limit) {
    if (limit <= 0) return null;

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
            MultiTree<Info> childTree = createTree(child.getAbsolutePath(), !vertical, limit - 1);
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
}
