package ar.com.integrarsoluciones.jafipws;

import java.io.File;
import java.net.URLDecoder;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import ar.com.integrarsoluciones.jafipws.client.TRA;
import ar.com.integrarsoluciones.jafipws.client.WSAA.TargetServices;
import ar.com.integrarsoluciones.jafipws.client.WSAA.WSModes;

/**
 * Hello world!
 *
 */
public class App 
{
	private static DB db;
	private static Map<String,String> configMap;
	
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        // Setup Logger
        String log4jConfPath = "src/main/resources/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
        // get a logger instance 
        Logger  logger = Logger.getLogger("ar.com.integrarsoluciones.jafipws");
        
        // Get working path
        String path = "";
        try
        {
	        path = URLDecoder.decode(
	        		App.class.getProtectionDomain()
	        		.getCodeSource()
	        		.getLocation()
	        		.getPath()
	        		, "UTF-8");
	        logger.info("Working dir: " + path);
        }
        catch (Exception e)
        {
        	logger.error(e.toString());
        }
        
        
        // Bootstrap local storage
        db = DBMaker.newFileDB(new File(path + "afipWS.db"))
        		.closeOnJvmShutdown()
        		.make();
        configMap = db.getTreeMap("configMap");
        
        
        TRA tra = new TRA("23288660069", TargetServices.WSFEV1, WSModes.HOMO, db);
        if (!tra.isValid())
        {
        	tra.callWSAA("C:\\source\\AFIP-WS\\AFIPWS_23288660069.jks");
        }

        logger.info(" >> TRA Token: " + tra.getToken());
        logger.info(" >> TRA Sign: " + tra.getSign());
        logger.info(" >> TRA Expiration: " + tra.getExpiration().toString());
        
   
    }
    
    


	
}
