package com.escacorp.dependencymap.functionaltests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.escacorp.dependencymap.controller.DependencyMapper;
import com.escacorp.dependencymap.exception.InvalidFormatException;
import com.escacorp.dependencymap.view.DependencyViewer;

public class DependencyViewerFunctionalTest {

	public DependencyViewer viewer;
	public DependencyMapper mapper;
	
	@Before
	public void setUp() {
		viewer = new DependencyViewer();
		mapper = viewer.getDepedencyMapper();
	}
	
	@After
	public void cleanUp() {
		mapper.clearMap();
	}
	
	@Test
	public void testNegativeBlankNode() {
		mapper.addEdge("A", "B");
		mapper.addEdge("B", "C");
		try {
			mapper.addEdge("C", "");
			fail("InvalidFormatException should have been thrown");
		}
		catch(InvalidFormatException e){
			//continue
		}
		assertEquals("A\n\\_B\n   \\_C", viewer.getLayout("A"));
	}
	
	@Test
	public void testNegativeNullNode() {
		mapper.addEdge("A", "B");
		mapper.addEdge("B", "C");
		try {
			mapper.addEdge("C", null);
			fail("InvalidFormatException should have been thrown");
		}
		catch(InvalidFormatException e){
			//continue
		}
		assertEquals("A\n\\_B\n   \\_C", viewer.getLayout("A"));
	}
	
	@Test
	public void testDuplicateNodes() {
		mapper.addEdge("A", "B");
		mapper.addEdge("B", "C");
		mapper.addEdge("B", "C");
		mapper.addEdge("B", "C");
		assertEquals("A\n\\_B\n   \\_C", viewer.getLayout("A"));
	}
	
	@Test
	public void testEmpty() {
		assertEquals("Does not contain A", viewer.getLayout("A"));
	}
	
	@Test
	public void testSingleLayout() {
		mapper.addEdge("A", "B");
		mapper.addEdge("B", "C");
		mapper.addEdge("C", "D");
		assertEquals("A\n\\_B\n   \\_C\n      \\_D",viewer.getLayout("A"));
	}
	
	@Test
	public void testLayoutWithMultipleDependencies() {
		mapper.addEdge("A", "B");
		mapper.addEdge("B", "C");
		mapper.addEdge("C", "D");
		mapper.addEdge("C", "E");
		assertEquals("A\n\\_B\n   \\_C\n      |_D\n      \\_E",viewer.getLayout("A"));
	}
	
	@Test
	public void testLayoutWithMiddleMultipleDependencies() {
		mapper.addEdge("A", "B");
		mapper.addEdge("B", "C");
		mapper.addEdge("B", "F");
		mapper.addEdge("C", "D");
		mapper.addEdge("C", "E");
		assertEquals("A\n\\_B\n   |_C\n   |  |_D\n   |  \\_E\n   \\_F",viewer.getLayout("A"));
	}
	
	@Test
	public void testLayoutWithMajorComplexityDependencies() {
		mapper.addEdge("A", "B");
		mapper.addEdge("B", "C");
		mapper.addEdge("B", "F");
		mapper.addEdge("C", "D");
		mapper.addEdge("C", "E");
		mapper.addEdge("F", "C");
		assertEquals("A\n\\_B\n   |_C\n   |  |_D\n   |  \\_E\n   \\_F\n      \\_C\n         |_D\n         \\_E",viewer.getLayout("A"));
	}
	
	@Test
	public void testLayoutWithMajorComplexityDependenciesTwoTier() {
		mapper.addEdge("A", "B");
		mapper.addEdge("A", "G");
		mapper.addEdge("B", "C");
		mapper.addEdge("B", "F");
		mapper.addEdge("C", "D");
		mapper.addEdge("C", "E");
		mapper.addEdge("F", "C");
		mapper.addEdge("G", "F");
		assertEquals("A\n|_B\n|  |_C\n|  |  |_D\n|  |  \\_E\n|  \\_F\n|     \\_C\n|        |_D\n|        \\_E\n" +
		"\\_G\n   \\_F\n      \\_C\n         |_D\n         \\_E", viewer.getLayout("A"));
	}
	
	@Test
	public void testLayoutWithMidformExample() {
		mapper.addEdge("A", "B");
		mapper.addEdge("A", "J");
		mapper.addEdge("B", "C");
		mapper.addEdge("B", "D");
		mapper.addEdge("C", "E");
		mapper.addEdge("D", "F");
		mapper.addEdge("D", "G");
		mapper.addEdge("D", "J");
		mapper.addEdge("E", "H");
		mapper.addEdge("E", "M");
		mapper.addEdge("F", "H");
		mapper.addEdge("H", "L");
		assertEquals("A\n|_B\n|  |_C\n|  |  \\_E\n|  |     |_H\n|  |     |  \\_L\n|  |     \\_M\n|  \\_D\n|     |_F\n|     |  \\_H\n" +
		"|     |     \\_L\n|     |_G\n|     \\_J\n\\_J", viewer.getLayout("A"));
	}
	
	@Test
	public void testLayoutWithCircularDependencies() {
		mapper.addEdge("A", "B");
		mapper.addEdge("B", "C");
		mapper.addEdge("C", "D");
		mapper.addEdge("D", "A");
		assertEquals("A\n\\_B\n   \\_C\n      \\_D\n         \\_A<circ>", viewer.getLayout("A"));
	}
	
	@Test
	public void testLayoutWithFileAndCircularDependencies() {
		viewer.submitGraphState(System.getProperty("user.dir")+"/src/main/resources/graph.txt");
		assertEquals("I\n|_O\n|  \\_P\n|     \\_Q\n|_P\n|  \\_Q\n\\_K\n   |_N\n   \\_L\n      \\_I<circ>", viewer.getLayout("I"));
	}
	
	//Used to print out example from email
	@Test
	public void testLayoutAll() {
		viewer.submitGraphState(System.getProperty("user.dir")+"/src/main/resources/graph.txt");
		System.out.println(viewer.getLayout("A"));
	}
}
