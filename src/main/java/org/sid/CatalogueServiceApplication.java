package org.sid;

import org.sid.dao.CategoryRepository;
import org.sid.dao.ProductRepository;
import org.sid.entities.Category;
import org.sid.entities.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.stream.Stream;

@SpringBootApplication
@EnableMongoRepositories("org.sid.dao")
@EnableDiscoveryClient
@RefreshScope
@Log4j2
public class CatalogueServiceApplication {
@Value("${me.email}")
public String x;
    public static void main(String[] args) {
        SpringApplication.run(CatalogueServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner start(CategoryRepository categoryRepository, ProductRepository  productRepository){
        return args->{
            categoryRepository.deleteAll();
            Stream.of("C1 Ordinateurs","C2 Imprimantes").forEach(c->{
                categoryRepository.save(new Category(c.split(" ")[0],c.split(" ")[1],new ArrayList<>()));
            });
            // categoryRepository.findAll().forEach(System.out::println);
            categoryRepository.findAll().forEach(log::info);

            productRepository.deleteAll();
            Category c1=categoryRepository.findById("C1").get();
            Stream.of("P1","P2","P3","P4").forEach(name->{
                Product p=productRepository.save(new Product(null,name,Math.random()*1000,c1));
                c1.getProducts().add(p);
                categoryRepository.save(c1);
            });

            Category c2=categoryRepository.findById("C2").get();
            Stream.of("P5","P6").forEach(name->{
                Product p=productRepository.save(new Product(null,name,Math.random()*1000,c2));
                c2.getProducts().add(p);
                categoryRepository.save(c2);
            });

            productRepository.findAll().forEach(log::info );             //System.out.println(p.toString());
            log.warn(x);
        };
    }

}

