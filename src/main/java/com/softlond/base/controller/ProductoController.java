package com.softlond.base.controller;

// import com.google.api.Authentication;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
// import com.google.api.client.http.HttpHeaders;
import com.google.gson.Gson;
import com.softlond.base.constant.ApiConstant;
import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Articulo;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.Producto;
import com.softlond.base.entity.Proveedor;
import com.softlond.base.entity.ReferenciaProd;
import com.softlond.base.entity.RemisionVenta;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.repository.ArticuloDao;
import com.softlond.base.repository.PrecioDao;
import com.softlond.base.repository.ProductoDao;
import com.softlond.base.service.ProductoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.http.HttpHeaders;

@Controller
@RequestMapping(ApiConstant.PRODUCTOS_CONTROL_API)
public class ProductoController {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private ProductoService productoService;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@GetMapping(value = ApiConstant.PRODUCTOS_CONTROL_API_LISTAR_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarProductos(Integer idI) {
		logger.info(idI);
		RespuestaDto respuestaDto;
		String codigo = "";
		String codigoD = "";
		String nombre = "";
		String nombreD = "";
		String costo = "0.0";
		String precio = "0.0";
		String iva = "E19";
		String ivaV = "";
		String medida = "MTR";
		// Double costo = ArticuloDao.obtenerUltimoPrecioCosto(body.getId(), idSede);

		try {
			List<Producto> productos = productoService.listarTodos(idI);
			// List<Producto> productos = productoService.listarTodos();

			logger.info("ingresa a productos listar-todos");
			// logger.info(productos.get(0));
			// logger.info(productos);
			logger.info(productos.size());
			// if (productos.size() > 0 && productos.get(0) != null) {
			// // logger.info(productos.get(0));
			// logger.info(productos.size());
			// // for (Producto p : productos) {
			// // if (p.getId() > idI && p.getId() <= idI + 1) {
			// // if (p != null) {
			// codigo = productos.get(0).getCodigo() != null ? productos.get(0).getCodigo()
			// : "";
			// nombre = productos.get(0).getTipo().getTTipo() + " " +
			// productos.get(0).getReferencia().getTreferencia() + " "
			// + productos.get(0).getPresentacion().getTPresentacion() + " " +
			// productos.get(0).getColor().getTColor();
			// if (productos.get(0).getPrecios().size() > 0) {
			// costo = productos.get(0).getPrecios().get(0).getPrecioCosto() != null
			// ? productos.get(0).getPrecios().get(0).getPrecioCosto().toString()
			// : "0.0";
			// precio = productos.get(0).getPrecios().get(0).getPrecioVenta() != null
			// ? productos.get(0).getPrecios().get(0).getPrecioVenta().toString()
			// : "0.0";
			// }
			// ivaV = productos.get(0).getIva() + "";
			// // ivaV = productos.get(0).getIva() + "" != null ? productos.get(0).getIva()
			// +
			// // "" : "" + "";
			// logger.info("el iva es de:");
			// logger.info(ivaV);
			// if (ivaV.equals("0.0")) {
			// iva = "E0";
			// } else if (ivaV.equals("19.0")) {
			// iva = "E19";
			// } else if (ivaV.equals("5.0")) {
			// iva = "E5";
			// }

			// if (productos.get(0).getUnidad().getId() == 1) {
			// logger.info("la unidad es el metro");
			// medida = "MTR";
			// } else if (productos.get(0).getUnidad().getId() == 2) {
			// logger.info("la unidad es el unidad");
			// medida = "UND";
			// } else if (productos.get(0).getUnidad().getId() == 3) {
			// logger.info("la unidad es el paquete");
			// medida = "PAQ";
			// }
			// logger.info(iva);
			// logger.info(medida);
			// // logger.info(productos.get(0).getCodigo());
			// String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";
			// logger.info(uri);

			// ReferenciaProd referenciaMekano = new ReferenciaProd();
			// referenciaMekano.setCLAVE("Set_Referencias");
			// referenciaMekano.setCODIGO(codigo);
			// referenciaMekano.setCODIGO2(codigoD);
			// referenciaMekano.setNOMBRE(nombre.toUpperCase());
			// referenciaMekano.setNOMBRE2(nombreD.toUpperCase());
			// referenciaMekano.setCOSTO(costo);
			// referenciaMekano.setPRECIO(precio);
			// referenciaMekano.setCODLINEA("PROD");
			// referenciaMekano.setCODMEDIDA(medida);
			// // referenciaMekano.setCOD_ESQIMPUESTO("E19");
			// referenciaMekano.setCOD_ESQIMPUESTO(iva);
			// referenciaMekano.setCOD_ESQRETENCION("ECOM1");
			// referenciaMekano.setCOD_ESQCONTABLE("ESQ");

			// Gson gson = new Gson();
			// String rjson = gson.toJson(referenciaMekano);

			// // logger.info(referenciaMekano.getCODMEDIDA());

			// logger.info(rjson);
			// HttpHeaders headers = new HttpHeaders();
			// headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			// // String jsonEntity = new
			// ObjectMapper().writeValueAsString(referenciaMekano);
			// // logger.info(jsonEntity);
			// // String encodedEntity = URLEncoder.encode(jsonEntity,
			// // StandardCharsets.UTF_8.toString());
			// // logger.info(encodedEntity);
			// // String rjson1 = gson.toJson(encodedEntity);
			// // logger.info(rjson1);
			// // HttpEntity<String> entity = new HttpEntity<>(rjson, headers);
			// HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
			// logger.info(entity);
			// RestTemplate rest = new RestTemplate();
			// if (entity != null) {
			// String result = rest.postForObject(uri, entity, String.class);
			// logger.info(result);
			// }
			// // String result = rest.postForObject(uri, entity, String.class);
			// // logger.info(result);
			// // }
			// // }
			// }

			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(productos);
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error listando productos...";
			logger.error(msj + "\n");
			logger.error(ex);
			// logger.info(ex.getStackTrace());
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = ApiConstant.PRODUCTOS_CONTROL_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity crearProducto(@RequestBody Producto body) throws Exception {
		logger.info("ingresa a crear producto");
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		String codigo = "";
		String codigoD = "";
		String nombre = "";
		String nombreD = "";
		String costo = "0.0";
		String precio = "0.0";
		String iva = "E19";
		String ivaV = "";
		String medida = "MTR";

		RespuestaDto respuestaDto;
		try {
			List<Producto> productos = productoService.listarTodos(body.getId());

			logger.info("ingresa a productos listar-todos");
			logger.info(productos.size());
			// if (productos.size() > 0 && productos.get(0) != null && (idSede == 7 ||
			// idSede == 15) && 1 != 1) {
			if (productos.size() > 0 && productos.get(0) != null && (idSede == 7 || idSede == 15)) {
				logger.info(productos.size());
				codigo = productos.get(0).getCodigo() != null ? productos.get(0).getCodigo() : "";
				nombre = productos.get(0).getTipo().getTTipo() + " " + productos.get(0).getReferencia().getTreferencia() + " "
						+ productos.get(0).getPresentacion().getTPresentacion() + " " + productos.get(0).getColor().getTColor();
				if (productos.get(0).getPrecios().size() > 0) {
					costo = productos.get(0).getPrecios().get(0).getPrecioCosto() != null
							? productos.get(0).getPrecios().get(0).getPrecioCosto().toString()
							: "0.0";
					precio = productos.get(0).getPrecios().get(0).getPrecioVenta() != null
							? productos.get(0).getPrecios().get(0).getPrecioVenta().toString()
							: "0.0";
				}
				iva = productos.get(0).getIva() + "";

				logger.info("el iva es de:");
				logger.info(iva);
				if (iva.equals("0.0")) {
					iva = "E0";
				} else if (ivaV.equals("19.0")) {
					iva = "E19";
				} else if (ivaV.equals("5.0")) {
					iva = "E5";
				}

				if (productos.get(0).getUnidad().getId() == 1) {
					logger.info("la unidad es el metro");
					medida = "MTR";
				} else if (productos.get(0).getUnidad().getId() == 2) {
					logger.info("la unidad es el unidad");
					medida = "UND";
				} else if (productos.get(0).getUnidad().getId() == 3) {
					logger.info("la unidad es el paquete");
					medida = "PAQ";
				}
				logger.info(iva);
				logger.info(medida);

				String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";
				logger.info(uri);

				ReferenciaProd referenciaMekano = new ReferenciaProd();
				referenciaMekano.setCLAVE("Set_Referencias");
				referenciaMekano.setCODIGO(codigo);
				referenciaMekano.setCODIGO2(codigoD);
				referenciaMekano.setNOMBRE(nombre.toUpperCase());
				referenciaMekano.setNOMBRE2(nombreD.toUpperCase());
				referenciaMekano.setCOSTO(costo);
				referenciaMekano.setPRECIO(precio);
				referenciaMekano.setCODLINEA("PROD");
				referenciaMekano.setCODMEDIDA(medida);
				referenciaMekano.setCOD_ESQIMPUESTO(iva);
				referenciaMekano.setCOD_ESQRETENCION("ECOM1");
				referenciaMekano.setCOD_ESQCONTABLE("ESQ");

				Gson gson = new Gson();
				String rjson = gson.toJson(referenciaMekano);
				logger.info(rjson);
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
				HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
				logger.info(entity);
				RestTemplate rest = new RestTemplate();
				// ! se condiciona la ejecucion del consumo mekano para poder hacer pruebas
				// if (entity != null && 1 != 1) {
				if (entity != null) {
					String result = rest.postForObject(uri, entity, String.class);
					logger.info(result);
					if (result.contains("LA REFERENCIA")) {
						if (result.contains("YA EXISTE")) {
							logger.info("existe un error de referencia existente");
							throw new Exception("Ya existe una referencia con este CODIGO");
						}
					}
				}
			}

			Producto nuevoProducto = productoService.crearProducto(body);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(nuevoProducto);
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error creando producto...";
			logger.error(msj + "\n");
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = ApiConstant.PRODUCTOS_CONTROL_API_EDITAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity editarProducto(@RequestBody Producto body) {
		RespuestaDto respuestaDto;
		try {
			productoService.editarProducto(body);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error editando producto...";

			logger.error(msj + "\n");
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = ApiConstant.PRODUCTOS_CONTROL_API_LISTAR_TODOS_ID_RECODIFICAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity obtenerTodosProductoRecodificar(@PathVariable Integer page,
			@RequestBody Producto body, Integer id) {
		RespuestaDto respuestaDto;
		try {
			logger.info("ingresa a PRODUCTOS_CONTROL_API_LISTAR_TODOS_ID_RECODIFICAR");
			logger.info(body.getProducto() + " " + id);
			this.productoService.editarProductoRecodificar(page, body, id);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error editando producto...";
			logger.error(msj + "\n");
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping(value = ApiConstant.PRODUCTOS_CONTROL_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity eliminarProducto(@RequestParam Integer producto) {
		RespuestaDto respuestaDto;
		try {
			productoService.eliminarProducto(producto);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error eliminando producto...";
			logger.error(msj + "\n");
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = ApiConstant.PRODUCTOS_CONTROL_API_LISTAR_BUSQUEDA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarProductosBusqueda(String texto) {
		RespuestaDto respuestaDto;
		try {
			List productos = productoService.listarProductosBusqueda(texto);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(productos);
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error listando productos...";
			logger.error(msj + "\n");
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = ApiConstant.PRODUCTOS_CONTROL_API_LISTAR_PROVEEDOR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarProductosPorProveedor(Integer proveedor) {
		RespuestaDto respuestaDto;
		try {
			List productos = productoService.listarProductosPorProveedor(proveedor);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(productos);
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error listando productos...";
			logger.error(msj + "\n");
			logger.error(ex);
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = ApiConstant.PRODUCTOS_CONTROL_API_LISTAR_CLIENTES_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerDescuentoClienteConsulta(@PathVariable Integer page, Integer cliente) {
		return this.productoService.obtenerDescuentoClienteConsulta(cliente, page);
	}

	/* Listar todos los productos */
	@GetMapping(value = ApiConstant.PRODUCTOS_CONTROL_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarProducto() {
		return productoService.listarProducto();
	}

	// Crear productos
	@PostMapping(value = ApiConstant.PRODUCTOS_CONTROL_API_CREAR_LISTADO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> crearProductos(@RequestBody Producto producto) throws Exception {
		logger.info("ingresa a crear productoooo");
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		String codigo = "";
		String codigoD = "";
		String nombre = "";
		String nombreD = "";
		String costo = "0.0";
		String precio = "0.0";
		String iva = "E19";
		String ivaV = "";
		String medida = "MTR";

		// ! se condiciona la ejecucion del consumo mekano para poder hacer pruebas
		// if (producto != null && (idSede == 7 || idSede == 15) && 1 != 1) {
		if (producto != null && (idSede == 7 || idSede == 15)) {
			codigo = producto.getCodigo() != null ? producto.getCodigo() : "";
			nombre = producto.getTipo().getTTipo() + " " + producto.getReferencia().getTreferencia() + " "
					+ producto.getPresentacion().getTPresentacion() + " " + producto.getColor().getTColor();
			if (producto.getPrecios().size() > 0) {
				// TODO:verificar si por este valor de precio costo en get(0)
				// !genera problema en la facturacion
				costo = producto.getPrecios().get(0).getPrecioCosto() != null
						? producto.getPrecios().get(0).getPrecioCosto().toString()
						: "0.0";
				precio = producto.getPrecios().get(0).getPrecioVenta() != null
						? producto.getPrecios().get(0).getPrecioVenta().toString()
						: "0.0";
			}
			ivaV = producto.getIva() + "";
			if (ivaV.equals("0.0")) {
				iva = "E0";
			} else if (ivaV.equals("19.0")) {
				iva = "E19";
			} else if (ivaV.equals("5.0")) {
				iva = "E5";
			}

			if (producto.getUnidad().getId() == 1) {
				logger.info("la unidad es el metro");
				medida = "MTR";
			} else if (producto.getUnidad().getId() == 2) {
				logger.info("la unidad es el unidad");
				medida = "UND";
			} else if (producto.getUnidad().getId() == 3) {
				logger.info("la unidad es el paquete");
				medida = "PAQ";
			}
			String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";
			logger.info(uri);

			ReferenciaProd referenciaMekano = new ReferenciaProd();
			referenciaMekano.setCLAVE("Set_Referencias");
			referenciaMekano.setCODIGO(codigo);
			referenciaMekano.setCODIGO2(codigoD);
			referenciaMekano.setNOMBRE(nombre.toUpperCase());
			referenciaMekano.setNOMBRE2(nombreD.toUpperCase());
			referenciaMekano.setCOSTO(costo);
			referenciaMekano.setPRECIO(precio);
			referenciaMekano.setCODLINEA("PROD");
			referenciaMekano.setCODMEDIDA(medida);
			referenciaMekano.setCOD_ESQIMPUESTO(iva);
			referenciaMekano.setCOD_ESQRETENCION("ECOM1");
			referenciaMekano.setCOD_ESQCONTABLE("ESQ");

			Gson gson = new Gson();
			String rjson = gson.toJson(referenciaMekano);

			logger.info(rjson);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
			logger.info(entity);
			RestTemplate rest = new RestTemplate();
			if (entity != null) {
				String result = rest.postForObject(uri, entity, String.class);
				logger.info(result);
				if (result.contains("LA REFERENCIA")) {
					if (result.contains("YA EXISTE")) {
						logger.info("existe un error de referencia existente");
						throw new Exception("Ya existe una referencia con este CODIGO");
					}
				}
			}
		}
		logger.info("peticion referencia exitosa");
		return productoService.crearProductos(producto);
	}

	@PutMapping(value = ApiConstant.PRODUCTOS_CONTROL_API_EDITAR_PRECIO, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> ActualizarPrecios(@RequestBody Producto producto) {
		return productoService.ActualizarPrecios(producto);
	}

	@GetMapping(value = ApiConstant.PRODUCTOS_CONTROL_API_LISTAR_CLIENTE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarProductosPorCliente(String fechaInicial, String fechaFinal,
			Integer idCliente, Integer page) {
		return productoService.obtenerProductosClientes(fechaInicial, fechaFinal, idCliente, page);
	}

	@GetMapping(value = ApiConstant.PRODUCTOS_CONTROL_API_LISTAR_CLIENTE_EXPORT, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity listarProductosPorCliente(String fechaInicial, String fechaFinal,
			Integer idCliente) {
		return productoService.obtenerProductosClientesExportar(fechaInicial, fechaFinal, idCliente);
	}

	@GetMapping(value = ApiConstant.PRODUCTOS_CONTROL_API_LISTAR_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity ListaDeProductosFiltro(String fechaInicial, String fechaFinal, String texto,
			Integer idClasificacion, String requierePrecio, Integer page) {
		return productoService.obtenerProductosFiltros(fechaInicial, fechaFinal,
				texto, idClasificacion, requierePrecio, page);
	}

	@GetMapping(value = ApiConstant.PRODUCTOS_CONTROL_API_LISTAR_CONSULTA_DISPONIBLES, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity ListaDeProductosFiltroDisponibles(String fechaInicial, String fechaFinal,
			String texto,
			Integer idClasificacion, String requierePrecio, Integer page) {
		return productoService.obtenerProductosFiltrosDisponibles(fechaInicial, fechaFinal, texto, idClasificacion,
				requierePrecio,
				page);
	}

	@PostMapping(value = ApiConstant.PRODUCTOS_CONTROL_API_REPLICAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity replicarProductos(@RequestBody List<Producto> body) throws Exception {
		return productoService.replicarProductos(body);
	}

	// Listar productos por id
	@GetMapping(value = ApiConstant.PRODUCTOS_CONTROL_API_LISTAR_TODOS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerTodosProducto(@PathVariable Integer page, Integer idProducto) {
		logger.info(idProducto + "-" + page);
		ResponseEntity<Object> respuesta = null;

		respuesta = productoService.obtenerTodosProducto(page, idProducto);
		if (respuesta == null) {
			logger.info("se solicita informacion a mekano");
		}
		return respuesta;
	}

	// Listar todos productos
	@GetMapping(value = ApiConstant.PRODUCTOS_CONTROL_API_LISTAR_TODOS_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerTodosProductoPage(@PathVariable Integer page) {
		return productoService.obtenerTodosProductoPage(page);
	}

	// Listar todos productos disponibles asociados a articulos
	@GetMapping(value = ApiConstant.PRODUCTOS_CONTROL_API_LISTAR_TODOS_CONSULTA_DISPONIBLES, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerTodosProductoPageDisponibles(@PathVariable Integer page) {
		return productoService.obtenerTodosProductoPageDisponibles(page);
	}

	@GetMapping(value = ApiConstant.PRODUCTOS_CONTROL_API_LISTAR_PRODUCTO_POR_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> obtenerProductoId(Integer idProducto) {
		return productoService.obtenerProductoId(idProducto);
	}

	@GetMapping(value = ApiConstant.PRODUCTOS_CONTROL_API_LISTAR_CONSULTA_EXCEL, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity ListaDeProductosFiltroExcel(String fechaInicial, String fechaFinal, String texto,
			Integer idClasificacion, String requierePrecio) {
		return productoService.obtenerProductosFiltrosExcel(fechaInicial, fechaFinal, texto, idClasificacion,
				requierePrecio);
	}
}
