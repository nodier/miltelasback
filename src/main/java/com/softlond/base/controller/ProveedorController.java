package com.softlond.base.controller;

import com.softlond.base.dto.RespuestaDto;

// import org.apache.logging.log4j.Logger;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
// import com.google.api.client.http.HttpHeaders;
import com.google.gson.Gson;
import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.Proveedor;
import com.softlond.base.entity.Tercero;
import com.softlond.base.repository.ProveedorDao;
import com.softlond.base.service.ProveedorService;

import net.minidev.json.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.softlond.base.entity.Usuario;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.repository.UsuarioInformacionDao;
import org.springframework.http.HttpHeaders;

@Controller
@RequestMapping(ApiConstant.PROVEEDORES_API)
public class ProveedorController {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private ProveedorService proveedorService;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@PostMapping(value = ApiConstant.PROVEEDORES_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> crearProveedor(@RequestBody Proveedor proveedor) throws Exception {
		logger.info("ingresa a crear proveedor");
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();

		// ! se condiciona la ejecucion del consumo mekano para poder hacer pruebas
		// if (proveedor != null && (idSede == 7 || idSede == 15) && 1 != 1) {
		if (proveedor != null && (idSede == 7 || idSede == 15)) {
			String clave = "Set_Terceros";
			String codigoProveedor = "";
			String digitoVerificacion = "";
			String clasificacionLegal = "";
			String tContacto = "";
			String nombre = "";
			String nombreD = "";
			String apellido = "";
			String apellidoD = "";
			String tProveedor = "";
			String tDireccion = "";
			String tTelefono = "";
			String tEmail = "";
			String tCodTipoIdentificacion = "";
			String nidCiudad = "";
			String codSociedad = "";

			// !si el tipo de proveedor es persona natural
			if (proveedor.getClasificacionLegal().getId() == 1 || proveedor.getClasificacionLegal().getId() == 12 || proveedor
					.getClasificacionLegal().getId() == 13 || proveedor.getClasificacionLegal().getId() == 15) {
				String str = proveedor.getProveedor();
				String[] arr = str.split("-");
				logger.info(arr.length);
				nombre = arr[0];
				nombreD = arr[1];
				apellido = arr[2];
				apellidoD = arr[3];
			}

			codigoProveedor = proveedor.getNit() != null ? proveedor.getNit() : "";
			digitoVerificacion = proveedor.getDigito() != null ? proveedor.getDigito().toString() : "";

			switch (proveedor.getClasificacionLegal().getId()) {
				case 1:
					clasificacionLegal = "N";
					codSociedad = "02";
					break;
				case 2:
					clasificacionLegal = "J";
					codSociedad = "04";
					break;
				case 3:
					clasificacionLegal = "J";
					codSociedad = "06";
					break;
				case 4:
					clasificacionLegal = "J";
					codSociedad = "05";
					break;
				case 5:
					clasificacionLegal = "J";
					codSociedad = "09";
					break;
				case 6:
					clasificacionLegal = "J";
					codSociedad = "09";
					break;
				case 7:
					clasificacionLegal = "J";
					codSociedad = "03";
					break;
				case 8:
					clasificacionLegal = "J";
					codSociedad = "09";
					break;
				case 9:
					clasificacionLegal = "N";
					codSociedad = "01";
					break;
				default:
					break;
			}

			tProveedor = proveedor.getProveedor() != null ? proveedor.getProveedor() : "";
			tDireccion = proveedor.getDireccion() != null ? proveedor.getDireccion() : "";
			tTelefono = proveedor.getTelefono() != null ? proveedor.getTelefono() : "";
			tEmail = proveedor.getEmail() != null ? proveedor.getEmail() : "";

			switch (proveedor.getTipoIdentificacion().getId()) {
				case 1:
					tCodTipoIdentificacion = "13";
					break;
				case 2:
					tCodTipoIdentificacion = "22";
					break;
				case 8:
					tCodTipoIdentificacion = "31";
					break;
				case 3:
					tCodTipoIdentificacion = "41";
					break;
				case 6:
					tCodTipoIdentificacion = "42";
					break;
				default:
					break;
			}
			nidCiudad = proveedor.getCiudad().getId() != null ? proveedor.getCiudad().getId().toString() : "";

			String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";
			logger.info(uri);
			Tercero proveedorMekano = new Tercero();
			proveedorMekano.setCLAVE(clave);
			proveedorMekano.setCODIGO(codigoProveedor);
			proveedorMekano.setDV(digitoVerificacion);
			proveedorMekano.setNATURALEZA(clasificacionLegal);
			proveedorMekano.setNOM1(nombre.toUpperCase());
			proveedorMekano.setNOM2(nombreD.toUpperCase());
			proveedorMekano.setAPL1(apellido.toUpperCase());
			proveedorMekano.setAPL2(apellidoD.toUpperCase());
			proveedorMekano.setEMPRESA(tProveedor.toUpperCase());
			proveedorMekano.setRAZON_COMERCIAL(tProveedor.toUpperCase());
			proveedorMekano.setDIRECCION(tDireccion);
			proveedorMekano.setTELEFONO(tTelefono);
			proveedorMekano.setMOVIL(tTelefono);
			proveedorMekano.setEMAIL(tEmail);
			proveedorMekano.setCODIGO_POSTAL("NA");
			proveedorMekano.setGERENTE("");
			proveedorMekano.setCODIDENTIDAD(tCodTipoIdentificacion);
			proveedorMekano.setCODSOCIEDAD(codSociedad);
			proveedorMekano.setCODPAIS("169");
			proveedorMekano.setCODACTIVIDAD("NA");
			proveedorMekano.setCODZONA("NA");

			if (nidCiudad.length() == 1) {
				proveedorMekano.setCODMUNICIPIO("000" + nidCiudad);
			} else if (nidCiudad.length() == 2) {
				proveedorMekano.setCODMUNICIPIO("00" + nidCiudad);
			} else if (nidCiudad.length() == 4) {
				proveedorMekano.setCODMUNICIPIO("0" + nidCiudad);
			} else {
				proveedorMekano.setCODMUNICIPIO(nidCiudad);
			}

			Gson gson = new Gson();
			String rjson = gson.toJson(proveedorMekano);
			logger.info(rjson);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
			RestTemplate rest = new RestTemplate();
			String result = rest.postForObject(uri, entity, String.class);
			logger.info(result);
			if (result.contains("EL TERCERO")) {
				if (result.contains("YA EXISTE")) {
					logger.info("existe un error de tercero existente");
					throw new Exception("Ya existe un tercero con este ID");
				}
			}
		}
		logger.info("peticion proveedor exitosa");
		return this.proveedorService.crearProveedor(proveedor);
	}

	@GetMapping(value = ApiConstant.PROVEEDORES_API_LISTAR_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerProveedor(@PathVariable Integer page) {
		return this.proveedorService.ObtenerProveedores(page);
	}

	@GetMapping(value = ApiConstant.PROVEEDORES_API_LISTAR_TODO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerTodosProveedor() {
		return this.proveedorService.ObtenerTodosProveedores();
	}

	@GetMapping(value = ApiConstant.PROVEEDORES_API_LISTAR_TODOS_NOMBRE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerProveedoresNombre() {
		return this.proveedorService.ObtenerNombreProveedores();
	}

	@GetMapping(value = ApiConstant.PROVEEDORES_API_LISTAR_TODOS_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerProveedoresConsulta(@PathVariable Integer page, String nitNombre, Integer ciudad,
			Integer departamento, Integer barrio) {
		return this.proveedorService.obtenerProveedoresConsulta(nitNombre, ciudad, departamento, barrio, page);
	}

	@PutMapping(value = ApiConstant.PROVEEDORES_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> ActualizarProveedor(@RequestBody Proveedor proveedor) {
		return this.proveedorService.actualizarProveedor(proveedor);
	}

	@DeleteMapping(value = ApiConstant.PROVEEDORES_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> eliminarProveedor(Integer idProveedor) {
		return this.proveedorService.eliminarProveedor(idProveedor);
	}

	@GetMapping(value = ApiConstant.PROVEEDORES_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerProveedores() {
		return this.proveedorService.obtenerProveedoresRemision();
	}

	@GetMapping(value = ApiConstant.PROVEEDORES_API_TOTAL_FACTURA, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerTotalFactura(Integer idProveedor) {
		return this.proveedorService.ObtenerValorFacturasPorProveedor(idProveedor);
	}

	@GetMapping(value = ApiConstant.PROVEEDORES_API_BUSCAR_PALABRA_CLAVE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity buscarProveedores(@RequestParam String palabra) {
		RespuestaDto respuestaDto;
		try {
			List<Proveedor> proveedores = proveedorService.buscarPorPalabraClave(palabra);
			respuestaDto = new RespuestaDto(HttpStatus.OK, "Ok");
			respuestaDto.setObjetoRespuesta(proveedores);
			return new ResponseEntity(respuestaDto, HttpStatus.OK);
		} catch (Exception ex) {
			String msj = "Error buscando proveedores... " + ex.getMessage();
			respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST, msj);
			return new ResponseEntity(respuestaDto, HttpStatus.BAD_REQUEST);
		}
	}
}
