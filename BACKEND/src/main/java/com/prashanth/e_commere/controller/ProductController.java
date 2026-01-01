package com.prashanth.e_commere.controller;

import com.prashanth.e_commere.model.Product;
import com.prashanth.e_commere.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService service;



    @GetMapping("/products")
    public ResponseEntity<List<Product>> getALlProducts(){
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{prodId}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer prodId)
    {
        Product product = service.getProductById(prodId);
        if(product != null)
        return new ResponseEntity<>(product, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(
            value = "/product",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<?> addProduct(
            @RequestPart("product") Product product,
            @RequestPart("imageFile") MultipartFile imageFile
    ) {
        System.out.println("Reached Controller");
        try {
            Product prod = service.addProduct(product, imageFile);
            return new ResponseEntity<>(prod, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/product/{prodId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable Integer prodId)
    {
//        service.getImageById(prodId);
        Product prod = service.getProductById(prodId);
        byte[] imageFile = prod.getImageData();
//        return new ResponseEntity<>(imageFile, HttpStatus.OK);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(prod.getImageType()))
                .body(prod.getImageData());

    }
    @PutMapping("/product/{prodId}")
    public ResponseEntity<String> updateProduct(@PathVariable int prodId, @RequestPart("product") Product product,
                                                @RequestPart("imageFile") MultipartFile imageFile) throws IOException {
        Product prod = null;
        try {
            prod = service.updateProduct(prodId,product,imageFile);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed To Update", HttpStatus.BAD_REQUEST);
        }
        if(prod != null) return new ResponseEntity<>("Updated Successfully", HttpStatus.OK);
        else return new ResponseEntity<>("Failed To Update", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/product/{prodId}")
    public ResponseEntity<String> deleteProduct(@PathVariable int prodId)
    {
        Product product = service.getProductById(prodId);
        if(product != null)
        {
            service.deleteProduct(prodId);
            return new ResponseEntity<>("Deleted Successfully!", HttpStatus.OK);
        }
        else return new ResponseEntity<>("Error Found! -_-",HttpStatus.NOT_FOUND);
    }

    @GetMapping("products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword)
    {
        List<Product> products = service.searchProducts(keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
