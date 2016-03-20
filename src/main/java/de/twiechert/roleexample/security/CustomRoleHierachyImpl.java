package de.twiechert.roleexample.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.hierarchicalroles.CycleInRoleHierarchyException;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is based on with {@link org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl} a minor modification.
 * @author Michael Mayr
 */
public class CustomRoleHierachyImpl
        implements RoleHierarchy {

    private final static Logger logger = (Logger) LoggerFactory.getLogger(CustomRoleHierachyImpl.class);


    /**
     * rolesReachableInOneStepMap is a Map that under the key of a specific role name contains a set of all roles
     * reachable from this role in 1 step
     */
    private Map<GrantedAuthority, Set<GrantedAuthority>> rolesReachableInOneStepMap = new HashMap<>();


    /**
     * rolesReachableInOneOrMoreStepsMap is a Map that under the key of a specific role name contains a set of all
     * roles reachable from this role in 1 or more steps
     */
    private Map<GrantedAuthority, Set<GrantedAuthority>> rolesReachableInOneOrMoreStepsMap = new HashMap<>();


    public void addRoleHierarchy(String roleHierarchyStringRepresentation) {

        buildRolesReachableInOneStepMap(roleHierarchyStringRepresentation);
        buildRolesReachableInOneOrMoreStepsMap();
    }

    /**
     * Parse input and build the map for the roles reachable in one step: the higher role will become a key that
     * references a set of the reachable lower roles.
     */
    protected void buildRolesReachableInOneStepMap(String roleHierarchyStringRepresentation) {
        Pattern pattern = Pattern.compile("(\\s*([^\\s>]+)\\s*>\\s*([^\\s>]+))");

        Matcher roleHierarchyMatcher = pattern.matcher(roleHierarchyStringRepresentation);

        while (roleHierarchyMatcher.find()) {
            GrantedAuthority higherRole = new SimpleGrantedAuthority(roleHierarchyMatcher.group(2));
            GrantedAuthority lowerRole = new SimpleGrantedAuthority(roleHierarchyMatcher.group(3));

            Set<GrantedAuthority> rolesReachableInOneStepSet;

            // higher role has not been registered as key ...
            if (!rolesReachableInOneStepMap.containsKey(higherRole)) {
                // create hash set that contains the roles reachable from the current role
                rolesReachableInOneStepSet = new HashSet<>();
                //
                rolesReachableInOneStepMap.put(higherRole, rolesReachableInOneStepSet);

            } else {

                rolesReachableInOneStepSet = rolesReachableInOneStepMap.get(higherRole);
            }
            addReachableRoles(rolesReachableInOneStepSet, lowerRole);

            logger.debug("buildRolesReachableInOneStepMap() - From role "
                    + higherRole + " one can reach role " + lowerRole + " in one step.");
        }
    }

    protected void addReachableRoles(Set<GrantedAuthority> reachableRoles,
                                     GrantedAuthority authority) {

        for (GrantedAuthority testAuthority : reachableRoles) {
            String testKey = testAuthority.getAuthority();
            if ((testKey != null) && (testKey.equals(authority.getAuthority()))) {
                return;
            }
        }
        reachableRoles.add(authority);
    }

    /**
     * For every higher role from rolesReachableInOneStepMap store all roles that are reachable from it in the map of
     * roles reachable in one or more steps. (Or throw a CycleInRoleHierarchyException if a cycle in the role
     * hierarchy definition is detected)
     */
    private void buildRolesReachableInOneOrMoreStepsMap() {
        // iterate over all higher roles from rolesReachableInOneStepMap

        for (GrantedAuthority role : rolesReachableInOneStepMap.keySet()) {
            Set<GrantedAuthority> rolesToVisitSet = new HashSet<>();

            if (rolesReachableInOneStepMap.containsKey(role)) {
                rolesToVisitSet.addAll(rolesReachableInOneStepMap.get(role));
            }

            Set<GrantedAuthority> visitedRolesSet = new HashSet<>();

            while (!rolesToVisitSet.isEmpty()) {
                // take a role from the rolesToVisit set
                GrantedAuthority aRole = rolesToVisitSet.iterator().next();
                rolesToVisitSet.remove(aRole);
                addReachableRoles(visitedRolesSet, aRole);
                if (rolesReachableInOneStepMap.containsKey(aRole)) {
                    Set<GrantedAuthority> newReachableRoles = rolesReachableInOneStepMap.get(aRole);

                    // definition of a cycle: you can reach the role you are starting from
                    if (rolesToVisitSet.contains(role) || visitedRolesSet.contains(role)) {
                        throw new CycleInRoleHierarchyException();
                    } else {
                        // no cycle
                        rolesToVisitSet.addAll(newReachableRoles);
                    }
                }
            }
            rolesReachableInOneOrMoreStepsMap.put(role, visitedRolesSet);

            logger.debug("buildRolesReachableInOneOrMoreStepsMap() - From role "
                    + role + " one can reach " + visitedRolesSet + " in one or more steps.");
        }

    }


    private Set<GrantedAuthority> getRolesReachableInOneOrMoreSteps(
            GrantedAuthority authority) {

        if (authority.getAuthority() == null) {
            return null;
        }

        for (GrantedAuthority testAuthority : rolesReachableInOneOrMoreStepsMap.keySet()) {
            String testKey = testAuthority.getAuthority();
            if ((testKey != null) && (testKey.equals(authority.getAuthority()))) {
                return rolesReachableInOneOrMoreStepsMap.get(testAuthority);
            }
        }

        return null;
    }


    public Collection<GrantedAuthority> getReachableGrantedAuthorities(
            Collection<? extends GrantedAuthority> authorities) {
        if (authorities == null || authorities.isEmpty()) {
            return AuthorityUtils.NO_AUTHORITIES;
        }

        Set<GrantedAuthority> reachableRoles = new HashSet<>();

        for (GrantedAuthority authority : authorities) {
            addReachableRoles(reachableRoles, authority);
            Set<GrantedAuthority> additionalReachableRoles = getRolesReachableInOneOrMoreSteps(authority);
            if (additionalReachableRoles != null) {
                reachableRoles.addAll(additionalReachableRoles);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("getReachableGrantedAuthorities() - From the roles "
                    + authorities + " one can reach " + reachableRoles
                    + " in zero or more steps.");
        }

        List<GrantedAuthority> reachableRoleList = new ArrayList<>(
                reachableRoles.size());
        reachableRoleList.addAll(reachableRoles);

        return reachableRoleList;
    }

}
