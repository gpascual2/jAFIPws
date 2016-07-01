package ar.com.integrarsoluciones.jafipws.custom;

import java.util.ArrayList;

public class MyArrayOfFECAEDetRequest extends wsfe.wsdl.ArrayOfFECAEDetRequest {
	public void addCAEDetRequest(wsfe.wsdl.FECAEDetRequest detReq){
		if (super.fecaeDetRequest == null) {
			super.fecaeDetRequest = new ArrayList<wsfe.wsdl.FECAEDetRequest>();
        }
		super.fecaeDetRequest.add(detReq);
	}
}
