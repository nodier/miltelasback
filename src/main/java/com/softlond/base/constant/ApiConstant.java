package com.softlond.base.constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ApiConstant {

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	/*** API´s Usuarios ***/
	public static final String USUARIO_CONTROLADOR_API = "api/usuarios";
	public static final String USUARIO_CONTROLADOR_API_ACTUALIZAR = "actualizar";
	public static final String USUARIO_CONTROLADOR_API_SUPER_CREAR = "super/crear";
	public static final String USUARIO_CONTROLADOR_API_CREAR = "crear";
	public static final String USUARIO_CONTROLADOR_API_CAMBIAR_CONTRASENA = "cambiar-contrasena";
	public static final String USUARIO_CONTROLADOR_API_CAMBIAR_CONTRASENA_LOGUEADO = "cambiar-contrasena-sesion";
	public static final String USUARIO_CONTROLADOR_API_BORRAR = "borrar";
	public static final String USUARIO_CONTROLADOR_API_SUPER_LISTADO = "super/listar-todos";
	public static final String USUARIO_CONTROLADOR_API_ADMIN_LISTADO = "admin/listar-todos";
	public static final String USUARIO_CONTROLADOR_API_LISTADO = "listar-todos";
	public static final String USUARIO_CONTROLADOR_API_ID_LISTAR_POR_ROL = "listar-usuarios-id-name-por-rol";
	public static final String USUARIO_CONTROLLER_API_BUSCAR_POR_ID = "buscar-por-id";
	public static final String USUARIO_CONTROLADOR_API_ENLAZAR_PERFIL_USUARIO = "enlazar-perfil-usuario";
	public static final String USUARIO_CONTOLADOR_API_OBTENER_INFO_USUARIO = "obtener-info-usuario-autenticado";
	public static final String USUARIO_CONTOLADOR_API_LISTAR = "listar";

	/**
	 * MENU PATHS
	 */
	public static final String MENU_CONTROLADOR_API = "api/menu";
	public static final String MENU_CONTROLADOR_API_CREAR = "crear";
	public static final String MENU_CONTROLADOR_API_LISTAR_TODOS = "ver-todos";
	public static final String MENU_CONTROLADOR_API_BORRAR = "eliminar";
	public static final String MENU_CONTROLADOR_API_ACTUALIZAR = "actualizar";

	/**
	 * MODULE PATH
	 */
	public static final String MODULO_CONTROLADOR_API = "api/modulos";
	public static final String MODULO_CONTROLADOR_API_CREAR = "crear";
	public static final String MODULO_CONTROLADOR_API_SUPER_LISTAR_TODOS = "super/ver-todos";
	public static final String MODULO_CONTROLADOR_API_ADMIN_LIST = "listar-mis-modulos";
	public static final String MODULO_CONTROLADOR_API_BORRAR = "eliminar";
	public static final String MODULO_CONTROLADOR_API_ACTUALIZAR = "actualizar";
	public static final String MODULO_CONTROLADOR_API_ENLAZAR_MENU = "enlazar-modulo-menu";
	public static final String MODULO_CONTROLADOR_API_DESEMPAREJAR_MENU = "desenlazar-modulo-menu";
	public static final String MODULO_CONTROLADOR_API_LISTAR_MENUS_POR_MODULO = "listar-menus-por-modulo";
	public static final String MODULO_CONTROLADOR_API_OBTENER_MODULOS_MENU_JSON = "obtener-modulos-menus-json";
	public static final String MODULO_CONTROLAR_API_GUARDAR_POSICION = "guardar-posicion";

	/**
	 * PLAN PATHS
	 */
	public static final String PLAN_CONTROLADOR_API = "api/plan";
	public static final String PLAN_CONTROLADOR_API_CREAR = "crear";
	public static final String PLAN_CONTROLAR_API_ACTUALIZAR = "actualizar";
	public static final String PLAN_CONTROLADOR_API_BORRAR = "eliminar";
	public static final String PLAN_CONTROLADOR_API_SUPER_ENLAZAR_MODULO = "super/enlazar-plan-modulo";
	public static final String PLAN_CONTROLADOR_API_SUPER_DESENLAZAR_PLAN_MODULO = "super/desenlazar-plan-modulo";
	public static final String PLAN_CONTROLAR_API_SUPER_LISTADO = "super/listar-todos";
	public static final String PLAN_CONTROLADOR_API_LIST_MODULOS_POR_PLAN = "listar-modulos-por-plan";

	/**
	 * PROFILE PATHS
	 */
	public static final String PERFIL_CONTROLADOR_API = "api/perfil";
	public static final String PERFIL_CONTROLADOR_API_VER_TODOS = "ver-todos";
	public static final String PROFILE_CONTROLLER_API_LIST_PROFILES_BY_COMPANY = "listar-perfiles-por-empresa";
	public static final String PERFIL_CONTROLADOR_API_CREAR = "crear";
	public static final String PERFIL_CONTROLADOR_API_BORRAR = "eliminar";
	public static final String PERFIL_CONTROLADOR_API_ACTUALIZAR = "actualizar";
	public static final String PERFIL_CONTROLADOR_API_EMPAREJAR_PERFIL_MODULO = "enlazar-perfil-modulo";
	public static final String PERFIL_CONTROLADOR_API_DESEMPAREJAR_PERFIL_MODULO = "desenlazar-perfil-modulo";
	public static final String PERFIL_CONTROLADOR_API_LISTAR_MODULOS_POR_PERFIL = "listar-modulos-por-perfil";
	public static final String PERFIL_CONTROLADOR_API_ACTUALIZAR_POSICIONES = "actualizar-index";

	/**
	 * IMPEDIMENTS PROFILE
	 */
	public static final String IMPEDIMENTS_PROFILE_CONTROLLER_API = "api/perfil-impedimentos";
	public static final String IMPEDIMENTS_PROFILE_CONTROLLER_API_GET_ALL = "ver-todos";
	public static final String IMPEDIMENTS_PROFILE_CONTROLLER_API_CREATE = "crear";
	public static final String IMPEDIMENTS_PROFILE_CONTROLLER_API_DELETE = "eliminar";
	public static final String IMPEDIMENTS_PROFILE_CONTROLLER_API_UPDATE = "actualizar";
	public static final String IMPEDIMENTS_PROFILE_CONTROLLER_API_ASSIGN_MASSIVE = "asignar-restricciones";
	public static final String IMPEDIMENTS_PROFILE_CONTROLLER_API_GET_IMPEDIMENTS = "ver-restricciones";

	/**
	 * MASTER VALUE
	 */
	public static final String MAESTRO_VALOR_CONTROLADOR_API = "api/maestro-valor";
	public static final String MAESTRO_VALOR_CONTROLADOR_API_OBTENER_TODOS = "ver-todos";
	public static final String MAESTRO_VALOR_CONTROLADOR_API_CREAR = "crear";
	public static final String MAESTRO_VALOR_CONTROLADOR_API_CREAR_MASIVO = "crear-masivo";
	public static final String MAESTRO_VALOR_CONTROLADOR_API_BORRAR = "eliminar";
	public static final String MAESTRO_VALOR_CONTROLADOR_API_ACTUALIZAR = "actualizar";
	public static final String MAESTRO_VALOR_CONTROLADOR_API_VER_POR_OBJETIVO = "ver-por-objetivo";
	public static final String MAESTRO_VALOR_CONTROLADOR_API_VER_DOCUMENTOS = "ver-documentos";
	public static final String MAESTRO_VALOR_CONTROLADOR_API_VER_TIPOS_PROVEEDOR = "ver-tipos-proveedor";
	public static final String MAESTRO_VALOR_CONTROLADOR_API_VER_MOTIVOS_SALIDA = "ver-motivos-salida";
	/**
	 * ORGANIZACION PATHS
	 */

	public static final String ORGANIZACION_CONTROLADOR_API = "api/organizacion";
	public static final String ORGANIZACION_CONTROLADOR_API_SUPER_CREATE = "super/crear";
	public static final String ORGANIZACION_CONTROLADOR_API_SUPER_LIST = "super/ver-todas";
	public static final String ORGANIZACION_CONTROLADOR_API_ACTUALIZAR = "actualizar";
	public static final String ORGANIZACION_CONTROLADOR_API_SUPER_BORRAR = "super/eliminar";
	public static final String ORGANIZACION_CONTROLADOR_API_VER_MI_ORGANIZACION = "ver-mi-organizacion";

	/**
	 * Cities paths
	 */
	public static final String CIUDAD_CONTROLADOR_API = "api/ciudad";
	public static final String CIUDAD_CONTROLADOR_API_OBTENER_TODAS = "obtener-todos";
	public static final String CIUDAD_CONTROLADOR_API_OBTENER_POR_DEPARTAMENTO = "obtener-por-departamento";

	/**
	 * Deparments paths
	 */
	public static final String DEPARTAMENTO_CONTROLADOR_API = "api/departamento";
	public static final String DEPARTAMENTO_CONTROLADOR_API_OBTENER_TODOS = "obtener-todos";

	/***
	 * Barrios
	 */

	public static final String BARRIO_CONTROLADOR_API = "api/barrio";
	public static final String BARRIO_CONTROLADOR_API_CREAR = "crear";
	public static final String BARRIO_CONTROLADOR_API_OBTENER_POR_CIUDAD = "obtener-por-ciudad";

	/**
	 * Reset Password
	 */
	public static final String CONTROL_PASS_API = "api/password";
	public static final String CONTROL_PASS_API_RESET_PASS = "restablecer-contrasena";
	public static final String CONTROL_PASS_API_CHANGE_PASS = "cambiar-contrasena";
	public static final String CONTROL_PASS_API_CODE_VALIDATE = "validar-codigo";

	/**
	 * Prefijos
	 */
	public static final String PREFIJO_CONTROLADOR_API = "api/prefijo";
	public static final String PREFIJO_CONTROLADOR_API_CREAR = "crear";
	public static final String PREFIJO_CONTROLADOR_API_LISTAR = "listar-todos";
	public static final String PREFIJO_CONTROLADOR_API_ELIMINAR = "eliminar";
	public static final String PREFIJO_CONTROLADOR_API_ACTUALIZAR = "actualizar";
	public static final String PREFIJO_CONTROLADOR_API_PREFIJO_POR_SEDE = "prefijo-sede";
	public static final String PREFIJO_CONTROLADOR_API_PREFIJO_POR_SEDE_CONTADO = "prefijo-sede-contado";
	public static final String PREFIJO_CONTROLADOR_API_PREFIJO_POR_SEDE_NOTA_CREDITO = "prefijo-sede-egreso-nota-credito";
	public static final String PREFIJO_CONTROLADOR_API_PREFIJO_POR_SEDE_NOTA_CREDITO_CLIENTE = "prefijo-sede-egreso-nota-credito-cliente";
	public static final String PREFIJO_CONTROLADOR_API_PREFIJO_POR_SEDE_NOTA_DEBITO = "prefijo-sede-egreso-nota-debito";
	public static final String PREFIJO_CONTROLADOR_API_PREFIJO_POR_SEDE_NOTA_DEVOLUCION = "prefijo-sede-egreso-nota-devolucion";
	public static final String PREFIJO_CONTROLADOR_API_PREFIJO_POR_SEDE_REMISION_VENTA = "prefijo-sede-egreso-remision-venta";
	public static final String PREFIJO_CONTROLADOR_API_PREFIJO_POR_SEDE_NOTA_DEBITO_CLIENTE = "prefijo-sede-egreso-nota-debito-cliente";
	public static final String PREFIJO_CONTROLADOR_API_PREFIJO_RECIBO_CAJA = "prefijo-recibo-caja";
	public static final String PREFIJO_CONTROLADOR_API_PREFIJO_FACTURA_ELECTRONICA = "prefijo-factura-electronica";
	public static final String PREFIJO_CONTROLADOR_API_PREFIJO_INVENTARIO = "prefijo-inventario";
	public static final String PREFIJO_CONTROLADOR_API_PREFIJO_POR_SEDE_DEVOLUCION_CREDITO_CLIENTE = "prefijo-sede-egreso-nota-devolucion-cliente";

	public static final String PREFIJO_EGRESO_CONTROLADOR_API = "api/prefijo-egreso";
	public static final String PREFIJO_EGRESO_CONTROLADOR_API_LISTAR = "listar-todos";
	public static final String PREFIJO_EGRESO_CONTROLADOR_API_CREAR = "crear";
	public static final String PREFIJO_EGRESO_CONTROLADOR_API_ACTUALIZAR = "actualizar";
	public static final String PREFIJO_EGRESO_CONTROLADOR_API_LISTAR_POR_TIPO = "listar-por-tipo";
	public static final String PREFIJO_EGRESO_CONTROLADOR_API_VALIDAR_VALOR_INICIO = "validar-inicio-prefijo";
	public static final String PREFIJO_EGRESO_CONTROLADOR_API_LISTAR1 = "listar-todos1";

	/**
	 * Plazos de créditos
	 */
	public static final String PLAZOS_CREDITO_CONTROLADOR_API = "api/plazo_credito";
	public static final String PLAZOS_CREDITO_CONTROLADOR_API_CREAR = "crear";
	public static final String PLAZOS_CREDITO_CONTROLADOR_API_LISTAR = "listar-todos";
	public static final String PLAZOS_CREDITO_CONTROLADOR_API_ELIMINAR = "eliminar";
	public static final String PLAZOS_CREDITO_CONTROLADOR_API_ACTUALIZAR = "actualizar";

	/***
	 * Sedes por Usuario
	 */
	public static final String USUARIO_SEDE_CONTROL_API = "api/user-sede";
	public static final String USUARIO_SEDE_CONTROL_API_ADD = "add-user-sede";
	public static final String USUARIO_SEDE_CONTROL_API_GET = "get-user-sede";
	// public static final String USUARIO_SEDE_CONTROL_API_UPDATE =
	// "update-user-sede";
	public static final String USUARIO_SEDE_CONTROL_API_REMOVE = "remove-user-sede";
	public static final String USUARIO_SEDE_CONTROL_API_SEDE = "get-sede";

	/***
	 * Cajas
	 */
	public static final String CAJA_CONTROL_API = "api/cajas";
	public static final String CAJA_CONTROL_API_ADD = "add-cajas";
	public static final String CAJA_CONTROL_API_GET_ALL = "get-cajas-all";
	public static final String CAJA_CONTROL_API_GET_ALL_SEDE = "get-cajas-all-sede";
	public static final String CAJA_CONTROL_API_UPDATE = "update-caja";
	public static final String CAJA_CONTROL_API_REMOVE = "remove-caja";
	public static final String CAJA_CONTROL_API_CAJAS_SEDE = "cajas-sede";
	public static final String CAJA_CONTROL_API_CAJAS_SEDE_INFORME = "cajas-sede-informe";

	/***
	 * Periodo contable
	 */
	public static final String PERIODO_CONTABLE_CONTROL_API = "api/periodo-contable";
	public static final String PERIODO_CONTABLE_CONTROL_API_GET = "obtener-periodo";
	public static final String PERIODO_CONTABLE_CONTROL_API_GET_SEDE = "obtener-periodo-sede";

	/***
	 * Clientes
	 */
	public static final String CLIENTES_CONTROL_API = "api/clientes";
	public static final String CLIENTES_CONTROL_API_LISTAR = "listar-todos";
	public static final String CLIENTES_CONTROL_API_BUSCAR = "buscar-cliente";
	public static final String CLIENTES_CONTROL_API_CREAR = "crear-cliente";
	public static final String CLIENTES_CONTROL_API_ACTUALIZAR = "actualizar-cliente";
	public static final String CLIENTES_CONTROL_API_ELIMINAR = "eliminar-cliente";
	public static final String CLIENTES_CONTROL_API_LISTAR_TODOS = "listar-todos/{page}";
	public static final String CLIENTES_CONTROL_API_LISTAR_TODOS_NOMBRE = "listar-todos-nombre";
	public static final String CLIENTES_CONTROL_API_LISTAR_TODOS_CONSULTA = "listar-todos-consulta/{page}";
	public static final String CLIENTES_CONTROL_API_LISTAR_TODO = "listar-todo";
	public static final String CLIENTES_CONTROL_API_LISTAR_CREDITO = "listar-todos-credito";
	public static final String CLIENTES_CONTROL_API_LISTAR_RETENCIONES_CONSULTA = "listar-retenciones-consulta/{page}";
	public static final String CLIENTES_CONTROL_API_OBTENER_CUPO = "obtener-cupo";
	public static final String CLIENTES_CONTROL_API_OBTENER_SALDO = "obtener-saldo";
	public static final String CLIENTES_CONTROL_API_LISTAR_TEXTO = "listar-todos-texto";

	/***
	 * Formas de pagos
	 */
	public static final String FORMAS_PAGO_API = "api/formas-pago";
	public static final String FORMAS_PAGO_API_LISTAR = "listar";
	public static final String FORMAS_PAGO_API_GET = "get";

	/***
	 * Tipos tarjetas
	 */
	public static final String TIPOS_TARJETAS_API = "api/tipos-tarjetas";
	public static final String TIPOS_TARJETAS_API_LISTAR = "listar";

	/***
	 * Recibo de caja
	 */
	public static final String RECIBO_CAJA_API = "api/recibo-caja";
	public static final String RECIBO_CAJA_API_GUARDAR = "guardar";
	public static final String RECIBO_CAJA_API_OBTENER = "obtener-por-cliente";
	public static final String RECIBO_CAJA_API_OBTENER_CAMBIO = "obtener-cambio";
	public static final String RECIBO_CAJA_API_OBTENER_CAMBIO_SEDE = "obtener-cambio-sede";
	public static final String RECIBO_CAJA_API_OBTENER_RECIBO = "obtener-recibo";
	public static final String RECIBO_CAJA_API_ANULAR_RECIBO = "anular-recibo";
	public static final String LISTAR_TODOS_FILTRO = "listar-filtros";
	public static final String RECIBO_CAJA_API_ELIMINAR = "eliminar";
	public static final String RECIBO_CAJA_API_ACTUALIZAR = "actualizar";
	public static final String LISTAR_TODOS_EXPORTAR = "listar-filtros-exportar";

	/***
	 * Cierre Cajas
	 */
	public static final String CIERRE_CAJAS_API = "api/cierre-caja";
	public static final String CIERRE_CAJAS_API_CIERRE_POR_CAJA = "cierre-por-caja";
	public static final String CIERRE_CAJAS_API_REGISTRAR = "registrar-cierre";

	/***
	 * Conceptos Recibo Caja
	 */
	public static final String CONCEPTOS_RECIBO_CAJA = "api/conceptos-recibo-caja";
	public static final String CONCEPTOS_RECIBO_CAJA_LISTAR = "listar";
	public static final String CONCEPTOS_RECIBO_CAJA_CLIENTE = "conceptos-cliente";
	public static final String CONCEPTOS_RECIBO_CAJA_ABONO = "registrar-abono";
	/***
	 * Pagos Recibos de caja
	 */
	public static final String PAGOS_RECIBO_CAJA = "api/pagos-recibo-caja";
	public static final String PAGOS_RECIBO_CAJA_ADICIONAR = "adicionar-pago";
	public static final String PAGOS_RECIBO_CAJA_LISTAR_PAGO_CAJA = "listar-pago-caja";
	public static final String PAGOS_RECIBO_CAJA_DESHACER_PAGO = "deshacer-pago";
	public static final String PAGOS_RECIBO_CAJA_PAGOS_FACTURA = "pagos-cliente";
	public static final String PAGOS_RECIBO_CAJA_INFORME_CAJA = "informe-caja";
	public static final String PAGOS_RECIBO_CAJA_INFORME_SEDE = "informe-sede";

	/**
	 * Productos
	 */
	public static final String PRODUCTOS_CONTROL_API = "api/productos";
	public static final String PRODUCTOS_CONTROL_API_LISTAR_TODOS = "listar-todos";
	public static final String PRODUCTOS_CONTROL_API_CREAR = "crear-producto";
	public static final String PRODUCTOS_CONTROL_API_EDITAR = "editar-producto";
	public static final String PRODUCTOS_CONTROL_API_ELIMINAR = "eliminar-producto";
	public static final String PRODUCTOS_CONTROL_API_LISTAR_BUSQUEDA = "listar-busqueda";
	public static final String PRODUCTOS_CONTROL_API_LISTAR_PROVEEDOR = "listar-por-proveedor";
	public static final String PRODUCTOS_CONTROL_API_LISTAR_CLIENTES_CONSULTA = "listar-clientes-consulta/{page}";
	public static final String PRODUCTOS_CONTROL_API_LISTAR_PRODUCTOS_EXISTENTES_CONSULTA = "listar-productos-existentes-consulta";
	public static final String PRODUCTOS_CONTROL_API_LISTAR = "listar-todos-productos";
	public static final String PRODUCTOS_CONTROL_API_CREAR_LISTADO = "crear";
	public static final String PRODUCTOS_CONTROL_API_EDITAR_PRECIO = "editar";
	public static final String PRODUCTOS_CONTROL_API_LISTAR_CLIENTE = "listar-por-cliente";
	public static final String PRODUCTOS_CONTROL_API_LISTAR_CLIENTE_EXPORT = "listar-por-cliente-export";
	public static final String PRODUCTOS_CONTROL_API_LISTAR_CONSULTA = "listar-consulta";
	public static final String PRODUCTOS_CONTROL_API_LISTAR_CONSULTA_DISPONIBLES = "listar-consulta-disponibles";
	public static final String PRODUCTOS_CONTROL_API_REPLICAR = "replicar-productos";
	public static final String PRODUCTOS_CONTROL_API_LISTAR_TODOS_ID = "listar-productos-id/{page}";
	public static final String PRODUCTOS_CONTROL_API_LISTAR_TODOS_CONSULTA = "listar-todos-consulta/{page}";
	public static final String PRODUCTOS_CONTROL_API_LISTAR_TODOS_CONSULTA_DISPONIBLES = "listar-todos-consulta-disponibles/{page}";
	public static final String PRODUCTOS_CONTROL_API_LISTAR_PRODUCTO_POR_ID = "listar-prod";
	public static final String PRODUCTOS_CONTROL_API_LISTAR_CONSULTA_EXCEL = "listar-consulta-Excel";
	public static final String PRODUCTOS_CONTROL_API_LISTAR_TODOS_ID_RECODIFICAR = "listar-productos-id-recodificar/{page}";
	// public static final String
	// PRODUCTOS_CONTROL_API_LISTAR_TODOS_ID_RECODIFICAR_OBTENER =
	// "listar-productos-id-recodificar/{page}";

	/**
	 * Artículos
	 */
	public static final String ARTICULOS_CONTROL_API = "api/articulos";
	public static final String ARTICULOS_CONTROL_API_LISTAR_TODOS = "listar-todos";
	public static final String ARTICULOS_CONTROL_API_LISTAR_POR_SEDE = "listar-por-sede";
	public static final String ARTICULOS_CONTROL_API_LISTAR_POR_PRODUCTO = "listar-por-producto";
	public static final String ARTICULOS_CONTROL_API_LISTAR_POR_PRODUCTO_Y_SEDE = "listar-por-producto-y-sede";
	public static final String ARTICULOS_CONTROL_API_CREAR = "crear-articulo";
	public static final String ARTICULOS_CONTROL_API_EDITAR = "editar-articulo";
	public static final String ARTICULOS_CONTROL_API_ELIMINAR = "eliminar-articulo";
	public static final String ARTICULOS_CONTROL_API_CONTAR = "contar-articulos";
	public static final String ARTICULOS_CONTROL_API_ULTIMO_PRECIO = "precio-articulo";
	public static final String ARTICULOS_CONTROL_API_CONSECUTIVO = "obtener-codigo-sede";
	public static final String ARTICULOS_CONTROL_API_BUSCAR_POR_CODIGO = "buscar_por_codigo";
	public static final String ARTICULOS_CONTROL_API_BUSCAR_POR_CODIGO_INVENTARIO = "buscar_por_codigo_inventario";
	public static final String ARTICULOS_CONTROL_API_BUSCAR_SIN_REMISION = "sin_remision";
	public static final String ARTICULOS_CONTROL_API_BUSCAR_DISPONIBLE = "disponible";
	public static final String ARTICULOS_CONTROL_API_ARTICULOS_FILTROS = "articulos-filtros";
	public static final String ARTICULOS_CONTROL_API_ARTICULOS_FILTROS_CAMBIAR_ESTADO = "articulos-filtros-cambiar-estado";
	public static final String ARTICULOS_CONTROL_API_ARTICULOS_FILTROS_EXPORTAR = "articulos-filtros-exportar";
	public static final String ARTICULOS_CONTROL_API_CAMBIAR_ESTADO = "cambiar-estado";
	public static final String ARTICULOS_CONTROL_API_LISTAR_PRODUCTOS_EXISTENTES_CONSULTA = "listar-productos-existentes-consulta";
	public static final String ARTICULOS_CONTROL_API_LISTAR_POR_PRODUCTO_PAGE = "listar-por-producto-inventario";
	public static final String ARTICULOS_CONTROL_API_LISTAR_ARTICULO = "listar";
	// public static final String ARTICULOS_CONTROL_API_LISTAR_CONSULTA =
	// "listar-consulta";
	public static final String ARTICULOS_CONTROL_API_LISTAR_TODOS_CONSULTA = "listar-todos-consulta/{page}";
	public static final String ARTICULOS_CONTROL_API_LISTAR_POR_ID = "listar-art";
	public static final String ARTICULOS_CONTROL_API_EDITAR_IDPRODUCTO_ARTICULO = "editar";
	public static final String ARTICULOS_CONTROL_API_LISTAR_TODOS_ARTICULO_CONSULTA = "listar-todos-articulo-consulta/{page}";
	public static final String ARTICULOS_CONTROL_API_LISTAR_TODOS_ARTICULO_PRODUCTO_CONSULTA = "listar-todos-articulo-producto-consulta/{page}";
	public static final String ARTICULOS_CONTROL_API_LISTAR_TODOS_ARTICULOS = "listar-articulos";

	public static final String ARTICULOS_CONTROL_API_LISTAR_SELECTOR = "listar-todos-selector";
	public static final String ARTICULOS_CONTROL_API_LISTAR_FILTRO_TEXTO = "listar-todos-filtro-texto";
	public static final String ARTICULOS_CONTROL_API_LISTAR_ARTICULO_LOCAL = "listar-local";
	public static final String ARTICULOS_CONTROL_API_LISTAR_DISPONIBLES_PAGE = "listar-disponibles";
	public static final String ARTICULOS_CONTROL_API_LISTAR_LOCAL_SECTOR_PAGE = "listar-local-sector";
	public static final String ARTICULOS_CONTROL_API_LISTAR_TODOS_PAGE = "listar-todos-sede-page";
	public static final String ARTICULOS_CONTROL_API_LISTAR_POR_SEDE_CODIGO = "listar-por-sede-codigo";
	public static final String ARTICULOS_CONTROL_API_LISTAR_TODOS_EXISTENTES_PRODUCTO = "listar-todos-existente-producto";
	public static final String ARTICULOS_CONTROL_API_ARTICULOS_TOTALES_FILTROS = "articulos-totales-filtros";
	public static final String ARTICULOS_CONTROL_API_LISTAR_TODOS_ARTICULO_MOVIMIENTO_CONSULTA = "listar-todos-articulo-movimiento-consulta/{page}";
	public static final String ARTICULOS_CONTROL_API_OBTENER_ARTICULO = "obtener-articulo";
	public static final String ARTICULOS_CONTROL_API_OBTENER_PRECIO_COSTO = "obtener-precio-costo";
	public static final String ARTICULOS_CONTROL_API_OBTENER_PRECIO_VENTA = "obtener-precio-venta";
	public static final String ARTICULOS_CONTROL_API_LISTAR_POR_SEDE_CODIGO_LOCAL = "listar-por-sede-codigo-local";
	/**
	 * estado articulos
	 */
	public static final String ESTADO_ARTICULOS_CONTROL_API = "api/estado-articulo";
	public static final String ESTADO_ARTICULOS_CONTROL_API_LISTAR_ESTADOS = "listar-estados";

	/**
	 * Clasificaciones
	 */
	public static final String CLASIFICACION_CONTROL_API = "api/clasificaciones";
	public static final String CLASIFICACION_CONTROL_API_LISTAR_TODOS = "listar-todos";
	public static final String CLASIFICACION_CONTROL_API_CREAR = "crear-clasificacion";
	public static final String CLASIFICACION_CONTROL_API_EDITAR = "editar-clasificacion";
	public static final String CLASIFICACION_CONTROL_API_ELIMINAR = "eliminar-clasificacion";
	public static final String CLASIFICACION_CONTROL_API_LISTAR = "listar-todos-clasificaciones";

	/**
	 * Colores
	 */
	public static final String COLOR_CONTROL_API = "api/colores";
	public static final String COLOR_CONTROL_API_LISTAR_TODOS = "listar-todos";
	public static final String COLOR_CONTROL_API_CREAR = "crear-color";
	public static final String COLOR_CONTROL_API_EDITAR = "editar-color";
	public static final String COLOR_CONTROL_API_ELIMINAR = "eliminar-color";
	public static final String COLOR_CONTROL_API_LISTAR = "listar-todos-colores";
	public static final String COLOR_CONTROL_API_LISTAR_TODOS_TEXTO = "listar-todos-texto";

	/**
	 * Presentaciones
	 */
	public static final String PRESENTACION_CONTROL_API = "api/presentaciones";
	public static final String PRESENTACION_CONTROL_API_LISTAR_TODOS = "listar-todos";
	public static final String PRESENTACION_CONTROL_API_CREAR = "crear-presentacion";
	public static final String PRESENTACION_CONTROL_API_EDITAR = "editar-presentacion";
	public static final String PRESENTACION_CONTROL_API_ELIMINAR = "eliminar-presentacion";
	public static final String PRESENTACION_CONTROL_API_LISTAR = "listar-todos-presentaciones";
	public static final String CLASIFICACION_CONTROL_API_LISTAR_PRESENTACION = "listar";
	public static final String PRESENTACION_CONTROL_API_LISTAR_TODOS_SELECTOR = "listar-todos-selector";
	public static final String PRESENTACION_CONTROL_API_LISTAR_TODOS_TEXTO = "listar-todos-texto";

	/**
	 * Referencias
	 */
	public static final String REFERENCIA_CONTROL_API = "api/referencias";
	public static final String REFERENCIA_CONTROL_API_LISTAR_TODOS = "listar-todos";
	public static final String REFERENCIA_CONTROL_API_CREAR = "crear-referencia";
	public static final String REFERENCIA_CONTROL_API_EDITAR = "editar-referencia";
	public static final String REFERENCIA_CONTROL_API_ELIMINAR = "eliminar-referencia";
	public static final String REFERENCIA_CONTROL_API_LISTAR_TODOS_TIPO = "listar-todos-tipo/{page}";
	public static final String REFERENCIA_CONTROL_API_OBTENER_POR_TIPO = "obtener-por-tipo";
	public static final String REFERENCIA_CONTROL_API_LISTAR_TODOS_TEXTO = "listar-todos-texto";

	/**
	 * Unidades
	 */
	public static final String UNIDAD_CONTROL_API = "api/unidades";
	public static final String UNIDAD_CONTROL_API_LISTAR_TODOS = "listar-todos";
	public static final String UNIDAD_CONTROL_API_CREAR = "crear-unidad";
	public static final String UNIDAD_CONTROL_API_EDITAR = "editar-unidad";
	public static final String UNIDAD_CONTROL_API_ELIMINAR = "eliminar-unidad";

	/**
	 * Tipos
	 */
	public static final String TIPO_CONTROL_API = "api/tipos-producto";
	public static final String TIPO_CONTROL_API_LISTAR_TODOS = "listar-todos";
	public static final String TIPO_CONTROL_API_CREAR = "crear-tipo";
	public static final String TIPO_CONTROL_API_EDITAR = "editar-tipo";
	public static final String TIPO_CONTROL_API_ELIMINAR = "eliminar-tipo";
	public static final String TIPO_CONTROL_API_LISTAR = "listar-todos-tipos";

	public static final String TIPO_CONTROL_API_LISTAR_SELECTOR = "listar-todos-selector";
	public static final String TIPO_CONTROL_API_LISTAR_FILTRO_TEXTO = "listar-todos-filtro-texto";

	/**
	 * Factura
	 */
	public static final String FACTURA_API = "api/factura";
	public static final String FACTURA_API_LISTAR_TODOS = "listar-todos";
	public static final String FACTURA_API_LISTAR_TODOS_SEDE = "listar-todos-sede";
	public static final String FACTURA_API_CREAR = "crear-factura";
	public static final String FACTURA_API_EDITAR = "editar-factura";
	public static final String FACTURA_API_ELIMINAR = "eliminar-factura";
	public static final String FACTURA_API_LISTAR_FACTURAS_CREDITO = "factura-credito";
	public static final String FACTURA_API_LISTAR_FACTURAS_INFORME = "factura-informe/{page}";
	public static final String FACTURA_API_LISTAR_FACTURAS_INFORME_TOTAL = "factura-informe-total";
	public static final String FACTURA_API_LISTAR_FECHA_DIA_SEDE = "listar-fecha-sede";
	public static final String FACTURA_API_LISTAR_CLIENTE_SEDE = "listar-cliente-sede";
	public static final String FACTURA_API_LISTAR_VENTAS_CREDITO = "listar-ventas-credito";
	public static final String FACTURA_API_LISTAR_VENTAS_PAGO = "listar-ventas-pagado";
	public static final String FACTURA_API_BUSCAR_FACTURA_NUMERO = "buscar-factura-numero";
	public static final String FACTURA_API_BUSCAR_FECHA = "buscar-fecha";
	public static final String FACTURA_API_BUSQUEDA_FILTROS = "busqueda-filtros";
	public static final String FACTURA_API_ANULAR_FACTURA = "anular-factura";
	public static final String FACTURA_API_BUSCAR_POR_SEDE_CLIENTE = "buscar-por-sede-cliente";
	public static final String FACTURA_API_LISTAR_ESPECIALES = "listar-todos-clientes-especiales";
	public static final String FACTURA_API_LISTAR_FRECUENTES = "listar-todos-clientes-frecuentes";
	public static final String FACTURA_API_BUSCAR_POR_CLIENTE = "buscar-por-cliente";
	public static final String FACTURA_API_BUSCAR_POR_CLIENTE_PENDIENTE = "buscar-por-cliente-pendiente";
	public static final String FACTURA_API_UPDATE_RETENCION = "actualizar-retencion";
	public static final String FACTURA_API_OBTENER_SALDO = "obtener-saldo";
	public static final String FACTURA_API_ADICIONAR_ARTICULO_REMISION = "adicionar-articulo"; // sirve para adicionar
	// un articulo a la
	// remision para evitar
	// incongruencias cuanto
	// se hace el descuento
	// al traer la remision

	/**
	 * Factura articulo
	 */
	public static final String FACTURA_ARTICULO_API = "api/factura-articulo";
	public static final String FACTURA_ARTICULO_API_LISTAR_TODOS = "listar-todos";
	public static final String FACTURA_ARTICULO_API_LISTAR_TODOS_SEDE = "listar-todos-sede";
	public static final String FACTURA_ARTICULO_API_CREAR = "crear-factura-articulo";
	public static final String FACTURA_ARTICULO_API_EDITAR = "editar-factura-articulo";
	public static final String FACTURA_ARTICULO_API_ELIMINAR = "eliminar-factura-factura";

	/**
	 * Sequencia
	 */
	public static final String SEQUENCIA_API = "api/sequencia";
	public static final String SEQUENCIA_API_SEDE = "obtenersequencia";
	public static final String SEQUENCIA_API_SEDE2 = "obtsequencia";
	public static final String SEQUENCIA_API_SEDE3 = "ultimasecuencia";
	public static final String SEQUENCIA_API_SEDE4 = "proximasecuencia";

	/**
	 * Reporte Diario
	 */
	public static final String INFORME_DIARIO_API = "api/informe";
	public static final String INFORME_DIARIO_API_SEDE = "obtener-informe";
	public static final String INFORME_DIARIO_API_RECAUDO = "obtener-informe-recaudo";
	public static final String INFORME_DIARIO_API_RECAUDO_SEDE = "obtener-informe-recaudo-sede";
	public static final String INFORME_DIARIO_API_RECAUDO_NO_EFECTIVO = "obtener-informe-recaudo-medios-pago";
	public static final String INFORME_DIARIO_API_RECAUDO_NO_EFECTIVO1 = "obtener-informe-recaudo-medios-pago-recaudos";
	public static final String INFORME_DIARIO_API_RECAUDO_NO_EFECTIVO_SEDE = "obtener-informe-recaudo-medios-pago-sede";
	public static final String INFORME_DIARIO_API_RECAUDO_NO_EFECTIVO_SEDE1 = "obtener-informe-recaudo-medios-pago-sede-recaudos";
	public static final String INFORME_DIARIO_API_RECAUDO_RECIBO = "obtener-informe-recaudo-recibo";
	public static final String INFORME_DIARIO_API_RECAUDO_SEDE_RECIBO = "obtener-informe-recaudo-sede-recibo";
	public static final String INFORME_DIARIO_API_RECAUDO_NO_EFECTIVO_RECIBO = "obtener-informe-recaudo-medios-pago-recibo";
	public static final String INFORME_DIARIO_API_RECAUDO_NO_EFECTIVO_SEDE_RECIBO = "obtener-informe-recaudo-medios-pago-sede-recibo";

	/**
	 * Reporte diario caja
	 */
	public static final String INFORME_DIARIO_CAJA_API = "api/informe-caja";
	public static final String INFORME_DIARIO_CAJA_API_GET = "obtener-informe";
	public static final String INFORME_DIARIO_CAJA_API_GET_IVA = "obtener-informe-iva";
	public static final String INFORME_DIARIO_CAJA_API_RECAUDOS = "recaudos-factura-electronica";

	/**
	 * Reporte diario sede
	 */
	public static final String INFORME_DIARIO_SEDE_API = "api/informe-sede";
	public static final String INFORME_DIARIO_SEDE_API_GET = "obtener-informe";
	public static final String INFORME_DIARIO_SEDE_API_GET_IVA = "obtener-informe-iva";
	public static final String INFORME_DIARIO_SEDE_API_RECAUDOS = "recaudos-factura-electronica";
	public static final String INFORME_DIARIO_SEDE_API_INFORME_MEKANO = "crear-informe-diario-mekano";

	/***
	 * Proveedores
	 */
	public static final String PROVEEDORES_API = "api/proveedores";
	public static final String PROVEEDORES_API_CREAR = "crear";
	public static final String PROVEEDORES_API_CREAR_TERCERO = "crearTercero";
	public static final String PROVEEDORES_API_ELIMINAR = "eliminar";
	public static final String PROVEEDORES_API_LISTAR_TODOS = "listar-todos/{page}";
	public static final String PROVEEDORES_API_LISTAR_TODO = "listar-todo";
	public static final String PROVEEDORES_API_LISTAR_TODOS_NOMBRE = "listar-todos-nombre";
	public static final String PROVEEDORES_API_LISTAR_TODOS_CONSULTA = "listar-todos-consulta/{page}";
	public static final String PROVEEDORES_API_ACTUALIZAR = "update";
	public static final String PROVEEDORES_API_LISTAR = "listar";
	public static final String PROVEEDORES_API_TOTAL_FACTURA = "obtener-valor-factura";
	public static final String PROVEEDORES_API_BUSCAR_PALABRA_CLAVE = "buscar-palabra-clave";

	/**
	 * Movimientos Proveedores
	 */
	public static final String PROVEEDORES_API_MOVIMENTOS = "api/movimientos-proveedores";
	public static final String PROVEEDORES_API_MOVIMENTOS_BUSCAR = "buscar";
	public static final String PROVEEDORES_API_MOVIMENTOS_BUSCAR_TODOS = "buscar-todos";
	public static final String PROVEEDORES_API_MOVIMENTOS_BUSCAR_PENDIENTES = "buscar-pendientes";
	public static final String PROVEEDORES_API_MOVIMENTOS_BUSCAR_PENDIENTES_ACTUALIZAR = "buscar-pendientes-actualizar";

	/***
	 * clasificacion legal
	 */
	public static final String CLASIFICACION_LEGAL_API = "api/clasificacion-legal";
	public static final String CLASIFICACION_LEGAL_API_LISTAR_TODOS = "listar";

	/***
	 * producto por proveedor
	 */
	public static final String PRODUCTO_PROVEEDOR_API = "api/producto-proveedor";
	public static final String PRODUCTO_PROVEEDOR_API_CREAR = "crear";
	public static final String PRODUCTO_PROVEEDOR_API_ACTUALIZAR = "actualizar";
	public static final String PRODUCTO_PROVEEDOR_API_LISTAR = "listar-todos";
	public static final String PRODUCTO_PROVEEDOR_API_LISTAR_PROVEEDOR = "listar-todos-proveedor";
	public static final String PRODUCTO_PROVEEDOR_API_LISTAR_PROVEEDOR_NO_ASIGNADO = "listar-no-asignados-proveedor";
	public static final String PRODUCTO_PROVEEDOR_API_ELIMINAR = "eliminar";
	public static final String PRODUCTO_PROVEEDOR_API_CANTIDAD_PRODUCTO = "cantidad-producto";
	public static final String PRODUCTO_PROVEEDOR_API_BUSCAR_CODIGO = "buscar-por-codigo";
	public static final String PRODUCTO_PROVEEDOR_API_BUSCAR_NOMBRE_PROVEEDOR = "buscar-por-nombre-proveedor";
	public static final String PRODUCTO_PROVEEDOR_API_LISTAR_POR_PROVEEDOR = "listar-por-proveedor";
	public static final String PRODUCTO_PROVEEDOR_API_LISTAR_POR_PROVEEDOR_NO_ASIGNADOS = "listar-por-proveedor-no-asignados";
	public static final String PRODUCTO_PROVEEDOR_API_LISTAR_POR_PRODUCTO = "listar-por-producto";
	public static final String PRODUCTO_PROVEEDOR_API_OBTENER_POR_ARTICULO = "obtener-articulo";
	public static final String PRODUCTO_PROVEEDOR_API_LISTAR_PROVEEDOR_PRODUCTO = "listar-todos-producto";

	/***
	 * Condiciones Comerciales
	 */
	public static final String CONDICION_COMERCIAL_PROVEEDOR_API = "api/condicion-comercial-proveedor";
	public static final String CONDICION_COMERCIAL_PROVEEDOR_API_CREAR = "crear";
	public static final String CONDICION_COMERCIAL_PROVEEDOR_API_LISTAR = "listar-todos";
	public static final String CONDICION_COMERCIAL_PROVEEDOR_API_LISTAR_PROVEEDOR = "listar-todos-proveedor";
	public static final String CONDICION_COMERCIAL_PROVEEDOR_API_ELIMINAR = "eliminar";

	/***
	 * Remision de compra
	 */
	public static final String REMISION_COMPRA_API = "api/remision-compra";
	public static final String REMISION_COMPRA_API_CREAR = "crear";
	public static final String REMISION_COMPRA_API_BUSCAR_POR_CODIGO = "buscar_remsion_codigo";
	public static final String REMISION_COMPRA_API_BUSCAR_FILTRO = "buscar_por_filtro";
	public static final String REMISION_COMPRA_API_BUSCAR_MES = "buscar_por_mes";
	public static final String REMISION_COMPRA_API_ACTUALIZAR = "actualizar";
	public static final String REMISION_COMPRA_API_ELIMINAR = "eliminar";
	public static final String REMISION_COMPRA_API_LISTAR_TODO = "listar-todo";
	public static final String REMISION_COMPRA_API_LISTAR_FACTURA = "listar-factura";
	public static final String REMISION_COMPRA_API_LISTAR_POR_FECHA_ACTUAL = "listar-por-fecha-actual";
	public static final String REMISION_COMPRA_API_BUSCAR_ENTRADA = "buscar_por_entrada";

	/***
	 * Articulos Remision compra
	 */
	public static final String ARTICULOS_REMISION_COMPRA_API = "api/articulos-remision-compra";
	public static final String ARTICULOS_REMISION_COMPRA_API_CREAR = "crear";
	public static final String ARTICULOS_REMISION_COMPRA_API_BUSCAR = "buscar_por_remision";
	public static final String ARTICULOS_REMISION_COMPRA_API_BUSCAR_PENDIENTE = "buscar-por-remision-pendiente";
	public static final String ARTICULOS_REMISION_COMPRA_API_UPDATE = "buscar-por-remision-update";
	public static final String ARTICULOS_REMISION_COMPRA_API_LISTADO_PRODUCTO = "listar-por-producto/{page}";

	/***
	 * Articulos Devolucion Compra
	 */
	public static final String ARTICULOS_DEVOLUCION_COMPRA_API = "api/articulos-devolucion-compra";
	public static final String ARTICULOS_DEVOLUCION_COMPRA_API_LISTAR = "listar";
	/***
	 * Impuestos
	 */
	public static final String IMPUESTOS_API = "api/impuesto";
	public static final String IMPUESTOS_API_LISTAR = "listar-todos";
	public static final String IMPUESTOS_API_CREAR = "crear";
	public static final String IMPUESTOS_API_ELIMINAR = "eliminar";
	public static final String IMPUESTOS_API_ACTUALIZAR = "actualizar";
	public static final String IMPUESTOS_API_FACTURA = "factura";
	public static final String IMPUESTOS_API_CONEXION = "conexion";
	public static final String IMPUESTOS_API_ENLACE = "enlace";
	public static final String IMPUESTOS_API_ELIMINARENLACE = "eliminar-enlace";
	public static final String IMPUESTOS_API_LISTAR_RECIBO = "listar-todos-recibocaja";

	/***
	 * Factura retencion
	 */
	public static final String FACTURA_RETENCION_API = "api/factura-retencion-compra";
	public static final String FACTURA_RETENCION_API_CREAR = "crear";
	public static final String FACTURA_RETENCION_API_LISTAR = "listar";

	/***
	 * Factura impuesto
	 */
	public static final String FACTURA_IMPUESTO_API = "api/factura-impuesto-compra";
	public static final String FACTURA_IMPUESTO_API_CREAR = "crear";

	/***
	 * Factura compra proveedor
	 */
	public static final String FACTURA_COMPRA_API = "api/factura-compra-compra";
	public static final String FACTURA_COMPRA_API_CREAR = "crear";
	public static final String FACTURA_COMPRA_API_UPDATE = "actualizar";
	public static final String FACTURA_COMPRA_API_ELIMINAR = "eliminar";
	public static final String FACTURA_COMPRA_API_BUSCAR_POR_SEDE = "buscar-por-sede";
	public static final String FACTURA_COMPRA_API_DISMINUIR_VALOR = "disminuir-valor";
	public static final String FACTURA_COMPRA_API_LISTAR_NUMEROS = "listar-numeros";
	public static final String FACTURA_COMPRA_API_LISTAR_FACTURAS = "listar-facturas";
	public static final String FACTURA_COMPRA_API_LISTAR_FACTURAS_POR_PAGAR = "listar-facturas-por-pagar";
	public static final String FACTURA_COMPRA_API_LISTAR_CONSULTA = "listar-consulta/{page}";
	public static final String FACTURA_COMPRA_API_LISTAR_ESTADOS_CUENTA = "listar-estados_cuenta/{page}";
	public static final String FACTURA_COMPRA_API_TOTAL_ESTADOS_CUENTA = "listar-total_cuenta";
	public static final String FACTURA_COMPRA_API_LISTAR_ESTADOS_CUENTA_PAGAR = "listar-estados_cuenta_pagar/{page}";
	public static final String FACTURA_COMPRA_API_LISTAR_MES = "listar-mes/{page}";
	public static final String FACTURA_COMPRA_API_BUSCAR_POR_SEDE_PROVEEDOR = "buscar-por-sede-proveedor";
	public static final String FACTURA_COMPRA_API_LISTAR_TODOS_ESTADOS_CUENTA = "listar-todos-estados_cuenta/{page}";
	public static final String FACTURA_COMPRA_API_LISTAR_FACTURAS_ANULAR = "listar-facturas-anular";
	public static final String FACTURA_COMPRA_API_ANULAR = "anular";
	public static final String FACTURA_COMPRA_API_LISTAR_PENDIENTES = "listar-pendientes/{page}";
	public static final String FACTURA_COMPRA_API_UPDATE_RETENCION = "actualizar-retencion";
	public static final String FACTURA_COMPRA_API_CREAR_MEKANO = "crear-factura-compra-mekano";
	/***
	 * Nota Credito
	 */
	public static final String NOTA_CREDITO_API = "api/nota-credito";
	public static final String NOTA_CREDITO_API_CREAR = "crear";
	public static final String NOTA_CREDITO_API_OBTENER_NUMERO = "obtener-numero";
	public static final String NOTA_CREDITO_API_ACTUALIZAR = "actualizar";
	public static final String NOTA_CREDITO_API_BUSCAR_FILTRO = "buscar-por-filtro";
	public static final String NOTA_CREDITO_API_BUSCAR = "buscar-nota-credito";
	public static final String NOTA_CREDITO_API_LISTAR_POR_ESTADO = "listar-nota-credito-por-estado";
	public static final String NOTA_CREDITO_API_LISTAR_MES = "listar-nota-credito-por-mes";
	public static final String NOTA_CREDITO_API_ELIMINAR = "eliminar";
	public static final String NOTA_CREDITO_API_LISTAR_ANULACION = "listar-anulacion";
	public static final String NOTA_CREDITO_API_ANULAR = "anular";
	public static final String NOTA_CREDITO_API_LISTAR_PENDIENTE = "listar-nota-credito-pendientes";
	public static final String NOTA_CREDITO_API_LISTAR_PENDIENTE_ACTUALIZAR = "listar-nota-credito-pendientes-actualizar";

	/***
	 * Nota Debito
	 */
	public static final String NOTA_DEBITO_API = "api/nota-debito";
	public static final String NOTA_DEBITO_API_CREAR = "crear";
	public static final String NOTA_DEBITO_API_OBTENER_NUMERO = "obtener-numero";
	public static final String NOTA_DEBITO_API_ACTUALIZAR = "actualizar";
	public static final String NOTA_DEBITO_API_BUSCAR_FILTRO = "buscar-por-filtro";
	public static final String NOTA_DEBITO_API_BUSCAR = "buscar-nota-debito";
	public static final String NOTA_DEBITO_API_ELIMINAR = "eliminar";
	public static final String NOTA_DEBITO_API_LISTAR_POR_ESTADO = "listar-nota-debito-por-estado";
	public static final String NOTA_DEBITO_API_LISTAR_MES = "listar-nota-debito-por-mes";
	public static final String NOTA_DEBITO_API_LISTAR_ANULACION = "listar-anulacion";
	public static final String NOTA_DEBITO_API_ANULAR = "anular";
	/***
	 * Documentos Cuenta
	 */
	public static final String DOCUMENTO_CUENTA_API = "api/documento-cuenta";
	public static final String DOCUMENTO_CUENTA_API_LISTAR_SEDE = "listar-por-sede";

	/***
	 * Conceptos Nota Credito
	 */
	public static final String CONCEPTOS_NOTA_CREDITO_API = "api/conceptos-nota-credito";
	public static final String CONCEPTOS_NOTA_CREDITO_API_CREAR = "crear";
	public static final String CONCEPTOS_NOTA_CREDITO_API_LISTAR = "listar-por-sede";
	public static final String CONCEPTOS_NOTA_CREDITO_API_BUSCAR_FILTROS = "buscar-por-filtros";
	public static final String CONCEPTOS_NOTA_CREDITO_API_BUSCAR = "buscar";

	/***
	 * Conceptos Nota Debito
	 * 
	 */
	public static final String CONCEPTOS_NOTA_DEBITO_API = "api/conceptos-nota-debito";
	public static final String CONCEPTOS_NOTA_DEBITO_API_CREAR = "crear";
	public static final String CONCEPTOS_NOTA_DEBITO_API_BUSCAR = "buscar";
	public static final String CONCEPTOS_NOTA_DEBITO_API_ACTUALIZAR = "actualizar";
	/***
	 * Factura remision
	 * 
	 */
	public static final String FACTURA_REMISION_API = "api/factura-remision";
	public static final String FACTURA_REMISION_API_CANTIDAD = "cantidad-remisiones";
	public static final String FACTURA_REMISION_API_LISTAR_FACTURA = "lista-remisiones";

	/***
	 * Motivos
	 */
	public static final String MOTIVOS_API = "api/motivos";
	public static final String MOTIVOS_API_LISTAR = "listar";

	/***
	 * Factura devolucion
	 */
	public static final String FACTURA_DEVOLUCION_API = "api/factura-devolucion-compra";
	public static final String FACTURA_DEVOLUCION_API_CREAR = "crear";
	public static final String FACTURA_DEVOLUCION_API_NUMERO = "numero";
	public static final String FACTURA_DEVOLUCION_API_BUSCAR_POR_SEDE = "buscar-por-sede";
	public static final String FACTURA_DEVOLUCION_API_LISTAR = "listar";
	public static final String FACTURA_DEVOLUCION_API_ELIMINAR = "eliminar";
	public static final String FACTURA_DEVOLUCION_API_LISTAR_POR_MES = "listar-por-mes";
	public static final String FACTURA_DEVOLUCION_API_BUSCAR_DEVOLUCIONES_ANULAR = "listar-devoluciones-anular";
	public static final String FACTURA_DEVOLUCION_API_ANULAR = "anular";

	/**
	 * MOVIMIENTOS PROVEEDOR - REPORTE
	 */
	public static final String MOVIMIENTOS_CONTROLADOR_API = "api/reporte-moviemtos";
	public static final String MOVIMIENTOS_CONTROLADOR_API_REPORTE_MOVIMIENTO_PROVEEDOR = "reporte-movimiento-proveedor";
	public static final String FACTURA_DEVOLUCION_API_BUSCAR_POR_SEDE_PROVEEDOR = "buscar-por-sede-proveedor";

	/***
	 * Cuentas
	 */
	public static final String CUENTA_API = "api/cuenta";
	public static final String CUENTA_API_CREAR = "crear";
	public static final String CUENTA_API_LISTAR_TODOS = "listar-todos";
	public static final String CUENTA_API_LISTAR_TODOS_CREDITO = "listar-todos-credito";
	public static final String CUENTA_API_LISTAR_TODOS_CREDITO_CLIENTES = "listar-credito-clientes";
	public static final String CUENTA_API_LISTAR_TODOS_DEBITO = "listar-todos-debito";
	public static final String CUENTA_API_LISTAR_TODOS_DEBITO_CLIENTE = "listar-todos-debito-cliente";
	public static final String CUENTA_API_LISTAR_PRECARGADAS_DEVOLUCION = "listar-precargadas-devolucion";
	public static final String CUENTA_API_LISTAR_PRECARGADAS_DEVOLUCION_CLIENTES = "listar-precargadas-devolucion-clientes";

	public static final String CUENTA_API_LISTAR_TODOS_CREDITO_CLIENTE = "listar-todos-credito-cliente";
	public static final String CUENTA_API_LISTAR_TODOS_CREDITO_BANCARIA = "listar-todos-credito-bancaria";
	public static final String CUENTA_API_LISTAR_TODOS_DEBITO_BANCARIA = "listar-todos-debito-bancaria";
	public static final String CUENTA_API_LISTAR_TODOS_COMPROBANTE_SERVICIOS = "listar-todos-comprobante-servicios";
	public static final String CUENTA_API_LISTAR_TODOS_COMPROBANTE_GASTOS = "listar-todos-comprobante-gastos";
	public static final String CUENTA_API_LISTAR_TODOS_EFECTIVO_BANCARIA = "listar-todos-efectivo-bancaria";
	public static final String CUENTA_API_LISTAR_TODOS_CUENTAS_BANCARIAS = "listar-todos-cuenta-bancaria";
	public static final String CUENTA_API_LISTAR_TODOS_CUENTAS_PROVEEDORES = "listar-todos-cuenta-proveedores";

	/***
	 * Informe Facturas vencidas
	 */
	public static final String INFORME_FACTURAS_VENCIDAS_API = "api/informe-facturas-vencidas";
	// public static final String INFORME_FACTURAS_VENCIDAS_API_BUSCAR = "buscar";
	public static final String INFORME_FACTURAS_VENCIDAS_API_LISTAR_VENCIDAS = "listar-vencidas";
	public static final String INFORME_FACTURAS_VENCIDAS_API_LISTAR_VENCIDAS_CONSULTA = "listar-vencidas-consulta";
	public static final String INFORME_FACTURAS_VENCIDAS_API_LISTAR_VENCIDAS_COMPRA = "listar-vencidas-compra";
	public static final String INFORME_FACTURAS_VENCIDAS_API_LISTAR_VENCIDAS_COMPRA_CONSULTA = "listar-vencidas-compra-consulta";
	public static final String INFORME_FACTURAS_VENCIDAS_API_LISTAR_VENCIDAS_COMPRA_EXCEL = "listar-vencidas-compra-excel";

	/***
	 * Cuentas por pagar
	 */
	public static final String PROVEEDORES_API_CUENTAS_PAGAR = "api/cuentas-por-pagar";
	public static final String PROVEEDORES_API_CUENTAS_PAGAR_BUSCAR = "buscar";
	public static final String PROVEEDORES_API_CUENTAS_PAGAR_TOTAL = "total";
	public static final String PROVEEDORES_API_CUENTAS_PAGAR_BUSCAR_ALMACEN = "buscar-almacen";

	/***
	 * Estados Cuenta
	 */
	public static final String PROVEEDORES_API_ESTADOS_CUENTA = "api/estados-cuenta";
	public static final String PROVEEDORES_API_ESTADOS_CUENTA_BUSCAR = "buscar/{page}";
	public static final String PROVEEDORES_API_ESTADOS_CUENTA_TOTAL = "total";

	/**
	 * Comprobantes egreso
	 */
	public static final String COMPROBANTE_EGRESO_CONTROL_API = "api/comprobante-egreso";
	public static final String COMPROBANTE_EGRESO_CONTROL_API_CREAR = "crear-comprobante";
	public static final String COMPROBANTE_EGRESO_CONTROL_API_SIGUIENTE_NUMERO = "siguiente-numero";
	public static final String COMPROBANTE_EGRESO_CONTROL_API_LISTAR = "listar";
	public static final String COMPROBANTE_EGRESO_CONTROL_API_ELIMINAR = "eliminar";
	public static final String COMPROBANTE_EGRESO_CONTROL_API_ACTUALIZAR = "actualizar-comprobante";
	public static final String COMPROBANTE_EGRESO_CONTROL_API_LISTAR_TODO = "listar_todo";
	public static final String COMPROBANTE_EGRESO_CONTROL_API_LISTAR_MES = "listar-mes";
	public static final String COMPROBANTE_EGRESO_CONTROL_API_RETENCION = "retencion";
	public static final String COMPROBANTE_EGRESO_CONTROL_API_MODIFICAR_EDITABLE = "modificar-editable";
	public static final String COMPROBANTE_EGRESO_CONTROL_API_CREAR_MEKANO = "crear-comprobante-mekano";

	/**
	 * Etiquetas
	 */
	public static final String ETIQUETA_CONTROL_API = "api/etiqueta";
	public static final String ETIQUETA_CONTROL_API_SECTOR = "buscar-por-sector";
	public static final String ETIQUETA_CONTROL_API_PRODUCTO = "buscar-por-producto";
	public static final String ETIQUETA_CONTROL_API_REMISION = "buscar-por-remision";
	public static final String ETIQUETA_CONTROL_API_ARTICULO = "buscar-por-articulo";

	/***
	 * Clasificaciones
	 */

	public static final String CLASIFICACION_CONTROLADOR_API = "api/clasificacion";
	public static final String CLASIFICACION_CONTROLADOR_API_LISTAR_TODOS = "listar-todos";
	public static final String CLASIFICACION_CONTROLADOR_API_CREAR = "crear";
	public static final String CLASIFICACION_CONTROLADOR_API_REMOVE = "remove-clasificacion";

	/***
	 * ConceptosReciboEgresos
	 */
	public static final String CONCEPTO_RECIBO_EGRESO_API = "api/concepto-recibo-egreso";
	public static final String CONCEPTO_RECIBO_EGRESO_API_ABONO = "abonos";

	/***
	 * Descuentos
	 */

	public static final String DESCUENTOS_CONTROLADOR_API = "api/descuentos";
	public static final String DESCUENTOS_CONTROLADOR_API_LISTAR = "listar-todos";
	public static final String DESCUENTOS_CONTROLADOR_API_LISTAR_TODOS = "listar-todos/{page}";
	public static final String DESCUENTOS_CONTROLADOR_API_CREAR = "crear-descuento";
	public static final String DESCUENTOS_CONTROLADOR_API_ACTUALIZAR = "actualizar-descuento";
	public static final String DESCUENTOS_CONTROLADOR_API_ELIMINAR = "eliminar-descuento";
	public static final String DESCUENTOS_CONTROLADOR_API_LISTAR_TODOS_CONSULTA = "listar-todos-consulta/{page}";
	public static final String DESCUENTOS_CONTROLADOR_API_LISTAR_TODOS_CONSULTAS = "listar-todos-consultas/{page}";
	public static final String DESCUENTOS_CONTROLADOR_API_LISTAR_TODOS_PRODUCTO = "listar-descuentos-productos/{page}";
	public static final String DESCUENTOS_CONTROLADOR_API_LISTAR_TODOS_CONSULTAS_PRODUCTO = "listar-todos-consultas-producto/{page}";
	public static final String DESCUENTOS_CONTROLADOR_API_OBTENER_POR_CLIENTE_CLASIFICACION = "obtener-por-cliente-clasificacion";

	/***
	 * Asignaciones Pendientes
	 */
	public static final String ASIGNACION_PENDIENTE_API = "api/asignacion-pendiente";
	public static final String ASIGNACION_PENDIENTE_API_LISTAR = "listar";
	public static final String ASIGNACION_PENDIENTE_API_LISTAR_UPDATE = "listar-update";
	public static final String ASIGNACION_PENDIENTE_API_CREAR = "crear";

	/***
	 */
	public static final String PAGOS_DOCUMENTOS_API = "api/pagos-documentos";
	public static final String PAGOS_DOCUMENTOS_API_OBTENER = "obtener";
	public static final String PAGOS_DOCUMENTOS_API_OBTENER_COMPROBANTES = "obtener-comprobantes";
	/***
	 * Descuentos por clientes
	 */
	public static final String DESCUENTOS_CLIENTE_CONTROLADOR_API = "api/descuentos-clientes";
	public static final String DESCUENTOS_CLIENTE_CONTROLADOR_API_LISTAR = "listar-todos";
	public static final String DESCUENTOS_CLIENTE_CONTROLADOR_API_LISTAR_TODOS = "listar-todos/{page}";
	public static final String DESCUENTOS_CLIENTE_CONTROLADOR_API_LISTAR_TODOS_CONSULTA = "listar-todos-consulta/{page}";

	/***
	 * Estado Cuenta clientes
	 */

	public static final String CLIENTES_API_ESTADOS_CUENTA = "api/estado-cuenta-cliente";
	public static final String CLIENTES_API_ESTADOS_CUENTA_BUSCAR = "buscar/{page}";
	public static final String CLIENTES_API_ESTADOS_CUENTA_TOTAL = "total";

	/***
	 * Remision de venta
	 */
	public static final String REMISION_VENTA_API = "api/remision-venta";
	public static final String REMISION_VENTA_API_CREAR = "crear";
	public static final String REMISION_VENTA_API_ACTUALIZAR = "actualizar";
	public static final String REMISION_VENTA_API_LISTAR_TODOS_SEDE = "listar-todos-sede";
	public static final String REMISION_VENTA_API_LISTAR_TODOS_CONSULTA = "listar-todos-consulta";
	public static final String REMISION_VENTA_API_ELIMINAR = "eliminar";
	public static final String REMISION_VENTA_API_OBTENER_NUMERO_MAXIMO = "obtener-numero";
	public static final String REMISION_VENTA_API_OBTENER = "obtener";
	public static final String REMISION_VENTA_API_OBTENER_IMPRESION = "obtener-imprimir";
	public static final String REMISION_VENTA_API_LISTAR_TODOS_SEDE2 = "listar-todos-sede2";
	public static final String REMISION_VENTA_API_LISTAR_TODOS_CONSULTA2 = "listar-todos-consulta2";
	public static final String REMISION_VENTA_API_LISTAR_PENDIENTES = "listar-todos-pendientes";
	public static final String REMISION_VENTA_API_CEDULA_VENDEDOR = "listar-cedula-vendedor";
	public static final String REMISION_VENTA_API_AJUSTAR_REMISIONES_VENTA = "ajustar-remisiones";

	/***
	 * Estado documento
	 */
	public static final String ESTADODOCUMENTO_CONTROL_API = "api/estadodocumento";
	public static final String ESTADODOCUMENTO_CONTROL_API_LISTAR = "listar-todos";
	public static final String ESTADODOCUMENTO_CONTROL_API_LISTAR_TODOS_NOMBRE = "listar-todos-nombre";
	public static final String ESTADODOCUMENTO_CONTROL_API_LISTAR_TRASLADOS = "listar-traslados";

	/***
	 * Productos por cliente
	 */
	public static final String PRODUCTO_POR_CLIENTE_API = "api/propductos-por-cliente";
	public static final String PRODUCTO_POR_CLIENTE_API_LISTAR_TODOS_CONSULTA = "listar-todos-consulta";

	/***
	 * Nota Debito
	 */
	public static final String NOTA_DEBITO_CLIENTE_API = "api/nota-debito-cliente";
	public static final String NOTA_DEBITO_CLIENTE_API_CREAR = "crear";
	public static final String NOTA_DEBITO_CLIENTE_API_OBTENER_NUMERO = "obtener-numero";
	public static final String NOTA_DEBITO_CLIENTE_API_ACTUALIZAR = "actualizar";
	public static final String NOTA_DEBITO_CLIENTE_API_LISTAR_TODOS_CONSULTA = "listar-todos-consulta";
	public static final String NOTA_DEBITO_CLIENTE_API_BUSCAR = "buscar-nota-debito-cliente";
	public static final String NOTA_DEBITO_CLIENTE_API_ELIMINAR = "eliminar";
	public static final String NOTA_DEBITO_CLIENTE_API_LISTAR_POR_ESTADO = "listar-nota-debito-cliente-por-estado";
	public static final String NOTA_DEBITO_CLIENTE_API_LISTAR_MES = "listar-nota-debito-cliente-por-mes";

	/***
	 * Conceptos Nota Debito Cliente
	 * 
	 */
	public static final String CONCEPTOS_NOTA_DEBITO_CLIENTE_API = "api/conceptos-nota-debito-cliente";
	public static final String CONCEPTOS_NOTA_DEBITO_CLIENTE_API_CREAR = "crear";
	public static final String CONCEPTOS_NOTA_DEBITO_CLIENTE_API_BUSCAR = "buscar";
	/***
	 * Nota Credito Cliente
	 */
	public static final String NOTA_CREDITO_CLIENTE_API = "api/nota-credito-cliente";
	public static final String NOTA_CREDITO_CLIENTE_API_CREAR = "crear";
	public static final String NOTA_CREDITO_CLIENTE_API_OBTENER_NUMERO = "obtener-numero";
	public static final String NOTA_CREDITO_CLIENTE_API_ACTUALIZAR = "actualizar";
	public static final String NOTA_CREDITO_CLIENTE_API_LISTAR_TODOS_CONSULTA = "listar-todos-consulta";
	public static final String NOTA_CREDITO_CLIENTE_API_BUSCAR = "buscar-nota-credito";
	public static final String NOTA_CREDITO_CLIENTE_API_LISTAR_POR_ESTADO = "listar-nota-credito-por-estado";
	public static final String NOTA_CREDITO_CLIENTE_API_LISTAR_MES = "listar-nota-credito-por-mes";
	public static final String NOTA_CREDITO_CLIENTE_API_ELIMINAR = "eliminar";

	/***
	 * Factura devolucion
	 */
	public static final String FACTURAS_DEVOLUCION_API = "api/facturas-devolucion-venta";
	public static final String FACTURAS_DEVOLUCION_API_CREAR = "crear";
	public static final String FACTURAS_DEVOLUCION_API_BUSCAR_ID = "buscar-por-id";
	public static final String FACTURAS_DEVOLUCION_API_NUMERO = "numero";
	public static final String FACTURAS_DEVOLUCION_API_BUSCAR_POR_SEDE = "buscar-por-sede";
	public static final String FACTURAS_DEVOLUCION_API_LISTAR = "listar";
	public static final String FACTURAS_DEVOLUCION_API_ELIMINAR = "eliminar";
	public static final String FACTURAS_DEVOLUCION_API_LISTAR_POR_MES = "listar-por-mes";
	public static final String FACTURAS_DEVOLUCION_API_BUSCAR_POR_SEDE_CLIENTE = "buscar-por-sede-cliente";
	public static final String FACTURAS_DEVOLUCION_API_LISTAR_FILTRO = "listar-filtro";
	public static final String FACTURAS_DEVOLUCION_API_ACTUALIZAR = "actualizar";
	public static final String FACTURAS_DEVOLUCION_API_ESTADONOTACREDITO = "consultarNotacreditoAsignada";

	/***
	 * Articulos Devolucion Venta
	 */
	public static final String ARTICULOS_DEVOLUCION_VENTA_API = "api/articulos-devolucion-venta1";
	public static final String ARTICULOS_DEVOLUCION_VENTA_API_LISTAR = "listar";

	/***
	 * Conceptos Nota Credito Cliente
	 */
	public static final String CONCEPTOS_NOTA_CREDITO_CLIENTE_API = "api/conceptos-nota-credito-cliente";
	public static final String CONCEPTOS_NOTA_CREDITO_CLIENTE_API_CREAR = "crear";
	public static final String CONCEPTOS_NOTA_CREDITO_CLIENTE_API_LISTAR = "listar-por-sede";
	public static final String CONCEPTOS_NOTA_CREDITO_CLIENTE_API_BUSCAR_FILTROS = "buscar-por-filtros";
	public static final String CONCEPTOS_NOTA_CREDITO_CLIENTE_API_BUSCAR = "buscar";

	/**
	 * Movimientos Clientes
	 */
	public static final String CLIENTES_API_MOVIMENTOS = "api/movimientos-clientes";
	public static final String CLIENTES_API_MOVIMENTOS_BUSCAR = "buscar";
	public static final String CLIENTES_API_MOVIMENTOS_BUSCAR_TODOS = "buscar-todos";
	public static final String CLIENTES_API_MOVIMENTOS_BUSCAR_PENDIENTES = "buscar-pendientes";
	public static final String MOVIMIENTOS_CLIENTES_API = "api/movimientos-clientes";
	public static final String MOVIMIENTOS_CLIENTES_API_LISTAR_MOVIMIENTOS_CONSULTA = "listar-movimientos-cliente-consulta";

	/**
	 * Cartera Clientes
	 */
	public static final String CARTERA_CLIENTE_API = "api/cartera-cliente";
	public static final String CARTERA_CLIENTE_API_LISTAR_CARTERA_CLIENTE = "listar-cartera-cliente";
	/***
	 * Conceptos cliente
	 */
	public static final String CONCEPTOS_CLIENTE_API = "api/conceptos-por-cliente";
	public static final String CONCEPTOS_CLIENTE_API_LISTA = "listar";
	public static final String CONCEPTOS_CLIENTE_API_LISTA_ACTUALIZAR = "listar-actualizar";

	/***
	 * Asignaciones Pendientes
	 */
	public static final String ASIGNACION_PENDIENTE_RECIBO_API = "api/asignacion-pendiente-recibo";
	public static final String ASIGNACION_PENDIENTE_RECIBO_API_LISTAR = "listar";
	public static final String ASIGNACION_PENDIENTE_RECIBO_API_LISTAR_UPDATE = "listar-update";
	public static final String ASIGNACION_PENDIENTE_RECIBO_API_CREAR = "crear";

	/***
	 * Informe Facturas vencidas cliente
	 */
	public static final String INFORME_FACTURAS_VENCIDAS_CLIENTE_API = "api/informe-facturas-vencidas-cliente";
	// public static final String INFORME_FACTURAS_VENCIDAS_API_BUSCAR = "buscar";
	public static final String INFORME_FACTURAS_VENCIDAS_CLIENTE_API_LISTAR_VENCIDAS_CLIENTE = "listar-vencidas-cliente";
	public static final String INFORME_FACTURAS_VENCIDAS_CLIENTE_API_LISTAR_VENCIDAS_CLIENTE_CONSULTA = "listar-vencidas-cliente-consulta";

	/**
	 * Detalles de pago
	 */
	public static final String DETALLES_PAGO_API = "api/detalles-pago";
	public static final String DETALLES_PAGO_API_LISTAR_DETALLES_PAGO_CONSULTA = "listar-detalles-de-pago";

	/***
	 * Promocion
	 */
	public static final String PROMOCION_API = "api/promocion";
	public static final String PROMOCION_API_CREAR = "crear";
	public static final String PROMOCION_API_OBTENER_PROMOCION_TEXTO = "obtener-promocion-texto";
	public static final String PROMOCION_API_CONSULTA = "obtener-promocion-consulta";
	public static final String PROMOCION_API_ELIMINAR = "eliminar";
	public static final String PROMOCION_API_CONSULTA_EXPORT = "obtener-promocion-consulta-export";
	public static final String PROMOCION_API_OBTENER_PROMOCION_DIA = "obtener-promocion-dia";

	/***
	 * Productos existentes
	 */
	public static final String PRODUCTOS_EXISTENTES_API = "api/productos-existentes";
	public static final String PRODUCTOS_EXISTENTES_API_LISTAR_CONSULTA = "listar-consulta/{page}";
	public static final String PRODUCTOS_EXISTENTES_API_LISTAR_TODOS_PRODUCTO_EXISTENTE = "listar-productos-existentes/{page}";
	public static final String PRODUCTOS_EXISTENTES_API_LISTAR_PRODUCTO_EXISTENTE = "listar-existentes";

	/***
	 * Productos no existentes
	 */
	public static final String PRODUCTOS_NO_EXISTENTES_API = "api/productos-no-existentes";
	public static final String PRODUCTOS_NO_EXISTENTES_API_BUSCAR = "buscar";
	public static final String PRODUCTOS_NO_EXISTENTES_API_BUSCAR_TODOS = "buscar-todos";
	public static final String PRODUCTOS_NO_EXISTENTES_API_LISTAR_CONSULTA = "listar-consulta/{page}";

	/***
	 * Precios
	 */
	public static final String PRECIOS_API = "api/precios";
	public static final String PRECIOS_API_LISTAR_CONSULTA = "listar-consulta";
	public static final String PRECIOS_API_LISTAR_EXPORTAR = "listar-exportar";

	/***
	 * Precio producto
	 */
	public static final String PRECIO_PRODUCTO_CONTROL_API = "api/precio-producto";
	public static final String PRECIO_PRODUCTO_CONTROL_API_LISTAR_PRECIO = "listar";
	public static final String PRECIO_PRODUCTO_CONTROL_API_EDITAR_PRECIO = "editar";
	public static final String PRECIO_PRODUCTO_CONTROL_API_CREAR_PRECIO = "crear";

	/**
	 * Cambio Precios
	 */
	public static final String CAMBIO_PRECIOS_CONTROL_API = "api/cambio-precios";
	public static final String CAMBIO_PRECIOS_CONTROL_API_LISTAR_TODOS_CONSULTA = "listar-todos-consulta/{page}";
	public static final String CAMBIO_PRECIOS_CONTROL_API_GUARDAR_CONSULTA = "guardar-todos-consulta";

	/**
	 * Locales
	 */
	public static final String LOCALES_CONTROL_API = "api/locales";
	public static final String LOCALES_CONTROL_API_LISTAR = "listar";
	public static final String LOCALES_CONTROL_API_LISTAR_LOCAL = "listar-local";
	public static final String LOCALES_CONTROL_API_LISTAR_TODOS = "listar-todos";
	public static final String LOCALES_CONTROL_API_CREAR_LOCAL = "crear";
	public static final String LOCALES_CONTROL_API_EDITAR_LOCAL = "editar";
	public static final String LOCALES_CONTROL_API_ELIMINAR_LOCAL = "eliminar";
	public static final String LOCALES_CONTROL_API_LISTAR_CONSULTA = "listar-todos-consulta/{page}";
	public static final String LOCALES_CONTROL_API_LISTAR_CONSULTA2 = "listar-todos-consulta2/{page}";
	public static final String LOCALES_CONTROL_API_ELIMINAR = "eliminar_lola";
	public static final String LOCALES_CONTROL_API_LISTAR_TODOS_SEDE = "listar-todos-sede";

	/**
	 * Sectores
	 */
	public static final String SECTORES_CONTROL_API = "api/sectores";
	public static final String SECTORES_CONTROL_API_LISTAR_TODOS = "listar-todos";
	public static final String SECTORES_CONTROL_API_CREAR_SECTOR = "crear";
	public static final String SECTORES_CONTROL_API_EDITAR_SECTOR = "editar";
	public static final String SECTORES_CONTROL_API_ELIMINAR_SECTOR = "eliminar";
	public static final String SECTORES_CONTROL_API_LISTAR_POR_ID = "listar-todos-id";
	public static final String SECTORES_CONTROL_API_LISTAR_SECTOR = "listar";
	public static final String SECTORES_CONTROL_API_LISTAR_TODOS_CONSULTAS = "listar-todos-consultas/{page}";

	/***
	 * Productos Cantidad Articulos
	 */
	public static final String PRODUCTOS_CANTIDAD_ARTICULO_API = "api/productos-cantidad-articulos";
	public static final String PRODUCTOS_CANTIDAD_ARTICULO_API_LISTAR_CONSULTA = "listar-consulta/{page}";
	public static final String PRODUCTOS_CANTIDAD_ARTICULO_API_LISTAR_CONSULTA_PAGINADO = "listar-consulta-paginado/{page}";

	/***
	 * Productos Precios Articulos
	 */
	public static final String PRODUCTOS_PRECIOS_ARTICULO_API = "api/productos-precios-articulos";
	public static final String PRODUCTOS_PRECIOS_ARTICULO_API_LISTAR_CONSULTA = "listar-consulta/{page}";
	public static final String PRODUCTOS_PRECIOS_ARTICULO_API_LISTAR_CONSULTA_PAGINADO = "listar-consulta-paginado/{page}";

	/***
	 * Ventas de Productos
	 */
	public static final String VENTA_PRODUCTO_CONTROL_API = "api/venta-producto";
	public static final String VENTA_PRODUCTO_CONTROL_API_LISTAR_CONSULTA = "listar-todos-consulta/{page}";

	/***
	 * Ventas de Productos
	 */
	public static final String MOVIMIENTOS_ARTICULOS_API = "api/movimientos-articulos";
	public static final String MOVIMIENTOS_ARTICULOS_API_LISTAR_MOVIMIENTOS_ARTICULOS_CONSULTA = "listar-movimientos-articulos-consulta";

	/***
	 * Traslados
	 */
	public static final String TRASLADOS_API = "api/traslados";
	public static final String TRASLADOS_API_CREAR = "crear";
	public static final String TRASLADOS_API_LISTAR_CONSULTA = "listar-consulta/{page}";
	public static final String TRASLADOS_API_ACTUALIZAR = "actualizar";
	public static final String TRASLADOS_API_LISTAR_MES = "listar-mes/{page}";
	public static final String TRASLADOS_API_OBTENER_NUMERO = "obtener-numero";

	/***
	 * Salidas mercancia api/salidas-mercancia/listar-consulta
	 */
	public static final String SALIDAS_MERCANCIA_API = "api/salidas-mercancia";
	public static final String SALIDAS_MERCANCIA_API_CREAR = "crear";
	public static final String SALIDAS_MERCANCIA_API_LISTAR_CONSULTA = "listar-consulta/{page}";
	public static final String SALIDAS_MERCANCIA_API_ACTUALIZAR = "actualizar";
	public static final String SALIDAS_MERCANCIA_API_LISTAR_MES = "listar-mes/{page}";
	public static final String SALIDAS_MERCANCIA_API_OBTENER_NUMERO = "obtener-numero";

	/***
	 * Entradas mercancia
	 */
	public static final String ENTRADAS_MERCANCIA_API = "api/entradas-mercancia";
	public static final String ENTRADAS_MERCANCIA_API_CREAR = "crear";
	public static final String ENTRADAS_MERCANCIA_API_LISTAR_CONSULTA = "listar-consulta/{page}";
	public static final String ENTRADAS_MERCANCIA_API_ACTUALIZAR = "actualizar";
	public static final String ENTRADAS_MERCANCIA_API_LISTAR_MES = "listar-mes/{page}";
	public static final String ENTRADAS_MERCANCIA_API_OBTENER_NUMERO = "obtener-numero";

	/***
	 * Inventario
	 */
	public static final String INVENTARIO_API = "api/inventario";
	public static final String INVENTARIO_API_CODIGO_ARTICULO = "obtenerArticulo";
	public static final String INVENTARIO_API_GUARDAR = "guardar";
	public static final String INVENTARIO_API_VALIDAR_EXISTENCIA = "validarExistencia";
	public static final String INVENTARIO_API_OBTENER_INVENTARIO = "obtener-inventariopage";
	public static final String INVENTARIO_API_OBTENER_INVENTARIOW = "obtener-inventario";
	public static final String INVENTARIO_API_ARTICULOS_FINALIZADOS = "articulos-finalizados";
	public static final String INVENTARIO_API_ARTICULOS_INEXISTENTES = "articulos-inexistentes";
	public static final String INVENTARIO_API_OBTENER_INVENTARIOLOCAL = "obtener-inventario-Local";
	public static final String INVENTARIO_API_ELIMINAR = "eliminar";
	public static final String INVENTARIO_API_GUARDAR_REGISTRO = "guardar-registro";
	public static final String INVENTARIO_API_ELIMINAR_REGISTRO = "eliminar-registro";
	public static final String INVENTARIO_API_LIQUIDAR_INVENTARIO = "liquidar-inventario";
	public static final String INVENTARIO_API_ACTUALIZAR_INVENTARIO = "actualizar-inventario";
	public static final String INVENTARIO_API_FILTRAR_INVENTARIO = "filtrar";
	public static final String INVENTARIO_API_FILTRAR_INVENTARIO_WEB = "filtrar-web";
	public static final String INVENTARIO_API_REVIVIR_ARTICULOS = "revivir-articulos";
	public static final String INVENTARIO_API_FINALIZAR_ARTICULOS = "finalizar-articulos";
	public static final String INVENTARIO_API_FILTRAR_LOCAL_INVENTARIO = "filtrar-local";
	public static final String INVENTARIO_API_FILTRAR_LOCAL_INVENTARIO_WEB = "filtrar-local-web";
	public static final String INVENTARIO_API_DEVUELTO = "devuelto";
	public static final String INVENTARIO_API_BODEGA = "bodega";
	public static final String INVENTARIO_API_BUSCAR_POR_ARTICULO = "buscar-por-articulo";
	public static final String INVENTARIO_API_BUSCAR_POR_PRODUCTO = "buscar-por-producto";

}
