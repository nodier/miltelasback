package com.softlond.base.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
// import java.util.Date;
import java.sql.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaM;
import com.softlond.base.service.FacturaService;

import com.google.gson.Gson;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.util.DateTime;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping(ApiConstant.FACTURA_API)
public class FacturaController {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private FacturaService facturaService;

	@GetMapping(value = ApiConstant.FACTURA_API_LISTAR_TODOS_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerFacturasSede(Integer page) {
		return facturaService.listarFacturasSede(page);
	}

	@GetMapping(value = ApiConstant.FACTURA_API_LISTAR_FECHA_DIA_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerFacturasFechaSede(Integer page) {
		return facturaService.listarFacturasFechaSede(page);
	}

	@GetMapping(value = ApiConstant.FACTURA_API_LISTAR_VENTAS_CREDITO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerFacturasVentaCredito(Integer page) {
		return facturaService.listarFacturasVentaCredito(page);
	}

	@GetMapping(value = ApiConstant.FACTURA_API_LISTAR_VENTAS_PAGO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerFacturasVentaPago(Integer page) {
		return facturaService.listarFacturasVentaPago(page);
	}

	@GetMapping(value = ApiConstant.FACTURA_API_BUSCAR_FACTURA_NUMERO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerFacturaNumero(Integer numeroFactura) {
		return facturaService.obtenerFacturaNumero(numeroFactura);
	}

	@GetMapping(value = ApiConstant.FACTURA_API_BUSCAR_FECHA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerFacturaFecha(String fechaInicial, String fechaFinal,
			Integer page) {

		return facturaService.obtenerFacturaFecha(fechaInicial, fechaFinal, page);
	}

	@GetMapping(value = ApiConstant.FACTURA_API_LISTAR_CLIENTE_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerFacturasClienteSede(Integer idCliente, Integer page) {
		return facturaService.listarFacturasClienteSede(idCliente, page);
	}

	@PostMapping(value = ApiConstant.FACTURA_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crerFactura(@RequestBody Factura factura) throws Exception {

		// nota: no se puede utilizar el metodo de creacion de facturas de mekano porque
		// falta la informacion del numero de la factura que depende de la secuencia (se
		// crea en el service

		return facturaService.crearFactura(factura);
	}

	@GetMapping(value = ApiConstant.FACTURA_API_LISTAR_FACTURAS_CREDITO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerConceptoFactura(Integer idUsuario) {
		return facturaService.obtenerConceptoFactura(idUsuario);
	}

	@GetMapping(value = ApiConstant.FACTURA_API_BUSQUEDA_FILTROS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerFacturaFiltros(Integer idCliente, Integer numeroFactura,
			String fechaInicial, String fechaFinal, Integer estado, String idSede, Integer page, String orden)
			throws Exception {
		return facturaService.obtenerFacturaFiltros(idCliente, numeroFactura, fechaInicial, fechaFinal, estado, idSede,
				page, orden);
	}

	@GetMapping(value = ApiConstant.FACTURA_API_LISTAR_FACTURAS_INFORME, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerFacturasInforme(String fechaInicio, String fechaFin, Boolean sort,
			String tipoOrdenamiento, String estado, Integer numeroFactura, Integer vendedor, @PathVariable Integer page) {
		return facturaService.obtenerFacturasParaInformeVendedores(fechaInicio, fechaFin, sort, tipoOrdenamiento, estado,
				numeroFactura, vendedor, page);
	}

	@GetMapping(value = ApiConstant.FACTURA_API_LISTAR_FACTURAS_INFORME_TOTAL, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerTotalFacturas(String fechaInicio, String fechaFin, String estado,
			Integer numeroFactura, Integer vendedor) {
		return facturaService.obtenerTotalDeFacturasParaInformeVendedores(fechaInicio, fechaFin, estado, numeroFactura,
				vendedor);
	}

	// Anular factura
	@PostMapping(value = ApiConstant.FACTURA_API_ANULAR_FACTURA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> anularRecibo(@RequestBody Factura factura) throws Exception {
		return facturaService.anularFactura(factura);
	}

	/* Listar todos los clientes Especiales */
	@GetMapping(value = ApiConstant.FACTURA_API_LISTAR_ESPECIALES, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerClientesEspeciales() {
		return facturaService.obtenerClientesEspeciales();
	}

	/* Listar todos los clientes Frecuentes */
	@GetMapping(value = ApiConstant.FACTURA_API_LISTAR_FRECUENTES, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerClientesFrecuentes() {
		return facturaService.obtenerClientesFrecuentes();
	}

	@GetMapping(value = ApiConstant.FACTURA_API_BUSCAR_POR_SEDE_CLIENTE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerFacturasCompraSedeCliente(Integer idSede, Integer idCliente) {
		return facturaService.obtenerNumeroFactura(idSede, idCliente);
	}

	@GetMapping(value = ApiConstant.FACTURA_API_BUSCAR_POR_CLIENTE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerFacturasCompraCliente(Integer idCliente) {
		return facturaService.obtenerFacturasCliente(idCliente);
	}

	@GetMapping(value = ApiConstant.FACTURA_API_BUSCAR_POR_CLIENTE_PENDIENTE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerFacturasPendientesCompraCliente(Integer idCliente,
			Integer page) {
		return facturaService.obtenerFacturasPendientesCliente(idCliente, page);
	}

	@PostMapping(value = ApiConstant.FACTURA_API_UPDATE_RETENCION, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizarFacturaRetenciones(@RequestBody Factura factura) {
		return facturaService.ActualizarRetencion(factura);
	}

	@PostMapping(value = ApiConstant.FACTURA_API_OBTENER_SALDO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerSaldoFactura(@RequestBody Factura factura) {
		return facturaService.obtenerSaldo(factura);
	}

}
