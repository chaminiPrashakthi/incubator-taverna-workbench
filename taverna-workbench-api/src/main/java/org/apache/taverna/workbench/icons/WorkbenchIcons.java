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

package org.apache.taverna.workbench.icons;

import javax.swing.ImageIcon;

/**
 * A container for common icons used by the workbench
 *
 * @author David Withers
 * @author Stian Soiland-Reyes
 */
public class WorkbenchIcons {

	public static ImageIcon resultsPerspectiveIcon;
	public static ImageIcon closeIcon;
	public static ImageIcon closeAllIcon;
	public static ImageIcon deleteIcon;
	public static ImageIcon trashIcon; // not in use - using delete icon instead
	public static ImageIcon zoomIcon;
	public static ImageIcon zoomInIcon;
	public static ImageIcon zoomOutIcon;
	public static ImageIcon webIcon;
	public static ImageIcon openIcon;
	public static ImageIcon runIcon;
	public static ImageIcon refreshIcon;
	public static ImageIcon editIcon;
	public static ImageIcon findIcon;
	public static ImageIcon folderOpenIcon;
	public static ImageIcon folderClosedIcon;
	public static ImageIcon newInputIcon;
	public static ImageIcon newIcon;
	public static ImageIcon newListIcon;
	public static ImageIcon inputValueIcon;
	public static ImageIcon xmlNodeIcon;
	public static ImageIcon saveIcon;
	public static ImageIcon saveAllIcon;
	public static ImageIcon saveAsIcon;
	public static ImageIcon leafIcon;
	public static ImageIcon saveMenuIcon;
	public static ImageIcon savePNGIcon;
	public static ImageIcon importIcon;
	public static ImageIcon importFileIcon;
	public static ImageIcon importUrlIcon;
	public static ImageIcon openurlIcon;
	public static ImageIcon openMenuIcon;
	public static ImageIcon pauseIcon;
	public static ImageIcon playIcon;
	public static ImageIcon stopIcon;
	public static ImageIcon breakIcon;
	public static ImageIcon rbreakIcon;
	public static ImageIcon tickIcon;
	public static ImageIcon untickIcon;
	public static ImageIcon greentickIcon;
	public static ImageIcon renameIcon;
	public static ImageIcon databaseIcon;
	public static ImageIcon nullIcon;
	public static ImageIcon uninstallIcon;
	public static ImageIcon workingIcon;
	public static ImageIcon workingStoppedIcon;
	public static ImageIcon updateRecommendedIcon;
	public static ImageIcon updateIcon;
	public static ImageIcon searchIcon;
	public static ImageIcon pasteIcon;
	public static ImageIcon copyIcon;
	public static ImageIcon cutIcon;
	public static ImageIcon datalinkIcon;
	public static ImageIcon controlLinkIcon;
	public static ImageIcon mergeIcon;
	public static ImageIcon inputIcon;
	public static ImageIcon inputPortIcon;
	public static ImageIcon outputIcon;
	public static ImageIcon outputPortIcon;
	public static ImageIcon verticalIcon;
	public static ImageIcon horizontalIcon;
	public static ImageIcon noportIcon;
	public static ImageIcon allportIcon;
	public static ImageIcon blobIcon;
	public static ImageIcon expandNestedIcon;
	public static ImageIcon workflowExplorerIcon;
	public static ImageIcon configureIcon;
	public static ImageIcon plusIcon;
	public static ImageIcon minusIcon;
	public static ImageIcon undoIcon;
	public static ImageIcon redoIcon;
	public static ImageIcon upArrowIcon;
	public static ImageIcon downArrowIcon;
	public static ImageIcon tavernaCogs64x64Icon;
	public static ImageIcon tavernaCogs32x32Icon;
	public static ImageIcon opmIcon;
	public static ImageIcon janusIcon;
	public static ImageIcon workflowResultsIcon;
	public static ImageIcon normalizeIcon;
	public static ImageIcon errorMessageIcon;
	public static ImageIcon infoMessageIcon;
	public static ImageIcon questionMessageIcon;
	public static ImageIcon warningMessageIcon;

	static {
		// Load the image files found in this package into the class.
		Class<?> c = WorkbenchIcons.class;

		resultsPerspectiveIcon = new ImageIcon(c.getResource("generic/results-perspective.png"));
		closeIcon = new ImageIcon(c.getResource("generic/close.gif"));
		closeAllIcon = new ImageIcon(c.getResource("generic/closeAll.gif"));
		deleteIcon = new ImageIcon(c.getResource("generic/bin.png"));
		trashIcon = new ImageIcon(c.getResource("graph/trash.png"));
		zoomIcon = new ImageIcon(c.getResource("generic/zoom.gif"));
		zoomInIcon = new ImageIcon(c.getResource("generic/zoomin.png"));
		zoomOutIcon = new ImageIcon(c.getResource("generic/zoomout.png"));
		webIcon = new ImageIcon(c.getResource("generic/web.gif"));
		openIcon = new ImageIcon(c.getResource("generic/open.gif"));
		runIcon = new ImageIcon(c.getResource("generic/run.gif"));
		refreshIcon = new ImageIcon(c.getResource("generic/refresh.gif"));
		editIcon = new ImageIcon(c.getResource("generic/edit.gif"));
		findIcon = new ImageIcon(c.getResource("generic/find.gif"));
		folderOpenIcon = new ImageIcon(c.getResource("generic/folder-open.png"));
		folderClosedIcon = new ImageIcon(c.getResource("generic/folder-closed.png"));
		newInputIcon = new ImageIcon(c.getResource("generic/newinput.gif"));
		newIcon = new ImageIcon(c.getResource("generic/newinput.gif"));
		newListIcon = new ImageIcon(c.getResource("generic/newlist.gif"));
		inputValueIcon = new ImageIcon(c.getResource("generic/inputValue.gif"));

		xmlNodeIcon = new ImageIcon(c.getResource("generic/xml_node.gif"));
		leafIcon = new ImageIcon(c.getResource("generic/leaf.gif"));
		saveIcon = new ImageIcon(c.getResource("generic/save.png"));
		saveAllIcon = new ImageIcon(c.getResource("generic/saveAll.png"));
		saveAsIcon = new ImageIcon(c.getResource("generic/saveAs.png"));
		saveMenuIcon = new ImageIcon(c.getResource("generic/savemenu.gif"));
		savePNGIcon = new ImageIcon(c.getResource("generic/savepng.gif"));
		importIcon = new ImageIcon(c.getResource("generic/import.gif"));
		importFileIcon = new ImageIcon(c.getResource("generic/fileimport.png"));
		importUrlIcon = new ImageIcon(c.getResource("generic/urlimport.png"));
		openurlIcon = new ImageIcon(c.getResource("generic/openurl.gif"));
		openIcon = new ImageIcon(c.getResource("generic/open.gif"));
		openMenuIcon = new ImageIcon(c.getResource("generic/openmenu.gif"));
		pauseIcon = new ImageIcon(c.getResource("generic/pause.png"));
		playIcon = new ImageIcon(c.getResource("generic/play.png"));
		stopIcon = new ImageIcon(c.getResource("generic/stop.gif"));
		breakIcon = new ImageIcon(c.getResource("generic/break.gif"));
		rbreakIcon = new ImageIcon(c.getResource("generic/rbreak.gif"));
		tickIcon = new ImageIcon(c.getResource("generic/tick.png"));
		untickIcon = new ImageIcon(c.getResource("generic/untick.png"));
		greentickIcon = new ImageIcon(c.getResource("generic/greentick.png"));
		renameIcon = new ImageIcon(c.getResource("generic/rename.png"));
		databaseIcon = new ImageIcon(c.getResource("generic/database.gif"));
		nullIcon = new ImageIcon(new java.awt.image.BufferedImage(1, 1,
				java.awt.image.BufferedImage.TYPE_INT_RGB));
		copyIcon = new ImageIcon(c.getResource("generic/copy.png"));
		cutIcon = new ImageIcon(c.getResource("generic/cut.png"));
		pasteIcon = new ImageIcon(c.getResource("generic/paste.png"));
		searchIcon = new ImageIcon(c.getResource("generic/search.png"));
		updateIcon = new ImageIcon(c.getResource("generic/update.png"));
		updateRecommendedIcon = new ImageIcon(c.getResource("generic/updateRecommended.png"));
		uninstallIcon = new ImageIcon(c.getResource("generic/uninstall.png"));
		workingIcon = new ImageIcon(c.getResource("generic/working.gif"));
		workingStoppedIcon = new ImageIcon(c.getResource("generic/workingStopped.png"));
		datalinkIcon = new ImageIcon(c.getResource("explorer/datalink.gif"));
		controlLinkIcon = new ImageIcon(c.getResource("explorer/constraint.gif"));
		mergeIcon = new ImageIcon(c.getResource("explorer/merge.png"));
		inputIcon = new ImageIcon(c.getResource("explorer/input.png"));
		inputPortIcon = new ImageIcon(c.getResource("explorer/inputport.png"));
		outputIcon = new ImageIcon(c.getResource("explorer/output.png"));
		outputPortIcon = new ImageIcon(c.getResource("explorer/outputport.png"));
		verticalIcon = new ImageIcon(c.getResource("graph/vertical.png"));
		horizontalIcon = new ImageIcon(c.getResource("graph/horizontal.png"));
		noportIcon = new ImageIcon(c.getResource("graph/noport.png"));
		allportIcon = new ImageIcon(c.getResource("graph/allport.png"));
		blobIcon = new ImageIcon(c.getResource("graph/blob.png"));
		expandNestedIcon = new ImageIcon(c.getResource("graph/expandnested.png"));
		workflowExplorerIcon = new ImageIcon(c.getResource("explorer/workflow-explorer.png"));
		configureIcon = new ImageIcon(c.getResource("generic/configure.png"));
		plusIcon = new ImageIcon(c.getResource("generic/plus.png"));
		minusIcon = new ImageIcon(c.getResource("generic/minus.png"));
		undoIcon = new ImageIcon(c.getResource("generic/undo.png"));
		redoIcon = new ImageIcon(c.getResource("generic/redo.png"));
		upArrowIcon = new ImageIcon(c.getResource("generic/up-arrow.png"));
		downArrowIcon = new ImageIcon(c.getResource("generic/down-arrow.png"));
		tavernaCogs64x64Icon = new ImageIcon(c.getResource("generic/taverna_cogs_64x64.png"));
		tavernaCogs32x32Icon = new ImageIcon(c.getResource("generic/taverna_cogs_32x32.png"));
		opmIcon = new ImageIcon(c.getResource("generic/opmIcon.png"));
		janusIcon = new ImageIcon(c.getResource("generic/janus.png"));
		workflowResultsIcon = new ImageIcon(c.getResource("generic/workflowResults.png"));
		normalizeIcon = new ImageIcon(c.getResource("generic/normalize.png"));

		errorMessageIcon = new ImageIcon(
				c.getResource("generic/taverna-wheel-message-dialog-error-icon.png"));
		infoMessageIcon = new ImageIcon(
				c.getResource("generic/taverna-wheel-message-dialog-info-icon.png"));
		questionMessageIcon = new ImageIcon(
				c.getResource("generic/taverna-wheel-message-dialog-question-icon.png"));
		warningMessageIcon = new ImageIcon(
				c.getResource("generic/taverna-wheel-message-dialog-warning-icon.png"));

	}
}
