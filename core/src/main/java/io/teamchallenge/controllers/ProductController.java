package io.teamchallenge.controllers;

import io.teamchallenge.dto.PageableDto;
import io.teamchallenge.dto.ProductResponseDto;
import io.teamchallenge.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public PageableDto<ProductResponseDto> getAll(@RequestParam(required = false) String name, Pageable pageable){
        return productService.getAll(pageable,name);
    }
}
