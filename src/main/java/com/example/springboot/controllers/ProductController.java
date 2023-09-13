package com.example.springboot.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import com.example.springboot.dto.ProductRecordDTO;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repository.ProductRepository;

import jakarta.validation.Valid;

@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository; 


    @PostMapping("/create")
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDTO productRecordDTO){
        var productModel = new ProductModel();
        
        BeanUtils.copyProperties(productRecordDTO, productModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }


    @GetMapping("/list")
    public ResponseEntity<List<ProductModel>> getAllProducts(){
        List<ProductModel> productList = productRepository.findAll();

        if(!productList.isEmpty()){
            for(ProductModel product: productList){
                UUID id = product.getIdProduct();
                product.add(linkTo(methodOn(ProductController.class).getOneProducts(id)).withSelfRel());
            }

            
        }

        
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Object> getOneProducts(@PathVariable(value = "id") UUID id){

        Optional<ProductModel> product0 = productRepository.findById(id);

        if(product0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PRODUCT NOT FOUND");
        }

        product0.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());

        return ResponseEntity.status(HttpStatus.OK).body(product0.get());

        
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value="id") UUID id, @RequestBody @Valid ProductRecordDTO productRecordDTO){

        Optional <ProductModel> product0 = productRepository.findById(id);

        if(product0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PRODUCT NOT FOUND");
        }

        var productModel = product0.get();
        BeanUtils.copyProperties(productRecordDTO, productModel);
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));

    }


    @DeleteMapping("/delete/{id}")

        public ResponseEntity<Object> deleteProduct(@PathVariable(value="id") UUID id){
            Optional<ProductModel> product0 = productRepository.findById(id);
            if(product0.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PRODUCT NOT FOUND");
            }

            productRepository.delete(product0.get());
            return ResponseEntity.status(HttpStatus.OK).body("PRODUCT DELETED SUCESSFULLY");

        }

    
    
    
}


