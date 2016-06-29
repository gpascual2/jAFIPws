/**
 * 
 */
package ar.com.integrarsoluciones.jafipws.client;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ar.com.integrarsoluciones.jafipws.TRA;
import wsfe.wsdl.FEAuthRequest;

/**
 * @author pascualg
 *
 */
public class WSFE {
	private WSFEClient client;
	private Logger logger;
	private TRA tra;
	
	//private String serviceUrl;
	//private String token;
	//private String sign;
	//private String cuit;

	/**
	 * Constructor
	 * @param TRA object
	 */
	public WSFE(TRA tra) {
		this.logger = Logger.getLogger("ar.com.integrarsoluciones.jafipws.client.WSFE");
		this.tra = tra;
		//this.token = tra.getToken();
		//this.sign = tra.getSign();
		//this.cuit = tra.getCuit();
		//this.serviceUrl = tra.getTargetServiceUrl();
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(WSFEConfiguration.class);
        this.client = ctx.getBean(WSFEClient.class);
		client.setDefaultUri(tra.getTargetServiceUrl());
		ctx.close();
	}
	
	
	/**
	 * Call dummy service to get server status
	 */
	public String callDummy(){
		String result = "";
		try
		{
			result = client.dummy(tra.getTargetServiceUrl());
		}
		catch (Exception e)
		{
			logger.error(e.toString());
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Call dummy service to get server status
	 */
	public String getParamTiposComprobante(){
		String result = "";
		try
		{
			result = client.getParamTiposComprobante(tra);
		}
		catch (Exception e)
		{
			logger.error(e.toString());
		}
		
		return result;
	}


}
