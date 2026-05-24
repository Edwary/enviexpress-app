package com.enviexpres.logistica.packmodule.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;


public class UtilsGeneral {

	public static Date currentDate() {
		LocalDate fechaHoy = LocalDate.now();
		Date fechaDate = Date.from(fechaHoy.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return fechaDate;
	}
	
	public static String getMessage(String file, String core, String language) {
		ResourceBundle properties = ResourceBundle.getBundle(file + "_" + "es");
		return properties.getString(core);
	}
	
	public static String devolverConsecutivo4Digitos(String lastId) {
		String newId = "";
		if(lastId.isEmpty() || lastId.equals("000")) {
			newId = String.format("%04d", 1);
			return newId;
		} else {
			newId = String.format("%04d", Long.valueOf(lastId)+1);
			return newId;
		}
	}
	
	public static String devolverConsecutivo7Digitos(String lastId) {
		if(lastId.isEmpty()) {
			return String.format("%07d", 1);
		}else {
			return String.format("%07d", Long.valueOf(lastId)+1);
		}
	}
	
	public static String devolverConsecutivo12Digitos(String lastId) {
		if(lastId.isEmpty()) {
			return String.format("%012d", 1);
		}else {
			return String.format("%012d", Long.valueOf(lastId)+1);
		}
	}
	
}
