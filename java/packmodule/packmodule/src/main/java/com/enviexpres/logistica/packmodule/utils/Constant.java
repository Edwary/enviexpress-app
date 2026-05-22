package com.enviexpres.logistica.packmodule.utils;

public class Constant {

	/** Modulos */
	public static final String MODULO_TODOS = "TODOS";
	public static final String MODULO_ADM = "ADMINISTRACION";
	public static final String MODULO_CLIENTES = "CLIENTES";
	public static final String MODULO_PAQUETES = "PAQUETES";
	public static final String MODULO_USUARIOS = "USUARIOS";
	
	/** Estados Genericos */
	public static final String IND_ESTADO_ACTIVO = "0001";
	public static final String IND_ESTADO_INACTIVO = "0002";
	public static final String IND_ESTADO_EDITAR = "0003";
	public static final String IND_ESTADO_ANULADO = "0005";
	public static final String IND_ESTADO_ELIMINADO = "0007";
	
	public static final String SB_ESTADO_ACTIVO = "A";
	public static final String SB_ESTADO_INACTIVO = "I";
	public static final String SB_ESTADO_EDITAR = "E";
	public static final String SB_ESTADO_ANULADO = "X";
	public static final String SB_ESTADO_ELIMINADO = "EL";
	
	/** Estados de Paquete */
	public static final String IND_ESTADO_REGISTRADO = "0008";
	public static final String IND_ESTADO_RECOGIDO = "0009";
	public static final String IND_ESTADO_EN_CENTRO_DISTRIBUCION = "0010";
	public static final String IND_ESTADO_EN_REPARTO = "0011";
	public static final String IND_ESTADO_ENTREGADO = "0012";
	public static final String IND_ESTADO_NOVEDAD = "0013";
	public static final String IND_ESTADO_DEVUELTO = "0014";
	public static final String IND_ESTADO_CANCELADO = "0015";
	public static final String IND_ESTADO_NUEVO = "0016";
		
	/** Estados Acción */
	public static final String IND_ESTADO_PENDIENTE = "0017";
	public static final String IND_ESTADO_PROCESADA = "0018";
	public static final String IND_ESTADO_FALLIDA = "0019";
	
	/** Tipo Acción */
	public static final String ACCION_LIQUIDACION = "Liquidación";
	public static final String ACCION_ALERTA_OPERATIVA = "Alerta operativa";
	public static final String ACCION_PENDIENTE_REVISION = "Pendiente de revisión";
	/** VALORES VISIBLES */
	public static final String IND_VISIBLE_S = "S";
	public static final String IND_VISIBLE_N = "N";
	
	/** TIPO ZONA */
	public static final String TIPO_AREA_NO_MUNICIPALIZADA = "ÁREA NO MUNICIPALIZADA";
	public static final String TIPO_MUNICIPIO = "MUNICIPIO";
	public static final String TIPO_ISLA = "ISLA";
	public static final String TIPO_VEREDA = "VEREDA";
	public static final String TIPO_CORREGIMIENTO = "CORREGIMIENTO";
	public static final String TIPO_CENTRO_POBLADO = "CENTRO POBLADO";
	
	/** Valores Númericos */
	public static final String VALOR_0 = "0";
	public static final String VALOR_1 = "1";
	public static final String VALOR_2 = "2";
	public static final String VALOR_3 = "3";
	public static final String VALOR_4 = "4";
	public static final String VALOR_5 = "5";
	public static final String VALOR_6 = "6";
	public static final String VALOR_7 = "7";
	public static final String VALOR_8 = "9";
	public static final String VALOR_9 = "9";
	
	/** Valores Letras Español */
	public static final String VALOR_A = "A";
	public static final String VALOR_B = "B";
	public static final String VALOR_C = "C";
	public static final String VALOR_D = "D";
	public static final String VALOR_E = "E";
	public static final String VALOR_F = "F";
	public static final String VALOR_G = "G";
	public static final String VALOR_H = "H";
	public static final String VALOR_I = "I";
	public static final String VALOR_J = "J";
	public static final String VALOR_K = "K";
	public static final String VALOR_L = "L";
	public static final String VALOR_M = "M";
	public static final String VALOR_N = "N";
	public static final String VALOR_O = "O";
	public static final String VALOR_P = "P";
	public static final String VALOR_Q = "Q";
	public static final String VALOR_R = "R";
	public static final String VALOR_S = "S";
	public static final String VALOR_T = "T";
	public static final String VALOR_U = "U";
	public static final String VALOR_V = "V";
	public static final String VALOR_W = "W";
	public static final String VALOR_X = "X";
	public static final String VALOR_Y = "Y";
	public static final String VALOR_Z = "Z";
	public static final String VALOR_Ñ = "Ñ";
	
	/** Otros Valores Estandares */
	public static final String VALOR_NA = "NA";
	public static final String VALOR_NO = "NO";
	public static final String VALOR_SI = "SI";
	
	/** API's Externas */
	public static final String API_MUNICIPIOS_GOV_CO = "https://www.datos.gov.co/resource/xdk5-pm3f.json";
	public static final String API_INSTITUCIONES_SUP_GOV_CO = "https://www.datos.gov.co/resource/muyy-6yw9.json";
	public static final String API_INSTITUCIONES_BA_MED_TEC_GOV_CO = "https://www.datos.gov.co/resource/cfw5-qzt5.json";
	public static final String API_EDUCACION_SUPERIOR_GOV_CO = "https://www.datos.gov.co/resource/upr9-nkiz.json";
	public static final String API_ENTIDADES_SFC_GOV_CO = "https://www.datos.gov.co/resource/sr9n-792w.json";
		
	/** Tipo Variable */
	public static final String VARIABLE_TIPO_CONFIGURACION = "Configuración";
}
