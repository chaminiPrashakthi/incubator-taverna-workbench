/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements. See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership. The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied. See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.apache.taverna.workbench.design.actions;

import static org.apache.taverna.workbench.icons.WorkbenchIcons.deleteIcon;

import java.awt.Component;
import java.awt.event.ActionEvent;

import org.apache.taverna.workbench.edits.EditException;
import org.apache.taverna.workbench.edits.EditManager;
import org.apache.taverna.workbench.selection.SelectionManager;
import org.apache.taverna.workflow.edits.RemoveChildEdit;

import org.apache.log4j.Logger;

import org.apache.taverna.scufl2.api.core.ControlLink;
import org.apache.taverna.scufl2.api.core.Workflow;

/**
 * Action for removing a condition from the dataflow.
 * 
 * @author David Withers
 */
@SuppressWarnings("serial")
public class RemoveConditionAction extends DataflowEditAction {
	private static final Logger logger = Logger
			.getLogger(RemoveConditionAction.class);

	private ControlLink controlLink;

	public RemoveConditionAction(Workflow dataflow, ControlLink controlLink,
			Component component, EditManager editManager,
			SelectionManager selectionManager) {
		super(dataflow, component, editManager, selectionManager);
		this.controlLink = controlLink;
		putValue(SMALL_ICON, deleteIcon);
		putValue(NAME, "Delete control link");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			dataflowSelectionModel.removeSelection(controlLink);
			editManager.doDataflowEdit(dataflow.getParent(),
					new RemoveChildEdit<>(dataflow, controlLink));
		} catch (EditException e1) {
			logger.debug("Delete control link failed", e1);
		}
	}
}
