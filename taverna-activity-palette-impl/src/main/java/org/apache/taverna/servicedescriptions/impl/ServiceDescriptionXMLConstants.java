package org.apache.taverna.servicedescriptions.impl;

import org.jdom.Namespace;

public interface ServiceDescriptionXMLConstants {

	public static final Namespace SERVICE_DESCRIPTION_NS = Namespace
			.getNamespace("http://taverna.sf.net/2009/xml/servicedescription");
	public static final String PROVIDER = "provider";
	public static final String PROVIDERS = "providers";
	public static final String SERVICE_DESCRIPTIONS = "serviceDescriptions";
	public static final String IGNORED_PROVIDERS = "ignoredProviders";
	public static final String PROVIDER_IDENTIFIER = "providerId";

}
