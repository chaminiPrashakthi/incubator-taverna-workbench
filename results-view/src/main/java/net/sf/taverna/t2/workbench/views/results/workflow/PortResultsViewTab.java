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
package net.sf.taverna.t2.workbench.views.results.workflow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import net.sf.taverna.t2.workbench.views.results.workflow.FilteredWorkflowResultTreeModel.FilterType;

/**
 * A tab containing result tree for an output port and a panel with rendered result
 * of the currently selected node in the tree.
 * 
 * @author Alex Nenadic
 *
 */
public class PortResultsViewTab extends JPanel{
	
	private static final long serialVersionUID = -5531195402446371947L;
	
	// Tree model of results
	WorkflowResultTreeModel resultModel;
	
    FilteredWorkflowResultTreeModel filteredTreeModel;

	// Rendered result component
	private RenderedResultComponent renderedResultComponent;

	private String portName;

	private int portDepth;

	private JTree tree;
	
    private JComboBox filterChoiceBox;

	public PortResultsViewTab(String portName, int portDepth){
		super(new BorderLayout());
		this.portName = portName;
		this.portDepth = portDepth;

		initComponents();
	}

	private void initComponents() {
		
		// Split pane containing a tree with all results from an output port and 
		// rendered result component for individual result rendered currently selected 
		// from the tree
		JSplitPane splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		// Results tree (containing T2References to all individual results for this port)
		resultModel =  new WorkflowResultTreeModel(portName,
				portDepth);

		filteredTreeModel = new FilteredWorkflowResultTreeModel(getResultModel());
		tree = new JTree(filteredTreeModel);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setExpandsSelectedPaths(true);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		tree.setCellRenderer(new PortResultCellRenderer());
		
		// Component for rendering individual results
		renderedResultComponent = new RenderedResultComponent(); 

		tree.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent e) {
				TreePath selectionPath = e.getNewLeadSelectionPath();
				if (selectionPath != null) {
					// Get the selected node
					final Object selectedNode = selectionPath.getLastPathComponent();
					renderedResultComponent.setNode((WorkflowResultTreeNode)selectedNode);
				}
			}
			
		});

		filteredTreeModel.addTreeModelListener(new TreeModelListener() {

			public void treeNodesChanged(TreeModelEvent e) {
				
				tree.expandPath(e.getTreePath());
							
				// If nothing is currently selected in the tree - select either the
				// result or the first AVAILABLE item in the result list
				/*
				if (tree.getSelectionRows() == null || tree.getSelectionRows().length == 0){ 			
					ResultTreeNode parent = (ResultTreeNode)e.getTreePath().getLastPathComponent(); // parent of the changed node(s)
					int[] indices = e.getChildIndices(); //indexes of the changed node(s)
					ResultTreeNode firstChild = (ResultTreeNode) parent.getChildAt(indices[0]); // get the first changed node
					if (firstChild.getState().equals(ResultTreeNode.ResultTreeNodeState.RESULT_REFERENCE)){ // if this is the result node rather than result list placeholder
						tree.setSelectionPath(new TreePath(firstChild.getPath())); // select this node
					}
				}*/
			}

			public void treeNodesInserted(TreeModelEvent e) {
			}

			public void treeNodesRemoved(TreeModelEvent e) {
			}

			public void treeStructureChanged(TreeModelEvent e) {
			}
		});
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());

		JPanel treeSubPanel = new JPanel();
		treeSubPanel.setLayout(new BorderLayout());
		treeSubPanel.add(new JLabel("Click in tree to"), BorderLayout.WEST);
		filterChoiceBox = new JComboBox(new FilterType[] {FilterType.ALL, FilterType.RESULTS, FilterType.ERRORS});
		filterChoiceBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    updateTree();
			}
		    });
		treeSubPanel.add(filterChoiceBox);
		leftPanel.add(treeSubPanel, BorderLayout.NORTH);
		leftPanel.add(new JScrollPane(tree), BorderLayout.CENTER);
		splitPanel.setTopComponent(leftPanel);
		splitPanel.setBottomComponent(renderedResultComponent);
		splitPanel.setDividerLocation(400);
		
		// Add all to main panel
		add(splitPanel, BorderLayout.CENTER);
		
	}

	/**
	 * @return the resultModel
	 */
	public WorkflowResultTreeModel getResultModel() {
		return resultModel;
	}

    public FilteredWorkflowResultTreeModel getModel() {
	return filteredTreeModel;
    }

    private List<TreePath> expandedPaths = new ArrayList<TreePath>();
    private TreePath selectionPath = null;

    private void rememberPaths() {
	expandedPaths.clear();
	for (Enumeration e = tree.getExpandedDescendants(new TreePath(filteredTreeModel.getRoot())); (e != null) && e.hasMoreElements();) {
	    expandedPaths.add((TreePath) e.nextElement());
	}
	selectionPath = tree.getSelectionPath();
    }

    private void reinstatePaths() {
	for (TreePath path : expandedPaths) {
	    if (filteredTreeModel.isShown((DefaultMutableTreeNode) path.getLastPathComponent())) {
		tree.expandPath(path);
	    }
	}
	if (selectionPath != null) {
	    if (filteredTreeModel.isShown((DefaultMutableTreeNode) selectionPath.getLastPathComponent())) {
		    tree.setSelectionPath(selectionPath);
	    }
	    else {
		tree.clearSelection();
		renderedResultComponent.clearResult();
	    }
	}
    }

    private void updateTree() {
	filteredTreeModel.setFilter((FilterType) filterChoiceBox.getSelectedItem());
	rememberPaths();
	filteredTreeModel.reload();
	tree.setModel(filteredTreeModel);
	reinstatePaths();
    }

	public void expandTree() {

		if (tree != null){
			for (int row = 0; row < tree.getRowCount(); row ++) {
			      tree.expandRow(row);
			 }
		}
	}
}
