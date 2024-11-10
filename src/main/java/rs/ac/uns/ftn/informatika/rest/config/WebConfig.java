package rs.ac.uns.ftn.informatika.rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Read the upload directory path from application.properties
    @Value("${upload.directory}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map URLs starting with /uploads/images/ to the actual directory where images are stored
        registry.addResourceHandler("/uploads/images/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}
