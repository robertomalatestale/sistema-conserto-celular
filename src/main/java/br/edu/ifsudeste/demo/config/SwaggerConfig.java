package br.edu.ifsudeste.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
// import io.swagger.v3.oas.models.info.Contact;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                                .title("Sistema de Conserto de Celular")
                                .description("API de Conserto de Celulares")
                                .version("1.0")

                        // Exemplo de contato
                        /*
                        .contact(new Contact()
                                .name("Lucas, Marcos e Roberto")
                                .url("https://github.com/robertomalatestale/sistema-conserto-celular"))
                        */
                );
    }
}