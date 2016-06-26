package com.escacorp.dependencymap.view;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.escacorp.dependencymap.controller.DependencyMapper;
import com.escacorp.dependencymap.model.Node;
import com.escacorp.dependencymap.view.DependencyViewer;

public class DependencyViewerTest {
	
	private DependencyViewer viewer;
	private DependencyMapper mapper;

	@Before
	public void setUp() {
		mapper = mock(DependencyMapper.class);
		viewer = new DependencyViewer(mapper);
	}
	
	@Test
	public void testLayout_Empty() {
		when(mapper.getDependencyMap()).thenReturn(new HashMap<String, Node>());
		assertEquals("Does not contain A", viewer.getLayout("A"));
	}
	
	@Test
	public void testLayout_Null() {
		when(mapper.getDependencyMap()).thenReturn(null);
		assertEquals("Does not contain A", viewer.getLayout("A"));
	}
	
	@Test
	public void testLayout_NodeAndDependency() {
		HashMap<String, Node> singleNode = new HashMap<String, Node>();
		Node master = new Node("A");
		Node dependency = new Node("B");
		master.add(dependency);
		singleNode.put("A", master);
		when(mapper.getDependencyMap()).thenReturn(singleNode);
		assertEquals("A\n\\_B", viewer.getLayout("A"));
	}
	
	@Test
	public void testLayout_NodeAndDependencies() {
		HashMap<String, Node> singleNode = new HashMap<String, Node>();
		Node master = new Node("A");
		Node dependency1 = new Node("B");
		Node dependency2 = new Node("C");
		master.add(dependency1);
		master.add(dependency2);
		singleNode.put("A", master);
		when(mapper.getDependencyMap()).thenReturn(singleNode);
		assertEquals("A\n|_B\n\\_C", viewer.getLayout("A"));
	}
	
	@Test
	public void testLayout_CircularDependency() {
		HashMap<String, Node> map = new HashMap<String, Node>();
		Node master = new Node("A");
		Node dependency1 = new Node("B");
		Node dependency2 = new Node("C");
		Node circular = new Node("D");
		circular.add(master);
		dependency2.add(circular);
		dependency1.add(dependency2);
		master.add(dependency1);
		map.put(master.getNodeName(), master);
		map.put(dependency1.getNodeName(), dependency1);
		map.put(dependency2.getNodeName(), dependency2);
		map.put(circular.getNodeName(), circular);
		when(mapper.getDependencyMap()).thenReturn(map);
		assertEquals("A\n\\_B\n   \\_C\n      \\_D\n         \\_A<circ>", viewer.getLayout("A"));
	}
	
	@Test
	public void testLayout_CircularDependencyAndMultidepth() {
		HashMap<String, Node> map = new HashMap<String, Node>();
		Node master = new Node("A");
		Node dependency1 = new Node("B");
		Node dependency2 = new Node("C");
		Node circular = new Node("D");
		Node dependency3 = new Node("E");
		circular.add(master);
		dependency2.add(circular);
		dependency1.add(dependency2);
		master.add(dependency1);
		master.add(dependency3);
		map.put(master.getNodeName(), master);
		map.put(dependency1.getNodeName(), dependency1);
		map.put(dependency2.getNodeName(), dependency2);
		map.put(circular.getNodeName(), circular);
		when(mapper.getDependencyMap()).thenReturn(map);
		assertEquals("A\n|_B\n|  \\_C\n|     \\_D\n|        \\_A<circ>\n\\_E", viewer.getLayout("A"));
	}
}
