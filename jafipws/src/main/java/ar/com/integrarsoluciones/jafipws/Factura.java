/**
 * 
 */
package ar.com.integrarsoluciones.jafipws;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

import ar.com.integrarsoluciones.jafipws.client.WSAA.WSModes;

/**
 * @author pascualg
 *
 */
public class Factura {
	private Logger logger;
	private WSModes mode; 
	
	private String token;
	private String sign;
	private String cuit;
	private Date fechaComprobante;
	private int tipoComprobante;
	private int puntoVenta;
	private long numeroComprobante;
	private int tipoDocumentoCliente;
	private String numeroDocumentoCliente;
	private int concepto;
	private double importeTotal;
	private double importeNoGravado;
	private double importeGravado;
	private double importeExcento;
	private double importeTributos;
	private double importeIva;
	private Date fechaServicioDesde;
	private Date fechaServicioHasta;
	private Date fechaVencimiento;
	private String codigoMoneda;
	private double cotizacionMoneda;
	
	//private Set<ComprobanteAsociado> comprobantesAsociados = new HashSet<ComprobanteAsociado>();
	private List<ComprobanteAsociado> comprobantesAsociados;
	//private Set<Tributo> tributos = new HashSet<Tributo>();
	private List<Tributo> tributos;
	//private Set<AlicuotaIVA> alicuotasIva = new HashSet<AlicuotaIVA>();
	private List<AlicuotaIVA> alicuotasIva;
	
	private String estadoCae;
	private String cae;
	private Date vencimientoCae;
	private String observacionesCae;
	
	
	public Factura() {
		this.logger = Logger.getLogger("ar.com.integrarsoluciones.jafipws.Factura");
		//Inicializar defaults
		this.mode = WSModes.HOMO;
		this.token = "";
		this.sign = "";
		this.cuit = "";
		this.tipoComprobante = 0;
		this.puntoVenta = 0;
		this.fechaComprobante = new Date();
		this.numeroComprobante = 0;
		this.tipoDocumentoCliente = 99;
		this.numeroDocumentoCliente = "1";
		this.concepto = 1;                  //(1=Productos, 2=Servicios, 3=Productos y Servicios) 
		this.importeTotal = 0;
		this.importeNoGravado = 0;
		this.importeGravado = 0;
		this.importeExcento = 0;
		this.importeTributos = 0;
		this.importeIva = 0;
		this.fechaServicioDesde = this.fechaComprobante;
		this.fechaServicioHasta = this.fechaComprobante;
		this.fechaVencimiento = this.fechaComprobante;
		this.codigoMoneda = "PES";
		this.cotizacionMoneda = 1;
		this.estadoCae = "R";
		this.cae = "";
		this.vencimientoCae = this.fechaComprobante;
		this.observacionesCae = "";
		//Inicializar arrays
        this.tributos = new ArrayList<Tributo>();
        this.comprobantesAsociados = new ArrayList<ComprobanteAsociado>();
        this.alicuotasIva = new ArrayList<AlicuotaIVA>();
	}
	
	/**
	 * @param token
	 * @param sign
	 * @param cuit
	 * @param fechaComprobante
	 * @param tipoComprobante
	 * @param puntoVenta
	 * @param numeroComprobante
	 */
	public Factura(Config cfg, TRA tra, int tipoComprobante, int puntoVenta) {
		this.logger = Logger.getLogger("ar.com.integrarsoluciones.jafipws.Factura");
		this.mode = tra.getMode();
		this.token = tra.getToken();
		this.sign = tra.getSign();
		this.cuit = tra.getCuit();
		this.tipoComprobante = tipoComprobante;
		this.puntoVenta = puntoVenta;

		//Inicializar defaults
		this.fechaComprobante = new Date();
		this.numeroComprobante = 0;
		this.tipoDocumentoCliente = 99;
		this.numeroDocumentoCliente = "1";
		this.concepto = 1;                  //(1=Productos, 2=Servicios, 3=Productos y Servicios) 
		this.importeTotal = 0;
		this.importeNoGravado = 0;
		this.importeGravado = 0;
		this.importeExcento = 0;
		this.importeTributos = 0;
		this.importeIva = 0;
		this.fechaServicioDesde = this.fechaComprobante;
		this.fechaServicioHasta = this.fechaComprobante;
		this.fechaVencimiento = this.fechaComprobante;
		this.codigoMoneda = "PES";
		this.cotizacionMoneda = 1;
		this.estadoCae = "R";
		this.cae = "";
		this.vencimientoCae = this.fechaComprobante;
		this.observacionesCae = "";
		//Inicializar arrays
        this.tributos = new ArrayList<Tributo>();
        this.comprobantesAsociados = new ArrayList<ComprobanteAsociado>();
        this.alicuotasIva = new ArrayList<AlicuotaIVA>();
        

	}
	
	
	
	
	
	//TODO: 
	// - Crear WSFE con constructor para parametros que necesite para llamar al client
	// - Agregar metodos como dummy, getCAE
	// - Agregar verificación de errores y observaciones...  
	
	
	
	/**
	 * @param Comprobante asociado
	 */
	public void agregarComprobante(final ComprobanteAsociado comp) {
		comprobantesAsociados.add(comp);
    }
	
	/**
	 * @return the comprobantesAsociados
	 */
	public List<ComprobanteAsociado> getComprobantesAsociados() {
		return comprobantesAsociados;
	}
	
	/**
	 * @param Tributo
	 */
	public void agregarTributo(final Tributo tributo) {
		tributos.add(tributo);
    }

	/**
	 * @return the tributos
	 */
	public List<Tributo> getTributos() {
		return tributos;
	}

	/**
	 * @param Alicuota de IVA
	 */
	public void agregarIVA(final AlicuotaIVA iva) {
		alicuotasIva.add(iva);
    }
		
	/**
	 * @return the alicuotasIva
	 */
	public List<AlicuotaIVA> getAlicuotasIva() {
		return alicuotasIva;
	}
	
	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}

	/**
	 * @param sign the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}

	/**
	 * @return the cuit
	 */
	public String getCuit() {
		return cuit;
	}

	/**
	 * @param cuit the cuit to set
	 */
	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	/**
	 * @return the fechaComprobante
	 */
	public Date getFechaComprobante() {
		return fechaComprobante;
	}

	/**
	 * @param fechaComprobante the fechaComprobante to set
	 */
	public void setFechaComprobante(Date fechaComprobante) {
		this.fechaComprobante = fechaComprobante;
	}

	/**
	 * @return the tipoComprobante
	 */
	public int getTipoComprobante() {
		return tipoComprobante;
	}

	/**
	 * @param tipoComprobante the tipoComprobante to set
	 */
	public void setTipoComprobante(int tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
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
	 * @return the numeroComprobante
	 */
	public long getNumeroComprobante() {
		return numeroComprobante;
	}

	/**
	 * @param numeroComprobante the numeroComprobante to set
	 */
	public void setNumeroComprobante(long numeroComprobante) {
		this.numeroComprobante = numeroComprobante;
	}

	/**
	 * @return the tipoDocumentoCliente
	 */
	public int getTipoDocumentoCliente() {
		return tipoDocumentoCliente;
	}

	/**
	 * @param tipoDocumentoCliente the tipoDocumentoCliente to set
	 */
	public void setTipoDocumentoCliente(int tipoDocumentoCliente) {
		this.tipoDocumentoCliente = tipoDocumentoCliente;
	}

	/**
	 * @return the numeroDocumentoCliente
	 */
	public String getNumeroDocumentoCliente() {
		return numeroDocumentoCliente;
	}

	/**
	 * @param numeroDocumentoCliente the numeroDocumentoCliente to set
	 */
	public void setNumeroDocumentoCliente(String numeroDocumentoCliente) {
		this.numeroDocumentoCliente = numeroDocumentoCliente;
	}

	/**
	 * @return the concepto
	 */
	public int getConcepto() {
		return concepto;
	}

	/**
	 * @param concepto the concepto to set
	 */
	public void setConcepto(int concepto) {
		this.concepto = concepto;
	}

	/**
	 * @return the importeTotal
	 */
	public double getImporteTotal() {
		return importeTotal;
	}

	/**
	 * @param importeTotal the importeTotal to set
	 */
	public void setImporteTotal(double importeTotal) {
		this.importeTotal = importeTotal;
	}

	/**
	 * @return the importeNoGravado
	 */
	public double getImporteNoGravado() {
		return importeNoGravado;
	}

	/**
	 * @param importeNoGravado the importeNoGravado to set
	 */
	public void setImporteNoGravado(double importeNoGravado) {
		this.importeNoGravado = importeNoGravado;
	}

	/**
	 * @return the importeGravado
	 */
	public double getImporteGravado() {
		return importeGravado;
	}

	/**
	 * @param importeGravado the importeGravado to set
	 */
	public void setImporteGravado(double importeGravado) {
		this.importeGravado = importeGravado;
	}

	/**
	 * @return the importeExcento
	 */
	public double getImporteExcento() {
		return importeExcento;
	}

	/**
	 * @param importeExcento the importeExcento to set
	 */
	public void setImporteExcento(double importeExcento) {
		this.importeExcento = importeExcento;
	}

	/**
	 * @return the importeTributos
	 */
	public double getImporteTributos() {
		return importeTributos;
	}

	/**
	 * @param importeTributos the importeTributos to set
	 */
	public void setImporteTributos(double importeTributos) {
		this.importeTributos = importeTributos;
	}

	/**
	 * @return the importeIva
	 */
	public double getImporteIva() {
		return importeIva;
	}

	/**
	 * @param importeIva the importeIva to set
	 */
	public void setImporteIva(double importeIva) {
		this.importeIva = importeIva;
	}

	/**
	 * @return the fechaServicioDesde
	 */
	public Date getFechaServicioDesde() {
		return fechaServicioDesde;
	}

	/**
	 * @param fechaServicioDesde the fechaServicioDesde to set
	 */
	public void setFechaServicioDesde(Date fechaServicioDesde) {
		this.fechaServicioDesde = fechaServicioDesde;
	}

	/**
	 * @return the fechaServicioHasta
	 */
	public Date getFechaServicioHasta() {
		return fechaServicioHasta;
	}

	/**
	 * @param fechaServicioHasta the fechaServicioHasta to set
	 */
	public void setFechaServicioHasta(Date fechaServicioHasta) {
		this.fechaServicioHasta = fechaServicioHasta;
	}

	/**
	 * @return the fechaVencimiento
	 */
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	/**
	 * @param fechaVencimiento the fechaVencimiento to set
	 */
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	/**
	 * @return the codigoMoneda
	 */
	public String getCodigoMoneda() {
		return codigoMoneda;
	}

	/**
	 * @param codigoMoneda the codigoMoneda to set
	 */
	public void setCodigoMoneda(String codigoMoneda) {
		this.codigoMoneda = codigoMoneda;
	}

	/**
	 * @return the cotizacionMoneda
	 */
	public double getCotizacionMoneda() {
		return cotizacionMoneda;
	}

	/**
	 * @param cotizacionMoneda the cotizacionMoneda to set
	 */
	public void setCotizacionMoneda(double cotizacionMoneda) {
		this.cotizacionMoneda = cotizacionMoneda;
	}

	/**
	 * @return the estadoCae
	 */
	public String getEstadoCae() {
		return estadoCae;
	}

	/**
	 * @param estadoCae the estadoCae to set
	 */
	public void setEstadoCae(String estadoCae) {
		this.estadoCae = estadoCae;
	}

	/**
	 * @return the cae
	 */
	public String getCae() {
		return cae;
	}

	/**
	 * @param cae the cae to set
	 */
	public void setCae(String cae) {
		this.cae = cae;
	}

	/**
	 * @return the vencimientoCae
	 */
	public Date getVencimientoCae() {
		return vencimientoCae;
	}

	/**
	 * @param vencimientoCae the vencimientoCae to set
	 */
	public void setVencimientoCae(Date vencimientoCae) {
		this.vencimientoCae = vencimientoCae;
	}

	/**
	 * @return the observacionesCae
	 */
	public String getObservacionesCae() {
		return observacionesCae;
	}

	/**
	 * @param observacionesCae the observacionesCae to set
	 */
	public void setObservacionesCae(String observacionesCae) {
		this.observacionesCae = observacionesCae;
	}


	/**
	 * @return the mode
	 */
	public WSModes getMode() {
		return mode;
	}

}
