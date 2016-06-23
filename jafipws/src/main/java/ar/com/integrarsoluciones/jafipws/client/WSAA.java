package ar.com.integrarsoluciones.jafipws.client;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;;

/**
 * AFIP-WS WSAA Client
 * WSAA is the web service for Authentication and Authorization of AFIP
 * @author Guillermo Pascual
 *
 */
public class WSAA {
	
	public enum WSModes {
		HOMO,
		PROD
	};
	
	public enum TargetServices {
		WSFEV1,
		WSAA
	};
	
	// *************************************
	// ** Properties
	// *************************************
	private static String cuit = "";
	private static String keyStorePath = "";  //"C:\\source\\AFIP-WS\\AFIPWS_23288660069.jks"
	private static WSModes wsMode = WSModes.HOMO;
	private static TargetServices targetService = TargetServices.WSFEV1;
	private static Long ticketExpirationTime = (long) 900000;  //15 minutes
	
	
	// *************************************
	// ** Configuration 
	// *************************************
	private static String getIntServiceURL(String service)
	{
		Hashtable<String, String> serviceURL = new Hashtable<String, String>();
		serviceURL.put("WSAA_HOMO", "https://wsaahomo.afip.gov.ar/ws/services/LoginCms");
		serviceURL.put("WSAA_PROD", "https://wsaa.afip.gov.ar/ws/services/LoginCms");
		serviceURL.put("WSFEV1_HOMO", "https://wswhomo.afip.gov.ar/wsfev1/service.asmx");
		serviceURL.put("WSFEV1_PROD", "https://servicios1.afip.gov.ar/wsfev1/service.asmx");
		return serviceURL.get(service);
	}
	
	public String getServiceURL(TargetServices service, WSModes mode)
	{
		String svc = service.toString() + "_" + mode.toString();
		return getIntServiceURL(svc);
	}
	
	public String getServiceURL(TargetServices service)
	{
		String svc = service.toString() + "_" + wsMode.toString();
		return getIntServiceURL(svc);
	}
	
	public static String getServiceURL()
	{
		String svc = getTargetService().toString() + "_" + wsMode.toString();
		return getIntServiceURL(svc);
	}
	
	private static String getServiceCode(TargetServices service)
	{
		Hashtable<TargetServices, String> serviceCode = new Hashtable<TargetServices, String>();
		serviceCode.put(TargetServices.WSAA, "wsaa");
		serviceCode.put(TargetServices.WSFEV1, "wsfe");
		return serviceCode.get(service);
	}
	
	private static String getDestDN()
	{
		Hashtable<WSModes, String> dnPrefix = new Hashtable<WSModes, String>();
		dnPrefix.put(WSModes.HOMO, "cn=wsaahomo,o=afip,c=ar,serialNumber=CUIT ");
		dnPrefix.put(WSModes.PROD, "cn=wsaa,o=afip,c=ar,serialNumber=CUIT ");
		//String destDN = dnPrefix.get(wsMode) + cuit.trim();
		String destDN = dnPrefix.get(wsMode) + "33693450239";  // Destino es AFIP
		return destDN;
	}
	
	// *************************************
	// ** TRA processing
	// *************************************
	
	public String callWSAA(){
		String tra = null;
		byte [] cms = null;

		WSAAConfiguration wsaaConfig = new WSAAConfiguration();
		Jaxb2Marshaller marshaller = wsaaConfig.marshaller();
		WSAAClient client = wsaaConfig.wsaaClient(marshaller, getServiceURL(TargetServices.WSAA));
		
		try{
        	cms = createTRACMS();
        	tra = client.callWSAA(cms, getServiceURL(TargetServices.WSAA));
        	
        } catch(Exception e){
            e.printStackTrace();
            System.err.println("\n\n\n");
        }
		
        return tra;
    }
	
	
	/**
	 * Create XML Message for AFIP WSAA
	 * @param SignerDN
	 * @return AFIP TRA ticket (Ticket de Requerimiento de Acceso)
	 */
	private static String createTRARequest (String SignerDN) {

		String LoginTicketRequest_xml;

		Date GenTime = new Date();
		GregorianCalendar gentime = new GregorianCalendar();
		GregorianCalendar exptime = new GregorianCalendar();
		String UniqueId = new Long(GenTime.getTime() / 1000).toString();
		exptime.setTime(new Date(GenTime.getTime()+ticketExpirationTime));
		XMLGregorianCalendar XMLGenTime = null;
		XMLGregorianCalendar XMLExpTime = null;
		
		try
		{
			XMLGenTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(gentime);
			XMLExpTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(exptime);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		LoginTicketRequest_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
						+"<loginTicketRequest version=\"1.0\">"
			+"<header>"
			+"<source>" + SignerDN + "</source>"
			+"<destination>" + getDestDN() + "</destination>"
			+"<uniqueId>" + UniqueId + "</uniqueId>"
			+"<generationTime>" + XMLGenTime + "</generationTime>"
			+"<expirationTime>" + XMLExpTime + "</expirationTime>"
			+"</header>"
			+"<service>" + getServiceCode(targetService) + "</service>"
			+"</loginTicketRequest>";
		
		return (LoginTicketRequest_xml);
	}
	
	/**
	 * Creates a signed CMS with the WSAA request ticket (TRA)
	 * The returned data is ready to be sent to WSAA endpoint for authorization.  
	 * @return Signed CMS with WSAA TRA
	 */
	private static byte [] createTRACMS()
	{
		// Initialize variables
		PrivateKey pKey = null;
		X509Certificate pCertificate = null;
		String signerDN = null;
		JcaCertStore certs = null;
		String data = "";
		byte [] asn1_cms = null;
		
		try{
			//Set Bouncy Castle security provider
			if (Security.getProvider("BC") == null) {
				Security.addProvider(new BouncyCastleProvider());
			}
			
			// Open keystore
	    	KeyStore ks = KeyStore.getInstance("jks");
			FileInputStream jksStream = new FileInputStream (keyStorePath);
			ks.load(jksStream, cuit.toCharArray());
			jksStream.close();
			
			// Get Certificate & Private key from KeyStore
			pKey = (PrivateKey) ks.getKey(getKeyAlias(), cuit.toCharArray());
			pCertificate = (X509Certificate)ks.getCertificate(getKeyAlias());
			signerDN = pCertificate.getSubjectDN().toString();
	
			// Create a list of Certificates to include in the final CMS
			ArrayList<X509Certificate> certList = new ArrayList<X509Certificate>();
			certList.add(pCertificate);
			certs = new JcaCertStore(certList);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		// Get TRA data
	    data = createTRARequest(signerDN);
		
		try
		{
		    // Create CMS data generator instance
		    CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
	    
		    // Create Content signer and attach to CMS data generator
		    ContentSigner sha1Signer = new JcaContentSignerBuilder("SHA1with" + pKey.getAlgorithm())
		    		.setProvider("BC")
		    		.build(pKey);
		    gen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(
		    		new JcaDigestCalculatorProviderBuilder()
		    		.setProvider("BC")
		    		.build())
		    		.build(sha1Signer, pCertificate));
		    gen.addCertificates(certs);
		    
		    // Get data into cms format
		    CMSTypedData cmsData = new CMSProcessableByteArray(data.getBytes());
		    
		    //Sign data
		    CMSSignedData sigData = gen.generate(cmsData, true);
		    
		    asn1_cms = sigData.getEncoded();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return asn1_cms;
	}
	
	/**
	 * Get the key alias of the certificate to use from the key store 
	 */
	private static String getKeyAlias()
	{
		String keyAlias = cuit + "_" + wsMode.toString().toLowerCase();
		return keyAlias;
	}
	
	// *************************************
	// ** Getters & Setters
	// *************************************
	/**
	 * Get the CUIT set to operate with AFIP WSAA web service
	 * @return the CUIT set on the WSAA interface
	 */
	public String getCuit() {
		return cuit;
	}


	/**
	 * Set the CUIT to operate with AFIP WSAA web service
	 * @param CUIT or source entity
	 */
	public void setCuit(String cuit) {
		WSAA.cuit = cuit;
	}


	/**
	 * Get the path set of the Key Store containing the AFIP certificate
	 * @return the Key Store Path set on the WSAA interface
	 */
	public String getKeyStorePath() {
		return keyStorePath;
	}

	/**
	 * Set the path of the Key Store containing the AFIP certificate
	 * @param Path of the Key Store (JKS)
	 */
	public void setKeyStorePath(String keyStorePath) {
		WSAA.keyStorePath = keyStorePath;
	}

	/**
	 * Set the service operation mode: 
	 *   HOMO for "Homologación" (testing)
	 *   PROD for "Producción" (production)
	 * @param Mode (HOMO or PROD)
	 */
	public void setWsMode(WSModes wsMode) {
		WSAA.wsMode = wsMode;
	}

	/**
	 * Get the target web service set for use in the TRA ticket request
	 * @return the targetService
	 */
	public static TargetServices getTargetService() {
		return targetService;
	}

	/**
	 * Set the target AFIP's web service that will be used in the TRA ticket request
	 * @param Target Service (WSFEV1)
	 */
	public void setTargetService(TargetServices targetService) {
		WSAA.targetService = targetService;
	}

	
}