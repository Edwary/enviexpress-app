package com.enviexpres.logistica.packmodule.utils;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;

import com.enviexpres.logistica.packmodule.model.dto.TevnError;
import com.enviexpres.logistica.packmodule.utils.exception.ValidationException;

import io.micrometer.common.util.StringUtils;

public class UtilConverter {
	
		//@Autowired
		//private TvvnErrorRepository tvvnErrorRepository;

		public static Date toDate(String fecha) {
			if(fecha.isEmpty()) {
				fecha = "31-12-9999";
			}else if(fecha.equals(".")) {
				fecha = "01-01-9999";
			}
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			long f = 0;
			try {
				f = df.parse(fecha).getTime();
			} catch(ParseException e) {
				e.printStackTrace();
				return new Date();
			}
			Date date = new Date(f);
			return date;
		}
		
		public static Date currentDate() {
			LocalDate fechaHoy = LocalDate.now();
			Date fechaDate = Date.from(fechaHoy.atStartOfDay(ZoneId.systemDefault()).toInstant());
			return fechaDate;
		}
		
		public static Date currentDateComplete() {
			Date fechaDate = new Date();
			return fechaDate;
		}
		
		public static String dateToString(Date date) {
			if(Objects.isNull(date)) {
				return "";
			}
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			String sdate = "";
			sdate = df.format(date);
			return sdate;
		}
		
		public static String getHora(Date date) {
			if(Objects.isNull(date)) {
				return "";
			}
			Format format = new SimpleDateFormat("HH:mm:ss");
			return format.format(date);
		}
		
		public static String toMD5(String password) throws NoSuchAlgorithmException {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] passwordBytes = password.getBytes();
				byte[] digest = md.digest(passwordBytes);
				StringBuilder hexString = new StringBuilder();
				for(byte b : digest) {
					String hex = Integer.toHexString(0xff & b);
					if(hex.length() == 1) {
						hexString.append('0');
					}
					hexString.append(hex);
				}
				return hexString.toString();
			}catch (NoSuchAlgorithmException ne) {
				throw ne;
			}
		}
		
		public static String toSHA256(String password) throws NoSuchAlgorithmException {
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				byte[] passwordBytes = password.getBytes();
				byte[] digest = md.digest(passwordBytes);
				StringBuilder hexString = new StringBuilder();
				for(byte b : digest) {
					String hex = Integer.toHexString(0xff & b);
					if(hex.length() == 1) {
						hexString.append('0');
					}
					hexString.append(hex);
				}
				return hexString.toString();
			}catch (NoSuchAlgorithmException ne) {
				throw ne;
			}
		}
		
		@SuppressWarnings("deprecation")
		public static <T> T documentToClass(Class<T> clazz, Document document) throws IllegalAccessException, InstantiationException {
			T instance = clazz.newInstance();
			
			for(Field field : clazz.getDeclaredFields()) {
				String fieldName = field.getName();
				
				if(document.containsKey(fieldName)) {
					Object value = document.get(fieldName);
					field.setAccessible(true);
					if(value instanceof String | value instanceof Date | value instanceof Boolean) {
						field.set(instance, value);
					} else if (value instanceof Map<?,?>) {
						field.set(instance, value);
					} else if (value instanceof List<?>) {
						field.set(instance, value);
					}
				} else if (document.containsKey("_id")) {
					String idClazz = converToMongoIdString(document.get("_id"));
					field.setAccessible(true);
					field.set(instance, idClazz);
				}
			}
			return instance;
		}

		@SuppressWarnings("unchecked")
		public static Document classToDocument(Object object) {
			try {
				if(object instanceof Map<?,?>) {      	
					Map<String, Object> map = (Map<String, Object>) object;
		        	return new Document(map);
		        }else if(object instanceof List<?>) {
		        	List<Object> list = (List<Object>) object;
		        	return new Document("list", list);
		        }else {
		        	Class<?> clazz = object.getClass();
		            Document document = new Document();
		        	for (Field field : clazz.getDeclaredFields()) {
		        		field.setAccessible(true);
		                String fieldName = field.getName();
		                Object value = field.get(object);

		                if (value != null) {
		                    if (value instanceof String || value instanceof Number || value instanceof Boolean) {
		                        document.append(fieldName, value);
		                    } else if (value instanceof List<?>) {
		                    	Map<String, Object> map = (Map<String, Object>) value;
		                        document.append(fieldName, map);
		                    } else if (value instanceof Map<?, ?>) {
		                    	List<Object> list = (List<Object>) object;
		                        document.append(fieldName, list);
		                    } else {
		                        document.append(fieldName, classToDocument(value));
		                    }
		                }
		            }

		            return document;
		        }
			} catch (IllegalAccessException e) {
				//TvvnError tvvnError = UtilConverter.createError(e, ViConstant.MODULO_ADM);
				//tvvnErrorRepository.save(tvvnError);
				throw new ValidationException(HttpStatus.BAD_REQUEST, "general.atom.error.InformacionUsuario");
			}
		}
		
		public static <T> Map<String, Object> classToMap(T object) {
			Map<String, Object> map = new HashMap<>();
			Class<?>  clazz = object.getClass();
			
			for(Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);
				try {
					Object value = field.get(object);
					map.put(field.getName(), value);
				} catch(IllegalAccessException e) {
					throw new IllegalArgumentException("Ha ocurrido un error convirtiendo el objeto a un map.");
				}
			}
			return map;
		}
		
		public static <T> Map<String, String> classToMapString(T object) {
			Map<String, String> map = new HashMap<>();
			Class<?>  clazz = object.getClass();
			
			for(Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);
				try {
					Object value = field.get(object);
					map.put(field.getName(), String.valueOf(value));
				} catch(IllegalAccessException e) {
					throw new IllegalArgumentException("Ha ocurrido un error convirtiendo el objeto a un map.");
				}
			}
			return map;
		}
		
		@SuppressWarnings("deprecation")
		public static <T> T mapToClass(Map<String, ?> map, Class<T> clazz)  throws IllegalAccessException, InstantiationException {
			T object = clazz.newInstance();
			
			for(Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);
				Object value = map.get(field.getName());
				if(!Objects.isNull(value)) {
					if(field.getType().equals(Date.class)) {
						field.set(object, toDate(String.valueOf(value)));
					} else if(field.getType().equals(String.class)) {
						if(!StringUtils.isEmpty(String.valueOf(value))) { field.set(object, String.valueOf(value)); }
					} else {
						field.set(object, null);
					}
				} else {
					field.set(object, "");
				}
			}
			
			return object;
		}
		
		public static Map<String, Object> ObjectToMap(Object object) {
			Map<String, Object> map = new HashMap<>();
			Field[] fields = object.getClass().getDeclaredFields();
			
			for(Field field : fields) {
				field.setAccessible(true);
				try {
					map.put(field.getName(), field.get(object));
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException("Ha ocurrido un error convirtiendo el objeto a un map.");
				} catch (IllegalAccessException e) {
					throw new IllegalArgumentException("Ha ocurrido un error convirtiendo el objeto a un map.");
				}
			}
			
			return map;
		}
		
		public static String converToMongoIdString(Object idObject) {
			if(idObject instanceof String) {
				return (String) idObject;
			} else if(idObject instanceof ObjectId) {
				return ((ObjectId) idObject).toHexString();
			} else if (idObject instanceof Map) {
				Map<?,?> idMap = (Map<?,?>) idObject;
				if(idMap.containsKey("timestamp") && idMap.containsKey("date")) {
					long timestamp = (long) idMap.get("timestamp");
					return new ObjectId(new Date(timestamp), 0).toHexString();
				}
			}
			throw new IllegalArgumentException("El ID no es de un tipo válido.");
		}
		
		public static String convertObjectIdToNumber(String objectIdHex) {
			if (objectIdHex == null) {
	            throw new IllegalArgumentException("El ID debe ser un String hexadecimal");
	        }
	        BigInteger numberObjectId = new BigInteger(objectIdHex, 16);
	        return String.valueOf(numberObjectId);
		}
		
		public static String generateUuid(Map<String, String> parameters)  throws NoSuchAlgorithmException {
			try {
				StringBuilder concatenate = new StringBuilder();
				for(String key : parameters.keySet()) {
					concatenate.append(parameters.get(key));
				}
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				byte[] hashBytes = digest.digest(concatenate.toString().getBytes());
				
				StringBuilder hexString = new StringBuilder();
				for(byte hashByte : hashBytes) {
					String hex = Integer.toHexString(0xff & hashByte);
					if(hex.length() == 1) {
						hexString.append('0');
					}
					hexString.append(hex);
				}
				return hexString.toString();
			} catch (NoSuchAlgorithmException ne) {
				throw ne;
			}
		}

		public static TevnError createError(Exception e, String modulo) {
			TevnError tevnError = new TevnError();
			Date fechaError = currentDate();
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
			String sdate = "";
			sdate = df.format(fechaError);
			tevnError.setConsecutivo(null);
			tevnError.setFechaError(fechaError);
			tevnError.setHoraError(sdate);
			tevnError.setNmModulo(modulo);
			tevnError.setDsError(e.getMessage());
			return tevnError;
		}
		
		public static <T> Map<String, Object> apiResponse(HttpStatus code, T body){
			Map<String, Object> apiResponse = new HashMap<>();
			apiResponse.put("statusCodeValue", code.value());
			apiResponse.put("statusCode", code.name());
			apiResponse.put("headers", "");
			apiResponse.put("body", body);
			return apiResponse;
		}
		
		public void saveError(TevnError tevnError) {
			
		}
		
		public static <T> TreeSet<Map<String, Object>> getTree(Class<T> clazz, List<T> objectList, String comparator, String idField, String fieldSup) {
			TreeSet<Map<String, Object>> tree = new TreeSet<>(new IntComparator(comparator));
			
			if(Objects.isNull(objectList)) {
				return null;
			}
			
			List<String> fields = Arrays.asList(clazz.getDeclaredFields()).stream().map(field -> field.getName()).collect(Collectors.toList());
			if(!fields.contains(idField) && !fields.contains(fieldSup)) {
				return null;
			}
			
			List<T> padresList = objectList.stream()
				.filter(object -> {
					try {
						Field idFieldObject = object.getClass().getDeclaredField(idField);
						Field fieldSupObject = object.getClass().getDeclaredField(fieldSup);
						idFieldObject.setAccessible(true);
						fieldSupObject.setAccessible(true);
						Object idFieldValue = idFieldObject.get(object);
						Object fieldSupValue = fieldSupObject.get(object);
						return idFieldValue.equals(fieldSupValue);
					} catch(NoSuchFieldException | IllegalAccessException e) {
						return false;
					}
				}).collect(Collectors.toList());
			
			for(T padre : padresList) {
				
				try {
					Map<String, Object> padreMap = classToMap(padre);
					Field idFieldObject = padre.getClass().getDeclaredField(idField);
					idFieldObject.setAccessible(true);
					Object idFieldValue = idFieldObject.get(padre);
					List<T> hijos = objectList.stream().filter(object -> {
							try {
								Field hijoIdFieldObject = object.getClass().getDeclaredField(idField);
								Field fieldSupObject = object.getClass().getDeclaredField(fieldSup);
								hijoIdFieldObject.setAccessible(true);
								fieldSupObject.setAccessible(true);
								Object hijoIdFieldValue = hijoIdFieldObject.get(object);
								Object fieldSupValue = fieldSupObject.get(object);
								return !idFieldValue.equals(hijoIdFieldValue) && idFieldValue.equals(fieldSupValue);
							} catch(NoSuchFieldException | IllegalAccessException e) {
								return false;
							}
					}).collect(Collectors.toList());
					if(hijos.size() > 0) {
						padreMap.put("children", buscarHijos(padreMap.get(idField).toString(), hijos, objectList, comparator, idField, fieldSup));
					} else {
						padreMap.put("children", "");
					}
					tree.add(padreMap);
				} catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
					throw new IllegalArgumentException("Ha ocurrido un error en la generación del árbol.");
				}
			}
			return tree;
		}
		
		private static <T> TreeSet<Map<String, Object>> buscarHijos(String idPadre, List<T> hijos, List<T> objectList, String comparator, String idField, String fieldSup){
			TreeSet<Map<String, Object>> hijoTree = new TreeSet<>(new IntComparator(comparator));
			
			for(T hijo : hijos) {
				try {
					Map<String, Object> hijoMap = classToMap(hijo);
					Field idFieldObject = hijo.getClass().getDeclaredField(idField);
					Field fieldSupObject = hijo.getClass().getDeclaredField(fieldSup);
					idFieldObject.setAccessible(true);
					fieldSupObject.setAccessible(true);
					Object idFieldValue = idFieldObject.get(hijo);
					Object fieldSupValue = fieldSupObject.get(hijo);
					if(idFieldValue.equals(fieldSupValue)) continue;
					List<T> hijosSubList = objectList.stream().filter(object -> {
							try {
								Field hijoIdFieldObject = object.getClass().getDeclaredField(idField);
								Field hijoFieldSupObject = object.getClass().getDeclaredField(fieldSup);
								hijoIdFieldObject.setAccessible(true);
								hijoFieldSupObject.setAccessible(true);
								Object hijoIdFieldValue = hijoIdFieldObject.get(object);
								Object hijoFieldSupValue = hijoFieldSupObject.get(object);
								return !idFieldValue.equals(hijoIdFieldValue) && idFieldValue.equals(hijoFieldSupValue) && !idFieldValue.equals(hijoFieldSupValue);
							} catch(NoSuchFieldException | IllegalAccessException e) {
								return false;
							}
					}).collect(Collectors.toList());
					if(hijosSubList.size() > 0) {
						hijoMap.put("children", buscarHijos(hijoMap.get(idField).toString(), hijos, objectList, comparator, idField, fieldSup));
					} else {
						hijoMap.put("children", "");
					}
					hijoTree.add(hijoMap);
				}  catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
					throw new IllegalArgumentException("Ha ocurrido un error en la generación del árbol.");
				}
			}
			
			return hijoTree;
		}
		
		public static Map<String, Object> documentToMap(Document document) {
			Map<String, Object> map = new HashMap<>();
			for(String key : document.keySet()) {
				map.put(key, document.get(key));
			}
			return map;
		}
}
