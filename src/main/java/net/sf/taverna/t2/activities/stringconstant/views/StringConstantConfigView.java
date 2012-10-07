/**
 *
 */
package net.sf.taverna.t2.activities.stringconstant.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.Collections;

import javax.help.CSH;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import uk.org.taverna.scufl2.api.activity.Activity;
import uk.org.taverna.scufl2.api.configurations.Configuration;
import uk.org.taverna.scufl2.api.property.PropertyException;

import net.sf.taverna.t2.activities.stringconstant.StringConstantActivity;
import net.sf.taverna.t2.activities.stringconstant.StringConstantConfigurationBean;
import net.sf.taverna.t2.activities.stringconstant.servicedescriptions.StringConstantTemplateService;
import net.sf.taverna.t2.lang.ui.FileTools;
import net.sf.taverna.t2.lang.ui.KeywordDocument;
import net.sf.taverna.t2.lang.ui.LineEnabledTextPanel;
import net.sf.taverna.t2.lang.ui.LinePainter;
import net.sf.taverna.t2.lang.ui.NoWrapEditorKit;
import net.sf.taverna.t2.visit.VisitReport;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationPanel;

/**
 * @author alanrw
 *
 */
public class StringConstantConfigView extends ActivityConfigurationPanel {

	/** The configuration bean used to configure the activity */
	private Configuration configuration;

	/** The text */
	private JEditorPane scriptTextArea;

	private static final Color LINE_COLOR = new Color(225,225,225);

	private final Activity activity;

	public StringConstantConfigView(Activity activity, Configuration configuration) {
		this.activity = activity;
		this.configuration = configuration;
		setLayout(new GridBagLayout());
		initialise();
		this.addAncestorListener(new AncestorListener() {

			@Override
			public void ancestorAdded(AncestorEvent event) {
				StringConstantConfigView.this.whenOpened();
			}

			@Override
			public void ancestorMoved(AncestorEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void ancestorRemoved(AncestorEvent event) {
				// TODO Auto-generated method stub

			}});
	}

    public void whenOpened() {
    	scriptTextArea.requestFocus();
    	if (scriptTextArea.getText().equals(StringConstantTemplateService.DEFAULT_VALUE)) {
    		scriptTextArea.selectAll();
    	}
    }

	private void initialise() {
		CSH
		.setHelpIDString(
				this,
				"net.sf.taverna.t2.activities.stringconstant.views.StringConstantConfigView");

		setBorder(javax.swing.BorderFactory.createTitledBorder(null, null,
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Lucida Grande", 1, 12)));

		JPanel scriptEditPanel = new JPanel(new BorderLayout());

		scriptTextArea = new JTextPane();
		new LinePainter(scriptTextArea, LINE_COLOR);

		// NOTE: Due to T2-1145 - always set editor kit BEFORE setDocument
		scriptTextArea.setEditorKit( new NoWrapEditorKit() );
		scriptTextArea.setFont(new Font("Monospaced",Font.PLAIN,14));
		scriptTextArea.setText(getValue(configuration));
		scriptTextArea.setCaretPosition(0);
		scriptTextArea.setPreferredSize(new Dimension(200, 100));

		scriptEditPanel.add(new LineEnabledTextPanel(scriptTextArea), BorderLayout.CENTER);

		GridBagConstraints outerConstraint = new GridBagConstraints();
		outerConstraint.anchor = GridBagConstraints.FIRST_LINE_START;
		outerConstraint.gridx = 0;
		outerConstraint.gridy = 0;

		outerConstraint.fill = GridBagConstraints.BOTH;
		outerConstraint.weighty = 0.1;
		outerConstraint.weightx = 0.1;
		add(scriptEditPanel, outerConstraint);

		JButton loadScriptButton = new JButton("Load text");
		loadScriptButton.setToolTipText("Load text from a file");
		loadScriptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    String newScript = FileTools.readStringFromFile(StringConstantConfigView.this, "Load text", ".txt");
				if (newScript != null) {
					scriptTextArea.setText(newScript);
					scriptTextArea.setCaretPosition(0);
				}
			}
		});

		JButton saveRScriptButton = new JButton("Save text");
		saveRScriptButton.setToolTipText("Save the text to a file");
		saveRScriptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileTools.saveStringToFile(StringConstantConfigView.this, "Save text", ".txt", scriptTextArea.getText());
			}
		});

		JButton clearScriptButton = new JButton("Clear text");
		clearScriptButton
				.setToolTipText("Clear current text from the edit area");
		clearScriptButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				cleaText();
			}

		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(loadScriptButton);
		buttonPanel.add(saveRScriptButton);
		buttonPanel.add(clearScriptButton);

		scriptEditPanel.add(buttonPanel, BorderLayout.SOUTH);
		setPreferredSize(new Dimension(600,500));
		this.validate();

	}

	/**
	 * Method for clearing the script
	 *
	 */
	private void cleaText() {
		if (JOptionPane.showConfirmDialog(this,
				"Do you really want to clear the text?",
				"Clearing the script", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			scriptTextArea.setText("");
		}

	}


	@Override
	public boolean checkValues() {
		return true;
	}

	@Override
	public Configuration getConfiguration() {
		return configuration;
	}

	@Override
	public boolean isConfigurationChanged() {
		return !scriptTextArea.getText().equals(getValue(configuration));
	}

	@Override
	public void noteConfiguration() {
		configuration = makeConfiguration();
	}

	private Configuration makeConfiguration() {
		URI activityURI = StringConstantTemplateService.ACTIVITY_TYPE;
		Configuration configuration = new Configuration();
		configuration.setConfigurableType(activityURI.resolve("#Config"));
		configuration.setConfigures(activity);
		configuration.getPropertyResource().addPropertyAsString(StringConstantTemplateService.ACTIVITY_TYPE.resolve("#string"), scriptTextArea.getText());
		return configuration;
	}

	private String getValue(Configuration configuration) {
		String value = null;
		try {
			value = configuration.getPropertyResource().getPropertyAsString(StringConstantTemplateService.ACTIVITY_TYPE.resolve("#string"));
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	@Override
	public void refreshConfiguration() {
		// TODO Auto-generated method stub

	}

}
