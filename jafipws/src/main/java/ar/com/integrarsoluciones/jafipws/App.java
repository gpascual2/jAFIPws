package ar.com.integrarsoluciones.jafipws;

import java.util.Date;

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
        
        
        try
        {
        	wsfe =  new WSFE(tra);
        	
        	String serverStatus = "n/a";
        	serverStatus = wsfe.callDummy();
        	logger.info("WSFE Servers Status: " + serverStatus);
        	
        	int ultimoComprobante = -1;
        	ultimoComprobante = wsfe.getUltimoComprobante(1, 6);
        	logger.info("Ultimo comprobante (ptoVenta=1, tipocomprobante=6): " + Integer.toString(ultimoComprobante));
        	if(wsfe.hasIssues()){
        		logger.info("uppss");
        	}
        	
        	/*  Prueba de getComprobante
        	Factura comp = wsfe.getComprobante(1, 1, 15);
        	if (comp != null){
        		logger.info("CAE de comprobante recuperado: " + comp.getCae());
        	}
        	if(wsfe.hasIssues()){
        		logger.info("uppss!");
        	}
        	*/
        	
        	/* Prueba de solicitarCAE */
        	Factura comp = new Factura(cfg, tra, 6, 1);
        	comp.setNumeroComprobante(1);
        	comp.setTipoDocumentoCliente(99);
        	comp.setNumeroDocumentoCliente("0");
        	
        	comp.setConcepto(1);
        	comp.setFechaComprobante(new Date());
        	comp.setFechaVencimiento(new Date());
        	
        	comp.agregarIVA(new AlicuotaIVA((short) 5, (double) 100, (double) 21));
        	
        	comp.agregarTributo(new Tributo((short) 3, "Impuesto Municipal Jesus Maria", (double) 100, (double) 1, (double) 1));
        	
        	comp.setImporteTotal(122);   //100 + 21 + 1
        	comp.setImporteNoGravado(0);
        	comp.setImporteGravado(100);
        	comp.setImporteTributos(1);
        	comp.setImporteExcento(0);
        	comp.setImporteIva(21);
        	
        	String cae = wsfe.solicitarCAE(comp);
        	if (cae != null){
        		logger.info("Resultado autorización: Estado:" + wsfe.getEstadoCAE() + ",CAE:" + wsfe.getCae() + ",VencimientoCAE:" +  (wsfe.getVencimientoCAE() != null ? wsfe.getVencimientoCAE().toString() : "") + ",Observaciones:" + wsfe.getObservaciones());
        	}
        	if(wsfe.hasIssues()){
        		logger.info("uppss!");
        	}	
        	
        }
        catch (Exception e)
        {
        	logger.error(e);
        }
        
        
        
        
        
   
    }
    
    


	
}
