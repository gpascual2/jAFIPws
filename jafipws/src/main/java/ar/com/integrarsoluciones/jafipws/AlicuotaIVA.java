/**
 * 
 */
package ar.com.integrarsoluciones.jafipws;

/**
 * @author pascualg
 *
 */
public class AlicuotaIVA {
	private short codigo;
	private double baseImponible;
	private double importe;
	
	/**
	 * @param codigo
	 * @param baseImponible
	 * @param importe
	 */
	public AlicuotaIVA(short codigo, double baseImponible, double importe) {
		this.codigo = codigo;
		this.baseImponible = baseImponible;
		this.importe = importe;
	}
	
	/**
	 * @return the codigo
	 */
	public short getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(short codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return the baseImponible
	 */
	public double getBaseImponible() {
		return baseImponible;
	}
	/**
	 * @param baseImponible the baseImponible to set
	 */
	public void setBaseImponible(double baseImponible) {
		this.baseImponible = baseImponible;
	}
	/**
	 * @return the importe
	 */
	public double getImporte() {
		return importe;
	}
	/**
	 * @param importe the importe to set
	 */
	public void setImporte(double importe) {
		this.importe = importe;
	}
}
