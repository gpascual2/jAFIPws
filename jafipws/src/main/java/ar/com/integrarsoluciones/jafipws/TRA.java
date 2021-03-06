/**
 * 
 */
package ar.com.integrarsoluciones.jafipws;

import java.io.Reader;
import java.io.StringReader;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import ar.com.integrarsoluciones.jafipws.client.WSAA;
import ar.com.integrarsoluciones.jafipws.client.WSAA.TargetServices;
import ar.com.integrarsoluciones.jafipws.client.WSAA.WSModes;


/**
 * @author pascualg
 *
 */
public class TRA {
	private Boolean valid;
	private String cuit;
	private TargetServices service;
	private WSModes mode;
	private String targetServiceUrl;
	private String token;
	private String sign;
	private Date expiration;
	private Logger logger;
	
	public TRA(Config cfg, String cuit, TargetServices service, WSModes mode) {
		super();
		this.cuit = cuit;
		this.service = service;
		this.mode = mode;
		this.valid = false;
		this.logger = Logger.getLogger("ar.com.integrarsoluciones.jafipws.TRA");
		
		Map<String,String> map = cfg.getDb().getTreeMap("wsaaTraMap");
		String traKey = this.cuit + "_" + service.toString() + "_" + mode.toString();
		String storedTra = map.get(traKey);
		// If there is a TRA cached, then validate and load
		if (storedTra != null)
		{
			// parse cached object
			this.parseCacheString(storedTra);
			// validate expiration
			if(this.getExpiration() != null)
			{
				// if expired delete the cached object and clear local variables
				if(this.getExpiration().before(new Date()))
				{
					map.remove(traKey);
					cfg.getDb().commit();
					this.setToken("");
					this.setSign("");
					this.setExpiration(null);
					this.valid = false;
					this.logger.info("Cached TRA expired: " + traKey + " - Requesting new one to WSAA...");
				}
				// Else, is current, then set valid flag to true
				else
				{
					this.valid = true;
					this.logger.info("Cached TRA found: " + traKey);
				}
			}
		}

	}
	
	// Call WSAA, get a valid TRA and then store in cache
	public void callWSAA(Config cfg, String keyStore)
    {
    	String tra = null;
    	WSAA wsaa = new WSAA();
    	wsaa.setCuit(this.getCuit());
    	wsaa.setKeyStorePath(keyStore);
    	wsaa.setTargetService(this.getService());
    	wsaa.setWsMode(this.getMode());
    	this.setTargetServiceUrl(wsaa.getServiceURL());
    	try
    	{
    		tra = wsaa.callWSAA();
    		this.logger.debug("WSAA TRA: " + tra);
    	}
    	catch (Exception e)
    	{
    		this.logger.error(e.toString());
    	}
    	//Parse WSAA response and store values in local object
    	parseWsaaResponse(tra);
    	this.valid = true;
    	
    	//Cache response
    	Map<String,String> map = cfg.getDb().getTreeMap("wsaaTraMap");
		String traKey = this.cuit + "_" + this.getService().toString() + "_" + this.getMode().toString();
		String traCacheXml = this.getCacheString();
		map.put(traKey, traCacheXml);
        cfg.getDb().commit();
        this.logger.debug("TRA added to cache: " + traKey);
    }
		
	// Returns the info in the String format to store in cache
	private String getCacheString(){
		// convert expiration time to XML Calendar
		GregorianCalendar expTime = new GregorianCalendar();
		expTime.setTime(this.getExpiration());
		XMLGregorianCalendar xmlExpTime = null;
		try
		{
			xmlExpTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(expTime);
		}
		catch (Exception e)
		{
			this.logger.error(e.toString());
		}
		String xml = "";
		xml = xml + "<tra>";
		xml = xml + "<expirationTime>" + xmlExpTime + "</expirationTime>";
		xml = xml + "<token>" + this.getToken() + "</token>";
		xml = xml + "<sign>" + this.getSign() + "</sign>";
		xml = xml + "<serviceUrl>" + this.getTargetServiceUrl() + "</serviceUrl>";
		xml = xml + "</tra>";
		return xml;
	}
	
	private void parseCacheString(String xml)
	{
        String readExpTime = "";
        
        // Get token & sign from xml cache string
		try {
			Reader tokenReader = new StringReader(xml);
			Document tokenDoc = new SAXReader(false).read(tokenReader);
			readExpTime = tokenDoc.valueOf("/tra/expirationTime");
			this.setToken(tokenDoc.valueOf("/tra/token"));
			this.setSign(tokenDoc.valueOf("/tra/sign"));
			this.setTargetServiceUrl(tokenDoc.valueOf("/tra/serviceUrl"));
		} catch (Exception e) {
			this.logger.error(e.toString());
		}
		
		// convert & store expiration date
		XMLGregorianCalendar xmlExpTime = null;
		try
		{
			xmlExpTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(readExpTime);
			Date expTime = xmlExpTime.toGregorianCalendar().getTime();
			this.setExpiration(expTime);
		}
		catch (Exception e)
		{
			this.logger.error(e.toString());
		}
	}
	
	private void parseWsaaResponse(String xml)
	{
        String readExpTime = "";
        
        // Get token & sign from xml cache string
		try {
			Reader tokenReader = new StringReader(xml);
			Document tokenDoc = new SAXReader(false).read(tokenReader);
			readExpTime = tokenDoc.valueOf("/loginTicketResponse/header/expirationTime");
			this.setToken(tokenDoc.valueOf("/loginTicketResponse/credentials/token"));
			this.setSign(tokenDoc.valueOf("/loginTicketResponse/credentials/sign"));
		} catch (Exception e) {
			this.logger.error(e.toString());
		}
		
		// convert & store expiration date
		XMLGregorianCalendar xmlExpTime = null;
		try
		{
			xmlExpTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(readExpTime);
			Date expTime = xmlExpTime.toGregorianCalendar().getTime();
			this.setExpiration(expTime);
		}
		catch (Exception e)
		{
			this.logger.error(e.toString());
		}
	}
	
	
	/**
	 * @return the cuit
	 */
	public String getCuit() {
		return cuit;
	}
	/**
	 * @param cuit the cuit to set
	 */
	public void setCuit(String cuit) {
		this.cuit = cuit;
	}
	/**
	 * @return the service
	 */
	public TargetServices getService() {
		return service;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(TargetServices service) {
		this.service = service;
	}
	/**
	 * @return the mode
	 */
	public WSModes getMode() {
		return mode;
	}
	/**
	 * @param mode the mode to set
	 */
	public void setMode(WSModes mode) {
		this.mode = mode;
	}
	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}
	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}
	/**
	 * @param sign the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}
	/**
	 * @return the expiration
	 */
	public Date getExpiration() {
		return expiration;
	}
	/**
	 * @param expiration the expiration to set
	 */
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	/**
	 * @return Is the TRA info valid?
	 */
	public Boolean isValid() {
		return valid;
	}

	/**
	 * @return the targetServiceUrl
	 */
	public String getTargetServiceUrl() {
		return targetServiceUrl;
	}

	/**
	 * @param targetServiceUrl the targetServiceUrl to set
	 */
	public void setTargetServiceUrl(String targetServiceUrl) {
		this.targetServiceUrl = targetServiceUrl;
	}

	
	
}
