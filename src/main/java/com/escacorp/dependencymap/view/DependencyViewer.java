package com.escacorp.dependencymap.view;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.escacorp.dependencymap.controller.DependencyMapper;
import com.escacorp.dependencymap.model.Node;

public class DependencyViewer {
	
	private DependencyMapper mapper;
	private Map<String, Node> dependencyMap;
	
	public DependencyViewer() {
		this.mapper = new DependencyMapper();
	}
	
	public DependencyViewer(DependencyMapper mapper) {
		this.mapper = mapper;
	}
	
	/**
	 * Returns the DependencyMapper class
	 * @return DependencyMapper class tied to the view
	 */
	public DependencyMapper getDependencyMapper() {
		return mapper;
	}
	
	/**
	 * Method used to forward text file containing a list of 
	 * @param file
	 */
	public void submitGraphState(String file) {
		mapper.readGraphState(file);
	}
	
	/**
	 * Main method to get graphical layout of the dependency tree
	 * 
	 * @param startNode - node in the entire layout 
	 * @return layout equal to the dependency tree with startNode as its root or empty string if startNode doesn't exist or map is empty
	 */
	public String getLayout(String startNode) {
		String layout = "";
		dependencyMap = mapper.getDependencyMap();
		if((dependencyMap != null) && dependencyMap.containsKey(startNode))
			layout = getLayout(startNode, layout, 0, new HashSet<>(), new HashSet<>());
		else
			layout = "Does not contain " + startNode;
		return layout;
	}
	
	/**
	 * Main method that will traverse the entire graph and print each dependency edge it has. Each dependency along the way may continue "horizontally"
	 * based on its indirect dependencies down the branch, or vertically, as it prints its direct dependencies. 
	 * 
	 * By nature, the method will go through depth first, traversing through its indirect dependencies first. The text \<circ\> will appear if a circular
	 * dependency is found. This is done by keeping record of its depth and looking for duplicates of itself. When the currentNode has iterated through all of its
	 * indirect dependencies and the branch is done, the method will remove the nodes value from the previousNodes list, eventually cleaning out the list.
	 * 
	 * The method also keeps track of its direct dependencies by iterating through ArrayList of dependencies. If the node still has more direct dependencies 
	 * to print, it will denote its dependency with the mark \"|_\" and set its current level in the ArrayList continuedDependency as TRUE. This will tell the
	 * string print to keep a hash mark | at the appropriate level (@see calculateSpacing). Once the node no longer has any more direct dependencies, it will denote
	 * this with the mark \"\\_\" and return its continuedDependency level to FALSE. 
	 * 
	 * @param currentNode - current node we want to analyze
	 * @param layout - the String representation of the graph
	 * @param depth - the depth in dependencies the method is currently on
	 * @param previousNodes - record of the nodes previously used in the branch. Used to detected circular dependencies
	 * @param continuedDependency - record of nodes which still has more dependencies to print as a specific depth
	 * @return string representation of a node, including all of its direct and indirect dependencies
	 */
	private String getLayout(String currentNode, String layout, int depth, HashSet<String> previousNodes, HashSet<Integer> continuedDependency) {
		layout = layout.concat(currentNode);
		
		// if a previous node matches our current node, the set is circular
		if(previousNodes.contains(currentNode))
			layout = layout.concat("<circ>");
		
		else if(dependencyMap.containsKey(currentNode))
		{
			previousNodes.add(currentNode);
			Iterator<Node> dependencies = dependencyMap.get(currentNode).iterator();
			
			while(dependencies.hasNext()) {
				Node dependency = dependencies.next();
				layout = layout.concat("\n");
				
				//spacing needed for previous dependencies in the branch
				layout = layout + calculateSpacing(depth, continuedDependency);
				
				// vertical branch will continue
				if(dependencies.hasNext()) {
					layout = layout.concat("|_");
					continuedDependency.add(depth);
				}
				
				// end of branch for our current node level
				else {
					layout = layout.concat("\\_");
					continuedDependency.remove(depth);
				}
				layout = getLayout(dependency.getNodeName(), layout, depth+1, previousNodes, continuedDependency);
			}
		}
			
		previousNodes.remove(currentNode);
		return layout;
	}
	
	/**
	 * Calculating method that returns a specific line of print, including continued hash marks for ongoing dependency depth connections and proper spacing
	 * 
	 * @param depth - depth of graph for a particular line of print. Used to calculate spacing and character print for each line of depth.
	 * @param continuedDependency - recorded list of ongoing dependencies at a particular depth.
	 * @return string value of spaces and characters prior to a node being added
	 */
	private String calculateSpacing(int depth, HashSet<Integer> continuedDependency) {
		String line = "";
		for(int space = 0; space < depth; ++space) {
			if(continuedDependency.contains(space))
				line = line.concat("|  ");
			else
				line = line.concat("   ");
		}
		return line;
	}
	

	public static void main(String args[]) {
		Long time = System.currentTimeMillis();
		if(args.length != 2) {
			System.out.println("Usage: java -jar dependencymap.jar <fileLocation> <startNode>");
		}
		else {
			DependencyViewer viewer = new DependencyViewer();
			viewer.submitGraphState(args[0]);
			System.out.println("Dependency Map:");
			System.out.println(viewer.getLayout(args[1]));
			System.out.println("Execution time: " + (System.currentTimeMillis() - time) + " ms");
		}
	}

}
