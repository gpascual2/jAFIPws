package ar.com.integrarsoluciones.jafipws.client;

import org.apache.commons.codec.binary.Base64;

//import ar.com.integrarsoluciones.jafipws.service.wsaahomo.LoginCMSServiceStub;

public class WSAA_HOMO extends WSAAInterface {

	public String callWSAA(byte [] cms, String serviceURL){
		String tra = null;
		/*
        try{
       		LoginCMSServiceStub stub = new LoginCMSServiceStub(serviceURL);
        	tra = wsaaLogin(stub, cms);
        } catch(Exception e){
            e.printStackTrace();
            System.err.println("\n\n\n");
        }
        */
        return tra;
    }
	
	/*
    private String wsaaLogin(LoginCMSServiceStub stub, byte [] cms){
    	String LoginTicketResponse = null;
        try{
        	LoginCMSServiceStub.LoginCms req = new LoginCMSServiceStub.LoginCms();
        	req.setIn0(Base64.encodeBase64String(cms));

        	LoginCMSServiceStub.LoginCmsResponse res = stub.loginCms(req);
        	LoginTicketResponse = res.getLoginCmsReturn();

            System.err.println(LoginTicketResponse);
        } catch(Exception e){
            e.printStackTrace();
            System.err.println("\n\n\n");
        }
        return LoginTicketResponse;
    }
	*/
}
