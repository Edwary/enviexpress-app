package com.enviexpres.logistica.admmodule.utils.properties;

import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {

	private Properties props;

    public PropertyUtil(String propertyFile) throws IOException{
        props = new Properties();
        try
        {
            props.load(ClassLoader.getSystemResourceAsStream(propertyFile));
        } catch (IOException e)
        {
            throw e;
        }
    }

    public String getProperty(String key){
        return props.getProperty(key);
    }
}
