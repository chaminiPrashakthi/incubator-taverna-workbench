package org.apache.taverna.activities.disabled.menu;

import javax.swing.Action;

import org.apache.taverna.activities.disabled.actions.DisabledActivityConfigurationAction;
import org.apache.taverna.activities.disabled.views.DisabledActivityViewFactory;
import org.apache.taverna.servicedescriptions.ServiceDescriptionRegistry;
import org.apache.taverna.workbench.activityicons.ActivityIconManager;
import org.apache.taverna.workbench.activitytools.AbstractConfigureActivityMenuAction;
import org.apache.taverna.workbench.edits.EditManager;
import org.apache.taverna.workbench.file.FileManager;
import org.apache.taverna.workbench.report.ReportManager;

public class ConfigureDisabledMenuAction extends AbstractConfigureActivityMenuAction {

	private EditManager editManager;
	private FileManager fileManager;
	private ReportManager reportManager;
	private ActivityIconManager activityIconManager;
	private ServiceDescriptionRegistry serviceDescriptionRegistry;

	public ConfigureDisabledMenuAction() {
		super(DisabledActivityViewFactory.ACTIVITY_TYPE);
	}

	@Override
	protected Action createAction() {
		return new DisabledActivityConfigurationAction(findActivity(), getParentFrame(),
				editManager, fileManager, reportManager, activityIconManager, serviceDescriptionRegistry);
	}

	public void setEditManager(EditManager editManager) {
		this.editManager = editManager;
	}

	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	public void setReportManager(ReportManager reportManager) {
		this.reportManager = reportManager;
	}

	public void setActivityIconManager(ActivityIconManager activityIconManager) {
		this.activityIconManager = activityIconManager;
	}

	public void setServiceDescriptionRegistry(ServiceDescriptionRegistry serviceDescriptionRegistry) {
		this.serviceDescriptionRegistry = serviceDescriptionRegistry;
	}

}
