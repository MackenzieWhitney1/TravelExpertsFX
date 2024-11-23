package org.example.travelexpertsfx;

public enum Mode {
    ADD, EDIT, DELETE;

    @Override
    public String toString() {
        String name = this.name();
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        return name;
    }
}
