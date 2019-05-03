package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrganizationSection extends Section {
    private final List<Organization> organizations = new ArrayList<>();

    public OrganizationSection(Organization... organizations) {
        Collections.addAll(this.organizations, organizations);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Organization organization : organizations) {
            if (builder.length() != 0) {
                builder.append("\r\n");
            }
            builder.append(organization);
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationSection that = (OrganizationSection) o;
        return organizations.equals(that.organizations);
    }

    @Override
    public int hashCode() {
        return organizations.hashCode();
    }
}
