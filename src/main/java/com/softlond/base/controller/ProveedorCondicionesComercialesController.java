package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.ProveedorCondicionesComerciales;
import com.softlond.base.service.ProveedorCondicionesComercialesService;

@Controller
@RequestMapping(ApiConstant.CONDICION_COMERCIAL_PROVEEDOR_API)
public class ProveedorCondicionesComercialesController {

	@Autowired
	private ProveedorCondicionesComercialesService proveedorCondicionesComercialesService;
	
	@PostMapping(value = ApiConstant.CONDICION_COMERCIAL_PROVEEDOR_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> crearProductoProveedor(@RequestBody ProveedorCondicionesComerciales condicionComercial){
		return this.proveedorCondicionesComercialesService.crearCondicionComercial(condicionComercial);
	}
	
	
	@GetMapping(value = ApiConstant.CONDICION_COMERCIAL_PROVEEDOR_API_LISTAR_PROVEEDOR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerProdcutosProveedores(Integer idProveedor){
		return this.proveedorCondicionesComercialesService.ObtenerCondicionesComercialesbyProveedor(idProveedor);
	}
	
	@DeleteMapping(value = ApiConstant.CONDICION_COMERCIAL_PROVEEDOR_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> eliminarProductoProveedor(Integer idProveedorProducto){
		return this.proveedorCondicionesComercialesService.eliminarCondicionComercial(idProveedorProducto);
	}
	
	@GetMapping(value = ApiConstant.CONDICION_COMERCIAL_PROVEEDOR_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerCondicionesComerciales(){
		return this.proveedorCondicionesComercialesService.ObtenerCondicionesComerciales();
	}
}
