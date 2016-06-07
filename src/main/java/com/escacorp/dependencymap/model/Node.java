package com.escacorp.dependencymap.model;

import java.util.ArrayList;
import java.util.Iterator;

public class Node{

	private String nodeName;
	private ArrayList<Node> dependencies = new ArrayList<Node>();
	
	public Node(String nodeName) {
		this.nodeName = nodeName;
	}
	
	// TODO: Possible rename
	public void add(Node node) {
		dependencies.add(node);
	}
	
	public String getNodeName() {
		return nodeName;
	}
	
	public Boolean hasDependency(String dependency) {
		return hasDependency(new Node(dependency));
	}
	
	public Boolean hasDependency(Node dependency) {
		return dependencies.contains(dependency);
	}
	
	@Override
	public boolean equals(Object dependency) {
		return (((Node)dependency).getNodeName().equals(nodeName));
	}
	
	public Iterator<Node> iterator() {
		return dependencies.iterator();
	}
}
