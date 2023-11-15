package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.Cuenta;
import com.softlond.base.service.CuentaService;

@Controller
@RequestMapping(ApiConstant.CUENTA_API)
public class CuentaController {
	
	@Autowired
	CuentaService cuentaService;
	
	@PostMapping(value = ApiConstant.CUENTA_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crearCuenta(@RequestBody Cuenta cuenta) {
		return cuentaService.crearCuenta(cuenta);
	}
	
	@GetMapping(value = ApiConstant.CUENTA_API_LISTAR_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerCuentas() {
		return cuentaService.listarTodasCuentas();
	}
	
	@GetMapping(value = ApiConstant.CUENTA_API_LISTAR_TODOS_DEBITO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerCuentasDebito() {
		return cuentaService.listarCuentasNotaDebito();
	}
	
	@GetMapping(value = ApiConstant.CUENTA_API_LISTAR_TODOS_DEBITO_CLIENTE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerCuentasDebitoCliente() {
		return cuentaService.obtenerCuentasDebitoCliente();
	}
	
	@GetMapping(value = ApiConstant.CUENTA_API_LISTAR_TODOS_CREDITO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerCuentasCredito() {
		return cuentaService.listarCuentasNotaCredito();
	}
	
	@GetMapping(value = ApiConstant.CUENTA_API_LISTAR_PRECARGADAS_DEVOLUCION, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerCuentasPrecargadasDevolucion() {
		return cuentaService.listarCuentasPrecargadasDevolucion();
	}
	
	@GetMapping(value = ApiConstant.CUENTA_API_LISTAR_TODOS_CREDITO_BANCARIA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerCuentasCreditoBancarias() {
		return cuentaService.listarCuentasBancariasCredito();
	}
	
	@GetMapping(value = ApiConstant.CUENTA_API_LISTAR_TODOS_DEBITO_BANCARIA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerCuentasDebitoBancarias() {
		return cuentaService.listarCuentasBancariasDebito();
	}
	
	@GetMapping(value = ApiConstant.CUENTA_API_LISTAR_TODOS_COMPROBANTE_SERVICIOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerCuentasComprobanteServicios() {
		return cuentaService.listarCuentasComprobanteServicios();
	}
	
	@GetMapping(value = ApiConstant.CUENTA_API_LISTAR_TODOS_COMPROBANTE_GASTOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerCuentasComprobandeGastos() {
		return cuentaService.listarCuentasComprobanteGastos();
	}
	@GetMapping(value = ApiConstant.CUENTA_API_LISTAR_TODOS_CREDITO_CLIENTE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarCuentasNotaCreditoCliente() {
		return cuentaService.listarCuentasNotaCreditoCliente();
	}
	
	@GetMapping(value = ApiConstant.CUENTA_API_LISTAR_TODOS_EFECTIVO_BANCARIA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerCuentasEfectivoBancarias() {
		return cuentaService.listarCuentasBancariasEfectivo();
	}
	
	@GetMapping(value = ApiConstant.CUENTA_API_LISTAR_TODOS_CUENTAS_BANCARIAS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarCuentasBancarias() {
		return cuentaService.listarCuentasBancarias();
	}
	
	@GetMapping(value = ApiConstant.CUENTA_API_LISTAR_TODOS_CUENTAS_PROVEEDORES, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarCuentasProveedores() {
		return cuentaService.listarCuentasProveedores();
	}
	
	@GetMapping(value = ApiConstant.CUENTA_API_LISTAR_TODOS_CREDITO_CLIENTES, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerCuentasCreditoClientes() {
		return cuentaService.listarCuentasNotaCreditoClientes();
	}
	
	@GetMapping(value = ApiConstant.CUENTA_API_LISTAR_PRECARGADAS_DEVOLUCION_CLIENTES, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerCuentasPrecargadasDevolucionClientes() {
		return cuentaService.listarCuentasPrecargadasDevolucionClientes();
	}

}


