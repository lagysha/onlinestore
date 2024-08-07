package io.teamchallenge.mapper;

import io.teamchallenge.dto.PostAddressDto;
import io.teamchallenge.entity.PostAddress;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PostAddressDtoMapperTest {
    @InjectMocks
    private PostAddressDtoMapper postAddressDtoMapper;

    @Test
    void testConvert() {
        // Arrange
        PostAddress source = new PostAddress();
        source.setCity("New York");
        source.setDepartment("IT");
        PostAddressDto expected = PostAddressDto.builder()
            .city(source.getCity())
            .department(source.getDepartment())
            .build();
        // Act
        PostAddressDto result = postAddressDtoMapper.convert(source);

        // Assert
        assertEquals(expected, result);
    }
}
