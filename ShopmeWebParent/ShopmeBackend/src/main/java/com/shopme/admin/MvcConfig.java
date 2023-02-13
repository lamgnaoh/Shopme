package com.shopme.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // user photos
//        String dirName = "src/main/resources/user-photos";
//        Path userPhotosDir = Paths.get(dirName);
//        String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
//        registry.addResourceHandler("/" + dirName + "/**")
//                .addResourceLocations("file:/" + userPhotosPath + "/");
        exposeDirectory("src/main/resources/user-photos", registry);

//        category photos
        exposeDirectory("../category-images", registry);

//        brand photos
        exposeDirectory("../brand-logos", registry);

//        product photos
        exposeDirectory("../product-images", registry);
//        site logos
        exposeDirectory("../site-logo", registry);

    }

    private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
        Path path = Paths.get(pathPattern);
        String absolutePath = path.toFile().getAbsolutePath();

        String logicalPath = pathPattern.replace("../", "") + "/**";

        registry.addResourceHandler(logicalPath)
                .addResourceLocations("file:/" + absolutePath + "/");
    }
}
