
/*******************************************************************************
 * Copyright (C) 2009 The University of Manchester   
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
package org.apache.taverna.workbench.views.results.processor;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;
import static org.apache.taverna.workbench.MainWindow.getMainWindow;
import static org.apache.taverna.workbench.icons.WorkbenchIcons.workingIcon;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.taverna.workbench.helper.HelpEnabledDialog;

@SuppressWarnings("serial")
public class IntermediateValuesInProgressDialog extends HelpEnabledDialog {
	/**
	 * Cancellation does not work; disable the button for it.
	 */
	private static final boolean CANCELLATION_ENABLED = false;

	private boolean userCancelled = false;

	public IntermediateValuesInProgressDialog() {
		super(getMainWindow(), "Fetching intermediate values", true, null);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel textPanel = new JPanel();
		JLabel text = new JLabel(workingIcon);
		text.setText("Fetching intermediate values...");
		text.setBorder(new EmptyBorder(10, 0, 10, 0));
		textPanel.add(text);
		panel.add(textPanel, CENTER);

		// Cancel button
		if (CANCELLATION_ENABLED) {
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					userCancelled = true;
					setVisible(false);
					dispose();
				}
			});
			JPanel cancelButtonPanel = new JPanel();
			cancelButtonPanel.add(cancelButton);
			panel.add(cancelButtonPanel, SOUTH);
		}
		setContentPane(panel);
		setPreferredSize(new Dimension(300, 100));

		pack();		
	}

	public boolean hasUserCancelled() {
		return userCancelled;
	}
}
