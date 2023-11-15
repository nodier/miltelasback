package com.softlond.base.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.softlond.base.dto.RespuestaDto;
import com.softlond.base.entity.Clientes;
import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.FacturaImpuestos;
import com.softlond.base.entity.FacturaRetenciones;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaCreditoCliente;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.entity.NotaDebitoCliente;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.AsignacionComprobanteDao;
import com.softlond.base.repository.AsignacionReciboDao;
import com.softlond.base.repository.ClientesDao;
import com.softlond.base.repository.ConceptosReciboCajaDao;
import com.softlond.base.repository.FacturaDao;
import com.softlond.base.repository.InformeFacturasVencidasDao;
import com.softlond.base.repository.NotaCreditoClienteDao;
import com.softlond.base.repository.NotaDebitoClienteDao;
import com.softlond.base.repository.ReciboCajaVentaDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.Cartera;
import com.softlond.base.response.InfromeFacturasVencidas;
import com.softlond.base.response.InfromeFacturasVencidasCliente;
import com.softlond.base.response.MovimientoProveedor;
import com.softlond.base.response.Paginacion;

@Service
public class CarteraService {

	private static final Logger logger = Logger.getLogger(CarteraService.class);

	final int ITEMS_POR_PAGINA = 10;

	/*
	 * @Autowired private InformeFacturasVencidasDao informeFacturasVencidasDao;
	 */

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	@Autowired
	private ClientesDao clientesDao;

	@Autowired
	private NotaDebitoClienteService servicioND;

	@Autowired
	private NotaCreditoClienteService servicioNC;

	@Autowired
	private ConceptosReciboCajaDao conceptosReciboCajaDao;

	@Autowired
	private AsignacionReciboDao asignacionReciboDao;

	@Autowired
	private FacturaDao facturaDao;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private NotaCreditoClienteDao ncDao;

	@Autowired
	private NotaDebitoClienteDao ndDao;

	@Autowired
	private ReciboCajaVentaDao reciboCajaVentaDao;

	public List<Cartera> listarCarteraCliente(int idSede, Integer page) {

		// Pageable pageConfig = PageRequest.of(pagina, 10);

		logger.info("ingresa a listar cartera cliente");
		List<Cartera> vecom = new ArrayList<>();
		// Obtener clientes
		Pageable pageConfig = PageRequest.of(page, 10);
		Page<Clientes> clientes = clientesDao.obtenerTodosClientes(pageConfig);
		logger.info(clientes.getSize());
		// List<Clientes> clientes = clientesDao.obtenerTodosClientes(pageConfig);
		for (Clientes c : clientes) {
			Integer totalFactura = facturaDao.obtenerTotalFacturasPorPagar(c.getId(), idSede);
			Integer totalNotaCredito = ncDao.obtenerTotalNotasCreditosClientePorPagar(c.getId(), idSede);
			Integer totalNotaDebito = ndDao.obtenerTotalNotasDebitosPorPagar(c.getId(),
					idSede);
			Date fechaPago = reciboCajaVentaDao.obtenerFechaUltimoPago(c.getId());
			// logger.info(fechaPago);
			// Date fechaPago = new Date();
			// if (fechaPago != null) {
			// logger.info(fechaPago);
			// }
			Cartera vencida = Cartera.convertirCartera(c, fechaPago);
			Integer abonos = conceptosReciboCajaDao.totalConceptosClienteFacturas(c.getId(), idSede) +
					conceptosReciboCajaDao.totalConceptosClienteNotaDebito(c.getId(), idSede);
			Integer descuentos = conceptosReciboCajaDao.obtenerTotalDescuentoClienteFacturas(c.getId(),
					idSede) +
					conceptosReciboCajaDao.obtenerTotalDescuentoClienteNotaDebito(c.getId(),
							idSede);
			Integer asignacion = asignacionReciboDao.asignacionFacturaCliente(c.getId(),
					idSede);
			Integer total = totalFactura - totalNotaCredito + totalNotaDebito - abonos -
					descuentos - asignacion;
			vencida.setSaldo(total);
			// vencida.setSaldo(0);
			vecom.add(vencida);
		}
		vecom.sort(new OrdenPorFechaCartera());
		// logger.info(vecom);
		// if (vecom != null) {
		// logger.info("existe vecom en paginado");
		// logger.info(vecom);
		// }
		return vecom;
	}

	public Paginacion listarCarteraPaginado(int idSede, int pagina) {
		// logger.info("ingresa a paginacion");
		// logger.info(idSede + "-" + pagina);
		// List<Cartera> vecom = listarCarteraCliente(idSede);
		List<Cartera> vecom = listarCarteraCliente(idSede, pagina);
		Integer cantidadClientes = clientesDao.obtenerCantidadClientes();
		logger.info(cantidadClientes);
		// if (vecom != null) {
		// logger.info("existe vecom");
		// logger.info(vecom);
		// }
		// return Paginacion.paginar(vecom, ITEMS_POR_PAGINA, pagina);
		return new Paginacion(vecom, cantidadClientes);
	}

}

class OrdenPorFechaCartera implements Comparator<Cartera> {
	public int compare(Cartera a, Cartera b) {
		return (int) (a.getDiasVencidos() - b.getDiasVencidos());
	}
}
