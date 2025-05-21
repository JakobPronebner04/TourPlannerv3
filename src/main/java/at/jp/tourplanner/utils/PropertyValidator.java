package at.jp.tourplanner.utils;

import java.lang.reflect.Field;

import at.jp.tourplanner.exception.ValidationException;
import jakarta.validation.*;
import java.util.Set;
import java.util.stream.Collectors;

public class PropertyValidator {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    public static <T> void validateOrThrow(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);

        if (!violations.isEmpty()) {
            String errorMessages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("\n"));
            throw new ValidationException("\n" + errorMessages);
        }
    }
}
