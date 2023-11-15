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
import org.apache.log4j.Logger;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaCreditoCliente;
import com.softlond.base.service.NotaCreditoClienteService;

@RestController
@RequestMapping(ApiConstant.NOTA_CREDITO_CLIENTE_API)
public class NotaCreditoClienteController {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	public NotaCreditoClienteService notacreditoClienteService;

	// Crear
	@PostMapping(value = ApiConstant.NOTA_CREDITO_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crearNotaDebito(@RequestBody NotaCreditoCliente nuevoNotaCredito)
			throws Exception {
		logger.info("ingresa a crear nota credito cliente por la ruta incorrecta");
		return notacreditoClienteService.crearNotaCredito(nuevoNotaCredito);
	}

	// Actualizar
	@PostMapping(value = ApiConstant.NOTA_CREDITO_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizarNotaCredito(
			@RequestBody NotaCreditoCliente actualizarNotaCredito) {
		return notacreditoClienteService.actualizarNotaCredito(actualizarNotaCredito);
	}

	// Obtener numero
	@GetMapping(value = ApiConstant.NOTA_CREDITO_API_OBTENER_NUMERO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerNumero(Integer idSede) {
		return notacreditoClienteService.obtenerNumero(idSede);
	}

	// busqueda por filtro
	@GetMapping(value = ApiConstant.NOTA_CREDITO_CLIENTE_API_LISTAR_TODOS_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerNDFiltros(String fechaInicial,
			String fechaFinal, String numeroDocumento, String estadoDocumento, Integer page) {
		return notacreditoClienteService.obtenerNotaCreditoClienteConsulta(fechaInicial, fechaFinal, numeroDocumento,
				estadoDocumento, page);
	}

	// obtener remision por numero
	@GetMapping(value = ApiConstant.NOTA_CREDITO_API_BUSCAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerNC(String numeroDocumento) {
		return notacreditoClienteService.obtenerNC(numeroDocumento);
	}

	// Listar notas credito del mes
	@GetMapping(value = ApiConstant.NOTA_CREDITO_API_LISTAR_MES, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarnotacreditoMes(Integer page) {
		return notacreditoClienteService.obtenerNotasCreditoMes(page);
	}

	@DeleteMapping(value = ApiConstant.NOTA_CREDITO_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> eliminarNotaCredito(Integer idNotaCredito) {
		return notacreditoClienteService.eliminarNotaCredito(idNotaCredito);
	}

	@GetMapping(value = ApiConstant.NOTA_CREDITO_API_LISTAR_PENDIENTE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarnotacreditoPendiente(Integer idCliente, Integer page) {
		return notacreditoClienteService.obtenerNotasCreditoPendientes(idCliente, page);
	}

	@GetMapping(value = ApiConstant.NOTA_CREDITO_API_LISTAR_PENDIENTE_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerNotasCreditoPendientesActualizar(Integer idCliente,
			Integer idCredito, Integer page) {
		return notacreditoClienteService.obtenerNotasCreditoPendientesActualizar(idCliente, idCredito, page);
	}

}
