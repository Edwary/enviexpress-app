package com.enviexpres.logistica.admmodule.configuration;

import org.bson.Document;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;

public class ApiConsumer {
	private final WebClient webClient;
	
	public ApiConsumer() {
		this.webClient = WebClient.create();
	}
	
	public Flux<Document> fetchDataFromApi(String apiUrl){
		return webClient.get()
				.uri(apiUrl)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToFlux(Document.class);
	}
}
