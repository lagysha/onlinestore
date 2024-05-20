package io.teamchallenge.config;

import java.util.List;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    /**
     * Configures and provides a ModelMapper bean for object mapping.
     */
    @Bean
    public ModelMapper modelMapper(List<Converter<?, ?>> converters) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper
            .getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setFieldMatchingEnabled(true)
            .setSkipNullEnabled(true)
            .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        converters.forEach(modelMapper::addConverter);

        return modelMapper;
    }
}
