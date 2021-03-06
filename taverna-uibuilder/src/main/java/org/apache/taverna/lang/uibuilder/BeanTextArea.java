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

package org.apache.taverna.lang.uibuilder;

import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

import org.apache.taverna.lang.ui.DialogTextArea;

/**
 * Bean editor based on a DialogTextArea for use with longer strings such as
 * descriptions. Supports the 'nofilter' property, if this is not specified then
 * the text inserted initially (but not on subsequent events such as property
 * change messages) will be filtered to remove multiple whitespace elements,
 * replacing them with spaces, and to trim leading and trailing whitespace.
 * 
 * @author Tom Oinn
 * 
 */
public class BeanTextArea extends BeanTextComponent implements
		AlignableComponent {

	private static final long serialVersionUID = 6418526320837944375L;
	boolean initialized = false;

	public BeanTextArea(Object target, String propertyName, Properties props)
			throws NoSuchMethodException {
		super(target, propertyName, props);
		initialized = true;
	}

	@SuppressWarnings( { "serial", "unchecked" })
	@Override
	protected JTextComponent getTextComponent() {
		DialogTextArea result = new DialogTextArea() {
			@Override
			public void setText(String text) {
				if (!initialized && !getProperties().containsKey("nofilter")) {
					super.setText(text.replaceAll("[ \\t\\n\\x0B\\f\\r]+", " ")
							.trim());
				} else {
					super.setText(text);
				}
			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(0, super.getPreferredSize().height);
			}

			@Override
			public Dimension getMinimumSize() {
				return new Dimension(0, super.getPreferredSize().height);
			}
		};
		// Fix to add borders to DialogTextArea on old look and feel implementations,
		// the new one (Nimbus) already has this
		if (!UIManager.getLookAndFeel().getName().equals("Nimbus")) {
			result.setBorder(UIManager.getBorder("TextField.border"));
			result.setFont(UIManager.getFont("TextField.font"));
		}
		// Change tab behaviour to allow tab to move to the next field - this
		// effectively prevents a tab being placed in the text area but hey, we
		// don't really want people doing that anyway in these cases.
		Set set = new HashSet(
				result
						.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
		set.add(KeyStroke.getKeyStroke("TAB"));
		result.setFocusTraversalKeys(
				KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, set);

		set = new HashSet(
				result
						.getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
		set.add(KeyStroke.getKeyStroke("shift TAB"));
		result.setFocusTraversalKeys(
				KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, set);

		result.setLineWrap(true);
		result.setWrapStyleWord(true);
		return result;
	}

}
