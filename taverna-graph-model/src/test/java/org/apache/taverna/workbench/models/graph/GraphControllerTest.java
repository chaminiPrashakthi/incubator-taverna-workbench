/*******************************************************************************
 * Copyright (C) 2007 The University of Manchester
 *
 *  Modifications to the initial code base are copyright of their
 *  respective authors, or their employers as appropriate.
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 ******************************************************************************/
package org.apache.taverna.workbench.models.graph;

import org.apache.taverna.workbench.models.graph.GraphNode;
import org.apache.taverna.workbench.models.graph.Graph;
import org.apache.taverna.workbench.models.graph.GraphEdge;
import org.apache.taverna.workbench.models.graph.GraphController;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.taverna.workbench.models.graph.GraphController.PortStyle;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.apache.taverna.scufl2.api.core.Workflow;

public class GraphControllerTest {

	Workflow dataflow;

	GraphController graphController;

	@Before
	public void setUp() throws Exception {
//		System.setProperty("raven.eclipse", "true");
//		setUpRavenRepository();
//		dataflow = WorkflowModelTranslator.doTranslation(loadScufl("nested_iteration.xml"));
		graphController = new GraphController(dataflow, null, false, null, null, null, null) {

			@Override
			public GraphEdge createGraphEdge() {
				return new GraphEdge(this);
			}

			@Override
			public Graph createGraph() {
				return new Graph(this);
			}

			@Override
			public GraphNode createGraphNode() {
				return new GraphNode(this);
			}

			@Override
			public void redraw() {

			}

		};
		graphController.setPortStyle(PortStyle.NONE);
	}

	@Test
	@Ignore
	public void testGenerateGraph() throws IOException, InterruptedException {
		Graph graph = graphController.generateGraph();
		assertEquals(5, graph.getNodes().size());
		assertEquals(9, graph.getEdges().size());
		assertEquals(1, graph.getSubgraphs().size());
	}

}
