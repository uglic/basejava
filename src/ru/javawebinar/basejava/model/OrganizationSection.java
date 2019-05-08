package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class OrganizationSection extends AbstractSection {
    private static final long serialVersionUID = 1L;

    private final List<Organization> organizations = new ArrayList<>();

    private OrganizationSection() {// only for marshalling
    }

    public OrganizationSection(Organization... organizations) {
        Objects.requireNonNull(organizations, "history must be non-null");
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

    public List<Organization> getOrganizations() {
        return organizations;
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
