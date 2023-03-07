private void validateAttribute(Object object, Attribute attribute, Object value) {
    if (value == null) {
        throw new ValidationException(attribute.getName() + " is required.");
    }

    if (attribute.getType() == AttributeType.STRING) {
        String strValue = (String) value;
        if (attribute.getPattern() != null && !strValue.matches(attribute.getPattern())) {
            throw new ValidationException(attribute.getName() + " must match pattern: " + attribute.getPattern());
        }

        if (attribute.getMaxLength() != null && strValue.length() > attribute.getMaxLength()) {
            throw new ValidationException(attribute.getName() + " cannot be longer than " + attribute.getMaxLength() + " characters.");
        }

        if (attribute.getMinLength() != null && strValue.length() < attribute.getMinLength()) {
            throw new ValidationException(attribute.getName() + " must be at least " + attribute.getMinLength() + " characters long.");
        }

        // additional validations for List<String> attribute
        if (attribute.getType() == AttributeType.LIST_STRING) {
            List<String> strListValue = (List<String>) value;
            if (attribute.getRegex() != null) {
                for (String str : strListValue) {
                    if (!str.matches(attribute.getRegex())) {
                        throw new ValidationException(attribute.getName() + " must contain only alpha numerics.");
                    }
                }
            }
        }
    }

    // similar validations can be added for List<Integer> and String[]
    else if (attribute.getType() == AttributeType.LIST_INTEGER) {
        List<Integer> intValue = (List<Integer>) value;
        // add validations for List<Integer> attribute
    } else if (attribute.getType() == AttributeType.STRING_ARRAY) {
        String[] strArrayValue = (String[]) value;
        // add validations for String[] attribute
    }
}
