package de.twiechert.roleexample.configuration;


import de.twiechert.roleexample.security.Role;
import de.twiechert.roleexample.security.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

/**
 * @author Tayfun Wiechert <tayfun.wiechert@gmail.com>
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public RoleHierarchy roleHierarchy() {
        return Role.resolveToRoleHierachy();
    }

    /**
     * This bean is required when using hierarchical roles.
     * Otherwise role resolution will not work reliability.
     *
     * @return the role hierarchy voter
     */
    @Bean
    protected RoleVoter roleVoter(RoleHierarchy roleHierarchy) {
        return new RoleHierarchyVoter(roleHierarchy);
    }

    @Bean
    protected DefaultWebSecurityExpressionHandler webExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy);
        return defaultWebSecurityExpressionHandler;
    }


    @Configuration
    //@Order(1)
    public static class RestWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Autowired
        private DefaultWebSecurityExpressionHandler securityExpressionHandler;

        @Autowired
        private UserService userService;

        private final static Logger LOGGER = (Logger) LoggerFactory.getLogger(WebSecurityConfig.class);


        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {

            auth.inMemoryAuthentication()
                    .withUser("admin").password("admin").roles(Role.ROLE_ADMIN.getShortName());

            auth.inMemoryAuthentication()
                    .withUser("user").password("user").roles(Role.ROLE_INTERNAL_EMPLOYEE.getShortName());

            auth.userDetailsService(userService)
                    .passwordEncoder(new BCryptPasswordEncoder());


        }

        /**
         * Retrieving, Deleting and Putting resources is basically restricted to admina ccounts
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
            http.authorizeRequests()
                    .expressionHandler(securityExpressionHandler)
                    // consider to order from  specific to general
                    // otherwise the general matchers will match first
                    .antMatchers(HttpMethod.OPTIONS, "/API/**").permitAll()
                    .antMatchers("/admin/**").hasRole(Role.ROLE_ADMIN.getShortName())
                    .antMatchers(HttpMethod.GET, "/API/**").hasAnyRole(Role.ROLE_ANY_EMPLOYEE.getShortName())
                    .antMatchers(HttpMethod.PUT, "/API/**").hasAnyRole(Role.ROLE_ANY_EMPLOYEE.getShortName())
                    .antMatchers("/**").hasAnyRole(Role.ROLE_ADMIN.getShortName())
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .httpBasic();
        }

    }


}