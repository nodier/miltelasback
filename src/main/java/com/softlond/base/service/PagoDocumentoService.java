package com.softlond.base.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.softlond.base.entity.Factura;
import com.softlond.base.entity.FacturaCompra;
import com.softlond.base.entity.InformacionUsuario;
import com.softlond.base.entity.NotaCredito;
import com.softlond.base.entity.NotaDebito;
import com.softlond.base.entity.Organizacion;
import com.softlond.base.entity.Usuario;
import com.softlond.base.repository.ConceptoReciboEgresoDao;
import com.softlond.base.repository.FacturaCompraDao;
import com.softlond.base.repository.FacturaDao;
import com.softlond.base.repository.NotaCreditoClienteDao;
import com.softlond.base.repository.NotaCreditoDao;
import com.softlond.base.repository.NotaDebitoClienteDao;
import com.softlond.base.repository.NotaDebitoDao;
import com.softlond.base.repository.UsuarioInformacionDao;
import com.softlond.base.response.PagoDocumento;

@Service
public class PagoDocumentoService {

	@Autowired
	private FacturaCompraDao facturaCompraDao;

	@Autowired
	private NotaCreditoDao notaCreditoDao;

	@Autowired
	private NotaDebitoDao notaDebitoDao;

	@Autowired
	private FacturaDao facturaDao;

	@Autowired
	private NotaCreditoClienteDao notaCreditoClienteDao;

	@Autowired
	private NotaDebitoClienteDao notaDebitoClienteDao;

	@Autowired
	private ConceptoReciboEgresoDao conceptosDao;

	@Autowired
	UsuarioInformacionDao usuarioInformacionDao;

	public List<PagoDocumento> listarPagos(String numero, String tipoPago, String tipoDocumento) {
		Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = (Usuario) autenticacion.getPrincipal();
		InformacionUsuario usuarioInformacion = this.usuarioInformacionDao
				.buscarPorIdUsuario(usuarioAutenticado.getId());
		Organizacion sede = usuarioInformacion.getIdOrganizacion();
		List<PagoDocumento> pagos = new ArrayList<PagoDocumento>();
		PagoDocumento documento;
		if (tipoPago.equals("Proveedores")) {
			switch (tipoDocumento) {
				case "Facturas de Compra":
					FacturaCompra factura = facturaCompraDao.obtenerFacturaNumero(numero, sede.getId());
					documento = PagoDocumento.convertirFactura(factura);
					pagos.add(documento);
					break;
				case "Notas Débito":
					NotaDebito notaDebito = notaDebitoDao.findByNumeroDocumento(numero);
					documento = PagoDocumento.convertirNotaDebito(notaDebito);
					pagos.add(documento);
					break;
				default:
					break;
			}
		} else if (tipoPago.equals("Clientes")) {
			switch (tipoDocumento) {
				case "Facturas de Venta":
					Factura factura = facturaDao.facturaPorNumero(numero);
					documento = PagoDocumento.convertirFacturaCliente(factura);
					pagos.add(documento);
					break;
				case "Notas Débito":
					NotaDebito notaDebito = notaDebitoDao.findByNumeroDocumento(numero);
					documento = PagoDocumento.convertirNotaDebito(notaDebito);
					pagos.add(documento);
					break;
				default:
					break;
			}
		}
		return pagos;
	}

}
