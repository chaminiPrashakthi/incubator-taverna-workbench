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
package org.apache.taverna.workbench.ui.activitypalette;

import javax.swing.JPanel;
import org.apache.taverna.configuration.Configurable;
import org.apache.taverna.configuration.ConfigurationUIFactory;

public class ActivityPaletteConfigurationUIFactory implements
		ConfigurationUIFactory {
	private ActivityPaletteConfiguration activityPaletteConfiguration;

	@Override
	public boolean canHandle(String uuid) {
		return uuid != null && uuid.equals(getConfigurable().getUUID());
	}

	@Override
	public Configurable getConfigurable() {
		return activityPaletteConfiguration;
	}

	@Override
	public JPanel getConfigurationPanel() {
		return new ActivityPaletteConfigurationPanel(
				activityPaletteConfiguration);
	}

	public void setActivityPaletteConfiguration(
			ActivityPaletteConfiguration activityPaletteConfiguration) {
		this.activityPaletteConfiguration = activityPaletteConfiguration;
	}
}
