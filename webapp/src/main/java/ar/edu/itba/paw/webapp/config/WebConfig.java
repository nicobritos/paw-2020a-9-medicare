package ar.edu.itba.paw.webapp.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

@EnableWebMvc
@ComponentScan({
        "ar.edu.itba.paw.webapp.controller",
        "ar.edu.itba.paw.services",
        "ar.edu.itba.paw.persistence",
        "ar.edu.itba.paw.webapp.transformer",
        "ar.edu.itba.paw.webapp.events",
        "ar.edu.itba.paw.webapp.handlers"
})
@Configuration
@EnableTransactionManagement
@EnableAsync
public class WebConfig {
    protected static final String DB_URL = "jdbc:postgresql://10.16.1.110:5432/paw-2020a-9?useUnicode=true&amp;characterEncoding=utf8";
    protected static final String DB_USER = "paw-2020a-9";
    protected static final String DB_PASSWORD = "N4wC7cmxe";

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/message");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
        messageSource.setCacheSeconds(5);
        return messageSource;
    }

    @Bean
    public ViewResolver viewResolver() {
        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setContentType("text/html; charset=UTF-8");

        return viewResolver;
    }

    @Bean
    public DataSource dataSource() {
        final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(org.postgresql.Driver.class);
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USER);
        dataSource.setPassword(DB_PASSWORD);

        return dataSource;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);

        return mapper;
    }

    @Bean
    public PlatformTransactionManager transactionManager(final DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

    //TODO:set max size upload maybe?
    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        //final long maxSize = 100000;
        CommonsMultipartResolver cmr = new CommonsMultipartResolver();
        //cmr.setMaxUploadSize(maxSize);
        return cmr;
    }

    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver exceptionResolver = new MappingExceptionResolver();
        Properties properties = new Properties();
        properties.setProperty(Exception.class.getName(), "error/500");

        exceptionResolver.setExceptionMappings(properties);
        exceptionResolver.setDefaultStatusCode(500);
        exceptionResolver.setDefaultErrorView("error/500");
        exceptionResolver.setExceptionAttribute("ex");
        return exceptionResolver;
    }

    private static class MappingExceptionResolver extends SimpleMappingExceptionResolver {
        private static final Logger LOGGER = LoggerFactory.getLogger(MappingExceptionResolver.class);

        @Override
        protected void logException(Exception ex, HttpServletRequest request) {
            LOGGER.error("Request: {} raised: {}", request.getRequestURL(), Arrays.toString(ex.getStackTrace()));
        }
    }
}
