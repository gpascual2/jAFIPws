/**
 * 
 */
package ar.com.integrarsoluciones.jafipws.client.wsaa;
/*
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
*/
//import javax.xml.rpc.ParameterMode;
/*
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
*/
/**
 * @author Guillermo Pascual
 *
 */
public class Client {

	
	/*
	
	//
	// Create the CMS Message
	//
	public static byte [] create_cms (String p12file, String p12pass, String signer, String dstDN, String service, Long TicketTime) {
		// Initialize variables
		PrivateKey pKey = null;
		X509Certificate pCertificate = null;
		byte [] asn1_cms = null;
		CertStore cstore = null;
		String LoginTicketRequest_xml;
		String SignerDN = null;

		//
		// Manage Keys & Certificates
		//
		try {
			
			// Create a keystore using keys from the pkcs#12 p12file
			KeyStore ks = KeyStore.getInstance("pkcs12");
			FileInputStream p12stream = new FileInputStream ( p12file ) ;
			ks.load(p12stream, p12pass.toCharArray());
			p12stream.close();

			// Get Certificate & Private key from KeyStore
			pKey = (PrivateKey) ks.getKey(signer, p12pass.toCharArray());
			pCertificate = (X509Certificate)ks.getCertificate(signer);
			SignerDN = pCertificate.getSubjectDN().toString();

			// Create a list of Certificates to include in the final CMS
			ArrayList<X509Certificate> certList = new ArrayList<X509Certificate>();
			certList.add(pCertificate);

			if (Security.getProvider("BC") == null) {
				Security.addProvider(new BouncyCastleProvider());
			}

			cstore = CertStore.getInstance("Collection", new CollectionCertStoreParameters (certList), "BC");
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 

		//
		// Create XML Message
		// 
		LoginTicketRequest_xml = create_LoginTicketRequest(SignerDN, dstDN, service, TicketTime);
		
		//
		// Create CMS Message
		//
		try {
			// Create a new empty CMS Message
			CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
			gen.addSigners(null);
			
			SignerInformation signerInfo = new SignerInformation();
			SignerInformationStore signerStore = new SignerInformationStore();
			

			// Add a Signer to the Message
			gen.addSigner(pKey, pCertificate, CMSSignedDataGenerator.DIGEST_SHA1);

			// Add the Certificate to the Message
      		gen.addCertificatesAndCRLs(cstore);

			// Add the data (XML) to the Message
			CMSProcessable data = new CMSProcessableByteArray(LoginTicketRequest_xml.getBytes());

			// Add a Sign of the Data to the Message
			CMSSignedData signed = gen.generate(data, true, "BC");	

			// 
			asn1_cms = signed.getEncoded();
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
	
		return (asn1_cms);
	}

	//
	// Create XML Message for AFIP wsaa
	// 	
	public static String create_LoginTicketRequest (String SignerDN, String dstDN, String service, Long TicketTime) {

		String LoginTicketRequest_xml;

		Date GenTime = new Date();
		GregorianCalendar gentime = new GregorianCalendar();
		GregorianCalendar exptime = new GregorianCalendar();
		String UniqueId = new Long(GenTime.getTime() / 1000).toString();
		
		exptime.setTime(new Date(GenTime.getTime()+TicketTime));
		
		XMLGregorianCalendarImpl XMLGenTime = new XMLGregorianCalendarImpl(gentime);
		XMLGregorianCalendarImpl XMLExpTime = new XMLGregorianCalendarImpl(exptime);

		LoginTicketRequest_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
						+"<loginTicketRequest version=\"1.0\">"
			+"<header>"
			+"<source>" + SignerDN + "</source>"
			+"<destination>" + dstDN + "</destination>"
			+"<uniqueId>" + UniqueId + "</uniqueId>"
			+"<generationTime>" + XMLGenTime + "</generationTime>"
			+"<expirationTime>" + XMLExpTime + "</expirationTime>"
			+"</header>"
			+"<service>" + service + "</service>"
			+"</loginTicketRequest>";
		
		//System.out.println("TRA: " + LoginTicketRequest_xml);
		
		return (LoginTicketRequest_xml);
	}
	
	*/
	
	
	
}
