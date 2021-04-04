package com.dembla.security.nimbusjwt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class NimbusJoseJwtController {

    @RequestMapping("/company/employees-information")
    Map<String, String> getResponse() {
        return returnEmployeeInfo();
    }


    private Map<String, String> returnEmployeeInfo() {

        Map<String, String> dummyEmployeesInfo = new HashMap<>();

        dummyEmployeesInfo.put("Mira", "Manager");
        dummyEmployeesInfo.put("Sam", "Senior Dev");
        dummyEmployeesInfo.put("John", "Systems Engineer");
        dummyEmployeesInfo.put("Abraham", "HR Administrator");

        return dummyEmployeesInfo;
    }

}
