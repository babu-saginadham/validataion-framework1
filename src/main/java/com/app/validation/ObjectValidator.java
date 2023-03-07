package com.app.validation;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Validation;
import javax.validation.Validator;

public class ObjectValidator {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private static ValidationRules validationRules;

    static {
        try {
            validationRules = ValidationRuleLoader.loadValidationRules(new File("validation-rules.yaml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void validateObject(Validatable object, String moduleName) {
        ValidationRules.Module module = validationRules.getModules().stream()
                .filter(m -> m.getName().equals(moduleName))
                .findFirst().orElseThrow(() -> new RuntimeException("Module not found"));

        Map<String, ValidationRules.Attribute> attributeMap = module.getAttributes();
        for (String attributeName : attributeMap.keySet()) {
            //Object attributeValue = getAttributeValue(object, attributeName);
        	Object attributeValue = object.getAttributeValue(attributeName);
            ValidationRules.Attribute attribute = attributeMap.get(attributeName);
            validateAttributeValue(object, attribute, attributeName);
        }
    }

    //TODO: bkp
    private static Object getAttributeValue1(Object object, String attributeName) {
        // Assume attribute name is in camel case (e.g. "firstName"), and getter method follows JavaBeans convention (e.g. "getFirstName")
        String getterName = "get" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
        try {
            Method getter = object.getClass().getMethod(getterName);
            return getter.invoke(object);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error getting attribute value", e);
        }	
    }


    private static void validateAttributeValue(Validatable object, ValidationRules.Attribute attribute, String attributeName) {
        Object attributeValue = object.getAttributeValue(attributeName);
        if (attributeValue != null) {
            if (attributeValue instanceof List) {
                List<Object> attributeList = ((List<?>) attributeValue).stream().map(a -> (Object) a).collect(Collectors.toList());
                for (Object attributeListItem : attributeList) {
                    if (attributeListItem instanceof Validatable) {
                        validateObject((Validatable) attributeListItem);
                    }
                    validateAttributeValueForSingleValue(attributeName, attribute, attributeListItem);
                }
            } else {
                if (attributeValue instanceof Validatable) {
                    validateObject((Validatable) attributeValue);
                }
                validateAttributeValueForSingleValue(attributeName, attribute, attributeValue);
            }
        }
    }

    private static void validateAttributeValueForSingleValue(String attributeName, ValidationRules.Attribute attribute, Object attributeValue) {
        if (attribute.getMin() != null && attributeValue instanceof Number) {
            int min = attribute.getMin();
            if (((Number) attributeValue).intValue() < min) {
                throw new RuntimeException(attributeName + ": " + attribute.getMessage());
            }
        }
        if (attribute.getMax() != null && attributeValue instanceof Number) {
            int max = attribute.getMax();
            if (((Number) attributeValue).intValue() > max) {
                throw new RuntimeException(attributeName + ": " + attribute.getMessage());
            }
        }
        if (attribute.getRegex() != null && attributeValue instanceof String) {
            String regex = attribute.getRegex();
            if (!((String) attributeValue).matches(regex)) {
                throw new RuntimeException(attributeName + ": " + attribute.getMessage());
            }
        }
    }

    
    private static void validateObject(Validatable object) {
        for (ValidationRules.Module module : validationRules.getModules()) {
        	for (Map.Entry<String, ValidationRules.Attribute> entry : module.getAttributes().entrySet()) {
                String attributeName = entry.getKey();
                ValidationRules.Attribute attribute = entry.getValue();
                validateAttributeValue(object, attribute, attributeName);
            }
        }
    }
}

