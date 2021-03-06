package org.apache.taverna.ui.perspectives.biocatalogue.integration.health_check;

import org.apache.taverna.visit.VisitKind;
import org.apache.taverna.visit.Visitor;

/**
 * A {@link BioCatalogueWSDLActivityHealthCheck} is a kind of visit that determines
 * if the corresponding WSDL activity in a workflow will work during a workflow run -
 * checks will be made based on the monitoring status held about that service in BioCatalogue.
 * 
 * @author Sergejs Aleksejevs
 */
public class BioCatalogueWSDLActivityHealthCheck extends VisitKind
{
  // The following values indicate the type of results that can be associated
  // with a VisitReport generated by a health-checking visitor.
  public static final int MESSAGE_IN_VISIT_REPORT = 0;
  
  
  // property names to be placed into VisitReport generated by BioCatalogueWSDLActivityHealthChecker
  public static final String WSDL_LOCATION_PROPERTY = "wsdlLocation";
  public static final String OPERATION_NAME_PROPERTY = "soapOperationName";
  public static final String EXPLANATION_MSG_PROPERTY = "fullExplanationMessage";
  
  
  
  
  @Override
  public Class<? extends Visitor> getVisitorClass() {
    return BioCatalogueWSDLActivityHealthChecker.class;
  }
  
  private static class Singleton {
    private static BioCatalogueWSDLActivityHealthCheck instance = new BioCatalogueWSDLActivityHealthCheck();
  }
  
  public static BioCatalogueWSDLActivityHealthCheck getInstance() {
    return Singleton.instance;
  }
}
