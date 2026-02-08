package org.springinfra.utility.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springinfra.assets.PersistableEnum;

import java.lang.reflect.ParameterizedType;
import java.util.stream.Stream;

@Converter(autoApply = true)
public abstract class PersistableEnumConverter<T extends Enum<T> & PersistableEnum<T>> implements AttributeConverter<T, Integer> {

    private final Class<T> classOfEnum;

    protected PersistableEnumConverter() {
        this.classOfEnum = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public Integer convertToDatabaseColumn(T attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public T convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }

        return Stream.of(classOfEnum.getEnumConstants())
                .filter(e -> e.getCode().intValue() == code.intValue())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Value [%d] is not exist in %s", code, classOfEnum.getSimpleName())));
    }
}