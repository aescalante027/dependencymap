package com.escacorp.dependencymap.controller;

import com.escacorp.dependencymap.exception.InvalidFormatException;
import com.escacorp.dependencymap.model.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DependencyMapper {

	private Map<String, Node> map = new HashMap<>();
	
	/**
	 * Flushes map of its contents
	 */
	public void clearMap() {
		map = new HashMap<String, Node>();
	}
	
	
	/**
	 * HashMap / Graph of all dependencies
	 * 
	 * @return map - the current state of the Dependency Map
	 */
	public Map<String, Node> getDependencyMap() {
		return map;
	}
	
	/**
	 * 
	 * Generates the HashMap for every node generated and the edges it creates with its dependencies.
	 * 
	 * @param file - String representation of file location and file name
	 */
	public void readGraphState(String file) {
		try {
			Object[] fromFile = Files.lines(Paths.get(file)).toArray();
			List<String>fileLines = new ArrayList<>(Arrays.asList(
					Arrays.copyOf(fromFile, fromFile.length, String[].class)));
			for(String line : fileLines) {
				String[] edge = line.toString().split("->");
				addEdge(edge[0], edge[1]);
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(ArrayIndexOutOfBoundsException oobe) {
			System.out.println("File contains errors. Please check format.");
			oobe.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * 
	 * Adds a node tie with its dependency, hence an edge. This method will either create a new registry in the 
	 * HashMap to denote its existence in the graph, or add a new dependency if the node already exists. 
	 * 
	 * @throws InvalidFormatException for the following reasons - either node (master or dependency) is null or empty
	 * or if master equals dependency
	 * @param masterNode - key value for HashMap, denoting the node as the center
	 * @param dependencyNode - connection to the key node, as specified by the HashMap key. This will be used to iterate the node through its dependencies
	 * @return true if a change was made to the HashMap
	 * @return false if no change to the HashMap was made
	 */
	public boolean addEdge(String masterNode, String dependencyNode) {
		boolean wasEdgeAdded = false;
		if(masterNode == null || dependencyNode == null || masterNode.trim().equals("") || dependencyNode.trim().equals(""))
			throw new InvalidFormatException("Both vertices need to be defined");
		if(masterNode.equals(dependencyNode))
			throw new InvalidFormatException("Node cannot be dependent of itself");
		
		// Master doesn't exist
		if(!map.containsKey(masterNode)) {
			map.put(masterNode, new Node(masterNode));
		}
		// Dependency doesn't exist
		if(!map.containsKey(dependencyNode)) {
			map.put(dependencyNode, new Node(dependencyNode));
		}
		// Prevent dupes
		if(!map.get(masterNode).hasDependency(dependencyNode)) {
				map.get(masterNode).add(map.get(dependencyNode));
				wasEdgeAdded = true;
		}
		return wasEdgeAdded;
	}
}
