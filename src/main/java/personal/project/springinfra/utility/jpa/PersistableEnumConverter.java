package personal.project.springinfra.utility.jpa;

import personal.project.springinfra.assets.PersistableEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.lang.reflect.ParameterizedType;
import java.util.stream.Stream;

@Converter(autoApply = true)
public abstract class PersistableEnumConverter<T extends Enum<T> & PersistableEnum> implements AttributeConverter<T, Integer> {

    private final Class<T> classOfEnum;

    public PersistableEnumConverter() {
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
                .orElseThrow(() -> new IllegalArgumentException(String.format("Value [%d] is not exist in %s", code.intValue(), classOfEnum.getSimpleName())));
    }
}