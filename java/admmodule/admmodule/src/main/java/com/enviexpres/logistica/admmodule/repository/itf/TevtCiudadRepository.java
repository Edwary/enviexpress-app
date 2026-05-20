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
	    "{ $lookup: { from: 'tvvt_ciudad', localField: '_id', foreignField: '_id', as: 'tvvt_ciudad' } }",
	    "{ $unwind: { path: '$tvvt_ciudad' } }",
	    "{ $lookup: { from: 'tvvs_departamento', localField: 'idDepartamento', foreignField: 'codigoDane', as: 'tvvs_departamento' } }",
	    "{ $unwind: { path: '$tvvs_departamento' } }",
	    "{ $lookup: { from: 'tvvp_pais', localField: 'tvvs_departamento.idPais', foreignField: '_id', as: 'tvvp_pais' } }",
	    "{ $unwind: { path: '$tvvp_pais' } }",
	    "{ $lookup: { from: 'tvvn_estado', localField: 'idEstado', foreignField: '_id', as: 'tvvn_estado' } }",
	    "{ $unwind: { path: '$tvvn_estado' } }",
	    "{ $project: { 'tvvt_ciudad': 1, 'tvvs_departamento': 2, 'tvvp_pais': 3, 'tvvn_estado': 4 } }"
	})
	Flux<Document> findObjectIfContains(Map<String, String> filter);
	
	@Query("{ homologacion : { $ne: null } }")
	Flux<TevtCiudad> getCiudadesHomologacion();
	
}
