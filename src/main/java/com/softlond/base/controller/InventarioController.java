package com.softlond.base.controller;

import org.apache.log4j.Logger;
// import org.hibernate.annotations.common.util.impl.Log_.logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.Inventario;
import com.softlond.base.entity.RowInventario;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.service.InventarioService;

@Controller
@RequestMapping(ApiConstant.INVENTARIO_API)
public class InventarioController {
	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	InventarioService inventarioService;

	@GetMapping(value = ApiConstant.INVENTARIO_API_CODIGO_ARTICULO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> buscarArticuloCodigo(String codigo) {
		return inventarioService.obtenerArticuloCodigo(codigo);
	}

	@PostMapping(value = ApiConstant.INVENTARIO_API_GUARDAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> GuardarInventario(@RequestBody Inventario inventario) {
		return inventarioService.guardarInventario(inventario);
	}

	@GetMapping(value = ApiConstant.INVENTARIO_API_VALIDAR_EXISTENCIA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> validarExistencia(String codigo) {
		return inventarioService.ValidarSiExisteArticuloInventario(codigo);
	}

	@GetMapping(value = ApiConstant.INVENTARIO_API_OBTENER_INVENTARIO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerInventario(Integer page) {
		return inventarioService.obtenerInventario(page);
	}

	@GetMapping(value = ApiConstant.INVENTARIO_API_OBTENER_INVENTARIOW, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerInventario2() {
		return inventarioService.obtenerInventario2();
	}

	@GetMapping(value = ApiConstant.INVENTARIO_API_OBTENER_INVENTARIOLOCAL, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerInventarioLocal(Integer page) {
		return inventarioService.obtenerInventarioLocal(page);
	}

	@DeleteMapping(value = ApiConstant.INVENTARIO_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> eliminarInventario() {
		return inventarioService.eliminarInventario();
	}

	@PostMapping(value = ApiConstant.INVENTARIO_API_GUARDAR_REGISTRO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> guardarInventarioRegistro(@RequestBody RowInventario registro) {
		return inventarioService.actualizarRow(registro);
	}

	@DeleteMapping(value = ApiConstant.INVENTARIO_API_ELIMINAR_REGISTRO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> eliminarInventarioRegistro(Integer idRegistro) {
		return inventarioService.eliminarRow(idRegistro);
	}

	@GetMapping(value = ApiConstant.INVENTARIO_API_LIQUIDAR_INVENTARIO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> liquidarInventario() {
		return inventarioService.liquidarInvetario();
	}

	@GetMapping(value = ApiConstant.INVENTARIO_API_ACTUALIZAR_INVENTARIO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> ActualizarInventario() {
		return inventarioService.ActualizarInvetario();
	}

	@GetMapping(value = ApiConstant.INVENTARIO_API_FILTRAR_INVENTARIO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarInventarioFiltros(Integer page, Integer local, Integer sector) {
		return inventarioService.inventariosFiltros(page, local, sector);
	}

	@GetMapping(value = ApiConstant.INVENTARIO_API_FILTRAR_INVENTARIO_WEB, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarInventarioFiltrosWeb(Integer page, Integer local, Integer sector) {
		return inventarioService.inventariosFiltrosWeb(page, local, sector);
	}

	@GetMapping(value = ApiConstant.INVENTARIO_API_ARTICULOS_FINALIZADOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerArticulosFinalizados(Integer page) {
		return inventarioService.obtenerArticulosFinalizados(page);
	}

	@GetMapping(value = ApiConstant.INVENTARIO_API_ARTICULOS_INEXISTENTES, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerArticulosInexistentes(Integer page) {
		return inventarioService.obtenerArticulosInexistentes(page);
	}

	@GetMapping(value = ApiConstant.INVENTARIO_API_REVIVIR_ARTICULOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> revivirArticulosFinalizados() {
		return inventarioService.revivirArticulosFinalizados();
	}

	@GetMapping(value = ApiConstant.INVENTARIO_API_FINALIZAR_ARTICULOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> finalizarArticulosInexistentes() {
		return inventarioService.finalizarArticulosInexistentes();
	}

	@GetMapping(value = ApiConstant.INVENTARIO_API_FILTRAR_LOCAL_INVENTARIO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarInventarioFiltrosLocal(Integer page, Integer local) {
		return inventarioService.inventariosFiltrosLocal(page, local);
	}

	@GetMapping(value = ApiConstant.INVENTARIO_API_FILTRAR_LOCAL_INVENTARIO_WEB, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarInventarioFiltrosLocalWeb(Integer page, Integer local) {
		return inventarioService.inventariosFiltrosLocalWeb(page, local);
	}

	@GetMapping(value = ApiConstant.INVENTARIO_API_DEVUELTO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> EstadoDevuelto() {
		return inventarioService.estadoDevuelto();
	}

	@GetMapping(value = ApiConstant.INVENTARIO_API_BODEGA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> EstadoBodega() {
		return inventarioService.estadoBodega();
	}

	@GetMapping(value = ApiConstant.INVENTARIO_API_BUSCAR_POR_ARTICULO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> ListarInformeArticulos(@RequestParam String numArticulo,
			@RequestParam String tipoDocumento) {
		logger.info(tipoDocumento);
		return inventarioService.obtenerArticulosInformeInventario(numArticulo, tipoDocumento);
	}

	@GetMapping(value = ApiConstant.INVENTARIO_API_BUSCAR_POR_PRODUCTO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> ListarInformeArticulosProducto(@RequestParam String numProducto) {
		return inventarioService.obtenerArticulosInformeInventarioProducto(numProducto);
	}
}
