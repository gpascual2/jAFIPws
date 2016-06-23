package ar.com.integrarsoluciones.jafipws;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;

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
        
        //tests here...
        //testKeyStore();
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
    	
    }
    
    
    private static void testKeyStore() 
    {
		// Initialize variables
		PrivateKey pKey = null;
		X509Certificate pCertificate = null;
		String SignerDN = null;

    	try{
    		
 
	    	String jksPass = "23288660069";
			String keyAlias = "23288660069_homo";
			String data = "<osodilarip>";
			
			// Open keystore
	    	KeyStore ks = KeyStore.getInstance("jks");
			FileInputStream jksStream = new FileInputStream ("C:\\source\\AFIP-WS\\AFIPWS_23288660069.jks");
			ks.load(jksStream, jksPass.toCharArray());
			jksStream.close();
			
			// Get Certificate & Private key from KeyStore
			pKey = (PrivateKey) ks.getKey(keyAlias, jksPass.toCharArray());
			pCertificate = (X509Certificate)ks.getCertificate(keyAlias);
			SignerDN = pCertificate.getSubjectDN().toString();

			// Create a list of Certificates to include in the final CMS
			ArrayList<X509Certificate> certList = new ArrayList<X509Certificate>();
			certList.add(pCertificate);
		    JcaCertStore certs = new JcaCertStore(certList);
		    
		    // Create CMS data generator instance
		    CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
		    
		    // Create Content signer and attach to CMS data generator
		    ContentSigner signer = new JcaContentSignerBuilder(pKey.getAlgorithm()).build(pKey);

		    gen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(
		    		new JcaDigestCalculatorProviderBuilder()
		    		.build())
		    		.setDirectSignature(true)
		    		.build(signer, pCertificate));
		    
		    gen.addCertificates(certs);
		    
		    // Get data into cms format
		    CMSTypedData cmsData = new CMSProcessableByteArray(data.getBytes());
		    
		    //Sign data
		    CMSSignedData sigData = gen.generate(cmsData, false);
		    

    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    }
}
