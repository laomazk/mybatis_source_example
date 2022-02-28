package com.magic.mb.config;

import com.magic.mb.plugins.ExamplePlugin;
import com.magic.mb.plugins.PageHelperPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author laoma
 * @create 2022-02-28 0:14
 */
@Configuration
public class MyBatisConfig {
    @Bean
    ExamplePlugin examplePlugin() {
        ExamplePlugin examplePlugin = new ExamplePlugin();
        Properties properties = new Properties();
        properties.put("some", 100);
        examplePlugin.setProperties(properties);
        return examplePlugin;
    }

    @Bean
    PageHelperPlugin pageHelperPlugin(){
        return new PageHelperPlugin();
    }
}
