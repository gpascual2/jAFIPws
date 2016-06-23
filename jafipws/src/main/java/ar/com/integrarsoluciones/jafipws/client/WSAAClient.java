package ar.com.integrarsoluciones.jafipws.client;

import org.apache.commons.codec.binary.Base64;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import wsaa.wsdl.LoginCms;
import wsaa.wsdl.LoginCmsResponse;

public class WSAAClient extends WebServiceGatewaySupport {

	public String callWSAA(byte [] cms, String serviceURL){
		
		LoginCms request = new LoginCms();
		request.setIn0(Base64.encodeBase64String(cms));

		LoginCmsResponse response = (LoginCmsResponse) getWebServiceTemplate()
				.marshalSendAndReceive(
						serviceURL,
						request,
						new SoapActionCallback(serviceURL));  
		
		return response.getLoginCmsReturn();
	}
	
}
