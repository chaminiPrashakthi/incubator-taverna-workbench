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

package org.apache.taverna.lang.ui;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * @author alanrw
 *
 */
public class FileTools {
	
	private static Logger logger = Logger.getLogger(FileTools.class);
	
	

	public static boolean saveStringToFile(Component parent, String dialogTitle, String extension, String content) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle(dialogTitle);

		fileChooser.resetChoosableFileFilters();
		fileChooser.setAcceptAllFileFilterUsed(true);
		
		fileChooser.setFileFilter(new ExtensionFileFilter(new String[] { extension }));

		Preferences prefs = Preferences.userNodeForPackage(FileTools.class);
		String curDir = prefs
				.get("currentDir", System.getProperty("user.home"));
		fileChooser.setCurrentDirectory(new File(curDir));

		boolean tryAgain = true;
		while (tryAgain) {
			tryAgain = false;
			int returnVal = fileChooser.showSaveDialog(parent);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				prefs.put("currentDir", fileChooser.getCurrentDirectory()
						.toString());
				File file = fileChooser.getSelectedFile();
				if (!file.getName().contains(".")) {
					String newName = file.getName() + extension;
					file = new File(file.getParentFile(), newName);
				}

				// TODO: Open in separate thread to avoid hanging UI
				try {
					if (file.exists()) {
						logger.info("File already exists: " + file);
						String msg = "Are you sure you want to overwrite existing file "
								+ file + "?";
						int ret = JOptionPane.showConfirmDialog(
								parent, msg, "File already exists",
								JOptionPane.YES_NO_CANCEL_OPTION);
						if (ret == JOptionPane.YES_OPTION) {
							
						} else if (ret == JOptionPane.NO_OPTION) {
							tryAgain = true;
							continue;
						} else {
							logger.info("Aborted overwrite of " + file);
							return false;
						}
					}
					FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8.name());
					logger.info("Saved content by overwriting " + file);
					return true;
				} catch (IOException ex) {
					logger.warn("Could not save content to " + file, ex);
					JOptionPane.showMessageDialog(parent,
							"Could not save to " + file + ": \n\n"
									+ ex.getMessage(), "Warning",
							JOptionPane.WARNING_MESSAGE);
					return false;
				}
			}
		}
		return false;
	}
	
    public static String readStringFromFile(Component parent, String dialogTitle, String extension) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle(dialogTitle);
		fileChooser.resetChoosableFileFilters();
		fileChooser.setAcceptAllFileFilterUsed(true);
		
		fileChooser.setFileFilter(new ExtensionFileFilter(new String[] { extension }));
		
		Preferences prefs = Preferences.userNodeForPackage(FileTools.class);
		String curDir = prefs
				.get("currentDir", System.getProperty("user.home"));
		fileChooser.setCurrentDirectory(new File(curDir));

		if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			
			try {
				return FileUtils.readFileToString(selectedFile, StandardCharsets.UTF_8.name());
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(parent, "Can not read file '"
						+ selectedFile.getName() + "'", "Can not read file",
						JOptionPane.ERROR_MESSAGE);
			}

		}
		return null;
	}
}
