/**
 * 
 */
package ar.com.integrarsoluciones.jafipws.client;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ar.com.integrarsoluciones.jafipws.Factura;
import ar.com.integrarsoluciones.jafipws.TRA;

/**
 * @author pascualg
 *
 */
public class WSFE {
	private WSFEClient client;
	private Logger logger;
	private TRA tra;
	private String errors;
	private String events;
	private boolean hasIssues;
	private String observaciones;
	private String cae;
	private String estadoCAE;
	private Date vencimientoCAE;

	/**
	 * Constructor
	 * @param TRA object
	 */
	public WSFE(TRA tra) {
		this.logger = Logger.getLogger("ar.com.integrarsoluciones.jafipws.client.WSFE");
		this.tra = tra;
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
	 * Autoriza un comprobante y obtiene los datos del CAE asignado.
	 * @param Instancia de Factura
	 * @return CAE asignado
	 */
	public String solicitarCAE(Factura fact){
		String result = null;
		try
		{
			logger.info(">>> WSFE.solicitarCAE - Nueva solicitud (tipoComprobante="+Integer.toString(fact.getTipoComprobante())+", ptoVenta="+Integer.toString(fact.getPuntoVenta())+", numeroComprobante="+Long.toString(fact.getNumeroComprobante())+") <<<");
			result = client.solicitarCAE(tra, fact);
			this.hasIssues = client.hasIssues();
			this.errors = client.getErrors();
			this.events = client.getEvents();
			//log result
			if(result != null){
				logger.info("WSFE.solicitarCAE - Comprobante obtenido desde AFIP. Estado:"+client.getEstadoCAE()+", CAE:" + result);
				this.cae = client.getCae();
				this.estadoCAE = client.getEstadoCAE();
				this.vencimientoCAE = client.getVencimientoCAE();
				this.observaciones = client.getObservaciones();
			}
			if(this.hasIssues){
				if(this.getErrors() != "")
					logger.error("WSFE.solicitarCAE_Errors: " + this.getErrors());
				if(this.getEvents() != "")
					logger.warn("WSFE.solicitarCAE_Events: " + this.getEvents());
        	}
		}
		catch (Exception e)
		{
			logger.error(e.toString());
		}
		return result;
	}
	
	
	/**
	 * Obtiene los datos del último comprobante autorizado para un determinado Punto de Venta y Tipo de Comprobante.
	 * @param Punto de Venta
	 * @param Tipo de Comprobante
	 * @return Ultimo número de comprobante autorizado
	 */
	public int getUltimoComprobante(int puntoVenta, int tipoComprobante){
		int result = -1;
		try
		{
			logger.info(">>> WSFE.getUltimoComprobante - Nueva solicitud (tipoComprobante="+Integer.toString(tipoComprobante)+", ptoVenta="+Integer.toString(puntoVenta)+") <<<");
			result = client.getUltimoComprobante(tra, puntoVenta, tipoComprobante);
			this.hasIssues = client.hasIssues();
			this.errors = client.getErrors();
			this.events = client.getEvents();
			//log result
			if(result != -1)
				logger.info("WSFE.getUltimoComprobante - Numero de ultimo comprobante obtenido desde AFIP: " + Long.toString(result));
			if(this.hasIssues){
				if(this.getErrors() != "")
					logger.error("WSFE.getUltimoComprobante_Errors: " + this.getErrors());
				if(this.getEvents() != "")
					logger.warn("WSFE.getUltimoComprobante_Events: " + this.getEvents());
        	}
		}
		catch (Exception e)
		{
			logger.error(e.toString());
		}
		return result;
	}
	

	/**
	 * Retorna los datos de un comprobante previamente autorizado
	 * @param Punto de Venta
	 * @param Tipo de Comprobante
	 * @param Numero de Comprobante
	 * @return Instancia de Factura con los datos del comprobante
	 */
	public Factura getComprobante(int tipoComprobante, int puntoVenta, long numeroComprobante){
		Factura result = null;
		try
		{
			logger.info(">>> WSFE.getComprobante - Nueva solicitud (tipoComprobante="+Integer.toString(tipoComprobante)+", ptoVenta="+Integer.toString(puntoVenta)+", numeroComprobante="+Long.toString(numeroComprobante)+") <<<");
			result = client.getComprobante(tra, tipoComprobante, puntoVenta, numeroComprobante);
			this.hasIssues = client.hasIssues();
			this.errors = client.getErrors();
			this.events = client.getEvents();
			//log result
			if(result != null)
				logger.info("WSFE.getComprobante - Comprobante obtenido desde AFIP. CAE: " + result.getCae());
			if(this.hasIssues){
				if(this.getErrors() != "")
					logger.error("WSFE.getComprobante_Errors: " + this.getErrors());
				if(this.getEvents() != "")
					logger.warn("WSFE.getComprobante_Events: " + this.getEvents());
        	}
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
	
	/**
	 * @return the errors
	 */
	public String getErrors() {
		return errors;
	}

	/**
	 * @return the events
	 */
	public String getEvents() {
		return events;
	}

	/**
	 * @return the hasIssues
	 */
	public boolean hasIssues() {
		return hasIssues;
	}


	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}


	/**
	 * @return the cae
	 */
	public String getCae() {
		return cae;
	}


	/**
	 * @return the estadoCAE
	 */
	public String getEstadoCAE() {
		return estadoCAE;
	}


	/**
	 * @return the vencimientoCAE
	 */
	public Date getVencimientoCAE() {
		return vencimientoCAE;
	}


}
