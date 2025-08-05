package utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.TypeSafetyException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class JsonService {

    private final ObjectMapper objectMapper;

    public JsonService() {
        this.objectMapper = configureObjectMapper();
    }

    private ObjectMapper configureObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Type Safety
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);

        return mapper;
    }

    public <T> String serializeRequest(T request) throws TypeSafetyException {
        if (request == null) {
            throw new TypeSafetyException("Request object cannot be null");
        }

        try {
            return objectMapper.writeValueAsString(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize request: " + e.getMessage(), e);
        }
    }

    public <T> T deserializeResponse(String json, Class<T> responseClass) throws TypeSafetyException {
        if (json == null || json.trim().isEmpty()) {
            throw new TypeSafetyException("JSON response cannot be null or empty");
        }

        if (responseClass == null) {
            throw new TypeSafetyException("Response Class cannot be null");
        }

        try {
            return objectMapper.readValue(json, responseClass);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize response: " + e.getMessage(), e);
        }
    }

    public <T> T[] deserializeResponseArray(String json, Class<T[]> arrayType) throws TypeSafetyException {
        if (json == null || json.trim().isEmpty()) {
            throw new TypeSafetyException("JSON response cannot be null or empty");
        }
        try {
            T[] responseArray = objectMapper.readValue(json, arrayType);
            return  responseArray;

        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize response array: " + e.getMessage(), e);
        }
    }

    private Validator createValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

    /**
     * Validate object using Bean Validation annotations
     */
//    private <T> void validateObject(T object) throws TypeSafetyException {
//        if (object == null) {
//            return;
//        }
//
//        Set<ConstraintViolation<T>> violations = validator.validate(object);
//
//        if (!violations.isEmpty()) {
//            StringBuilder errorMessage = new StringBuilder();
//            errorMessage.append("Validation failed for ").append(object.getClass().getSimpleName()).append(": ");
//
//            for (ConstraintViolation<T> violation : violations) {
//                errorMessage.append(violation.getPropertyPath())
//                        .append(" ")
//                        .append(violation.getMessage())
//                        .append("; ");
//            }
//
//            throw new TypeSafetyException(errorMessage.toString());
//        }
//    }

    /**
     * Type-safe method to validate that JSON structure matches expected schema
     */
    public <T> boolean isValidJsonStructure(String json, Class<T> expectedType) {
        try {
            deserializeResponse(json, expectedType);
            return true;
        } catch (TypeSafetyException e) {
            return false;
        }
    }

    /**
     * Get detailed validation errors without throwing exception
     */
    public <T> ValidationResult<T> validateJsonStructure(String json, Class<T> expectedType) {
        try {
            T object = deserializeResponse(json, expectedType);
            return new ValidationResult<>(true, object, null);
        } catch (TypeSafetyException e) {
            return new ValidationResult<>(false, null, e.getMessage());
        }
    }
}
