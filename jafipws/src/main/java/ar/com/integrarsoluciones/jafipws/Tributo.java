/**
 * 
 */
package ar.com.integrarsoluciones.jafipws;

/**
 * @author pascualg
 *
 */
public class Tributo {
	private short codigo;
	private String descripcion;
	private double baseImponible;
	private double alicuota;
	private double importe;
	
	/**
	 * @param codigo
	 * @param descripcion
	 * @param baseImponible
	 * @param alicuota
	 * @param importe
	 */
	public Tributo(short codigo, String descripcion, double baseImponible, double alicuota, double importe) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.baseImponible = baseImponible;
		this.alicuota = alicuota;
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
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	 * @return the alicuota
	 */
	public double getAlicuota() {
		return alicuota;
	}
	/**
	 * @param alicuota the alicuota to set
	 */
	public void setAlicuota(double alicuota) {
		this.alicuota = alicuota;
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
