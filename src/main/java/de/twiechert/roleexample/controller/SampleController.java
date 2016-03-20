package de.twiechert.roleexample.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tayfun Wiechert <tayfun.wiechert@gmail.com>
 */
@RestController
@RequestMapping("/API")
public class SampleController {


    public static final String ADMIN_HEARTBEAT = "/AdminHeartbeat";
    @PreAuthorize("hasRole('ADMIN') ")
    @RequestMapping(value = ADMIN_HEARTBEAT)
    public @ResponseBody  boolean adminHeartbeat() throws Exception {
        return true;
    }

    public static final String ANY_HEARTBEAT = "/AnyHeartbeat";
    @PreAuthorize("hasRole('ANY_EMPLOYEE') ")
    @RequestMapping(value = ANY_HEARTBEAT)
    public @ResponseBody  boolean anyEmployeeHeartbeat() throws Exception {
        return true;
    }

    public static final String INTERNAL_HEARTBEAT = "/InternalHeartbeat";
    @PreAuthorize("hasRole('INTERNAL_EMPLOYEE') ")
    @RequestMapping(value = INTERNAL_HEARTBEAT)
    public @ResponseBody boolean internalEmployeeHeartbeat() throws Exception {
        return true;
    }

    public static final String DELETE_ACTION = "/Delete";
    @PreAuthorize("hasRole('INTERNAL_EMPLOYEE') ")
    @RequestMapping(value = DELETE_ACTION, method = RequestMethod.DELETE)
    public @ResponseBody boolean deleteAction() throws Exception {
        return true;
    }
}
