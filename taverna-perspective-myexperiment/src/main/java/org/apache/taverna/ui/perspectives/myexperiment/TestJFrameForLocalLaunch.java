/*******************************************************************************
 * Copyright (C) 2009 The University of Manchester
 *
 * Modifications to the initial code base are copyright of their respective
 * authors, or their employers as appropriate.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307
 ******************************************************************************/
package org.apache.taverna.ui.perspectives.myexperiment;

import java.awt.Dimension;
import javax.swing.JFrame;

public class TestJFrameForLocalLaunch {

	/**
	 * This is a simple test class for launching myExperiment perspective
	 * from outside Taverna. At some point it will be not usable anymore,
	 * when proper integration of myExperiment plugin is made.
	 *
	 * @author Sergejs Aleksejevs
	 */
	public static void main(String[] args)
	{
	  JFrame frame = new JFrame("myExperiment Perspective Test");
	  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	  frame.setMinimumSize(new Dimension(1000, 700));
	  frame.setLocation(300, 150);
	  frame.getContentPane().add(new org.apache.taverna.ui.perspectives.myexperiment.MainComponent(null, null));

	  frame.pack();
	  frame.setVisible(true);
	}

}
