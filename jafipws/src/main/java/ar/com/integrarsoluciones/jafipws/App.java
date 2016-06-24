package ar.com.integrarsoluciones.jafipws;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import ar.com.integrarsoluciones.jafipws.client.TRA;
import ar.com.integrarsoluciones.jafipws.client.WSAA.TargetServices;
import ar.com.integrarsoluciones.jafipws.client.WSAA.WSModes;

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
        
        try{
	        // Get config object
        	Config cfg = new Config("C:/source/AFIP-WS/jCfgFiles/");
        	// get TRA
        	TRA tra = new TRA(cfg, "23288660069", TargetServices.WSFEV1, WSModes.HOMO);
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
   
    }
    
    


	
}
