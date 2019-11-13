package com.oracle.javafx.scenebuilder.kit.editor.panel.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;

public class SceneLayoutReset implements Runnable
{

	private final Scene scene;

	public SceneLayoutReset(Scene scene)
	{
		this.scene = scene;
	}

	@Override
	public void run()
	{
		final List<Parent> layoutNodes = new LinkedList<>();
		layoutNodes.add(scene.getRoot());

		final Set<Parent> needsLayoutNodes = new HashSet<>();
		while (!layoutNodes.isEmpty()) {
			final Parent layoutNode = layoutNodes.remove(0);
			if (layoutNode.isVisible()) {
				for (Node childNode : layoutNode.getChildrenUnmodifiable()) {
					if (childNode instanceof Parent) {
						layoutNodes.add((Parent) childNode);
					} else if (childNode instanceof SubScene) {
						layoutNodes.add(((SubScene) childNode).getRoot());
					}
				}
				if (layoutNode.isNeedsLayout()) {
					// System.err.printf("WARNING: node %s has needLayout=true immediately after layout!%n", layoutNode);
					needsLayoutNodes.add(layoutNode);
				}
			}
		}
		System.out.printf("There are %d visible nodes under scene %s currently needing layout%n", needsLayoutNodes.size(), scene);
		needsLayoutNodes.forEach(n -> System.out.printf("\t %s%n", n));
	}

}

