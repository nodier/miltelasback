package com.softlond.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.softlond.base.constant.ApiConstant;
import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.Proveedor;
import com.softlond.base.entity.Tercero;
import com.softlond.base.service.ClientesService;

// import com.google.api.client.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.apache.log4j.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.softlond.base.entity.Usuario;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.repository.UsuarioInformacionDao;

@RestController
@RequestMapping(ApiConstant.CLIENTES_CONTROL_API)
public class ClientesController {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private ClientesService clientesService;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	/* Listar todos los clientes */
	@GetMapping(value = ApiConstant.CLIENTES_CONTROL_API_LISTAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarClientes() {
		return clientesService.listarClientes();
	}

	/* obtener clientes */
	@GetMapping(value = ApiConstant.CLIENTES_CONTROL_API_BUSCAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> buscarCliente(Integer idCliente) {
		return clientesService.buscarCliente(idCliente);
	}

	/* Crear Cliente */
	@PostMapping(value = ApiConstant.CLIENTES_CONTROL_API_CREAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> crearCliente(@RequestBody Clientes clientes) throws Exception {

		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Integer idSede = usuarioInformacion.getIdOrganizacion().getId();
		// ! se condiciona la ejecucion del consumo mekano para poder hacer pruebas
		if (clientes != null && (idSede == 7 || idSede == 15) && 1 != 1) {
			// if (clientes != null && (idSede == 7 || idSede == 15)) {
			logger.info("ingresa a la creacion de clientes desde la sede: " + idSede);
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

			if (clientes != null) {
				codigoProveedor = clientes.getNitocc() != null ? clientes.getNitocc() : "";
				digitoVerificacion = clientes.getDigito() != null ? clientes.getDigito() : "";
				nombre = clientes.getNombres() != null ? clientes.getNombres().toUpperCase() : "";
				apellido = clientes.getApellidos() != null ? clientes.getApellidos().toUpperCase() : "";
				tDireccion = clientes.getDireccion() != null ? clientes.getDireccion().toUpperCase() : "";
				tTelefono = clientes.getTelefono() != null ? clientes.getTelefono() : "";
				tEmail = clientes.getEmail() != null ? clientes.getEmail().toUpperCase() : "copru@gmail.com";

				switch (clientes.getIdTipoDocumento().getId()) {
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

				switch (clientes.getCodClasificacionLegal().getId()) {
					case 1:
						clasificacionLegal = "N";
						codSociedad = "01";
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
						codSociedad = "09";
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

				nidCiudad = clientes.getIdCiudad().getId() != null ? clientes.getIdCiudad().getId().toString() : "";

				String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";
				logger.info(uri);
				Tercero proveedorMekano = new Tercero();
				proveedorMekano.setCLAVE(clave);
				proveedorMekano.setCODIGO(codigoProveedor);
				proveedorMekano.setDV(digitoVerificacion);
				proveedorMekano.setNATURALEZA(clasificacionLegal);
				proveedorMekano.setNOM1(nombre);
				proveedorMekano.setNOM2(nombreD);
				proveedorMekano.setAPL1(apellido);
				proveedorMekano.setAPL2(apellidoD);
				proveedorMekano.setEMPRESA(tProveedor);
				proveedorMekano.setRAZON_COMERCIAL(tProveedor);
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

				// ! para hacer esta consulta se debe primero crear la entidad para buscar (o
				// solo un dto?)
				// String resultObtener = rest.postForObject(uri, entity, String.class);
				String result = rest.postForObject(uri, entity, String.class);
				logger.info(result);
				if (result.contains("EL TERCERO")) {
					if (result.contains("YA EXISTE")) {
						logger.info("existe un error de tercero existente");
						throw new Exception("Ya existe un tercero con este ID");
					}
				}
			}
		}
		logger.info("peticion terceros exitosa");
		return this.clientesService.crearCliente(clientes);
	}

	// Actualizar Cliente
	@PutMapping(value = ApiConstant.CLIENTES_CONTROL_API_ACTUALIZAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> ActualizarCliente(@RequestBody Clientes clientes) {
		return this.clientesService.actualizarCliente(clientes);
	}

	// Eliminar Cliente
	@DeleteMapping(value = ApiConstant.CLIENTES_CONTROL_API_ELIMINAR, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> eliminarCliente(Integer idCliente) {
		return this.clientesService.eliminarCliente(idCliente);
	}

	@GetMapping(value = ApiConstant.CLIENTES_CONTROL_API_LISTAR_TODOS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerTodosClientes(@PathVariable Integer page) {
		return this.clientesService.obtenerTodosClientes(page);
	}

	@GetMapping(value = ApiConstant.CLIENTES_CONTROL_API_LISTAR_TODOS_NOMBRE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerClientesNombre() {
		return this.clientesService.ObtenerNombreClientes();
	}

	@GetMapping(value = ApiConstant.CLIENTES_CONTROL_API_LISTAR_TODOS_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerClientesConsulta(@PathVariable Integer page, Integer cliente, Integer ciudad,
			Integer departamento, Integer barrio, Integer clasificacion, String activo) {
		return this.clientesService.obtenerClientesConsulta(cliente, ciudad, departamento, barrio, clasificacion, activo,
				page);
	}

	// Listar todos los clientes
	@GetMapping(value = ApiConstant.CLIENTES_CONTROL_API_LISTAR_TODO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> ObtenerClientes() {
		return this.clientesService.ObtenerClientes();

	}

	// Listar Retenciones por cliente
	@GetMapping(value = ApiConstant.CLIENTES_CONTROL_API_LISTAR_RETENCIONES_CONSULTA, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> obtenerRetencionesClientes(@PathVariable Integer page, String nombreApellido) {
		return this.clientesService.obtenerRetencionesClientes(nombreApellido, page);

	}

	/* Listar todos los clientes menos el cliente 99 */
	@GetMapping(value = ApiConstant.CLIENTES_CONTROL_API_LISTAR_CREDITO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarClientesCredito() {
		return clientesService.listarClientesCredito();
	}

	/* Obtener cupo */
	@PostMapping(value = ApiConstant.CLIENTES_CONTROL_API_OBTENER_CUPO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> cupoCliente(@RequestBody Clientes cliente) {
		return clientesService.obtenerCupo(cliente);
	}

	@GetMapping(value = ApiConstant.CLIENTES_CONTROL_API_OBTENER_SALDO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> SaldoCliente(Integer idCliente) {
		return clientesService.obtenerSaldo(idCliente);
	}

	/* Listar todos los clientes */
	@GetMapping(value = ApiConstant.CLIENTES_CONTROL_API_LISTAR_TEXTO, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarClientesPorCodigoNombre(String texto) {
		return clientesService.listarClientesTexto(texto);
	}
}
