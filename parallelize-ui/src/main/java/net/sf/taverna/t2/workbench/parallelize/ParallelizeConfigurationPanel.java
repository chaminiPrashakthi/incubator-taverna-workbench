package net.sf.taverna.t2.workbench.parallelize;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.sf.taverna.t2.workflowmodel.processor.dispatch.layers.ParallelizeConfig;

@SuppressWarnings("serial")
public class ParallelizeConfigurationPanel extends JPanel {

	private final ParallelizeConfig configuration;
	private JTextField maxJobsField = new JTextField();

	public ParallelizeConfigurationPanel(ParallelizeConfig configuration) {
		this.configuration = configuration;
		this.setLayout(new GridLayout(1,2));
		this.setBorder(new EmptyBorder(10,10,10,10));
		populate();
	}

	public void populate() {
		this.removeAll();
		JLabel jobs = new JLabel("Maximum number of jobs");
		jobs.setBorder(new EmptyBorder(0,0,0,10));
		this.add(jobs);
		maxJobsField.setText(Integer.toString(configuration.getMaximumJobs()));
		this.add(maxJobsField);
	}

	public boolean validateConfig() {
		String errorText = "";
		int maxJobs = -1;
		try {
			maxJobs = Integer.parseInt(maxJobsField.getText());
			if (maxJobs < 1) {
				errorText += "The maximum number of jobs must be a positive integer.\n";
			}
		}
		catch (NumberFormatException e) {
			errorText += "The maximum number of jobs must be an integer.\n";
		}

		if (errorText.length() > 0) {
			JOptionPane.showMessageDialog(this, errorText, "", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	public ParallelizeConfig getConfiguration() {
		ParallelizeConfig newConfig = new ParallelizeConfig();
		newConfig.setMaximumJobs(Integer.parseInt(maxJobsField.getText()));
		return newConfig;
	}

}