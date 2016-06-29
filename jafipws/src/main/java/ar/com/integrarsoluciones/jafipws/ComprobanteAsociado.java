/**
 * 
 */
package ar.com.integrarsoluciones.jafipws;

/**
 * @author pascualg
 *
 */
public class ComprobanteAsociado {
	private short tipo;
	private int puntoVenta;
	private long numero;
	
	/**
	 * @param tipo
	 * @param puntoVenta
	 * @param numero
	 */
	public ComprobanteAsociado(short tipo, int puntoVenta, long numero) {
		this.tipo = tipo;
		this.puntoVenta = puntoVenta;
		this.numero = numero;
	}
	
	/**
	 * @return the tipo
	 */
	public short getTipo() {
		return tipo;
	}
	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(short tipo) {
		this.tipo = tipo;
	}
	/**
	 * @return the puntoVenta
	 */
	public int getPuntoVenta() {
		return puntoVenta;
	}
	/**
	 * @param puntoVenta the puntoVenta to set
	 */
	public void setPuntoVenta(int puntoVenta) {
		this.puntoVenta = puntoVenta;
	}
	/**
	 * @return the numero
	 */
	public long getNumero() {
		return numero;
	}
	/**
	 * @param numero the numero to set
	 */
	public void setNumero(long numero) {
		this.numero = numero;
	}
}
