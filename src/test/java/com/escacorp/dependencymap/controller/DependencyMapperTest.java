package com.escacorp.dependencymap.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.escacorp.dependencymap.controller.DependencyMapper;
import com.escacorp.dependencymap.exception.InvalidFormatException;
import com.escacorp.dependencymap.model.Node;

public class DependencyMapperTest {

	private DependencyMapper mapper;
	
	@Before
	public void setUp() {
		mapper = new DependencyMapper();
	}
	
	@Test
	public void testNegativeEdge_EqualNode() {
		try {
			mapper.addEdge("A", "A");
			fail("Method should not accept the same node as a dependency");
		}
		catch(InvalidFormatException e) {
			//pass
		}
	}
	
	@Test
	public void testNegativeEdge_NullMaster() {
		try {
			mapper.addEdge(null, "A");
			fail("Method should not accept null as master");
		}
		catch(InvalidFormatException e) {
			//pass
		}
	}
	
	@Test
	public void testNegativeEdge_NullDependency() {
		try {
			mapper.addEdge("A", null);
			fail("Method should not accept null as a dependency");
		}
		catch(InvalidFormatException e) {
			//pass
		}
	}
	
	@Test
	public void testNegativeEdge_EmptyMaster() {
		try {
			mapper.addEdge("", "A");
			fail("Method should not accept empty as master");
		}
		catch(InvalidFormatException e) {
			//pass
		}
	}
	
	@Test
	public void testNegativeEdge_EmptyDependency() {
		try {
			mapper.addEdge("A", "");
			fail("Method should not accept empty as a dependency");
		}
		catch(InvalidFormatException e) {
			//pass
		}
	}
	
	@Test
	public void testSingleEdge() {
		mapper.addEdge("A", "B");
		HashMap<String, Node> result = mapper.getDependencyMap();
		assertTrue(result.containsKey("A"));
		assertTrue(result.get("A").hasDependency("B"));
		assertEquals(result.get("A").iterator().next().getNodeName(), "B");
	}
	
	@Test
	public void testMultiEdge() {
		mapper.addEdge("A", "B");
		mapper.addEdge("B", "C");
		HashMap<String, Node> result = mapper.getDependencyMap();
		assertTrue(result.containsKey("A"));
		assertTrue(result.get("A").hasDependency("B"));
		assertEquals(result.get("A").iterator().next().getNodeName(), "B");
		assertTrue(result.get("B").hasDependency("C"));
	}
}
