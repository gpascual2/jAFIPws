package ar.com.integrarsoluciones.jafipws.custom;

import java.util.ArrayList;

public class MyArrayOfTributo extends wsfe.wsdl.ArrayOfTributo {
	public void addTributo(wsfe.wsdl.Tributo trib){
		if (super.tributo == null) {
			super.tributo = new ArrayList<wsfe.wsdl.Tributo>();
        }
		super.tributo.add(trib);
	}
}
