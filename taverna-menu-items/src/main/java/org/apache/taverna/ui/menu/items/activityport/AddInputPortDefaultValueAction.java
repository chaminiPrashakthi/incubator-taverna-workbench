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
package org.apache.taverna.ui.menu.items.activityport;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.taverna.activities.stringconstant.views.StringConstantConfigView;
import org.apache.taverna.workbench.design.actions.DataflowEditAction;
import org.apache.taverna.workbench.edits.CompoundEdit;
import org.apache.taverna.workbench.edits.Edit;
import org.apache.taverna.workbench.edits.EditException;
import org.apache.taverna.workbench.edits.EditManager;
import org.apache.taverna.workbench.icons.WorkbenchIcons;
import org.apache.taverna.workbench.selection.SelectionManager;
import org.apache.taverna.workflow.edits.AddChildEdit;
import org.apache.taverna.workflow.edits.AddDataLinkEdit;
import org.apache.taverna.workflow.edits.AddProcessorEdit;

import org.apache.log4j.Logger;

import org.apache.taverna.services.ServiceRegistry;
import org.apache.taverna.scufl2.api.activity.Activity;
import org.apache.taverna.scufl2.api.configurations.Configuration;
import org.apache.taverna.scufl2.api.core.DataLink;
import org.apache.taverna.scufl2.api.core.Processor;
import org.apache.taverna.scufl2.api.core.Workflow;
import org.apache.taverna.scufl2.api.iterationstrategy.CrossProduct;
import org.apache.taverna.scufl2.api.port.InputProcessorPort;
import org.apache.taverna.scufl2.api.port.OutputActivityPort;
import org.apache.taverna.scufl2.api.port.OutputProcessorPort;
import org.apache.taverna.scufl2.api.profiles.ProcessorBinding;
import org.apache.taverna.scufl2.api.profiles.ProcessorOutputPortBinding;
import org.apache.taverna.scufl2.api.profiles.Profile;

/**
 * Action for adding a default value to an input port of a processor.
 *
 * @author Alex Nenadic
 */
@SuppressWarnings("serial")
public class AddInputPortDefaultValueAction extends DataflowEditAction {

	private static Logger logger = Logger.getLogger(AddInputPortDefaultValueAction.class);

	private static final URI STRING_CONSTANT = URI
			.create("http://ns.taverna.org.uk/2010/activity/constant");

	private InputProcessorPort inputPort;

	private final ServiceRegistry serviceRegistry;

	public AddInputPortDefaultValueAction(Workflow workflow, InputProcessorPort inputPort,
			Component component, EditManager editManager, SelectionManager selectionManager,
			ServiceRegistry serviceRegistry) {
		super(workflow, component, editManager, selectionManager);
		this.inputPort = inputPort;
		this.serviceRegistry = serviceRegistry;
		putValue(SMALL_ICON, WorkbenchIcons.inputValueIcon);
		putValue(NAME, "Set constant value");
	}

	public void actionPerformed(ActionEvent e) {
		try {
			Activity activity = new Activity();
			activity.setType(STRING_CONSTANT);
			Configuration configuration = new Configuration();
			configuration.setType(STRING_CONSTANT.resolve("#Config"));
			configuration.getJsonAsObjectNode().put("string", "");
			configuration.setConfigures(activity);

			StringConstantConfigView configView = new StringConstantConfigView(activity,
					configuration, serviceRegistry);

			int answer = JOptionPane.showConfirmDialog(component, configView,
					"Text constant value", JOptionPane.OK_CANCEL_OPTION);
			if (answer != JOptionPane.CANCEL_OPTION) {

				configView.noteConfiguration();
				configuration.setJson(configView.getJson());

				Profile profile = selectionManager.getSelectedProfile();

				Processor processor = new Processor();
				processor.setName(inputPort.getName() + "_value");

				CrossProduct crossProduct = new CrossProduct();
				crossProduct.setParent(processor.getIterationStrategyStack());

				ProcessorBinding processorBinding = new ProcessorBinding();
				processorBinding.setBoundProcessor(processor);
				processorBinding.setBoundActivity(activity);

				// create activity port
				OutputActivityPort activityPort = new OutputActivityPort(activity, "value");
				activityPort.setDepth(0);
				activityPort.setGranularDepth(0);
				// create processor port
				OutputProcessorPort processorPort = new OutputProcessorPort(processor,
						activityPort.getName());
				processorPort.setDepth(0);
				processorPort.setGranularDepth(0);
				// add a new port binding
				new ProcessorOutputPortBinding(processorBinding, activityPort, processorPort);

				// Add a data link between the string constant processor's output port
				// and the processor containing the passed inputPort.
				DataLink datalink = new DataLink();
				datalink.setReceivesFrom(processorPort);
				datalink.setSendsTo(inputPort);

				List<Edit<?>> editList = new ArrayList<Edit<?>>();
				editList.add(new AddChildEdit<Profile>(profile, activity));
				editList.add(new AddChildEdit<Profile>(profile, configuration));
				editList.add(new AddChildEdit<Profile>(profile, processorBinding));
				editList.add(new AddProcessorEdit(dataflow, processor));
				editList.add(new AddDataLinkEdit(dataflow, datalink));

				editManager.doDataflowEdit(dataflow.getParent(), new CompoundEdit(editList));

			}
		} catch (EditException ex) {
			logger.error("Adding default value for input port failed", ex);
		}
	}

}
