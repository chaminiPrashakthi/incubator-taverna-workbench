/**********************************************************************
 * Copyright (C) 2007-2009 The University of Manchester
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
 **********************************************************************/
package org.apache.taverna.ui.menu.items.controllink;

import java.net.URI;

import javax.swing.Action;

import org.apache.taverna.scufl2.api.core.BlockingControlLink;

import org.apache.taverna.ui.menu.AbstractMenuSection;
import org.apache.taverna.ui.menu.ContextualMenuComponent;
import org.apache.taverna.ui.menu.ContextualSelection;
import org.apache.taverna.ui.menu.DefaultContextualMenu;

public class ConditionSection extends AbstractMenuSection implements
		ContextualMenuComponent {

	private static final String CONTROL_LINK = "Control link: ";
	public static final URI conditionSection = URI
			.create("http://taverna.sf.net/2009/contextMenu/condition");
	private ContextualSelection contextualSelection;

	public ConditionSection() {
		super(DefaultContextualMenu.DEFAULT_CONTEXT_MENU, 10, conditionSection);
	}

	public ContextualSelection getContextualSelection() {
		return contextualSelection;
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled()
				&& getContextualSelection().getSelection() instanceof BlockingControlLink;
	}

	public void setContextualSelection(ContextualSelection contextualSelection) {
		this.contextualSelection = contextualSelection;
		this.action = null;
	}

	@Override
	protected Action createAction() {
		BlockingControlLink controllink = (BlockingControlLink) getContextualSelection()
				.getSelection();
		String name = CONTROL_LINK + controllink.getBlock().getName()
				+ " RUNS_AFTER " + controllink.getUntilFinished().getName();
		return new DummyAction(name);
	}

}
