/**
 * 
 */
package ar.com.integrarsoluciones.jafipws.client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import ar.com.integrarsoluciones.jafipws.AlicuotaIVA;
import ar.com.integrarsoluciones.jafipws.ComprobanteAsociado;
import ar.com.integrarsoluciones.jafipws.Factura;
import ar.com.integrarsoluciones.jafipws.TRA;
import ar.com.integrarsoluciones.jafipws.custom.MyArrayOfAlicIva;
import ar.com.integrarsoluciones.jafipws.custom.MyArrayOfCbteAsoc;
import ar.com.integrarsoluciones.jafipws.custom.MyArrayOfFECAEDetRequest;
import ar.com.integrarsoluciones.jafipws.custom.MyArrayOfTributo;
import wsfe.wsdl.AlicIva;
import wsfe.wsdl.ArrayOfCbteAsoc;
import wsfe.wsdl.ArrayOfCbteTipo;
import wsfe.wsdl.ArrayOfErr;
import wsfe.wsdl.ArrayOfEvt;
import wsfe.wsdl.ArrayOfFECAEDetResponse;
import wsfe.wsdl.CbteAsoc;
import wsfe.wsdl.CbteTipo;
import wsfe.wsdl.CbteTipoResponse;
import wsfe.wsdl.DummyResponse;
import wsfe.wsdl.Err;
import wsfe.wsdl.Evt;
import wsfe.wsdl.FEAuthRequest;
import wsfe.wsdl.FECAECabRequest;
import wsfe.wsdl.FECAEDetRequest;
import wsfe.wsdl.FECAEDetResponse;
import wsfe.wsdl.FECAERequest;
import wsfe.wsdl.FECAEResponse;
import wsfe.wsdl.FECAESolicitar;
import wsfe.wsdl.FECAESolicitarResponse;
import wsfe.wsdl.FECabRequest;
import wsfe.wsdl.FECompConsResponse;
import wsfe.wsdl.FECompConsultaReq;
import wsfe.wsdl.FECompConsultar;
import wsfe.wsdl.FECompConsultarResponse;
import wsfe.wsdl.FECompUltimoAutorizado;
import wsfe.wsdl.FECompUltimoAutorizadoResponse;
import wsfe.wsdl.FEDetRequest;
import wsfe.wsdl.FEDummy;
import wsfe.wsdl.FEDummyResponse;
import wsfe.wsdl.FEParamGetTiposCbte;
import wsfe.wsdl.FEParamGetTiposCbteResponse;
import wsfe.wsdl.FERecuperaLastCbteResponse;
import wsfe.wsdl.Obs;
import wsfe.wsdl.Tributo;

/**
 * @author pascualg
 *
 */
@Component
public class WSFEClient extends WebServiceGatewaySupport{
	
	private String errors;
	private String events;
	private boolean hasIssues; 
	private String observaciones;
	private String cae;
	private String estadoCAE;
	private Date vencimientoCAE;
	
	@Autowired
    private WebServiceTemplate webServiceTemplate;
		
	private void checkErrors(ArrayOfErr errors, ArrayOfEvt events){
		this.hasIssues = false;
		this.errors = "";
		this.events = "";
		//Check for errors
		if (errors != null){
			for (Err error : errors.getErr()){
				if (this.errors != "")
					this.errors += "\n";
				this.errors += "Error: (" + Integer.toString(error.getCode()) + ") " + error.getMsg();
				this.hasIssues = true;
			}
		}
		//Check for events
		if (events != null){
			for (Evt event : events.getEvt()){
				if (this.events != "")
					this.events += "\n";
				this.events += "Event: (" + Integer.toString(event.getCode()) + ") " + event.getMsg();
				this.hasIssues = true;
			}
		}
	}
	
	public String dummy(String serviceURL){
		FEDummy request = new FEDummy();
		webServiceTemplate.setDefaultUri(serviceURL);  
		FEDummyResponse response = (FEDummyResponse) webServiceTemplate
				.marshalSendAndReceive(request);
		
		DummyResponse dummyResponse = response.getFEDummyResult();
		String result = "AppServer: " + dummyResponse.getAppServer() + " /n " +
				"DbServer: " + dummyResponse.getDbServer() + " /n " +
				"AuthServer: " + dummyResponse.getAuthServer();
		return result;
	}
	
	//FECAESolicitar - FACTURA!!
	public String solicitarCAE(TRA tra, Factura fact){
		String result = null;
		webServiceTemplate.setDefaultUri(tra.getTargetServiceUrl());
		//Create child  requests that later will be added to the main one... 
		// Auth
		FEAuthRequest auth = new FEAuthRequest();
		auth.setCuit(Long.parseLong(tra.getCuit()));
		auth.setToken(tra.getToken());
		auth.setSign(tra.getSign());
		// FeCabReq -> FeCAEReq
		FECAECabRequest feCabReq = new FECAECabRequest();
		feCabReq.setCbteTipo(fact.getTipoComprobante());
		feCabReq.setPtoVta(fact.getPuntoVenta());
		feCabReq.setCantReg(1);
		// FeDetReq -> FeCAEReq
		FECAEDetRequest detReq = new FECAEDetRequest();
		detReq.setConcepto(fact.getConcepto());
		detReq.setDocTipo(fact.getTipoDocumentoCliente());
		detReq.setDocNro(Long.parseLong(fact.getNumeroDocumentoCliente()));
		detReq.setCbteDesde(fact.getNumeroComprobante());
		detReq.setCbteHasta(fact.getNumeroComprobante());
		detReq.setCbteFch(date2YMD(fact.getFechaComprobante()));
		detReq.setImpTotal(fact.getImporteTotal());
		detReq.setImpTotConc(fact.getImporteNoGravado());
		detReq.setImpNeto(fact.getImporteGravado());
		detReq.setImpOpEx(fact.getImporteExcento());
		detReq.setImpTrib(fact.getImporteTributos());
		detReq.setImpIVA(fact.getImporteIva());
		detReq.setMonId(fact.getCodigoMoneda());
		detReq.setMonCotiz(fact.getCotizacionMoneda());
		if (detReq.getConcepto() != 1){
			detReq.setFchServDesde(date2YMD(fact.getFechaServicioDesde()));
			detReq.setFchServHasta(date2YMD(fact.getFechaServicioHasta()));
			detReq.setFchVtoPago(date2YMD(fact.getFechaVencimiento()));
		}
		// Comprobantes asociados
		if (fact.getComprobantesAsociados().size() > 0){
			MyArrayOfCbteAsoc cbtes = new MyArrayOfCbteAsoc();
			for (ComprobanteAsociado factComp : fact.getComprobantesAsociados()){
				CbteAsoc cbte = new CbteAsoc();
				cbte.setTipo(factComp.getTipo());
				cbte.setPtoVta(factComp.getPuntoVenta());
				cbte.setNro(factComp.getNumero());
				cbtes.addCbteAsoc(cbte);
			}
			detReq.setCbtesAsoc(cbtes);
		}
		// Tributos
		if (fact.getTributos().size() > 0){
			MyArrayOfTributo tributos = new MyArrayOfTributo();
			for (ar.com.integrarsoluciones.jafipws.Tributo factTrib : fact.getTributos()){
				Tributo trib = new Tributo();
				trib.setId(factTrib.getCodigo());
				trib.setDesc(factTrib.getDescripcion());
				trib.setBaseImp(factTrib.getBaseImponible());
				trib.setAlic(factTrib.getBaseImponible());
				trib.setImporte(factTrib.getImporte());
				tributos.addTributo(trib);
			}
			detReq.setTributos(tributos);
		}
		// Alicuotas de IVA
		if (fact.getAlicuotasIva().size() > 0){
			MyArrayOfAlicIva alicuotasIva = new MyArrayOfAlicIva();
			for (AlicuotaIVA factIva : fact.getAlicuotasIva()){
				AlicIva iva = new AlicIva();
				iva.setId(factIva.getCodigo());
				iva.setBaseImp(factIva.getBaseImponible());
				iva.setImporte(factIva.getImporte());
				alicuotasIva.addAlicuotaIva(iva);
			}
			detReq.setIva(alicuotasIva);
		}
		// Array of FECAEDetRequest
		MyArrayOfFECAEDetRequest detRequests = new MyArrayOfFECAEDetRequest();
		detRequests.addCAEDetRequest(detReq);
		// FeCAEReq
		FECAERequest feCaeReq = new FECAERequest();
		feCaeReq.setFeCabReq(feCabReq);
		feCaeReq.setFeDetReq(detRequests); 
		
		//FECAESolicitar
		FECAESolicitar solicitudCae = new FECAESolicitar();
		solicitudCae.setAuth(auth);
		solicitudCae.setFeCAEReq(feCaeReq);
		
		//call webservice and get response
		FECAESolicitarResponse response = (FECAESolicitarResponse) webServiceTemplate
				.marshalSendAndReceive(solicitudCae);
		FECAEResponse caeResponse = response.getFECAESolicitarResult();
		//fill result and check for errors
		if (caeResponse != null){
			for (FECAEDetResponse caeDetResp : caeResponse.getFeDetResp().getFECAEDetResponse()){
				this.cae = caeDetResp.getCAE();
				result = caeDetResp.getCAE();
				this.estadoCAE = caeDetResp.getResultado();
				this.vencimientoCAE = dateStr2Date(caeDetResp.getCAEFchVto());
				this.observaciones = "";
				if (caeDetResp.getObservaciones() != null){
					for (Obs obs : caeDetResp.getObservaciones().getObs()){
						if (this.observaciones != "")
							this.observaciones += "\n";
						this.observaciones += "Obs: (" + Integer.toString(obs.getCode()) + ") " + obs.getMsg();
					}
				}
			}
		}
		checkErrors(caeResponse.getErrors(), caeResponse.getEvents());
		return result;
	}
	
	
	public int getUltimoComprobante(TRA tra, int puntoVenta, int tipoComprobante){
		int result = -1;
		webServiceTemplate.setDefaultUri(tra.getTargetServiceUrl());
		//Create request and params, including Auth
		FEAuthRequest auth = new FEAuthRequest();
		auth.setCuit(Long.parseLong(tra.getCuit()));
		auth.setToken(tra.getToken());
		auth.setSign(tra.getSign());
		FECompUltimoAutorizado request = new FECompUltimoAutorizado();
		request.setAuth(auth);
		request.setCbteTipo(tipoComprobante);
		request.setPtoVta(puntoVenta);
		//call webservice and get response
		FECompUltimoAutorizadoResponse response = (FECompUltimoAutorizadoResponse) webServiceTemplate
				.marshalSendAndReceive(request);
		FERecuperaLastCbteResponse ultimoComprobante = response.getFECompUltimoAutorizadoResult();
		//fill result and check for errors
		if (ultimoComprobante != null)
			result = ultimoComprobante.getCbteNro();
		checkErrors(ultimoComprobante.getErrors(), ultimoComprobante.getEvents());
		return result;
	}
	
	public Factura getComprobante(TRA tra, int tipoComprobante, int puntoVenta, long numeroComprobante){
		webServiceTemplate.setDefaultUri(tra.getTargetServiceUrl());
		//Create request and params, including Auth
		FEAuthRequest auth = new FEAuthRequest();
		auth.setCuit(Long.parseLong(tra.getCuit()));
		auth.setToken(tra.getToken());
		auth.setSign(tra.getSign());
		
		FECompConsultaReq compRequest = new FECompConsultaReq();
		compRequest.setCbteTipo(tipoComprobante);
		compRequest.setPtoVta(puntoVenta);
		compRequest.setCbteNro(numeroComprobante);
		
		FECompConsultar request = new FECompConsultar();
		request.setAuth(auth);
		request.setFeCompConsReq(compRequest);
		//call webservice and get response
		FECompConsultarResponse response = (FECompConsultarResponse) webServiceTemplate
				.marshalSendAndReceive(request);
		FECompConsResponse comprobante = response.getFECompConsultarResult().getResultGet();
		//fill result and check for errors
		Factura fact = null;
		if (comprobante != null){
			fact = new Factura();
			fact.setPuntoVenta(comprobante.getPtoVta());
			fact.setNumeroComprobante(numeroComprobante);
			fact.setTipoComprobante(comprobante.getCbteTipo());
			fact.setCuit(tra.getCuit());
			fact.setFechaComprobante(dateStr2Date(comprobante.getCbteFch()));
			fact.setTipoDocumentoCliente(comprobante.getDocTipo());
			fact.setNumeroDocumentoCliente(Long.toString(comprobante.getDocNro()));
			fact.setConcepto(comprobante.getConcepto());
			fact.setImporteTotal(comprobante.getImpTotal());
			fact.setImporteNoGravado(comprobante.getImpTotConc());
			fact.setImporteGravado(comprobante.getImpNeto());
			fact.setImporteExcento(comprobante.getImpOpEx());
			fact.setImporteTributos(comprobante.getImpTrib());
			fact.setImporteIva(comprobante.getImpIVA());
			fact.setFechaServicioDesde(dateStr2Date(comprobante.getFchServDesde()));
			fact.setFechaServicioHasta(dateStr2Date(comprobante.getFchServHasta()));
			fact.setFechaVencimiento(dateStr2Date(comprobante.getFchVtoPago()));
			fact.setCodigoMoneda(comprobante.getMonId());
			fact.setCotizacionMoneda(comprobante.getMonCotiz());
			fact.setCae(comprobante.getCodAutorizacion());
			fact.setEstadoCae(comprobante.getResultado());
			fact.setVencimientoCae(dateStr2Date(comprobante.getFchVto()));
			//Parse Comprobantes Asociados
			if (comprobante.getCbtesAsoc() != null){
				for (CbteAsoc cbte : comprobante.getCbtesAsoc().getCbteAsoc()){
					ComprobanteAsociado nCbte = new ComprobanteAsociado((short) cbte.getTipo(),cbte.getPtoVta(),cbte.getNro());
					fact.agregarComprobante(nCbte);
				}
			}
			//Parse Tributos
			if (comprobante.getTributos() != null){
				for (Tributo tributo : comprobante.getTributos().getTributo()){
					ar.com.integrarsoluciones.jafipws.Tributo nTributo = 
							new ar.com.integrarsoluciones.jafipws.Tributo(tributo.getId(),tributo.getDesc(),tributo.getBaseImp(),tributo.getAlic(),tributo.getImporte());
					fact.agregarTributo(nTributo);
				}
			}
			//Parse IVA
			if (comprobante.getIva() != null){
				for (AlicIva iva : comprobante.getIva().getAlicIva()){
					AlicuotaIVA nIva = new AlicuotaIVA((short) iva.getId(), iva.getBaseImp(), iva.getImporte());
					fact.agregarIVA(nIva);
				}
			}
			//Parse Observaciones CAE
			if (comprobante.getObservaciones() != null){
				String obs = "";
				for (Obs observacion : comprobante.getObservaciones().getObs()){
					obs += "Obs: (" + Integer.toString(observacion.getCode()) + ") " + observacion.getMsg() + "\n";
				}
				fact.setObservacionesCae(obs);
			}
		}
		checkErrors(response.getFECompConsultarResult().getErrors(), response.getFECompConsultarResult().getEvents());
		return fact;
	}
	
	
	public String getParamTiposComprobante(TRA tra){
		String result = "";
		webServiceTemplate.setDefaultUri(tra.getTargetServiceUrl());
		FEAuthRequest auth = new FEAuthRequest();
		auth.setCuit(Long.parseLong(tra.getCuit()));
		auth.setToken(tra.getToken());
		auth.setSign(tra.getSign());
		
		FEParamGetTiposCbte request = new FEParamGetTiposCbte();
		request.setAuth(auth);
		
		FEParamGetTiposCbteResponse response = (FEParamGetTiposCbteResponse) webServiceTemplate
				.marshalSendAndReceive(request);
		
		CbteTipoResponse cbteTipoResponse = response.getFEParamGetTiposCbteResult();
		ArrayOfCbteTipo tiposCbte = cbteTipoResponse.getResultGet();
		
		for (CbteTipo cbte : tiposCbte.getCbteTipo())
		{
			result = result + Integer.toString(cbte.getId()) + "_:_" + cbte.getDesc() + "\n";
		}
		
		return result;
	}
	
	private Date dateStr2Date(String strDate)
	{
        Date date = null;
        if (strDate.length() > 0){
			XMLGregorianCalendar xmlDate = null;
			try
			{
				xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(strDate);
				date = new Date();
				date = xmlDate.toGregorianCalendar().getTime();
			}
			catch (Exception e)
			{
				this.logger.error(e.toString());
			}
        }
		return date;
	}
	
	private String date2Str(Date dte)
	{
		String strDate = ""; 
		GregorianCalendar gregCal = new GregorianCalendar();
		gregCal.setTime(dte);
		XMLGregorianCalendar xmlDate = null;
		try
		{
			xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal);
			strDate = xmlDate.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return strDate;
	}
	
	private String date2YMD(Date dte)
	{
		String strDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		strDate = sdf.format(dte);
		return strDate;
	}

	/**
	 * @return the errors
	 */
	public String getErrors() {
		return errors;
	}

	/**
	 * @return the events
	 */
	public String getEvents() {
		return events;
	}

	/**
	 * @return the hasIssues
	 */
	public boolean hasIssues() {
		return hasIssues;
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @return the cae
	 */
	public String getCae() {
		return cae;
	}

	/**
	 * @return the estadoCAE
	 */
	public String getEstadoCAE() {
		return estadoCAE;
	}

	/**
	 * @return the vencimientoCAE
	 */
	public Date getVencimientoCAE() {
		return vencimientoCAE;
	}
	

}
