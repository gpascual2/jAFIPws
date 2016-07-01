package ar.com.integrarsoluciones.jafipws.custom;

import java.util.ArrayList;

import wsfe.wsdl.CbteAsoc;

public class MyArrayOfCbteAsoc extends wsfe.wsdl.ArrayOfCbteAsoc {
	public void addCbteAsoc(CbteAsoc cbte){
		if (super.cbteAsoc == null) {
			super.cbteAsoc = new ArrayList<CbteAsoc>();
        }
		super.cbteAsoc.add(cbte);
	}

}
