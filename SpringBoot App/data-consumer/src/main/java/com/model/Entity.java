package com.model;

public class Entity{
    public String name;
    public Entity(String name){
        this.name=name;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "name='" + name + '\'' +
                '}';
    }
}
