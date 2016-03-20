package de.twiechert.roleexample.security;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;

/**
 * @author Tayfun Wiechert <tayfun.wiechert@gmail.com>
 */
public enum Role {

    ROLE_ANY_EMPLOYEE("ANY_EMPLOYEE"),
    ROLE_PRIVILEGED_USER("PRIVILEGED_USER"),
    ROLE_INTERNAL_EMPLOYEE("INTERNAL_EMPLOYEE", ROLE_ANY_EMPLOYEE),
    ROLE_EXTERNAL_EMPLOYEE("EXTERNAL_EMPLOYEE", ROLE_ANY_EMPLOYEE),
    ROLE_ADMIN("ADMIN", ROLE_INTERNAL_EMPLOYEE, ROLE_EXTERNAL_EMPLOYEE, ROLE_PRIVILEGED_USER);


    private Role[] includings;
    public final String shortName;

    Role(String shortName, Role... includings) {
        this.includings = includings;
        this.shortName = shortName;
    }

    public Role[] getIncludings() {
        return includings;
    }

    public boolean hasIncludings() {
        return includings != null && includings.length > 0;
    }

    public String getShortName() {
        return shortName;
    }

    public String role() {
        return this.name();
    }

    public static RoleHierarchy resolveToRoleHierachy() {
        CustomRoleHierachyImpl roleHierarchy = new CustomRoleHierachyImpl();
        String currentHierachy = "";
        // iterate over all defined system roles ...
        for (Role systemRole : Role.values()) {

            if (systemRole.hasIncludings()) {
                // iterate over all includings
                for (Role includedRole : systemRole.getIncludings()) {
                    roleHierarchy.addRoleHierarchy(systemRole.role() + " > " + includedRole.role());
                }
            }
        }
        return roleHierarchy;
    }
}
