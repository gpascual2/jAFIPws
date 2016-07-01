package ar.com.integrarsoluciones.jafipws.custom;

import java.util.ArrayList;

import wsfe.wsdl.AlicIva;

public class MyArrayOfAlicIva extends wsfe.wsdl.ArrayOfAlicIva {
	public void addAlicuotaIva(wsfe.wsdl.AlicIva iva){
		if (super.alicIva == null) {
            super.alicIva = new ArrayList<AlicIva>();
        }
		super.alicIva.add(iva);
	}
}
