package com.effugium.core.model;

public class EntityFilter {
    private String name;
    private Object value;

    public EntityFilter(String name, Object value) {
	super();
	this.name = name;
	this.value = value;
    }

    public String getName() {
	return name;
    }

    public Object getValue() {
	return value;
    }
}
