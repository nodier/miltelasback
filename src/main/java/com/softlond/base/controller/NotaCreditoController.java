package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.FacturaM;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.service.NotaCreditoService;

import org.springframework.http.HttpEntity;
import org.apache.log4j.Logger;
import com.google.api.client.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(ApiConstant.NOTA_CREDITO_API)
public class NotaCreditoController {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	public NotaCreditoService notaCreditoService;

	// Crear
	@PostMapping(value = ApiConstant.NOTA_CREDITO_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crearNotaDebito(@RequestBody NotaCredito nuevoNotaCredito)
			throws Exception {
		logger.info("ingresa por nota credito proveedor crear");
		return notaCreditoService.crearNotaCredito(nuevoNotaCredito);
	}

	// Actualizar
	@PostMapping(value = ApiConstant.NOTA_CREDITO_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizarNotaCredito(@RequestBody NotaCredito actualizarNotaCredito) {
		return notaCreditoService.actualizarNotaCredito(actualizarNotaCredito);
	}

	// Obtener numero
	@GetMapping(value = ApiConstant.NOTA_CREDITO_API_OBTENER_NUMERO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerNumero(Integer idSede) {
		return notaCreditoService.obtenerNumero(idSede);
	}

	// busqueda por filtro
	@GetMapping(value = ApiConstant.NOTA_CREDITO_API_BUSCAR_FILTRO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerNCFiltros(String numeroDocumento, String fechaInicial,
			String fechaFinal, String estadoDocumento, Integer page) throws Exception {
		return notaCreditoService.obtenerNcFiltros(numeroDocumento, fechaInicial, fechaFinal, estadoDocumento, page);
	}

	// obtener remision por numero
	@GetMapping(value = ApiConstant.NOTA_CREDITO_API_BUSCAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerNC(String numeroDocumento) {
		return notaCreditoService.obtenerNC(numeroDocumento);
	}

	// Listar notas credito del mes
	@GetMapping(value = ApiConstant.NOTA_CREDITO_API_LISTAR_MES, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarnotacreditoMes(Integer page) {
		return notaCreditoService.obtenerNotasCreditoMes(page);
	}

	@DeleteMapping(value = ApiConstant.NOTA_CREDITO_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> eliminarNotaCredito(Integer idNotaCredito) {
		return notaCreditoService.eliminarNotaCredito(idNotaCredito);
	}

	@GetMapping(value = ApiConstant.NOTA_CREDITO_API_LISTAR_ANULACION, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarNotasCreditoAnulacion(String numero) {
		return notaCreditoService.obtenerNotasCreditoAnular(numero);
	}

	@PostMapping(value = ApiConstant.NOTA_CREDITO_API_ANULAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> anularNotaDebito(@RequestBody NotaCredito notaCredito) {
		return notaCreditoService.AnularNotaCredito(notaCredito);
	}

	@GetMapping(value = ApiConstant.NOTA_CREDITO_API_LISTAR_PENDIENTE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarnotacreditoPendiente(Integer idProveedor, Integer page) {
		return notaCreditoService.obtenerNotasCreditoPendientes(idProveedor, page);
	}

	@GetMapping(value = ApiConstant.NOTA_CREDITO_API_LISTAR_PENDIENTE_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerNotasCreditoPendientesActualizar(Integer idProveedor,
			Integer idCredito, Integer page) {
		return notaCreditoService.obtenerNotasCreditoPendientesActualizar(idProveedor, idCredito, page);
	}
}
