package com.softlond.base.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Articulo;
import com.softlond.base.entity.PrdTipos;
import com.softlond.base.entity.Precio;
import com.softlond.base.entity.Prefijo;
import com.softlond.base.service.ArticuloService;

@Controller
@RequestMapping(ApiConstant.ARTICULOS_CONTROL_API)
public class ArticuloController {
	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private ArticuloService articuloService;

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarArticulos() {
		RespuestaDto respuestaDto;
		try {
			List<Articulo> articulos = articuloService.listarTodos();
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(articulos);
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error listando articulos... " + ex.getMessage();
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_POR_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarArticulosPorSerde(@RequestParam Integer sede) {
		RespuestaDto respuestaDto;
		try {
			List<Articulo> articulos = articuloService.listarPorSede(sede);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(articulos);
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error listando articulos por sede... " + ex.getMessage();
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_POR_PRODUCTO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarArticulosPorProducto(@RequestParam Integer producto) {
		RespuestaDto respuestaDto;
		try {
			List<Articulo> articulos = articuloService.listarPorProducto(producto);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(articulos);
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error listando articulos por producto... " + ex.getMessage();
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_POR_PRODUCTO_Y_SEDE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarArticulosPorProductoYSerde(@RequestParam Integer sede,
			@RequestParam Integer producto) {
		RespuestaDto respuestaDto;
		try {
			List<Articulo> articulos = articuloService.listarPorProductoYSede(producto, sede);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(articulos);
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error listando articulos por producto y sede... " + ex.getMessage();
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_CONSECUTIVO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerCodigoConsecutivo(Integer idSede) {
		return articuloService.obtenerCodigo(idSede);
	}

	@PostMapping(value = ApiConstant.ARTICULOS_CONTROL_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> crearArticulo(@RequestBody Articulo articuloNuevo) {
		return articuloService.crearArticuloRemision(articuloNuevo);
	}

	@PostMapping(value = ApiConstant.ARTICULOS_CONTROL_API_EDITAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> actualizarArticulo(@RequestBody Articulo articuloActualizar) {
		return articuloService.actualizarArticulo(articuloActualizar);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_BUSCAR_POR_CODIGO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerArticuloCodigo(String codigo) {
		return articuloService.obtenerArticuloCodigo(codigo);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_BUSCAR_POR_CODIGO_INVENTARIO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerArticuloCodigoInventario(String codigo) {
		return articuloService.obtenerArticuloCodigoInventario(codigo);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_CONTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> totalArticulosProducto(@RequestParam Integer producto,
			@RequestParam Integer idProveedor) {
		return articuloService.obtenerNumeroArticulos(producto, idProveedor);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_BUSCAR_SIN_REMISION, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> articulosSinRemision(Integer sede) {
		return articuloService.obtenerArticulosSinRemision(sede);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_ULTIMO_PRECIO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> ultimoPrecio(@RequestParam Integer idProducto,
			@RequestParam Integer idProveedor) {
		return articuloService.obtenerCostoArticulo(idProducto, idProveedor);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_BUSCAR_DISPONIBLE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarArticulosDisponibles(@RequestParam String texto,
			@RequestParam Integer page) {
		return articuloService.obtenerArticulosDisponibles(texto, page);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_ARTICULO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerArticuloProducto(@RequestParam Integer idProducto) {
		return articuloService.obtenerArticuloProducto(idProducto);
	}

	/*
	 * @GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_CONSULTA,
	 * produces = MediaType.APPLICATION_JSON_VALUE) public @ResponseBody
	 * ResponseEntity obtenerRecodificarProductosConsulta( Integer producto, Integer
	 * estadoArticulo, Integer page) { return
	 * articuloService.obtenerRecodificarProductosConsulta(producto, estadoArticulo,
	 * page); }
	 */

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_TODOS_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerDescuentoConsulta(@PathVariable Integer page, Integer producto,
			Integer estadoArticulo) {
		return this.articuloService.obtenerRecodificarProductosConsulta(producto, estadoArticulo, page);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_POR_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerArticuloId(Integer idArticulo) {
		return articuloService.obtenerArticuloId(idArticulo);
	}

	@PutMapping(value = ApiConstant.ARTICULOS_CONTROL_API_EDITAR_IDPRODUCTO_ARTICULO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> ActualizarCodificarProducto(@RequestBody Articulo articulo) {
		return articuloService.ActualizarCodificarProducto(articulo);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_ARTICULOS_FILTROS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarArticulosFiltros(Integer page, Integer tipo, Integer referencia,
			Integer presentacion, Integer color, String texto, String order, boolean sort) {
		return articuloService.obtenerArticulosFiltros(page, tipo, referencia, presentacion, color, texto, order, sort);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_ARTICULOS_FILTROS_CAMBIAR_ESTADO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarArticulosFiltrosCambiarEstado(Integer page, Integer tipo,
			Integer referencia,
			Integer presentacion, Integer color, String texto, String order, boolean sort) {
		return articuloService.obtenerArticulosFiltrosCambiarEstado(page, tipo, referencia, presentacion, color, texto,
				order, sort);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_ARTICULOS_FILTROS_EXPORTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarArticulosFiltrosExportar(Integer tipo, Integer referencia,
			Integer presentacion, Integer color, String texto) {
		return articuloService.obtenerArticulosFiltrosExportar(tipo, referencia, presentacion, color, texto);
	}

	@PostMapping(value = ApiConstant.ARTICULOS_CONTROL_API_CAMBIAR_ESTADO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> cambiarEstadoArticulo(@RequestBody Articulo articuloNuevo) {
		return articuloService.cambiarEstadoArticulo(articuloNuevo);
	}

	/*
	 * @GetMapping(value =
	 * ApiConstant.ARTICULOS_CONTROL_API_LISTAR_POR_PRODUCTO_PAGE, produces =
	 * MediaType.APPLICATION_JSON_VALUE) public @ResponseBody ResponseEntity
	 * listarArticulosPorProductoPage(Integer producto, Integer page) { RespuestaDto
	 * respuestaDto; return articuloService.obtenerArticulosPorProducto(producto,
	 * page); }
	 */

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_SELECTOR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarTiposSelector() {
		RespuestaDto respuestaDto;
		try {
			List<Articulo> articulos = articuloService.listarTodosSelector();
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(articulos);
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error listando articulos... " + ex.getMessage();
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_FILTRO_TEXTO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarTodosFiltroTexto(String texto) {
		RespuestaDto respuestaDto;
		try {
			List<Articulo> articulos = articuloService.listarTodosFiltroTexto(texto);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(articulos);
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error listando articulos... " + ex.getMessage();
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_TODOS_ARTICULO_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerArticulosConsulta(@PathVariable Integer page, Integer articulo,
			Integer estadoArticulo) {
		return this.articuloService.obtenerArticulosConsulta(articulo, estadoArticulo, page);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_TODOS_ARTICULOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerArticulos() {
		return articuloService.obtenerArticulos();
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_TODOS_ARTICULO_PRODUCTO_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerArticulosProductoMovimientoConsulta(@PathVariable Integer page,
			Integer producto, Integer articulo, Integer estadoArticulo) {
		return this.articuloService.obtenerArticulosProductoMovimientoConsulta(producto, articulo, estadoArticulo,
				page);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_POR_PRODUCTO_PAGE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarArticulosPorProductoPage(Integer producto, Integer page, String order,
			String column) {
		return articuloService.obtenerArticulosPorProducto(producto, page, order, column);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_ARTICULO_LOCAL, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarArticulosPorLocal(Integer local) {
		return articuloService.obtenerArticulosPorLocal(local);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_DISPONIBLES_PAGE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarArticulosDisponibles(Integer page, String order, String column) {
		return articuloService.obtenerArticulosDisponibles(page, order, column);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_LOCAL_SECTOR_PAGE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarArticulosDisponiblesLocalSector(Integer page, String local, String sector,
			String order, String column) {
		return articuloService.obtenerArticulosLocalSector(page, local, sector, order, column);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_TODOS_PAGE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarTodosArticulos(Integer page, String order, String column) {
		return articuloService.obtenerTodosArticulos(page, order, column);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_POR_SEDE_CODIGO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarArticulosPorSerdeCodigo(@RequestParam Integer sede,
			@RequestParam String codigo) {
		RespuestaDto respuestaDto;
		try {
			List<Articulo> articulos = articuloService.listarPorSedeCodigo(sede, codigo);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(articulos);
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error listando articulos por sede... " + ex.getMessage();
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_POR_SEDE_CODIGO_LOCAL, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarArticulosPorSerdeCodigoLocal(@RequestParam Integer sede,
			@RequestParam String codigo,
			@RequestParam Integer local) {
		RespuestaDto respuestaDto;
		try {
			List<Articulo> articulos = articuloService.listarPorSedeCodigoLocal(sede, codigo, local);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(articulos);
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error listando articulos por sede... " + ex.getMessage();
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_TODOS_EXISTENTES_PRODUCTO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarTodosArticulos(Integer page, Integer idProducto) {
		return articuloService.obtenerArticulosExistentesProducto(idProducto, page);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_ARTICULOS_TOTALES_FILTROS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> listarTotalesArticulosFiltros(Integer page, Integer tipo,
			Integer referencia, Integer presentacion, Integer color, String texto) {
		return articuloService.obtenerTotalesArticulosFiltros(tipo, referencia, presentacion, color, texto);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_LISTAR_TODOS_ARTICULO_MOVIMIENTO_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerArticulosProductoMovimientosConsulta(@PathVariable Integer page,
			String producto, String articulo, Integer estadoArticulo) {
		return this.articuloService.obtenerArticulosProductoMovimientosConsulta(producto, articulo, estadoArticulo,
				page);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_OBTENER_ARTICULO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerArticulo(String codigoArticulo) {
		return this.articuloService.obtenerArticuloSede(codigoArticulo);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_OBTENER_PRECIO_COSTO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerPrecioCostoUltimoArticulo(Integer idProducto) {
		return this.articuloService.obtenerPrecioCostoUltimoArticulo(idProducto);
	}

	@GetMapping(value = ApiConstant.ARTICULOS_CONTROL_API_OBTENER_PRECIO_VENTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerPrecioVenta(Integer idProducto) {
		return this.articuloService.obtenerPrecioVenta(idProducto);
	}
}
