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
package org.apache.taverna.activities.disabled.views;

import java.awt.BorderLayout;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.help.CSH;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.taverna.scufl2.api.activity.Activity;

import org.apache.taverna.lang.uibuilder.UIBuilder;
import org.apache.taverna.workbench.ui.views.contextualviews.activity.ActivityConfigurationPanel;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.taverna.workflowmodel.processor.activity.ActivityAndBeanWrapper;

@SuppressWarnings("serial")
public class DisabledConfigView extends ActivityConfigurationPanel {

    private ActivityAndBeanWrapper configuration;
    private List<String> fieldNames;

    private Object clonedConfig = null;
    String origConfigXML = "";

    public DisabledConfigView(Activity activity) {
        super(activity);
        setLayout(new BorderLayout());
        fieldNames = null;
        initialise();
    }

    @Override
	protected void initialise() {
		CSH.setHelpIDString(
				    this,
				    "net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.DisabledConfigView");
		configuration = activity.getConfiguration();
		XStream xstream = new XStream(new DomDriver());
		Activity a = configuration.getActivity();
		xstream.setClassLoader(a.getClass().getClassLoader());
		Object origConfig = configuration.getBean();
		if (fieldNames == null) {
		    fieldNames = getFieldNames(origConfig);
		}
		origConfigXML = xstream.toXML(origConfig);
		clonedConfig  = xstream.fromXML(origConfigXML);
		JPanel panel = UIBuilder.buildEditor(clonedConfig, (String[]) fieldNames.toArray(new String[0]));
		this.add(panel, BorderLayout.CENTER);
		this.revalidate();
	}

	@Override
	public void refreshConfiguration() {
	    this.removeAll();
	    initialise();
	}

	public boolean checkValues() {
	    boolean result = false;
		result = activity.configurationWouldWork(clonedConfig);
		if (!result) {
		    JOptionPane.showMessageDialog(
						  this,
						  "The new properties are invalid or not consistent with the workflow",
						  "Invalid properties", JOptionPane.WARNING_MESSAGE);
		}
	    return result;
	}

    public void noteConfiguration() {
	if (isConfigurationChanged()) {
	    ActivityAndBeanWrapper newConfig = new ActivityAndBeanWrapper();
	    newConfig.setActivity(configuration.getActivity());
	    newConfig.setBean(clonedConfig);
	    configuration = newConfig;

	    XStream xstream = new XStream(new DomDriver());
	    xstream.setClassLoader(configuration.getActivity().getClass().getClassLoader());

	    origConfigXML = xstream.toXML(clonedConfig);
	}
    }

    @Override
	public ActivityAndBeanWrapper getConfiguration() {
	return configuration;
    }

    public boolean isConfigurationChanged() {
	XStream xstream = new XStream(new DomDriver());
	xstream.setClassLoader(configuration.getActivity().getClass().getClassLoader());
	return (!xstream.toXML(clonedConfig).equals(origConfigXML));
    }

    private List<String> getFieldNames(Object config) {
	List<String> result = new ArrayList<String>();
	try {
	    BeanInfo beanInfo = Introspector.getBeanInfo(config.getClass());
	    for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
		Method readMethod = pd.getReadMethod();
		if ((readMethod != null) && !(pd.getName().equals("class"))) {
		    try {
			result.add(pd.getName());
		    } catch (IllegalArgumentException ex) {
			// ignore
		    }
		}
	    }
	} catch (IntrospectionException e) {
	    // ignore
	}
	return result;
    }
}
