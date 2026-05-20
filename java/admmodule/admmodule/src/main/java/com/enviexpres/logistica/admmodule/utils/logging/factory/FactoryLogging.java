package com.enviexpres.logistica.admmodule.utils.logging.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.enviexpres.logistica.admmodule.utils.properties.PropertyUtil;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class FactoryLogging {

	public static MongoClient getNewMongoClient() throws IOException {
        MongoClient mongoClient=null;
        String servers=null;
        String credentials=null;
        try {
            PropertyUtil factoryProp=new PropertyUtil("factory.properties");
            servers=factoryProp.getProperty("servers");
            credentials=factoryProp.getProperty("credentials");
        } catch (IOException ex) {
            throw ex;
        }
        String [] serverList=servers.split(",");
        String [] credentialsList=credentials.split("&");
        List<ServerAddress> seeds=new ArrayList<ServerAddress>();
        for (String server : serverList) {
            seeds.add(new ServerAddress(server));
        }
        List<MongoCredential> credentialsM=new ArrayList<MongoCredential>();
        for (String credential : credentialsList) {
            String [] splitCredential=credential.split(",");
            credentialsM.add(MongoCredential.createCredential(splitCredential[0], splitCredential[1], splitCredential[2].toCharArray()));
        }
        mongoClient = new MongoClient(seeds, credentialsM);
        return mongoClient;
    }
}
