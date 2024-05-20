package io.teamchallenge.controller;

import io.teamchallenge.Utils.Utils;
import io.teamchallenge.dto.PageableDto;
import io.teamchallenge.handler.CustomExceptionHandler;
import io.teamchallenge.service.ProductService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    private static final String productLink = "/api/v1/products";
    private MockMvc mockMvc;
    private final ErrorAttributes errorAttributes = new DefaultErrorAttributes();

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(productController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .setControllerAdvice(new CustomExceptionHandler(errorAttributes))
            .build();
    }

    @Test
    void getAllTest(){
        var pageable = PageRequest.of(1,1, Sort.by("price"));
        var name = "phone";
        var response = new PageableDto<>(
            List.of(Utils.getShortProductResponseDto()), 1, 1, 1);

        when(productService.getAll(pageable,name))
            .thenReturn(response);


    }
}
