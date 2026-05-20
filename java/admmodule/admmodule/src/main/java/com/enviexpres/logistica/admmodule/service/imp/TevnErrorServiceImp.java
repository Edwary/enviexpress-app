package com.enviexpres.logistica.admmodule.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enviexpres.logistica.admmodule.model.TevnError;
import com.enviexpres.logistica.admmodule.repository.itf.TevnErrorRepository;
import com.enviexpres.logistica.admmodule.service.itf.TevnErrorService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TevnErrorServiceImp implements TevnErrorService {

	@Autowired
	private TevnErrorRepository tvvnErrorRepository;

	@Override
	public Mono<TevnError> create(TevnError tvvnError) {
		return tvvnErrorRepository.save(tvvnError);
	}

	@Override
	public Flux<TevnError> findAll() {
		return tvvnErrorRepository.findAll();
	}

	@Override
	public Mono<TevnError> finById(String id) {
		return tvvnErrorRepository.findById(id);
	}
	
	
}
