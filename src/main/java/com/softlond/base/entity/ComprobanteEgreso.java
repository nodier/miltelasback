package com.softlond.base.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Data
@Entity
@Table(name = "comprobante_egreso")
public class ComprobanteEgreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "fecha_documento")
    private Date fechaDocumento;

    @ManyToOne
    @JoinColumn(name = "id_periodo")
    private PeriodosContables periodo;

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "numero_documento")
    private Integer numeroDocumento;

    @Column(name = "prefijo")
    private String prefijo;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "id_sede")
    private Organizacion sede;

    @Column(name = "total")
    private Integer total;

    @Column(name = "saldo")
    private Float saldo;

    @Column(name = "observaciones")
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "cod_estado_con")
    private EstadoDocumento estado;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_comprobante")
    private List<PagoComprobanteEgreso> pagos;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_comprobante")
    private List<DescuentoComprobanteEgreso> descuentos;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "nid_comprobante_egreso")
    private List<RetencionComprobanteEgreso> listRetenciones;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "nid_rbo_egreso")
    private List<ConceptoReciboEgreso> conceptos;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_comprobante")
    private AsignacionComprobante asignacion;

    private boolean editable;

    private boolean isRestringido;
}
