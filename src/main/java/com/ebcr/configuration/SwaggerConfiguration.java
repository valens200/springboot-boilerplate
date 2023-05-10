package com.ebcr.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@EnableSwagger2
@Configuration
public class SwaggerConfiguration  extends WebMvcConfigurationSupport {
    ServletContext servletContext;
    public SwaggerConfiguration(ServletContext context){
        this.servletContext = context;
    }

    @Value("http://localhost:8000")
    private String host;

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry) {
                resourceHandlerRegistry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/webjars/");
                resourceHandlerRegistry.addResourceHandler("/webjars/**").addResourceLocations("classpath/:META-INF/webjars/");
            }
        };

    }
    private List<SecurityReference> getDefaultAuth(){
       final AuthorizationScope authorizationScope = new AuthorizationScope("global", "access everything");
       final AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
       return Collections.singletonList(new SecurityReference("Bearer", authorizationScopes));
    }
    @Bean
    public Docket Api(){
        return new Docket(DocumentationType.SWAGGER_2).directModelSubstitute(LocalDate.class, Date.class).host(this.host)
                .pathProvider(new RelativePathProvider(servletContext){
                    @Override
                    public String getApplicationBasePath() {
                        return "";
                    }
                }).select().apis(RequestHandlerSelectors.basePackage("com.ebcr.controller")).paths(PathSelectors.any()).build().apiInfo(apiInfo()).securitySchemes(Arrays.asList(apiKey())).securitySchemes(Arrays.asList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()));
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("EBCR portal backend_service").description("APIs Documentation")
                .termsOfServiceUrl("https://ebcrmanagementportal.com")
                .version("1.0").build();
    }
    private ApiKey apiKey() {
        return new ApiKey("Bearer", "Authorization", "header");
    }
    private SecurityContext securityContext(){
        return springfox.documentation.spi.service.contexts.SecurityContext.builder().securityReferences(getDefaultAuth()).forPaths(PathSelectors.regex("/.*")).build();
    }
}
