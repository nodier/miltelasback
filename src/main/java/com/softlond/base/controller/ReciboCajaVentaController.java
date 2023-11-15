package com.softlond.base.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpHeaders;
import com.google.api.client.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.ConceptosReciboCaja;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.ReciboCajaVenta;
import com.softlond.base.entity.Secundario;
import com.softlond.base.service.ReciboCajaVentaService;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

// import com.softlond.base.repository.FacturaDao;

@RestController
@RequestMapping(ApiConstant.RECIBO_CAJA_API)
public class ReciboCajaVentaController {

	private static final Logger logger = Logger.getLogger(ReciboCajaVentaService.class);

	@Autowired
	public ReciboCajaVentaService reciboCajaService;

	// Crear recibo de caja siendo user
	@PostMapping(value = ApiConstant.RECIBO_CAJA_API_GUARDAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> guardarRecibo(@RequestBody ReciboCajaVenta reciboNuevo) throws Exception {

		// if (reciboNuevo != null && (reciboNuevo.getIdSede().getId() == 7 ||
		// reciboNuevo.getIdSede().getId() == 15)) {
		// String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";
		// Secundario reciboCajaMekano = new Secundario();
		// reciboCajaMekano.setCLAVE("Set_Gestion_Secundario");
		// reciboCajaMekano.setTIPO("RC1");
		// reciboCajaMekano.setPREFIJO(reciboNuevo.getPrefijo().toString());
		// reciboCajaMekano.setNUMERO(reciboNuevo.getCodRbocajaventa().toString());
		// reciboCajaMekano.setFECHA("12.10.2022");
		// reciboCajaMekano.setVENCE("12.11.2022");
		// reciboCajaMekano.setNOTA(reciboNuevo.getObservaciones().toString());
		// reciboCajaMekano.setTERCERO(reciboNuevo.getIdCliente().getNitocc().toString());
		// // !se envia un valor por defecto porque el recibo de caja no tiene un
		// vendedor
		// // asociado
		// reciboCajaMekano.setVENDEDOR("24347052");
		// reciboCajaMekano.setBANCO("CG");
		// reciboCajaMekano.setUSUARIO("SUPERVISOR");
		// reciboCajaMekano.setTIPO_REF("FE1");
		// // reciboCajaMekano.setPREFIJO_REF(concepto.getCodDocumento());
		// reciboCajaMekano.setPREFIJO_REF("MT5");
		// reciboCajaMekano.setNUMERO_REF("173");
		// // Integer Abono = reciboNuevo.getTotalRecibo() - reciboNuevo.getSaldo();
		// // Integer Abono =
		// //
		// Integer.parseInt(reciboNuevo.getConceptos().get(0).getValorAbono().toString());
		// Integer Abono = 0;
		// logger.info(Abono);
		// reciboCajaMekano.setABONO(Abono);

		// Gson gson = new Gson();
		// String rjson = gson.toJson(reciboCajaMekano);

		// logger.info(rjson);
		// HttpHeaders headers = new HttpHeaders();
		// headers.setContentType(MediaType.APPLICATION_JSON_VALUE);
		// HttpEntity<String> entity = new HttpEntity<String>(rjson);
		// RestTemplate rest = new RestTemplate();
		// String result = rest.postForObject(uri, entity, String.class);
		// logger.info(result);
		// // }
		// }

		return reciboCajaService.guardarRecibo(reciboNuevo);
	}

	// Obtener recibo de caja por cliente
	@GetMapping(value = ApiConstant.RECIBO_CAJA_API_OBTENER, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerReciboCliente(Integer idCliente) {
		return reciboCajaService.obtenerReciboCliente(idCliente);
	}

	// Obtener cambio de caja
	@GetMapping(value = ApiConstant.RECIBO_CAJA_API_OBTENER_CAMBIO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerCambio(Integer idCaja, String fecha) {
		return reciboCajaService.obtenerCambioCaja(idCaja, fecha);
	}

	// Obtener cambio de caja
	@GetMapping(value = ApiConstant.RECIBO_CAJA_API_OBTENER_CAMBIO_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerCambioSede(Integer idSede, String fecha) {
		return reciboCajaService.obtenerCambioSede(idSede, fecha);
	}

	// Obtener recibo de caja
	@GetMapping(value = ApiConstant.RECIBO_CAJA_API_OBTENER_RECIBO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerRecibo(@RequestParam String numRecibo) {
		return reciboCajaService.obtenerRecibo(numRecibo);
	}

	// Anular recibo de caja
	@PostMapping(value = ApiConstant.RECIBO_CAJA_API_ANULAR_RECIBO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> anularRecibo(@RequestBody ReciboCajaVenta recibo) {
		return reciboCajaService.anularRecibo(recibo);
	}

	// Obtener recibos de caja
	@GetMapping(value = ApiConstant.LISTAR_TODOS_FILTRO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerRecibosCaja(String fechaInicio, String fechaFin, String estado,
			String numeroRecibo, Integer page) {
		return reciboCajaService.obtenerRecibosCaja(fechaInicio, fechaFin, estado, numeroRecibo, page);
	}

	@DeleteMapping(value = ApiConstant.RECIBO_CAJA_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> eliminarRecibosCaja(Integer idRecibo) {
		return reciboCajaService.eliminarRecibo(idRecibo);
	}

	// actualizar recibo de caja siendo user
	@PostMapping(value = ApiConstant.RECIBO_CAJA_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizarRecibo(@RequestBody ReciboCajaVenta reciboNuevo) {
		return reciboCajaService.actualizarRecibo(reciboNuevo);
	}

	// Obtener recibos de caja
	@GetMapping(value = ApiConstant.LISTAR_TODOS_EXPORTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerRecibosCajaExportar(String fechaInicio, String fechaFin,
			String estado,
			String numeroRecibo) {
		return reciboCajaService.obtenerRecibosCajaParaExportar(fechaInicio, fechaFin, estado, numeroRecibo);
	}

}
