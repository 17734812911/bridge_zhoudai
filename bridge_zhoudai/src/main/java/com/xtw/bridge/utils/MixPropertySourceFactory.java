package com.xtw.bridge.utils;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.Properties;

/**
 *  让@PropertySource注解既支持.properties格式也支持.yml格式
 *  不用修改，直接用就行
 */
public class MixPropertySourceFactory extends DefaultPropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource resource) throws IOException {
        String resourceName = name != null ? name : resource.getResource().getFilename();
        if(resourceName != null && resourceName.endsWith(".yml") || resourceName.endsWith(".yaml")){
            // 把.yml转换为properties格式
            Properties propertiesFromYml = loadYml(resource);
            // 使用PropertiesPropertySource进行绑定
            return new PropertiesPropertySource(resourceName, propertiesFromYml);
        } else{
            return super.createPropertySource(name, resource);
        }
    }

    // 把.yml转换为properties格式
    private Properties loadYml(EncodedResource resource){
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource.getResource());
        factory.afterPropertiesSet();
        return factory.getObject();
    }
}
