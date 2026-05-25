package com.enviexpres.logistica.admmodule.repository.itf;

import java.util.Map;

import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.enviexpres.logistica.admmodule.model.TevtCiudad;

import reactor.core.publisher.Flux;

@Repository
public interface TevtCiudadRepository extends ReactiveMongoRepository<TevtCiudad, String> {

	@Query("{ $and: ["
			+ "	{ $or: [ "
			+ "		{ 'idCiudad' : { $regex: ?#{[0].get('idCiudad') == null ? '' : [0].get('idCiudad') }, $options: 'i' } }, "
			+ "		{ 'idCiudad' : { $exists: false } }"
			+ "		] }, "
			+ "	{ $or: [ "
			+ "		{ 'nmCiudad' : { $regex: ?#{[0].get('nmCiudad') == null ? '' : [0].get('nmCiudad') }, $options: 'i' } }, "
			+ "		{ 'nmCiudad' : { $exists: false } }"
			+ "		] }, "
			+ "	{ $or: [ "
			+ "		{ 'sbCiudad' : { $regex: ?#{[0].get('sbCiudad') == null ? '' : [0].get('sbCiudad') }, $options: 'i' } }, "
			+ "		{ 'sbCiudad' : { $exists: false } }"
			+ "		] }, "
			+ "	{ $or: [ "
			+ "		{ 'codigoDane' : { $regex: ?#{[0].get('codigoDane') == null ? '' : [0].get('codigoDane') }, $options: 'i' } }, "
			+ "		{ 'codigoDane' : { $exists: false } }"
			+ "		] }, "
			+ "	{ $or: [ "
			+ "		{ 'codigoPostal' : { $regex: ?#{[0].get('codigoPostal') == null ? '' : [0].get('codigoPostal') }, $options: 'i' } }, "
			+ "		{ 'codigoPostal' : { $exists: false } }"
			+ "		] }, "
			+ "	{ $or: [ "
			+ "		{ 'subRegion' : { $regex: ?#{[0].get('subRegion') == null ? '' : [0].get('subRegion') }, $options: 'i' } }, "
			+ "		{ 'subRegion' : { $exists: false } }"
			+ "		] }, "
			+ "	{ $or: [ "
			+ "		{ 'tipo' : { $regex: ?#{[0].get('tipo') == null ? '' : [0].get('tipo') }, $options: 'i' } }, "
			+ "		{ 'tipo' : { $exists: false } }"
			+ "		] }, "
			+ "	{ $or: [ "
			+ "		{ 'idDepartamento' : { $regex: ?#{[0].get('idDepartamento') == null ? '' : [0].get('idDepartamento') }, $options: 'i' } }, "
			+ "		{ 'idDepartamento' : { $exists: false } }"
			+ "		] }, "
			+ "	{ $or: [ "
			+ "		{ 'idEstado' : { $regex: ?#{[0].get('idEstado') == null ? '' : [0].get('idEstado') }, $options: 'i' } }, "
			+ "		{ 'idEstado' : { $exists: false } }"
			+ "		] }, "
			+ " ] }")
	Flux<TevtCiudad> findIfContains(Map<String, String> filter);

	@Aggregation({
	    "{ $match:  { $and: ["
							+ "	{ $or: [ "
							+ "		{ 'idCiudad' : { $regex: ?#{[0].get('idCiudad') == null ? '' : [0].get('idCiudad') }, $options: 'i' } }, "
							+ "		{ 'idCiudad' : { $exists: false } }"
							+ "		] }, "
							+ "	{ $or: [ "
							+ "		{ 'nmCiudad' : { $regex: ?#{[0].get('nmCiudad') == null ? '' : [0].get('nmCiudad') }, $options: 'i' } }, "
							+ "		{ 'nmCiudad' : { $exists: false } }"
							+ "		] }, "
							+ "	{ $or: [ "
							+ "		{ 'sbCiudad' : { $regex: ?#{[0].get('sbCiudad') == null ? '' : [0].get('sbCiudad') }, $options: 'i' } }, "
							+ "		{ 'sbCiudad' : { $exists: false } }"
							+ "		] }, "
							+ "	{ $or: [ "
							+ "		{ 'codigoDane' : { $regex: ?#{[0].get('codigoDane') == null ? '' : [0].get('codigoDane') }, $options: 'i' } }, "
							+ "		{ 'codigoDane' : { $exists: false } }"
							+ "		] }, "
							+ "	{ $or: [ "
							+ "		{ 'codigoPostal' : { $regex: ?#{[0].get('codigoPostal') == null ? '' : [0].get('codigoPostal') }, $options: 'i' } }, "
							+ "		{ 'codigoPostal' : { $exists: false } }"
							+ "		] }, "
							+ "	{ $or: [ "
							+ "		{ 'subRegion' : { $regex: ?#{[0].get('subRegion') == null ? '' : [0].get('subRegion') }, $options: 'i' } }, "
							+ "		{ 'subRegion' : { $exists: false } }"
							+ "		] }, "
							+ "	{ $or: [ "
							+ "		{ 'tipo' : { $regex: ?#{[0].get('tipo') == null ? '' : [0].get('tipo') }, $options: 'i' } }, "
							+ "		{ 'tipo' : { $exists: false } }"
							+ "		] }, "
							+ "	{ $or: [ "
							+ "		{ 'idDepartamento' : { $regex: ?#{[0].get('idDepartamento') == null ? '' : [0].get('idDepartamento') }, $options: 'i' } }, "
							+ "		{ 'idDepartamento' : { $exists: false } }"
							+ "		] }, "
							+ "	{ $or: [ "
							+ "		{ 'idEstado' : { $regex: ?#{[0].get('idEstado') == null ? '' : [0].get('idEstado') }, $options: 'i' } }, "
							+ "		{ 'idEstado' : { $exists: false } }"
							+ "		] }, "
							+ " ] }"
		+ "}",
	    "{ $lookup: { from: 'tevt_ciudad', localField: '_id', foreignField: '_id', as: 'tevt_ciudad' } }",
	    "{ $unwind: { path: '$tevt_ciudad' } }",
	    "{ $lookup: { from: 'tevs_departamento', localField: 'idDepartamento', foreignField: 'codigoDane', as: 'tevs_departamento' } }",
	    "{ $unwind: { path: '$tevs_departamento' } }",
	    "{ $lookup: { from: 'tevp_pais', localField: 'tevs_departamento.idPais', foreignField: '_id', as: 'tevp_pais' } }",
	    "{ $unwind: { path: '$tevp_pais' } }",
	    "{ $lookup: { from: 'tevn_estado', localField: 'idEstado', foreignField: '_id', as: 'tevn_estado' } }",
	    "{ $unwind: { path: '$tevn_estado' } }",
	    "{ $project: { 'tevt_ciudad': 1, 'tevs_departamento': 1, 'tevp_pais': 1, 'tevn_estado': 1 } }"
	})
	Flux<Document> findObjectIfContains(Map<String, String> filter);
	
	@Query("{ homologacion : { $ne: null } }")
	Flux<TevtCiudad> getCiudadesHomologacion();
	
}
