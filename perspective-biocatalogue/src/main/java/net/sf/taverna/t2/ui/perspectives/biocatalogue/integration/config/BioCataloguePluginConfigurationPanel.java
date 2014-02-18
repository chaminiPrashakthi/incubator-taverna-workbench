package net.sf.taverna.t2.ui.perspectives.biocatalogue.integration.config;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProxySelector;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.sf.taverna.raven.appconfig.ApplicationRuntime;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
//import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;

/**
 * 
 * @author Sergejs Aleksejevs
 */
@SuppressWarnings("serial")
public class BioCataloguePluginConfigurationPanel extends JPanel {
	public static final String APPLICATION_XML_MIME_TYPE = "application/xml";

	public static String PROXY_HOST = "http.proxyHost";
	public static String PROXY_PORT = "http.proxyPort";
	public static String PROXY_USERNAME = "http.proxyUser";
	public static String PROXY_PASSWORD = "http.proxyPassword";

	// 1.0.0b and higher until the first digit changes, as according to
	// "Semantic Versioning"
	// from http://www.biocatalogue.org/wiki/doku.php?id=public:api:changelog
	// "Major version X (X.y.z | X > 0) MUST be incremented if any backwards
	// incompatible changes are introduced to the public API. It MAY include
	// minor and patch level changes."
	public static String[] MIN_SUPPORTED_BIOCATALOGUE_API_VERSION = { "1", "1",
			"0" }; // major, minor and patch versions
	public static String API_VERSION = "apiVersion";

	private static final String DEFAULT_SERVICE_CATALOGUE_TYPE = "DEFAULT";
	private static final String USER_ADDED_SERVICE_CATALOGUE_TYPE = "USER_ADDED";

	private static final Charset ENCODING = StandardCharsets.UTF_8;

	private static final String CONF = "conf";

	private static final String USER_ADDED_SERVICE_CATALOGUES_FILE = "user-added-service-catalogues.txt";

	// Default available service catalogues that come predefined with Taverna.
	private List<ServiceCatalogue> defaultCatalogues = new ArrayList<ServiceCatalogue>();
	{
		defaultCatalogues.add(new ServiceCatalogue("BioCatalogue",
				"https://www.biocatalogue.org", DEFAULT_SERVICE_CATALOGUE_TYPE));
		defaultCatalogues.add(new ServiceCatalogue("BiodiversityCatalogue",
				"https://www.biodiversitycatalogue.org",
				DEFAULT_SERVICE_CATALOGUE_TYPE));
	};

	// User-added catalogues
	private List<ServiceCatalogue> userAddedCatalogues;

	// All available catalogues - union of default and user-added.
	private Vector<ServiceCatalogue> allServiceCatalogues;

	private ServiceCatalogueComboBoxModel serviceCatalogueModel;

	private BioCataloguePluginConfiguration configuration = BioCataloguePluginConfiguration
			.getInstance();

	// UI elements
	JComboBox<ServiceCatalogue> cbBioCatalogueAPIBaseURL;

	private Logger logger = Logger
			.getLogger(BioCataloguePluginConfigurationPanel.class);

	private JButton bRemoveServiceCatalogue;


	public BioCataloguePluginConfigurationPanel() {
		initialiseUI();
		resetFields();
	}

	private void initialiseUI() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 1.0;

		c.gridx = 0;
		c.gridy = 0;
		JTextArea taDescription = new JTextArea(
				"Configure the Service Catalogue integration functionality");
		taDescription.setFont(taDescription.getFont()
				.deriveFont(Font.PLAIN, 11));
		taDescription.setLineWrap(true);
		taDescription.setWrapStyleWord(true);
		taDescription.setEditable(false);
		taDescription.setFocusable(false);
		taDescription
				.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(taDescription, c);

		c.gridy++;
		c.insets = new Insets(20, 0, 0, 0);
		JLabel jlBioCatalogueAPIBaseURL = new JLabel(
				"Base URL of the Service Catalogue instance to connect to:");
		this.add(jlBioCatalogueAPIBaseURL, c);

		c.gridy++;
		c.insets = new Insets(0, 0, 0, 0);

		userAddedCatalogues = getUserAddedServiceCatalogues();
		allServiceCatalogues = new Vector<ServiceCatalogue>();
		allServiceCatalogues.addAll(defaultCatalogues);
		allServiceCatalogues.addAll(userAddedCatalogues);
		serviceCatalogueModel = new ServiceCatalogueComboBoxModel(
				allServiceCatalogues);
		cbBioCatalogueAPIBaseURL = new JComboBox<ServiceCatalogue>(
				serviceCatalogueModel);
		cbBioCatalogueAPIBaseURL.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {	
				@SuppressWarnings("unchecked")
				JComboBox<ServiceCatalogue> combo = (JComboBox<ServiceCatalogue>)e.getSource();
				ServiceCatalogue current = (ServiceCatalogue)combo.getSelectedItem();
				if (current.getType().equals(USER_ADDED_SERVICE_CATALOGUE_TYPE)){
					bRemoveServiceCatalogue.setEnabled(true);
				}
				else {
					bRemoveServiceCatalogue.setEnabled(false);
				}
			}
		});
		this.add(cbBioCatalogueAPIBaseURL, c);
		
		c.gridy++;
		c.insets = new Insets(0, 0, 0, 0);
		c.weighty = 0.0;
		JPanel jpAddNewServiceCatalogue = new JPanel();
		JButton bAddNewServiceCatalogue = new JButton("Add Service Catalogue");
		bAddNewServiceCatalogue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addServiceCatalogue();
			}
		});
		bRemoveServiceCatalogue = new JButton("Remove Service Catalogue");
		bRemoveServiceCatalogue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeServiceCatalogue();
			}
		});
		jpAddNewServiceCatalogue.add(bAddNewServiceCatalogue);
		jpAddNewServiceCatalogue.add(bRemoveServiceCatalogue);
		this.add(jpAddNewServiceCatalogue, c);

		c.gridy++;
		c.insets = new Insets(30, 0, 0, 0);
		// We are not removing BioCatalogue services from its config panel any
		// more -
		// they are being handled by the Taverna's Service Registry
		// JButton bForgetStoredServices = new
		// JButton("Forget services added to Service Panel by BioCatalogue Plugin");
		// bForgetStoredServices.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e)
		// {
		// int response = JOptionPane.showConfirmDialog(null, // no way
		// T2ConfigurationFrame instance can be obtained to be used as a
		// parent...
		// "Are you sure you want to clear all SOAP operations and REST methods\n"
		// +
		// "that were added to the Service Panel by the BioCatalogue Plugin?\n\n"
		// +
		// "This action is permanent is cannot be undone.\n\n" +
		// "Do you want to proceed?", "BioCatalogue Plugin",
		// JOptionPane.YES_NO_OPTION);
		//
		// if (response == JOptionPane.YES_OPTION)
		// {
		// BioCatalogueServiceProvider.clearRegisteredServices();
		// JOptionPane.showMessageDialog(null, // no way T2ConfigurationFrame
		// instance can be obtained to be used as a parent...
		// "Stored services have been successfully cleared, but will remain\n" +
		// "being shown in Service Panel during this session.\n\n" +
		// "They will not appear in the Service Panel after you restart Taverna.",
		// "BioCatalogue Plugin", JOptionPane.INFORMATION_MESSAGE);
		// }
		// }
		// });
		// this.add(bForgetStoredServices, c);

		JButton bLoadDefaults = new JButton("Load Defaults");
		bLoadDefaults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadDefaults();
			}
		});

		JButton bReset = new JButton("Reset");
		bReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetFields();
			}
		});

		JButton bApply = new JButton("Apply");
		bApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyChanges();
			}
		});

		JPanel jpActionButtons = new JPanel();
		jpActionButtons.add(bLoadDefaults);
		jpActionButtons.add(bReset);
		jpActionButtons.add(bApply);
		c.insets = new Insets(30, 0, 0, 0);
		c.gridy++;
		c.weighty = 1.0;
		this.add(jpActionButtons, c);
	}

	private void addServiceCatalogue() {
		AddServiceCatalogueDialog addServiceCatalogueDialog = new AddServiceCatalogueDialog();
		addServiceCatalogueDialog.setLocationRelativeTo(null);
		addServiceCatalogueDialog.setVisible(true);

		String catalogueName = addServiceCatalogueDialog.getCatalogueName();
		String catalogueURL = addServiceCatalogueDialog.getCatalogueURL();
		
		if (catalogueName != null && catalogueURL != null){
			ServiceCatalogue sc = new ServiceCatalogue(catalogueName, catalogueURL, USER_ADDED_SERVICE_CATALOGUE_TYPE);
			userAddedCatalogues.add(sc);
			serviceCatalogueModel.addElement(sc);
			saveUserAddedServiceCatalogues(userAddedCatalogues);	
		}
	}
	
	private void removeServiceCatalogue() {		
		ServiceCatalogue serviceCatalogueToRemove = (ServiceCatalogue) serviceCatalogueModel.getSelectedItem();
		userAddedCatalogues.remove(serviceCatalogueToRemove);
		saveUserAddedServiceCatalogues(userAddedCatalogues);		
		serviceCatalogueModel.removeElement(serviceCatalogueToRemove);
	}

	/**
	 * Resets all fields to the last saved configuration.
	 */
	private void resetFields() {
		int index = serviceCatalogueModel
				.getIndexOfURL(configuration
						.getProperty(BioCataloguePluginConfiguration.SERVICE_CATALOGUE_BASE_URL));
		cbBioCatalogueAPIBaseURL.setSelectedIndex(index);
	}

	/**
	 * Resets all fields to the default values.
	 */
	private void loadDefaults() {
		int index = serviceCatalogueModel
				.getIndexOfURL(configuration
						.getProperty(BioCataloguePluginConfiguration.SERVICE_CATALOGUE_BASE_URL));
		cbBioCatalogueAPIBaseURL.setSelectedIndex(index);
	}

	/**
	 * Saves recent changes to the configuration parameter map. Some input
	 * validation is performed as well.
	 */
	private void applyChanges() {
		// --- BioCatalogue BASE URL ---

		String candidateBaseURL = ((ServiceCatalogue) cbBioCatalogueAPIBaseURL
				.getSelectedItem()).getUrl();
		if (candidateBaseURL.length() == 0) {
			JOptionPane.showMessageDialog(this,
					"Service Catalogue base URL must not be blank",
					"Service Catalogue Configuration",
					JOptionPane.WARNING_MESSAGE);
			cbBioCatalogueAPIBaseURL.requestFocusInWindow();
			return;
		} else {
			try {
				new URL(candidateBaseURL);
			} catch (MalformedURLException e) {
				JOptionPane.showMessageDialog(this,
						"Currently set Service Catalogue instance URL is not valid.\n"
								+ "Please check the URL and try again.",
						"Service Catalogue Configuration",
						JOptionPane.WARNING_MESSAGE);
				cbBioCatalogueAPIBaseURL.requestFocusInWindow();
				return;
			}

			// check if the base URL has changed from the last saved state
			if (!candidateBaseURL
					.equals(configuration
							.getProperty(BioCataloguePluginConfiguration.SERVICE_CATALOGUE_BASE_URL))) {
				// Perform various checks on the new URL

				// Do a GET with "Accept" header set to "application/xml"
				// We are expecting a 200 OK and an XML doc in return that
				// contains the BioCataogue version number element.
				DefaultHttpClient httpClient = new DefaultHttpClient();

				// Set the proxy settings, if any
				if (System.getProperty(PROXY_HOST) != null
						&& !System.getProperty(PROXY_HOST).equals("")) {
					// Instruct HttpClient to use the standard
					// JRE proxy selector to obtain proxy information
					ProxySelectorRoutePlanner routePlanner = new ProxySelectorRoutePlanner(
							httpClient.getConnectionManager()
									.getSchemeRegistry(),
							ProxySelector.getDefault());
					httpClient.setRoutePlanner(routePlanner);
					// Do we need to authenticate the user to the proxy?
					if (System.getProperty(PROXY_USERNAME) != null
							&& !System.getProperty(PROXY_USERNAME).equals("")) {
						// Add the proxy username and password to the list of
						// credentials
						httpClient.getCredentialsProvider().setCredentials(
								new AuthScope(System.getProperty(PROXY_HOST),
										Integer.parseInt(System
												.getProperty(PROXY_PORT))),
								new UsernamePasswordCredentials(System
										.getProperty(PROXY_USERNAME), System
										.getProperty(PROXY_PASSWORD)));
					}
				}

				HttpGet httpGet = new HttpGet(candidateBaseURL);
				httpGet.setHeader("Accept", APPLICATION_XML_MIME_TYPE);

				// Execute the request
				HttpContext localContext = new BasicHttpContext();
				HttpResponse httpResponse;
				try {
					httpResponse = httpClient.execute(httpGet, localContext);
				} catch (Exception ex1) {
					logger.error(
							"Service Catalogue preferences configuration: Failed to do "
									+ httpGet.getRequestLine(), ex1);
					// Warn the user
					JOptionPane.showMessageDialog(this,
							"Failed to connect to the URL of the Service Catalogue instance.\n"
									+ "Please check the URL and try again.",
							"Service Catalogue Configuration",
							JOptionPane.INFORMATION_MESSAGE);

					// Release resource
					httpClient.getConnectionManager().shutdown();

					cbBioCatalogueAPIBaseURL.requestFocusInWindow();
					return;
				}

				if (httpResponse.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) { // HTTP/1.1
																									// 200
																									// OK
					HttpEntity httpEntity = httpResponse.getEntity();
					String contentType = httpEntity.getContentType().getValue()
							.toLowerCase().trim();
					logger.info("Service Catalogue preferences configuration: Got 200 OK when testing the Service Catalogue instance by doing "
							+ httpResponse.getStatusLine()
							+ ". Content type of response " + contentType);
					if (contentType.startsWith(APPLICATION_XML_MIME_TYPE)) {
						String value = null;
						Document doc = null;
						try {
							value = readResponseBodyAsString(httpEntity).trim();
							// Try to read this string into an XML document
							SAXBuilder builder = new SAXBuilder();
							byte[] bytes = value.getBytes("UTF-8");
							doc = builder
									.build(new ByteArrayInputStream(bytes));
						} catch (Exception ex2) {
							logger.error(
									"Service Catalogue preferences configuration: Failed to build an XML document from the response.",
									ex2);
							// Warn the user
							JOptionPane
									.showMessageDialog(
											this,
											"Failed to get the expected response body when testing the Service Catalogue instance.\n"
													+ "The URL is probably wrong. Please check it and try again.",
											"Service Catalogue Configuration",
											JOptionPane.INFORMATION_MESSAGE);
							cbBioCatalogueAPIBaseURL.requestFocusInWindow();
							return;
						} finally {
							// Release resource
							httpClient.getConnectionManager().shutdown();
						}
						// Get the version element from the XML document
						Attribute apiVersionAttribute = doc.getRootElement()
								.getAttribute(API_VERSION);
						if (apiVersionAttribute != null) {
							String apiVersion = apiVersionAttribute.getValue();
							String versions[] = apiVersion.split("[.]");
							String majorVersion = versions[0];
							String minorVersion = versions[1];
							try {
								// String patchVersion = versions[2]; // we are
								// not comparing the patch versions
								String supportedMajorVersion = MIN_SUPPORTED_BIOCATALOGUE_API_VERSION[0];
								String supportedMinorVersion = MIN_SUPPORTED_BIOCATALOGUE_API_VERSION[1];
								Integer iSupportedMajorVersion = Integer
										.parseInt(supportedMajorVersion);
								Integer iMajorVersion = Integer
										.parseInt(majorVersion);
								Integer iSupportedMinorVersion = Integer
										.parseInt(supportedMinorVersion);
								Integer iMinorVersion = Integer
										.parseInt(minorVersion);
								if (!(iSupportedMajorVersion == iMajorVersion && iSupportedMinorVersion <= iMinorVersion)) {
									// Warn the user
									JOptionPane
											.showMessageDialog(
													this,
													"The version of the Service Catalogue instance you are trying to connect to is not supported.\n"
															+ "Please change the URL and try again.",
													"Service Catalogue Configuration",
													JOptionPane.INFORMATION_MESSAGE);
									cbBioCatalogueAPIBaseURL
											.requestFocusInWindow();
									return;
								}
							} catch (Exception e) {
								logger.error(e);
							}
						} // if null - we'll try to do our best to connect to
							// BioCatalogue anyway
					} else {
						logger.error("Service Catalogue preferences configuration: Failed to get the expected response content type when testing the Service Catalogue instance. "
								+ httpGet.getRequestLine()
								+ " returned content type '"
								+ contentType
								+ "'; expected response content type is 'application/xml'.");
						// Warn the user
						JOptionPane
								.showMessageDialog(
										this,
										"Failed to get the expected response content type when testing the Service Catalogue instance.\n"
												+ "The URL is probably wrong. Please check it and try again.",
										"Service Catalogue Plugin",
										JOptionPane.INFORMATION_MESSAGE);
						cbBioCatalogueAPIBaseURL.requestFocusInWindow();
						return;
					}
				} else {
					logger.error("Service Catalogue preferences configuration: Failed to get the expected response status code when testing the Service Catalogue instance. "
							+ httpGet.getRequestLine()
							+ " returned the status code "
							+ httpResponse.getStatusLine().getStatusCode()
							+ "; expected status code is 200 OK.");
					// Warn the user
					JOptionPane
							.showMessageDialog(
									this,
									"Failed to get the expected response status code when testing the Service Catalogue instance.\n"
											+ "The URL is probably wrong. Please check it and try again.",
									"Service Catalogue Configuration",
									JOptionPane.INFORMATION_MESSAGE);
					cbBioCatalogueAPIBaseURL.requestFocusInWindow();
					return;
				}

				// Warn the user of the changes in the BioCatalogue base URL
				JOptionPane
						.showMessageDialog(
								this,
								"You have updated the Service Catalogue base URL.\n"
										+ "This does not take effect until you restart Taverna.",
								"Service catalogue Configuration",
								JOptionPane.INFORMATION_MESSAGE);

			}

			// the new base URL seems to be valid - can save it into config
			// settings
			configuration.setProperty(
					BioCataloguePluginConfiguration.SERVICE_CATALOGUE_BASE_URL,
					candidateBaseURL);

			/*
			 * // also update the base URL in the BioCatalogueClient
			 * BioCatalogueClient.getInstance() .setBaseURL(candidateBaseURL);
			 */
		}

	}

	/**
	 * For testing only.
	 */
	public static void main(String[] args) {
		JFrame theFrame = new JFrame();
		theFrame.add(new BioCataloguePluginConfigurationPanel());
		theFrame.pack();
		theFrame.setLocationRelativeTo(null);
		theFrame.setVisible(true);
	}

	/**
	 * Worker method that extracts the content of the received HTTP message as a
	 * string. It also makes use of the charset that is specified in the
	 * Content-Type header of the received data to read it appropriately.
	 * 
	 * @param entity
	 * @return
	 * @throws IOException
	 */
	// Taken from HTTPRequestHandler in rest-activity by Sergejs Aleksejevs
	private static String readResponseBodyAsString(HttpEntity entity)
			throws IOException {
		// get charset name
		String charset = null;
		String contentType = entity.getContentType().getValue().toLowerCase();

		String[] contentTypeParts = contentType.split(";");
		for (String contentTypePart : contentTypeParts) {
			contentTypePart = contentTypePart.trim();
			if (contentTypePart.startsWith("charset=")) {
				charset = contentTypePart.substring("charset=".length());
			}
		}

		// read the data line by line
		StringBuilder responseBodyString = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				entity.getContent(), charset));

		String str;
		while ((str = reader.readLine()) != null) {
			responseBodyString.append(str + "\n");
		}

		return (responseBodyString.toString());
	}
	

	/*
	 *  Read a list of users added catalogue entries from a specially formatted file.
	 *  Each entry is on a separate line in the format:
	 *  <SERVICE_CATALOGUE_FRIENDLY_NAME>\t<SERVICE_CATALOGUE_URL> (i.e. tab separated).
	 */
	private List<ServiceCatalogue> getUserAddedServiceCatalogues() {

		List<ServiceCatalogue> catalogues = new ArrayList<ServiceCatalogue>();

		File confDir = new File(ApplicationRuntime.getInstance()
				.getApplicationHomeDir(), CONF);
		if (!confDir.exists()) {
			confDir.mkdir();
		}
		File cataloguesFile = new File(confDir,
				USER_ADDED_SERVICE_CATALOGUES_FILE);
		Path path = cataloguesFile.toPath();

		List<String> list = new ArrayList<String>();

		if (cataloguesFile.exists()) {
			try {
				list = Files.readAllLines(path, ENCODING);
			} catch (IOException ioex) {
				logger.error(
						"Failed to read user-defined service catalogues from: "
								+ cataloguesFile.getAbsolutePath(), ioex);
				JOptionPane.showMessageDialog(this,
						"Failed to read user-defined service catalogues from:\n"
								+ path.toString() + "\n"
								+ "Please check the file above.",
						"Service Catalogue Configuration",
						JOptionPane.ERROR_MESSAGE);
			}
			for (String catalogueEntry : list) {
				// Split the friendly name and URL our of the catalogue entry
				// line read from the file
				String[] parts = catalogueEntry.split("\\t");
				if (parts[0] != null && parts[1] != null){
					ServiceCatalogue sc = new ServiceCatalogue(parts[0], parts[1],
						USER_ADDED_SERVICE_CATALOGUE_TYPE);
					catalogues.add(sc);
				}
			}
		}
		return catalogues;
	}
	
	/*
	 *  Write user-added service catalogues to a file.
	 *  Each entry is on a separate line in the format: 
	 *  <SERVICE_CATALOGUE_FRIENDLY_NAME>\t<SERVICE_CATALOGUE_URL> (i.e. tab separated).
	 */
	private void saveUserAddedServiceCatalogues(List<ServiceCatalogue> catalogues){
		
		File confDir = new File(ApplicationRuntime.getInstance()
				.getApplicationHomeDir(), CONF);
		if (!confDir.exists()){
			confDir.mkdir();
		}
		File cataloguesFile = new File(confDir, USER_ADDED_SERVICE_CATALOGUES_FILE);
	    Path path = cataloguesFile.toPath();
		
		List<String> list = new ArrayList<String>();
		for (ServiceCatalogue catalogue : catalogues){
			list.add(catalogue.getFriendlyName() + "\t" + catalogue.getUrl());
		}
		
	    try {
			Files.write(cataloguesFile.toPath(), list, ENCODING, StandardOpenOption. CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException ioex) {
	    	logger.error("Failed to save user-defined service catalogues to: " + cataloguesFile.getAbsolutePath(), ioex);
			JOptionPane.showMessageDialog(this,
					"Failed to save user-defined service catalogues to:\n"
							+ path.toString(),
					"Service Catalogue Configuration",
					JOptionPane.ERROR_MESSAGE);
		}		
	}

	/*
	 * A Service Catalogue entry. Has a friendly name, a URL and an indicator if
	 * it is user-added or comes with Taverna.
	 */
	public static class ServiceCatalogue {

		private String friendlyName;
		private String url;
		private String type;

		ServiceCatalogue(String friendlyName, String url, String type) {
			this.setFriendlyName(friendlyName);
			this.setUrl(url);
			this.setType(type);
		}

		public String getFriendlyName() {
			return friendlyName;
		}

		public void setFriendlyName(String friendlyName) {
			this.friendlyName = friendlyName;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		private String getType() {
			return type;
		}

		private void setType(String type) {
			this.type = type;
		}
			
		@Override
		public String toString(){
			return getFriendlyName();
		}
	}

	private class ServiceCatalogueComboBoxModel extends
			DefaultComboBoxModel<ServiceCatalogue> {

		public ServiceCatalogueComboBoxModel(Vector<ServiceCatalogue> catalogues) {
			super(catalogues);
		}

		// Finds the index of a ServiceCatalogue that contains a given URL
		public int getIndexOfURL(String URL) {
			for (int i = 0; i < this.getSize(); i++) {
				if (this.getElementAt(i).getUrl().equals(URL)) {
					return i;
				}
			}
			return -1;
		}

	}

}
