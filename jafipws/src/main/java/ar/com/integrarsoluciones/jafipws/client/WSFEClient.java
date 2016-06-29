/**
 * 
 */
package ar.com.integrarsoluciones.jafipws.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import ar.com.integrarsoluciones.jafipws.TRA;
import wsfe.wsdl.ArrayOfCbteTipo;
import wsfe.wsdl.CbteTipo;
import wsfe.wsdl.CbteTipoResponse;
import wsfe.wsdl.DummyResponse;
import wsfe.wsdl.FEAuthRequest;
import wsfe.wsdl.FEDummy;
import wsfe.wsdl.FEDummyResponse;
import wsfe.wsdl.FEParamGetTiposCbte;
import wsfe.wsdl.FEParamGetTiposCbteResponse;

/**
 * @author pascualg
 *
 */
@Component
public class WSFEClient extends WebServiceGatewaySupport {
	
	@Autowired
    private WebServiceTemplate webServiceTemplate;
	
	@Bean
	public SaajSoapMessageFactory messageFactory() {
	    SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
	    messageFactory.setSoapVersion(SoapVersion.SOAP_12);
	    return messageFactory;
	}
	
	public String dummy(String serviceURL){
		FEDummy request = new FEDummy();
		webServiceTemplate.setDefaultUri(serviceURL);  
		FEDummyResponse response = (FEDummyResponse) webServiceTemplate
				.marshalSendAndReceive(request);
		
		DummyResponse dummyResponse = response.getFEDummyResult();
		String result = "AppServer: " + dummyResponse.getAppServer() + " /n " +
				"DbServer: " + dummyResponse.getDbServer() + " /n " +
				"AuthServer: " + dummyResponse.getAuthServer();
		return result;
	}
	
	public String getParamTiposComprobante(TRA tra){
		String result = "";
		webServiceTemplate.setDefaultUri(tra.getTargetServiceUrl());
		FEAuthRequest auth = new FEAuthRequest();
		auth.setCuit(Long.parseLong(tra.getCuit()));
		auth.setToken(tra.getToken());
		auth.setSign(tra.getSign());
		
		FEParamGetTiposCbte request = new FEParamGetTiposCbte();
		request.setAuth(auth);
		
		FEParamGetTiposCbteResponse response = (FEParamGetTiposCbteResponse) webServiceTemplate
				.marshalSendAndReceive(request);
		
		CbteTipoResponse cbteTipoResponse = response.getFEParamGetTiposCbteResult();
		ArrayOfCbteTipo tiposCbte = cbteTipoResponse.getResultGet();
		
		for (CbteTipo cbte : tiposCbte.getCbteTipo())
		{
			result = result + Integer.toString(cbte.getId()) + "_:_" + cbte.getDesc() + "\n";
		}
		
		return result;
	}
	

}
