package ar.com.integrarsoluciones.jafipws;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import org.bouncycastle.cert.jcajce.JcaCertStore;
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
