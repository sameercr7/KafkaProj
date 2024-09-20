package com.gccloud.nocportal.Controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @PostMapping(path = "/unauthenticated")
    public String unAuthenticateException() {

        return "error/error-401";
    }

    @GetMapping(path = "/unauthorised")
    public String unAuthorisedException() {

        return "error/error-403";
    }

    
    @RequestMapping(ERROR_PATH)
    public String handleError(HttpServletRequest request) {

        int statusCode = (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (statusCode == 404) {
            // Return the 404 error page view
            return "error/error-404";
        } else if (statusCode == 500) {
            // Return the 500 error page view
            return "error/error-500";
        } else {
            // Return a generic error page view
            return "error/error-500";
        }
    }

    public String getErrorPath() {
        return ERROR_PATH;
    }

}