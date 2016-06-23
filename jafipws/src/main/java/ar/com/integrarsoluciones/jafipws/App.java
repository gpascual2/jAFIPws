package ar.com.integrarsoluciones.jafipws;

import org.apache.log4j.PropertyConfigurator;

import ar.com.integrarsoluciones.jafipws.client.WSAA;
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
        System.out.println( "Hello World!" );
        
        // Setup Logger
        String log4jConfPath = "src/main/resources/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
        
        //tests here...
        testWSAA();
    }
    
    
    private static void testWSAA()
    {
    	String tra = null;
    	
    	WSAA wsaa = new WSAA();
    	
    	wsaa.setCuit("23288660069");
    	wsaa.setKeyStorePath("C:\\source\\AFIP-WS\\AFIPWS_23288660069.jks");
    	wsaa.setTargetService(TargetServices.WSFEV1);
    	wsaa.setWsMode(WSModes.HOMO);
    	
    	try
    	{
    		tra = wsaa.callWSAA();
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    	
    	System.out.println(tra);
    }
    
}
