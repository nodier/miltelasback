package com.softlond.base.controller;

// import org.hibernate.annotations.common.util.impl.Log_.logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.ContableM;
import com.softlond.base.entity.ContableMD;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.RemisionCompra;
import com.softlond.base.request.FacturaCompraRemisionRequest;
import com.softlond.base.service.FacturaCompraService;
import java.util.Date;

import java.text.ParseException;

import org.apache.log4j.Logger;

@Controller
@RequestMapping(ApiConstant.FACTURA_COMPRA_API)
public class FacturaCompraController {
	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private FacturaCompraService facturaCompraService;

	@PostMapping(value = ApiConstant.FACTURA_COMPRA_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crerFactura(@RequestBody FacturaCompraRemisionRequest factura)
			throws Exception {
		return facturaCompraService.crearFactura(factura);
	}

	// @PostMapping(value = ApiConstant.FACTURA_COMPRA_API_CREAR_MEKANO, produces =
	// MediaType.APPLICATION_JSON_VALUE)
	// public @ResponseBody ResponseEntity<Object>
	// crearFacturaCompraMekano(@RequestBody ContableM contable,
	// Date fechaFactura) {
	// logger.info(contable);
	// logger.info(fechaFactura);
	// return facturaCompraService.crearFacturaCompraMekano(contable, fechaFactura);
	// }

	@GetMapping(value = ApiConstant.FACTURA_COMPRA_API_BUSCAR_POR_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerFacturasCompra(Integer idSede) {
		return facturaCompraService.obtenerNumero(idSede);
	}

	@PostMapping(value = ApiConstant.FACTURA_COMPRA_API_DISMINUIR_VALOR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> disminuirTotal(@RequestBody FacturaCompra factura) {
		return facturaCompraService.disminuirTotal(factura);
	}

	@GetMapping(value = ApiConstant.FACTURA_COMPRA_API_LISTAR_NUMEROS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarFacturas() {
		return facturaCompraService.listarNumeroFacturas();
	}

	@GetMapping(value = ApiConstant.FACTURA_COMPRA_API_LISTAR_FACTURAS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarTodasFacturas(Integer idProveedor) {
		return facturaCompraService.listarFacturas(idProveedor);
	}

	@GetMapping(value = ApiConstant.FACTURA_COMPRA_API_LISTAR_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarFacturasConsulta(String numeroFactura, String fechaInicial,
			String fechaFinal, Integer estado, Integer proveedor, @PathVariable Integer page) {
		return facturaCompraService.listarNumeroFacturasConsulta(numeroFactura, fechaInicial, fechaFinal, estado,
				proveedor, page);
	}

	@GetMapping(value = ApiConstant.FACTURA_COMPRA_API_LISTAR_ESTADOS_CUENTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarEstadosCuenta(Integer idProveedor, @PathVariable Integer page) {
		return facturaCompraService.ObtenerFacturasEstadosCuenta(idProveedor, page);
	}

	@GetMapping(value = ApiConstant.FACTURA_COMPRA_API_TOTAL_ESTADOS_CUENTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerTotalEstadoCuentas(Integer idProveedor) {
		return facturaCompraService.ObtenerTotalFacturasEstadosCuenta(idProveedor);
	}

	@GetMapping(value = ApiConstant.FACTURA_COMPRA_API_LISTAR_ESTADOS_CUENTA_PAGAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarEstadosCuentaSede(@PathVariable Integer page) {
		return facturaCompraService.ObtenerFacturasEstadosCuentaSede(page);
	}

	@PostMapping(value = ApiConstant.FACTURA_COMPRA_API_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizar(@RequestBody FacturaCompraRemisionRequest factura) {
		return facturaCompraService.Actualizar(factura);
	}

	@DeleteMapping(value = ApiConstant.FACTURA_COMPRA_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> eliminar(Integer idFactura) {
		return facturaCompraService.eliminar(idFactura);
	}

	@GetMapping(value = ApiConstant.FACTURA_COMPRA_API_BUSCAR_POR_SEDE_PROVEEDOR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerFacturasCompraSedeProveedor(Integer idSede,
			Integer idProveedor) {
		return facturaCompraService.obtenerNumeroFactura(idSede, idProveedor);
	}

	@GetMapping(value = ApiConstant.FACTURA_COMPRA_API_LISTAR_MES, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarMes(@PathVariable Integer page) {
		return facturaCompraService.ObtenerFacturasMes(page);
	}

	@GetMapping(value = ApiConstant.FACTURA_COMPRA_API_LISTAR_TODOS_ESTADOS_CUENTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarTodosEstadosCuenta(@PathVariable Integer page) {
		return facturaCompraService.ObtenerTodasFacturasEstadosCuenta(page);
	}

	@GetMapping(value = ApiConstant.FACTURA_COMPRA_API_LISTAR_FACTURAS_ANULAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarFacturasCompraAnular(String numero) {
		return facturaCompraService.ObtenerFacturasAnular(numero);
	}

	@PostMapping(value = ApiConstant.FACTURA_COMPRA_API_ANULAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> anularFactura(@RequestBody FacturaCompra factura) {
		return facturaCompraService.anular(factura);
	}

	@GetMapping(value = ApiConstant.FACTURA_COMPRA_API_LISTAR_PENDIENTES, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarFacturasPendientes(Integer idProveedor,
			@PathVariable Integer page) {
		return facturaCompraService.ObtenerFacturasPendientes(idProveedor, page);
	}

	@PostMapping(value = ApiConstant.FACTURA_COMPRA_API_UPDATE_RETENCION, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizarFacturaRetenciones(@RequestBody FacturaCompra factura) {
		return facturaCompraService.ActualizarRetencion(factura);
	}
}
