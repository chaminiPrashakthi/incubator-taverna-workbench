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

package org.apache.taverna.workbench.parallelize;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.apache.taverna.workbench.edits.EditManager;
import org.apache.taverna.workbench.selection.SelectionManager;
import org.apache.taverna.workbench.ui.views.contextualviews.ContextualView;
import org.apache.taverna.workbench.ui.views.contextualviews.activity.ContextualViewFactory;
import org.apache.taverna.scufl2.api.core.Processor;

public class ParallelizeContextualViewFactory implements ContextualViewFactory<Processor> {

	public static URI TYPE = URI.create("http://ns.taverna.org.uk/2010/scufl2/taverna/dispatchlayer/Parallelize");

	private EditManager editManager;
	private SelectionManager selectionManager;

	public boolean canHandle(Object selection) {
		return selection instanceof Processor;
	}

	public List<ContextualView> getViews(Processor selection) {
		return Arrays.asList(new ContextualView[] {new ParallelizeContextualView(selection, editManager, selectionManager)});
	}

	public void setEditManager(EditManager editManager) {
		this.editManager = editManager;
	}

	public void setSelectionManager(SelectionManager selectionManager) {
		this.selectionManager = selectionManager;
	}

}
