package io.teamchallenge.config;

import java.util.List;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Config for mapper.
 * @author Denys Liubchenko
 */
@Configuration
public class MapperConfig {
    /**
     * Provides a new ModelMapper object. Provides configuration for the object.
     * Sets source properties to be strictly matched to destination properties. Sets
     * matching fields to be enabled. Skips when the property value is {@code null}.
     * Sets {@code AccessLevel} to private.
     *
     * @param converters Converters, that are used by {@link ModelMapper} and are
     *                   discovered by Spring.
     * @return the configured instance of {@code ModelMapper}.
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