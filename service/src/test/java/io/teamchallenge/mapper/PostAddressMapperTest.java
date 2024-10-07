package io.teamchallenge.mapper;

import io.teamchallenge.dto.PostAddressDto;
import io.teamchallenge.entity.PostAddress;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.teamchallenge.util.Utils.getPostAddressDto;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PostAddressMapperTest {
    @InjectMocks
    private PostAddressMapper postAddressMapper;

    @Test
    void testConvert() {
        // Arrange
        PostAddressDto source = getPostAddressDto();
        PostAddress expected = PostAddress.builder()
            .city(source.getCity())
            .department(source.getDepartment())
            .build();
        // Act
        PostAddress result = postAddressMapper.convert(source);

        // Assert
        assertEquals(expected, result);
    }
}
