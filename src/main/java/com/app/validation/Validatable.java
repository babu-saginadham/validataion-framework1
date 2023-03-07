package com.app.validation;

import java.util.List;

public interface Validatable {
    Object getAttributeValue(String attributeName);
    List<Object> getListAttributeValue(String attributeName);
}
