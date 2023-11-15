package com.softlond.base.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.FacturaM;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.entity.Proveedor;
import com.softlond.base.entity.RemisionCompra;
import com.softlond.base.entity.Sequence;
import com.softlond.base.entity.Tercero;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ConceptoNotaDebitoDao;
import com.softlond.base.repository.NotaDebitoDao;
import com.softlond.base.repository.SequenceDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

@Service
public class NotaDebitoService {

    private static final Logger logger = Logger.getLogger(NotaDebitoService.class);

    @Autowired
    public NotaDebitoDao notaDebitoDao;

    @Autowired
    UsuarioInformacionDao usuarioInformacionDao;

    @Autowired
    private SequenceDao sequenceDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ConceptoNotaDebitoDao conceptoNdDao;

    // Crear
    @PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
    public ResponseEntity<Object> crearNotaDebito(@RequestBody NotaDebito nuevoNotaDebito) {

        ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
        Sequence secuenciaSave = new Sequence();
        secuenciaSave.setValorSequencia(Integer.parseInt(nuevoNotaDebito.getNumeroDocumento()));
        secuenciaSave.setIdPrefijo(nuevoNotaDebito.getPrefijo().getId());
        secuenciaSave.setIdSede(nuevoNotaDebito.getIdSede().getId());
        try {
            Sequence seqActualizada = sequenceDao
                    .findByIdSedeAndIdPrefijo(nuevoNotaDebito.getIdSede().getId(), nuevoNotaDebito.getPrefijo().getId())
                    .orElse(null);
            if (seqActualizada != null) {
                secuenciaSave.setId(seqActualizada.getId());
            }
            nuevoNotaDebito.setIdCreador(usuarioAutenticado);
            NotaDebito guardado = this.notaDebitoDao.save(nuevoNotaDebito);
            sequenceDao.save(secuenciaSave);

            // if (guardado != null && (guardado.getIdSede().getId() == 7 ||
            // guardado.getIdSede().getId() == 15) && 1 != 1) {
            if (guardado != null && (guardado.getIdSede().getId() == 7 ||
                    guardado.getIdSede().getId() == 15)) {
                logger.info("ingresa a crear nota debito desde la sede: " + guardado.getIdSede().getId());
                String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";

                logger.info(guardado.getFechaDocumento().toString());
                logger.info(guardado.getFechaDocumento().toString());
                // Date fechaEntradaVenta = guardado.getFechaDocumento();
                // Date fechaEntradaVence = guardado.getFechaDocumento();
                // String fechaVenta = fechaEntradaVenta.getDay() + "." +
                // fechaEntradaVenta.getMonth() + "."
                // + fechaEntradaVenta.getYear();
                // logger.info(fechaVenta);
                // String fechaVence = fechaEntradaVence.getDay() + "." +
                // fechaEntradaVence.getMonth() + "."
                // + fechaEntradaVence.getYear();
                // logger.info(fechaVence);
                Date fechaEntradaVenta = guardado.getFechaDocumento();
                Calendar calVent = Calendar.getInstance();
                calVent.setTime(fechaEntradaVenta);

                int yearVent = calVent.get(Calendar.YEAR);
                int monthVent = calVent.get(Calendar.MONTH) + 1;
                int dayVent = calVent.get(Calendar.DAY_OF_MONTH);

                Date fechaEntradaVence = guardado.getFechaDocumento();
                Calendar calVenc = Calendar.getInstance();
                calVenc.setTime(fechaEntradaVence);

                int yearVenc = calVenc.get(Calendar.YEAR);
                int monthVenc = calVenc.get(Calendar.MONTH) + 1;
                int dayVenc = calVenc.get(Calendar.DAY_OF_MONTH);

                String fechaVenta = dayVent + "." + monthVent + "." + yearVent;
                logger.info(fechaVenta);
                String fechaVence = dayVenc + "." + monthVenc + "." + yearVenc;
                logger.info(fechaVence);

                FacturaM facturaMekano = new FacturaM();
                facturaMekano.setCLAVE("Set_Gestion_Primario");
                facturaMekano.setTIPO("NDE1");
                facturaMekano.setPREFIJO(guardado.getPrefijo().getPrefijo());
                facturaMekano.setNUMERO(guardado.getNumeroDocumento());
                facturaMekano.setFECHA(fechaVenta);
                facturaMekano.setVENCE(fechaVence);
                facturaMekano.setTERCERO(guardado.getIdProveedor().getNit());
                // facturaMekano.setTERCERO("75082596");
                // facturaMekano.setVENDEDOR(factura.getIdVendedor().toString());
                // ! se debe enviar un valor por defecto porque no existen los vendedores
                // ! para las notas debito
                facturaMekano.setVENDEDOR("24347052");
                facturaMekano.setLISTA("NA");
                facturaMekano.setBANCO("CG");
                facturaMekano.setUSUARIO("SUPERVISOR");
                switch (guardado.getIdSede().getId()) {
                    case 1:
                        facturaMekano.setCENTRO("04");
                        break;
                    case 6:
                        facturaMekano.setCENTRO("02");
                        break;
                    case 7:
                        facturaMekano.setCENTRO("03");
                        break;
                    case 11:
                        facturaMekano.setCENTRO("01");
                        break;
                    case 12:
                        facturaMekano.setCENTRO("06");
                        break;
                    case 13:
                        facturaMekano.setCENTRO("10");
                        break;
                    case 14:
                        facturaMekano.setCENTRO("09");
                        break;
                    case 15:
                        facturaMekano.setCENTRO("05");
                        break;

                    default:
                        break;
                }
                // facturaMekano.setCENTRO(nuevoNotaDebito.getIdSede().getId().toString());
                facturaMekano.setBODEGA("NA");
                // ! se le envia valor por defecto porque no tiene asociada una referencia
                facturaMekano.setREFERENCIA("0103");
                facturaMekano.setENTRADA(1);
                facturaMekano.setSALIDA(0.0);
                facturaMekano.setUNITARIO(guardado.getTotal().doubleValue());
                facturaMekano.setNOTA(guardado.getObservaciones());
                // facturaMekano.setNOTA("-");

                Gson gson = new Gson();
                String rjson = gson.toJson(facturaMekano);

                logger.info(rjson);
                HttpHeaders headers = new HttpHeaders();
                // headers.setContentType(MediaType.APPLICATION_JSON_VALUE);
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                // HttpEntity<String> entity = new HttpEntity<String>(rjson);
                HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
                RestTemplate rest = new RestTemplate();

                if (entity != null && 1 != 1) {
                    // if (entity != null) {
                    String result = rest.postForObject(uri, entity, String.class);
                    if (result.contains("EL TERCERO")) {
                        if (result.contains("NO EXISTE")) {
                            logger.info("existe un error de tercero inexistente");
                            // this.crearClienteMekano(guardado.getIdProveedor());
                            // this.crearFacturaMekanoN(guardado);
                            logger.info("tercero inexistente");
                            throw new Exception("no existe un tercero");
                        }
                    }
                    logger.info(result);
                }
                // String result = rest.postForObject(uri, entity, String.class);

            }

            RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito creando nota debito");
            respuestaDto.setObjetoRespuesta(guardado);
            respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error en la creación de la nota debito");
            RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error en la creación de la nota debito");
            respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return respuesta;
    }

    private void crearClienteMekano(Proveedor proveedor) {

        String clave = "Set_Terceros";
        String codigoProveedor = "";
        String digitoVerificacion = "";
        String clasificacionLegal = "";
        String tContacto = "";
        String nombre = "";
        String nombreD = ""; // ! solo aplica para proveedor naturaleza (N)
        String apellido = ""; // ! solo aplica para proveedor naturaleza (N)
        String apellidoD = ""; // ! solo aplica para proveedor naturaleza (N)
        String tProveedor = "";
        String tDireccion = "";
        String tTelefono = "";
        String tEmail = "";
        String tCodTipoIdentificacion = "";
        String nidCiudad = "";
        String codSociedad = "";

        // if (clientes != null) {

        if (proveedor.getProveedor() != null) {
            logger.info(proveedor.getProveedor());
        }
        if (proveedor.getClasificacionLegal().getId() == 1 || proveedor.getClasificacionLegal().getId() == 12
                || proveedor
                        .getClasificacionLegal().getId() == 13
                || proveedor.getClasificacionLegal().getId() == 15) {
            String str = proveedor.getProveedor();
            String[] arr = str.split("-");
            logger.info(arr.length);
            nombre = arr[0];
            nombreD = arr[1];
            apellido = arr[2];
            apellidoD = arr[3];
        } else {
            nombre = proveedor.getProveedor();
        }

        // // if (proveedor.getContacto() != null) {
        // // logger.info(proveedor.getContacto());
        // // }
        logger.info(proveedor.getClasificacionLegal().getId());
        codigoProveedor = proveedor.getNit() != null ? proveedor.getNit() : "";
        // ! adicionar digito de verificacion
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
                break;
            case 6:
                clasificacionLegal = "J";
                break;
            case 7:
                clasificacionLegal = "J";
                codSociedad = "03";
                break;
            case 8:
                clasificacionLegal = "J";
                break;
            case 9:
                clasificacionLegal = "J";
                break;
            case 10:
                clasificacionLegal = "J";
                codSociedad = "00N";
                break;
            case 11:
                clasificacionLegal = "J";
                codSociedad = "00R";
                break;
            case 12:
                clasificacionLegal = "N";
                codSociedad = "01";
                break;
            case 13:
                clasificacionLegal = "N";
                codSociedad = "02";
                break;
            case 14:
                clasificacionLegal = "J";
                codSociedad = "03";
                break;
            case 15:
                clasificacionLegal = "N";
                codSociedad = "04";
                break;
            case 16:
                clasificacionLegal = "J";
                codSociedad = "05";
                break;
            case 17:
                clasificacionLegal = "J";
                codSociedad = "06";
                break;
            case 18:
                clasificacionLegal = "J";
                codSociedad = "07";
                break;
            case 19:
                clasificacionLegal = "J";
                codSociedad = "08";
                break;
            case 20:
                clasificacionLegal = "J";
                codSociedad = "09";
                break;
            case 21:
                clasificacionLegal = "J";
                codSociedad = "10";
                break;
            default:
                break;
        }
        // // if (proveedor.getClasificacionLegal().getId() == 1) {
        // // clasificacionLegal = "N";
        // // } else if (proveedor.getClasificacionLegal().getId() == 7) {
        // // clasificacionLegal = "J";
        // // }

        // // nombre = proveedor.getContacto() != null ? proveedor.getContacto() : "";
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
        // headers.setContentType(MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        // HttpEntity<String> entity = new HttpEntity<String>(rjson);
        HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
        RestTemplate rest = new RestTemplate();
        // String result = rest.postForObject(uri, entity, String.class);
        // logger.info(result);
        // }
    }

    private void crearFacturaMekanoN(NotaDebito guardado) throws InterruptedException {
        // if (factura.getIdTipoVenta().getId() == 15) {

        String uri = "http://190.145.10.42:8080/api/v1/TApoloRestInterface/execute";

        logger.info(guardado.getFechaDocumento().toString());
        logger.info(guardado.getFechaDocumento().toString());
        Date fechaEntradaVenta = guardado.getFechaDocumento();
        Date fechaEntradaVence = guardado.getFechaDocumento();
        String fechaVenta = fechaEntradaVenta.getDay() + "." + fechaEntradaVenta.getMonth() + "."
                + fechaEntradaVenta.getYear();
        logger.info(fechaVenta);
        String fechaVence = fechaEntradaVence.getDay() + "." + fechaEntradaVence.getMonth() + "."
                + fechaEntradaVence.getYear();
        logger.info(fechaVence);

        FacturaM facturaMekano = new FacturaM();
        facturaMekano.setCLAVE("Set_Gestion_Primario");
        facturaMekano.setTIPO("NDE1");
        facturaMekano.setPREFIJO(guardado.getPrefijo().getPrefijo());
        facturaMekano.setNUMERO(guardado.getNumeroDocumento());
        facturaMekano.setFECHA(fechaVenta);
        facturaMekano.setVENCE(fechaVence);
        facturaMekano.setTERCERO(guardado.getIdProveedor().getNit());
        // facturaMekano.setTERCERO("75082596");
        // facturaMekano.setVENDEDOR(factura.getIdVendedor().toString());
        // ! se debe enviar un valor por defecto porque no existen los vendedores
        // ! para las notas debito
        facturaMekano.setVENDEDOR("24347052");
        facturaMekano.setLISTA("NA");
        facturaMekano.setBANCO("CG");
        facturaMekano.setUSUARIO("SUPERVISOR");
        switch (guardado.getIdSede().getId()) {
            case 1:
                facturaMekano.setCENTRO("04");
                break;
            case 6:
                facturaMekano.setCENTRO("02");
                break;
            case 7:
                facturaMekano.setCENTRO("03");
                break;
            case 11:
                facturaMekano.setCENTRO("01");
                break;
            case 12:
                facturaMekano.setCENTRO("06");
                break;
            case 13:
                facturaMekano.setCENTRO("10");
                break;
            case 14:
                facturaMekano.setCENTRO("09");
                break;
            case 15:
                facturaMekano.setCENTRO("05");
                break;

            default:
                break;
        }
        // facturaMekano.setCENTRO(nuevoNotaDebito.getIdSede().getId().toString());
        facturaMekano.setBODEGA("NA");
        facturaMekano.setREFERENCIA("0103");// ! se le envia valor por defecto porque no tiene asociada una referencia
        facturaMekano.setENTRADA(1);
        facturaMekano.setSALIDA(0.0);
        facturaMekano.setUNITARIO(guardado.getTotal().doubleValue());
        facturaMekano.setNOTA(guardado.getObservaciones());
        // facturaMekano.setNOTA("-");

        Gson gson = new Gson();
        String rjson = gson.toJson(facturaMekano);

        logger.info(rjson);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(rjson, headers);
        logger.info(entity);
        RestTemplate rest = new RestTemplate();
        // if (entity != null && 1 != 1) {
        if (entity != null) {
            TimeUnit.SECONDS.sleep(2);
            String result = rest.postForObject(uri, entity, String.class);
            logger.info(result);
        }
        // }
    }

    // Obtener por numero
    @PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
    public ResponseEntity<Object> obtenerNumero(Integer idSede) {

        ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

        try {
            List<NotaDebito> notaDatos = this.notaDebitoDao.findByIdSede(idSede);
            RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de nota debito exitosa");
            respuestaDto.setObjetoRespuesta(notaDatos);
            respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error obteniendo nota debito " + e);
            RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error obteniendo nota debito");
            respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return respuesta;

    }

    // Listar Notas debito segun filtros
    @PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
    public ResponseEntity<Object> obtenerNDFiltros(String numeroDocumento, String fechaInicial, String fechaFinal,
            String estadoDocumento, Integer page) throws Exception {

        ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
        Integer suma = 0;
        try {
            Page<NotaDebito> notaDebito = null;
            Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
            InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
                    .buscarPorIdUsuario(usuarioAutenticado.getId());

            Pageable pageConfig = PageRequest.of(page, 10);

            int idSede = usuarioInformacion.getIdOrganizacion().getId();

            if (numeroDocumento != "" && fechaInicial == "" && fechaFinal == "" && estadoDocumento == "") {
                notaDebito = this.notaDebitoDao.obtenerNumeroDocumento(idSede, numeroDocumento, pageConfig);
                suma = this.notaDebitoDao.suma1(idSede, numeroDocumento);
            }

            if (numeroDocumento != "" && fechaInicial == "" && fechaFinal == "" && estadoDocumento != "") {
                notaDebito = notaDebitoDao.findByNumeroDocumentoEstado(idSede, numeroDocumento, estadoDocumento,
                        pageConfig);
                suma = this.notaDebitoDao.suma2(idSede, numeroDocumento, estadoDocumento);
            }

            if (numeroDocumento != "" && fechaInicial != "" && fechaFinal == "" && estadoDocumento == "") {
                notaDebito = notaDebitoDao.findByNumeroDocumentoFechaI(idSede, numeroDocumento, fechaInicial,
                        pageConfig);
                suma = this.notaDebitoDao.suma3(idSede, numeroDocumento, fechaInicial);
            }

            if (numeroDocumento != "" && fechaInicial != "" && fechaFinal != "" && estadoDocumento == "") {
                notaDebito = notaDebitoDao.findByNumeroFechas(idSede, numeroDocumento, fechaInicial, fechaFinal,
                        pageConfig);
                suma = this.notaDebitoDao.suma4(idSede, numeroDocumento, fechaInicial, fechaFinal);
            }

            if (numeroDocumento == "" && fechaInicial != "" && fechaFinal != "" && estadoDocumento == "") {
                notaDebito = notaDebitoDao.findByFechas(idSede, fechaInicial, fechaFinal, pageConfig);
                suma = this.notaDebitoDao.suma5(idSede, fechaInicial, fechaFinal);
            }

            if (numeroDocumento == "" && fechaInicial != "" && fechaFinal != "" && estadoDocumento != "") {
                notaDebito = notaDebitoDao.findByFechasEstado(idSede, estadoDocumento, fechaInicial, fechaFinal,
                        pageConfig);
                suma = this.notaDebitoDao.suma6(idSede, estadoDocumento, fechaInicial, fechaFinal);
            }

            if (numeroDocumento == "" && fechaInicial != "" && fechaFinal == "" && estadoDocumento == "") {
                notaDebito = notaDebitoDao.findByFechaInicial(idSede, fechaInicial, pageConfig);
                suma = this.notaDebitoDao.suma7(idSede, fechaInicial);
            }

            if (numeroDocumento == "" && fechaInicial != "" && fechaFinal == "" && estadoDocumento != "") {
                notaDebito = notaDebitoDao.findByFechaInicialEstado(idSede, estadoDocumento, fechaInicial, pageConfig);
                suma = this.notaDebitoDao.suma8(idSede, estadoDocumento, fechaInicial);
            }

            if (numeroDocumento == "" && fechaInicial == "" && fechaFinal == "" && estadoDocumento != "") {
                notaDebito = this.notaDebitoDao.obtenerEstadoDocumento(idSede, estadoDocumento, pageConfig);
                suma = this.notaDebitoDao.suma9(idSede, estadoDocumento);
            }

            RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de notas debito exitosa",
                    notaDebito.getContent(), suma, notaDebito.getTotalElements() + "");

            respuestaDto.setObjetoRespuesta(notaDebito);
            respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

        } catch (RuntimeException e) {
            logger.error("Error al obtener las notas debito" + e);
            RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener las notas debito " + e);
            respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }

    @PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
    public ResponseEntity<Object> obtenerND(String numeroDocumento) {

        ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

        try {
            Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
            InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
                    .buscarPorIdUsuario(usuarioAutenticado.getId());

            int idSede = usuarioInformacion.getIdOrganizacion().getId();
            NotaDebito notaDatos = this.notaDebitoDao.findByIdSedeNumero(idSede, numeroDocumento);
            RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de nota debito exitosa");
            respuestaDto.setObjetoRespuesta(notaDatos);
            respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error obteniendo nota debito " + e);
            RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error obteniendo nota debito");
            respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return respuesta;

    }

    @PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
    public ResponseEntity<Object> actualizarNotaDebito(@RequestBody NotaDebito notaDebito) {

        ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

        NotaDebito notaEnBd = this.notaDebitoDao.findById(notaDebito.getId()).get();
        try {
            conceptoNdDao.eliminarConceptosNotasDebito(notaDebito.getId());
            if (!notaEnBd.getId().equals(notaDebito.getId())) {
                NotaDebito actualizada = this.notaDebitoDao.save(notaDebito);

                RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito actualizando la nota debito");
                respuestaDto.setObjetoRespuesta(actualizada);
                respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

            } else {
                NotaDebito actualizada = this.notaDebitoDao.save(notaDebito);
                RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito actualizando la nota debito");
                respuestaDto.setObjetoRespuesta(actualizada);
                respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
            }

        } catch (Exception e) {
            logger.error("Error en la actualización la nota debito");
            RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error en la actualización de nota debito");
            respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return respuesta;
    }

    // Borrar
    @PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
    public ResponseEntity<Object> borrar(@RequestParam Integer idNotaDebito) {

        ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);

        try {
            conceptoNdDao.eliminarConceptosNotasDebito(idNotaDebito);
            this.notaDebitoDao.delete(this.notaDebitoDao.findById(idNotaDebito).get());

            RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito");
            respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
        } catch (Exception e) {
            Throwable t = e.getCause();

            if (t instanceof ConstraintViolationException) {
                logger.error("se encuentra asociada en el sistema " + e.getCause());
                RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.BAD_REQUEST,
                        "Error,  se encuentra asociada en el sistema");
                respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.BAD_REQUEST);
            } else {
                logger.error("Error en el borrado ");
                RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el borrado ");
                respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return respuesta;
    }

    public List<NotaDebito> notasDebitoPorProveedor(Integer idProveedor, Date fechaInicial, Date fechaFinal,
            Integer idSede) {
        List<NotaDebito> lista = notaDebitoDao.busarPorProveedorYFecha(idProveedor, fechaInicial, fechaFinal, idSede);
        return lista;
    }

    public List<NotaDebito> listarnotadebitopendiente(Integer idSede) {
        List<NotaDebito> lista = notaDebitoDao.listarnotadebitopendiente(idSede);
        return lista;
    }

    @PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
    public ResponseEntity<Object> obtenerNotasDebitoMes(Integer page) {

        ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
        Integer suma = 0;
        try {
            Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
            InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
                    .buscarPorIdUsuario(usuarioAutenticado.getId());
            Pageable pageConfig = PageRequest.of(page, 10);
            int idSede = usuarioInformacion.getIdOrganizacion().getId();
            Page<NotaDebito> notasDebito = notaDebitoDao.obtenerNotasDebitoDelMes(idSede, pageConfig);
            suma = notaDebitoDao.obtenerSumaDebitoDelMes(idSede);
            RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de notas credito exitosa",
                    notasDebito.getContent(), suma, notasDebito.getTotalElements() + "");
            respuestaDto.setObjetoRespuesta(notasDebito);
            respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

        } catch (RuntimeException e) {
            logger.error("Error al obtener las notas credito" + e);
            RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener las notas credito " + e);
            respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }

    public List<NotaDebito> listarinformeNotasDebitoVencidasCompraFiltros(Integer idSede, Integer idProveedor,
            String fechaInicial, String fechaFinal) {
        List<NotaDebito> notasDebitos;
        StringBuilder query = new StringBuilder();
        generarQueryInformeNotasDebitoVencida(query, idSede, fechaInicial, fechaFinal, idProveedor);
        TypedQuery<NotaDebito> notasDebitoInfoQuery = (TypedQuery<NotaDebito>) entityManager
                .createNativeQuery(query.toString(), NotaDebito.class);
        notasDebitos = notasDebitoInfoQuery.getResultList();
        return notasDebitos;
    }

    private void generarQueryInformeNotasDebitoVencida(StringBuilder query, Integer idSede, String fechaInicio,
            String fechaFin, Integer proveedor) {

        query.append("select * FROM nota_debito where id_sede=");

        query.append("" + idSede);
        if (!fechaInicio.equals("null") && fechaFin.equals("null")) {
            query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "= date('" + fechaInicio + "')");
        } else if (fechaInicio.equals("null") && !fechaFin.equals("null")) {
            query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "<= date('" + fechaFin + "')");
        } else if (!fechaInicio.equals("null") && !fechaFin.equals("null")) {
            query.append(" and date_format(date(fecha_documento),'%Y-%m-%d')" + "between " + "date('" + fechaInicio
                    + "') and " + "date('" + fechaFin + "')");
        }
        if (proveedor != 0) {
            query.append(" and id_proveedor=" + proveedor);
        }
    }

    @PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
    public ResponseEntity<Object> obtenerNotasDebitoAnular(String numero) {

        ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
        try {
            List<NotaDebito> notasDebito = notaDebitoDao.obtenerNotasDebitoAnular(numero);
            RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "obtencion de notas debito exitosa");
            respuestaDto.setObjetoRespuesta(notasDebito);
            respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);

        } catch (RuntimeException e) {
            logger.error("Error al obtener las notas credito" + e);
            RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener las notas debito " + e);
            respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuesta;
    }

    // anular
    @PreAuthorize("hasAuthority('SUPER') or hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('VENDEDOR')")
    public ResponseEntity<Object> anularNotaDebito(@RequestBody NotaDebito notaDebito) {
        logger.info(notaDebito);
        ResponseEntity<Object> respuesta = ResponseEntity.ok(HttpStatus.OK);
        try {
            logger.info(notaDebito);
            notaDebito.setEstadoDocumento("Anulado");
            conceptoNdDao.eliminarConceptosNotasDebito(notaDebito.getId());
            this.notaDebitoDao.save(notaDebito);
            RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.OK, "Exito anulando nota debito");
            respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error en la anulacion de la nota debito");
            RespuestaDto respuestaDto = new RespuestaDto(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error en la anulacion de la nota debito");
            respuesta = new ResponseEntity<>(respuestaDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return respuesta;
    }

    public List<NotaDebito> obtenerPendientesPorProveedor(Integer idProveedor, Integer idSede) {
        List<NotaDebito> notas = notaDebitoDao.obtenerNotasDebitosPorPagar(idProveedor, idSede);
        return notas;
    }

    public List<NotaDebito> obtenerPendientesPorProveedorActualizar(Integer idProveedor, Integer idSede,
            Integer idComprobante) {
        List<NotaDebito> notas = notaDebitoDao.obtenerNotasDebitosPorPagarActualizar(idProveedor, idSede,
                idComprobante);
        return notas;
    }
}
