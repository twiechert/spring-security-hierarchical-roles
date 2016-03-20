# Spring Security - Hierarchical Roles Sample App
This sample application uses hierarchical roles for validation of the user's permission to access a certain resource both on method and HTTP level.


## Extending the role hierarchy 

The role hierarchy is defined in the enum `Role`, where you can add roles and their includings. An entry could look like:

`ROLE_TEST_ENGINEER("TEST_ENGINEER", ROLE_TESTER, ROLE_DEVELOPER);`

## Securing the application

You can secure your application on HTTP level defining matching rules in the `WebSecurityConfig` or on method-level using the `@PreAuthorize` annotation. In this sample app method-level security is used to make sure that only admin users can view all other user obejcts:


    @PreAuthorize("hasRole('ADMIN')") 
    Iterable<User> findAll();

