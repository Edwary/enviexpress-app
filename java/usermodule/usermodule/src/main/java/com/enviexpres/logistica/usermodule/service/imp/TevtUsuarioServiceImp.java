package com.enviexpres.logistica.usermodule.service.imp;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enviexpres.logistica.usermodule.model.TevnRol;
import com.enviexpres.logistica.usermodule.model.TevtUsuario;
import com.enviexpres.logistica.usermodule.model.dto.TevnError;
import com.enviexpres.logistica.usermodule.repository.dto.itf.TevnErrorRepository;
import com.enviexpres.logistica.usermodule.repository.itf.TevnRolRepository;
import com.enviexpres.logistica.usermodule.repository.itf.TevtUsuarioRepository;
import com.enviexpres.logistica.usermodule.service.itf.TevtUsuarioService;
import com.enviexpres.logistica.usermodule.utils.Constant;
import com.enviexpres.logistica.usermodule.utils.UtilConverter;
import com.enviexpres.logistica.usermodule.utils.UtilsGeneral;
import com.enviexpres.logistica.usermodule.utils.exception.ValidationException;

import io.micrometer.common.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevtUsuarioServiceImp implements TevtUsuarioService {

	@Autowired
	private TevtUsuarioRepository tevtUsuarioRepository;
	
	@Autowired
	private TevnRolRepository tevnRolRepository;
	
	@Autowired
	private TevnErrorRepository tevnErrorRepository;

	@Override
	public Mono<TevtUsuario> create(Map<String, String> entity) {
	    String nus = entity.get("nus");
	    String idUsuario = entity.get("idUsuario");
	    String idRol = entity.get("idRol");

	    Mono<TevtUsuario> usuarioMono = StringUtils.isEmpty(nus)
	        ? tevtUsuarioRepository.findTopByOrderByNusDesc()
	            .map(last -> {
	                TevtUsuario nuevo = new TevtUsuario();
	                nuevo.setNus(UtilsGeneral.devolverConsecutivo12Digitos(last == null ? "0" : last.getNus()));
	                return nuevo;
	            })
	            .defaultIfEmpty(new TevtUsuario(UtilsGeneral.devolverConsecutivo12Digitos("0")))
	        : tevtUsuarioRepository.findByNus(idUsuario)
	            .switchIfEmpty(Mono.error(new ValidationException(HttpStatus.NOT_FOUND, "Usuario no encontrado")));

	    Mono<TevnRol> rolMono = tevnRolRepository.findByIdRol(idRol)
	        .switchIfEmpty(Mono.error(new ValidationException(HttpStatus.NOT_FOUND, "Rol no encontrado")));

	    return Mono.zip(usuarioMono, rolMono)
	        .flatMap(tuple -> {
	            TevtUsuario tevtUsuario = tuple.getT1(); 
	            TevnRol tevnRol = tuple.getT2();         

	            try {
	                tevtUsuario.setNmUsuario(entity.get("nmUsuario"));
	                tevtUsuario.setNombre(entity.get("nombre"));
	                tevtUsuario.setPassword(UtilConverter.toSHA256(entity.get("password")));
	                tevtUsuario.setEmail(entity.get("email"));
	                tevtUsuario.setFechaCreacion(UtilConverter.currentDate());
	                tevtUsuario.setIdRol(tevnRol.getIdRol());
	                tevtUsuario.setIdEstado(entity.get("idEstado"));

	                return tevtUsuarioRepository.save(tevtUsuario);
	                
	            } catch (NoSuchAlgorithmException e) {
	                TevnError tevnError = UtilConverter.createError(e, Constant.MODULO_USUARIOS);
	                return tevnErrorRepository.save(tevnError)
	                    .then(Mono.error(new ValidationException(HttpStatus.BAD_REQUEST, "Sin información para mostrar")));
	            }
	        });
	}

	@Override
	public Mono<TevtUsuario> findById(String nus) {
		return tevtUsuarioRepository.findByNus(nus)
				.switchIfEmpty(Mono.error(new ValidationException(HttpStatus.NOT_FOUND, "Usuario no encontrado")));
	}

	@Override
	public Flux<TevtUsuario> findAll() {
		return tevtUsuarioRepository.findAll();
	}

	@Override
	public Mono<TevtUsuario> toggle(Map<String, String> entity) {
		return tevtUsuarioRepository.findByNus(entity.get("nus"))
				.switchIfEmpty(Mono.error(new ValidationException(HttpStatus.NOT_FOUND, "Usuario no encontrado")))
				.flatMap(tevtUsuario -> { 
					tevtUsuario.setIdEstado(entity.get("idEstado"));
					return tevtUsuarioRepository.save(tevtUsuario);
				});
	}

	@Override
	public Mono<Void> remove(String nus) {
		return tevtUsuarioRepository.findByNus(nus)
				.switchIfEmpty(Mono.error(new ValidationException(HttpStatus.NOT_FOUND, "Usuario no encontrado")))
				.flatMap(tevtUsuario -> tevtUsuarioRepository.delete(tevtUsuario));
	}

	@Override
	public Mono<Map<String, Object>> login(Map<String, String> login) {
	    String usuario = login.get("nmUsuario") != null ? login.get("nmUsuario") : login.get("usuario");
	    String email = login.get("email") != null ? login.get("email") : usuario; 
	    String passwordPlain = login.get("password") != null ? login.get("password") : login.get("contrasena");

	    if (usuario == null || passwordPlain == null) {
	        return Mono.error(new ValidationException(HttpStatus.BAD_REQUEST, "Usuario/Email y contraseña son requeridos"));
	    }

	    try {
	    	String passwordHashed = UtilConverter.toSHA256(passwordPlain);

		    return tevtUsuarioRepository.findByNmUsuarioOrEmail(usuario, email)
		        .switchIfEmpty(Mono.error(new ValidationException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos")))
		        .flatMap(user -> {
		            String userPassword = user.getPassword();
	
		            if (!passwordHashed.equals(userPassword)) {
		                return Mono.error(new ValidationException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos"));
		            }
		            
		            user.setFechaUltimoIngreso(UtilConverter.currentDate());
		            tevtUsuarioRepository.save(user);
		            
		            Map<String, Object> userMap = UtilConverter.classToMap(user);
		            return Mono.just(userMap);
		        });
	    } catch (NoSuchAlgorithmException e) {
            return Mono.error(new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al procesar los datos de sesión"));
        }
	}
	
}
