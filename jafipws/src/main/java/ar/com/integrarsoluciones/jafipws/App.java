package ar.com.integrarsoluciones.jafipws;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import ar.com.integrarsoluciones.jafipws.client.WSAA.TargetServices;
import ar.com.integrarsoluciones.jafipws.client.WSAA.WSModes;
import ar.com.integrarsoluciones.jafipws.client.WSFE;

/**
 * Hello world!
 *
 */
public class App 
{
	
	
    public static void main( String[] args )
    {
        // Setup Logger
        String log4jConfPath = "src/main/resources/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
        // get a logger instance 
        Logger  logger = Logger.getLogger("ar.com.integrarsoluciones.jafipws");
        
        
        // Test WSAA with Config and TRA classes
        Config cfg = null;
        TRA tra = null;
        try{
	        // Get config object
        	cfg = new Config("C:/source/AFIP-WS/jCfgFiles/");
        	// get TRA
        	tra = new TRA(cfg, "23288660069", TargetServices.WSFEV1, WSModes.HOMO);
	        if (!tra.isValid())
	        {
	        	tra.callWSAA(cfg, "C:\\source\\AFIP-WS\\AFIPWS_23288660069.jks");
	        }
	        
	        logger.info(" >> TRA Token: " + tra.getToken());
	        logger.info(" >> TRA Sign: " + tra.getSign());
	        logger.info(" >> TRA Expiration: " + tra.getExpiration().toString());
        }
        catch (Exception e)
        {
        	logger.error(e);
        }
        
        // Test WSFE class
        WSFE wsfe = null;
        String serverStatus = "n/a";
        String tiposComprobante = "";
        try
        {
        	wsfe =  new WSFE(tra);
        	
        	serverStatus = wsfe.callDummy();
        	logger.info("WSFE Servers Status: " + serverStatus);
        	
        	tiposComprobante = wsfe.getParamTiposComprobante();
        	logger.info("Tipos de Comprobantes: " + tiposComprobante);
        	
        }
        catch (Exception e)
        {
        	logger.error(e);
        }
        
        
        
        
        
   
    }
    
    


	
}
