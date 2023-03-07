package com.app.model;

import java.util.List;
import java.util.stream.Collectors;

import com.app.validation.Validatable;

public class Student implements Validatable{

	int id;
	String name;
	String mobileNo;
	List<Address> addresses;
	
	@Override
    public Object getAttributeValue(String attributeName) {
        switch (attributeName) {
            case "id":
                return id;
            case "name":
                return name;
            case "mobileNo":
                return mobileNo;
            case "addresses":
            	return addresses;
            default:
                throw new IllegalArgumentException("Invalid attribute name: " + attributeName);
        }
    }
	
	@Override
    public List<Object> getListAttributeValue(String attributeName) {
        switch (attributeName) {
            case "addresses":
                return addresses.stream().map(a -> (Object) a).collect(Collectors.toList());
            default:
                throw new IllegalArgumentException("Invalid attribute name: " + attributeName);
        }
    }
}
