package org.springinfra.security.validator;

import examples.model.dto.request.UpdateUserRequest;
import jakarta.validation.Payload;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springinfra.annotation.validation.ValidateAs;

import java.lang.annotation.Annotation;

import static org.assertj.core.api.Assertions.assertThat;

class ValidateAsValidatorTest {

    private ValidatorFactory validatorFactory;
    private Validator validator;
    private ValidateAsValidator subject;

    @BeforeEach
    void setUp() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = this.validatorFactory.getValidator();
        this.subject = new ValidateAsValidator(this.validator);
        this.subject.initialize(buildValidateAs(UpdateUserRequest.class));
    }

    @AfterEach
    void cleanUp() {
        this.validatorFactory.close();
    }

    @Test
    void whenPropertyBagRequestIsNull_thenReturnsTrue() {
        boolean result = this.subject.isValid(null, null);

        assertThat(result).isTrue();
    }

    private ValidateAs buildValidateAs(Class<?> delegateClassName) {
        return new ValidateAs() {
            @Override
            public String message() {
                return "";
            }

            @SuppressWarnings("unchecked")
            @Override
            public Class<? extends Payload>[] payload() {
                return new Class[]{};
            }

            @Override
            public Class<?> value() {
                return delegateClassName;
            }

            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return ValidateAs.class;
            }
        };
    }
}
