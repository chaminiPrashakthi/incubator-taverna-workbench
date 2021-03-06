package org.apache.taverna.ui.perspectives.biocatalogue.integration.contextual_views;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.taverna.biocatalogue.model.BioCataloguePluginConstants;
import org.apache.taverna.workbench.ui.views.contextualviews.ContextualView;
import org.apache.taverna.workflowmodel.processor.activity.ActivityOutputPort;


public class ProcessorOutputPortView extends ContextualView
{
	private final ActivityOutputPort outputPort;
	private JPanel jPanel;

	public ProcessorOutputPortView(ActivityOutputPort outputPort) {
		this.outputPort = outputPort;
		
		jPanel = new JPanel();
		
		// NB! This is required to have the body of this contextual
		// view added to the main view; otherwise, body will be
		// blank
		initView();
	}
	
	@Override
	public JComponent getMainFrame()
	{
		return jPanel;
	}

	@Override
	public String getViewTitle() {
		return "Service Catalogue Information";
	} 

	@Override
	public void refreshView()
	{
	  // this actually causes the parent container to validate itself,
    // which is what is needed here
    this.revalidate();
    this.repaint();
	}
	
	@Override
	public int getPreferredPosition() {
		return BioCataloguePluginConstants.CONTEXTUAL_VIEW_PREFERRED_POSITION;
	}

}
