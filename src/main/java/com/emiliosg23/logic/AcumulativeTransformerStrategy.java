package com.emiliosg23.logic;

import com.emiliosg23.models.infos.ExtensionInfo;
import com.emiliosg23.models.infos.Info;
import com.emiliosg23.models.tdas.lists.List;
import com.emiliosg23.models.tdas.trees.MultiTree;

/* ONLY USE IF BEFORE YOU GENERATED A FILE EXTENSION TREE */
public class AcumulativeTransformerStrategy implements TreeTransformerStrategy {
	@Override
	public MultiTree<Info> transform(MultiTree<Info> tree) {
		if (tree == null) return null;

		List<Info> nodes = tree.traverseTree();
		MultiTree<Info> transformedTree = new MultiTree<>(tree.getRoot().getContent());

		for (Info node : nodes) {
			if (!(node instanceof ExtensionInfo)) continue;

			ExtensionInfo extNode = (ExtensionInfo) node;
			String ext = extNode.getName();
			boolean found = false;

			for (MultiTree<Info> child : transformedTree.getRoot().getChildren()) {
				Info childInfo = child.getRoot().getContent();

				if (childInfo instanceof ExtensionInfo) {
					ExtensionInfo existing = (ExtensionInfo) childInfo;
					if (existing.getName().equals(ext)) {
						existing.setSize(existing.getSize() + extNode.getSize());
						found = true;
						break;
					}
				}
			}

			if (!found) 
				transformedTree.addChild(new ExtensionInfo(ext, extNode.getSize()));
		}

		return transformedTree;
	}
}
