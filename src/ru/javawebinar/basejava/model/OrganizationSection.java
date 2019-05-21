package ru.javawebinar.basejava.model;

import java.time.LocalDate;
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
        Objects.requireNonNull(organizations, "organizations must be non-null");
        Collections.addAll(this.organizations, organizations);
    }

    public OrganizationSection(List<Organization> organizations) {
        Objects.requireNonNull(organizations, "organizations must not be null");
        this.organizations.addAll(organizations);
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    @Override
    public String toString() {
        return organizations.toString();
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

    public static AbstractSection getEmpty() {
        return new OrganizationSection(
                new Organization(
                        new Contact("", ""),
                        new Organization.Position(LocalDate.now(), LocalDate.now(), "", "")));
    }
}
