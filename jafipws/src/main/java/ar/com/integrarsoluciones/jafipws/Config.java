/**
 * 
 */
package ar.com.integrarsoluciones.jafipws;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mapdb.DB;
import org.mapdb.DBMaker;

/**
 * @author pascualg
 *
 */
public class Config {

	private DB db;
	private Logger logger;
	private String configPath;
	
	/**
	 * Constructor for the Config class used by the clients
	 * @param filesPath - Path where config files and cache DB are stored. Use "/".
	 */
	public Config(String filesPath) throws IllegalArgumentException 
	{
		// Validate files path
		if(!filesPath.endsWith("/"))
		{
			filesPath = filesPath + "/";
		}
		Path pFilesPath = Paths.get(filesPath);
		
		// Check if config path exists
		if (Files.exists(pFilesPath)) {
			this.configPath = filesPath;
		}
		else
		{
		    throw new IllegalArgumentException("Invalid path provided");
		}
		
		// Check if Logger config exists, if not, use internal properties...
		pFilesPath = Paths.get(this.configPath + "log4j.properties");
		String log4jConfPath = "src/main/resources/log4j.properties";
		if (Files.exists(pFilesPath)) {
		    log4jConfPath = pFilesPath.toString();
		}
		
		// Setup Logger
        PropertyConfigurator.configure(log4jConfPath);
        this.logger = Logger.getLogger("ar.com.integrarsoluciones.jafipws.config");
        
        // Bootstrap local storage
        this.db = DBMaker.newFileDB(new File(this.configPath + "afipWS.db"))
        		.closeOnJvmShutdown()
        		.make();
	}
	
	/**
	 * @return the db
	 */
	public DB getDb() {
		return db;
	}

	/**
	 * @return the configPath
	 */
	public String getConfigPath() {
		return configPath;
	}

	
	public String getLibPath()
	{
		
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
	        this.logger.info("Working dir: " + path);
        }
        catch (Exception e)
        {
        	this.logger.error(e.toString());
        }
        return path;
	}
}
