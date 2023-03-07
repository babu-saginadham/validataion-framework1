package com.app.model;

import java.util.List;

import com.app.validation.Validatable;

public class Address implements Validatable {
    private int dno;
    private String street;

    // getters and setters for dno and street

    @Override
    public Object getAttributeValue(String attributeName) {
        switch (attributeName) {
            case "dno":
                return dno;
            case "street":
                return street;
            default:
                throw new IllegalArgumentException("Invalid attribute name: " + attributeName);
        }
    }
    
    @Override
    public List<Object> getListAttributeValue(String attributeName) {
        throw new UnsupportedOperationException("Lists not supported for attribute " + attributeName);
    }
}
