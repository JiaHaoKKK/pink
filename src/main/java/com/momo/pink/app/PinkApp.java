package com.momo.pink.app;

import com.fasterxml.classmate.TypeResolver;
import com.momo.pink.owner.OwnerConfiguration;
import com.momo.pink.todo.TodoConfiguration;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

@SpringBootApplication
@EnableSwagger2
@Import({
    BeanValidatorPluginsConfiguration.class,
    OwnerConfiguration.class,
    TodoConfiguration.class
})
@MapperScan(annotationClass = Mapper.class, value = "com.momo.pink")
public class PinkApp {

    @Autowired
    private TypeResolver typeResolver;

    @Bean
    public Docket pinkApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.regex("/api/.+"))
            .build()
            .pathMapping("/")
            .directModelSubstitute(LocalDate.class,
                String.class)
            .genericModelSubstitutes(ResponseEntity.class)
            .alternateTypeRules(
                newRule(typeResolver.resolve(DeferredResult.class,
                    typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
                    typeResolver.resolve(WildcardType.class)))
            .useDefaultResponseMessages(false)
            .globalResponseMessage(RequestMethod.GET,
                newArrayList(new ResponseMessageBuilder()
                    .code(500)
                    .message("500 message")
                    .responseModel(new ModelRef("Error"))
                    .build()))
            .securitySchemes(newArrayList(apiKey()))
            .securityContexts(newArrayList(securityContext()))
            .enableUrlTemplating(true)
//            .globalOperationParameters(
//                newArrayList(new ParameterBuilder()
//                    .name("someGlobalParameter")
//                    .description("Description of someGlobalParameter")
//                    .modelRef(new ModelRef("string"))
//                    .parameterType("query")
//                    .required(true)
//                    .build()))
            .tags(new Tag("User", "All apis relating to users"),
                new Tag("Task", "All apis relating to tasks"))
//            .additionalModels(typeResolver.resolve(AdditionalModel.class))
            ;
    }

    private ApiKey apiKey() {
        return new ApiKey("mykey", "api_key", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.regex("/anyPath.*"))
            .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
            = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(
            new SecurityReference("mykey", authorizationScopes));
    }

    @Bean
    SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
            .clientId("test-app-client-id")
            .clientSecret("test-app-client-secret")
            .realm("pink")
            .appName("pink")
            .scopeSeparator(",")
            .additionalQueryStringParams(null)
            .useBasicAuthenticationWithAccessCodeGrant(false)
            .build();
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
            .deepLinking(true)
            .displayOperationId(false)
            .defaultModelsExpandDepth(1)
            .defaultModelExpandDepth(1)
            .defaultModelRendering(ModelRendering.EXAMPLE)
            .displayRequestDuration(false)
            .docExpansion(DocExpansion.NONE)
            .filter(false)
            .maxDisplayedTags(null)
            .operationsSorter(OperationsSorter.ALPHA)
            .showExtensions(false)
            .tagsSorter(TagsSorter.ALPHA)
            .validatorUrl(null)
            .build();
    }

    public static void main(String[] args) {
        StandardEnvironment env = new StandardEnvironment();
        MutablePropertySources propertySources = env.getPropertySources();
        Map<String, Object> properties = new HashMap<>();
        properties.put("encrypt.key", "${encrypt.rootKey}oUzxewPh"); //naN8cE/CoUzxewPh
        propertySources.addLast(new MapPropertySource("encrypt", properties));
        new SpringApplicationBuilder(PinkApp.class)
            .properties("spring.jackson.serialization.write_dates_as_timestamps", "false")
            .environment(env)
            .build()
            .run(args);
    }
}
