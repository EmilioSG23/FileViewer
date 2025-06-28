package com.emiliosg23.logic;

import com.emiliosg23.models.infos.Info;
import com.emiliosg23.models.tdas.trees.MultiTree;

public interface TreeTransformerStrategy {
	MultiTree<Info> transform(MultiTree<Info> tree);
}
