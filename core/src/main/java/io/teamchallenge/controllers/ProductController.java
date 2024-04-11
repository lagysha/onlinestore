package io.teamchallenge.controllers;

import io.teamchallenge.annatations.AllowedSortFields;
import io.teamchallenge.dto.PageableDto;
import io.teamchallenge.dto.ProductResponseDto;
import io.teamchallenge.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public PageableDto<ProductResponseDto> getAll(@RequestParam(required = false) String name,
                                                  @AllowedSortFields(values = {"name","quantity","price","createdAt"})
                                                  @PageableDefault(sort = "createdAt", direction = DESC)
                                                  Pageable pageable){
        return productService.getAll(pageable,name);
    }
}
