package com.daribear.PrefyBackend.Security.AuthenticationHandlers;

import com.daribear.PrefyBackend.Errors.CustomError;
import com.daribear.PrefyBackend.Errors.CustomErrorObjectMapper;
import org.springframework.core.NestedIOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Custom acess denied handler to override default response when user doesn't have sufficient permissions.
 * I've made it so that instead of the current error, it returns my custom error with code 10.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * Overrides the default handling behaviour to return a custom error.
     * Returns error with code 10
     *
     * @param request HTTPRequest that the user used and was denied
     * @param response the response to write into, that will be returned to the user.
     * @param e the default error thrown by springboot
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException e) {
        CustomError customError =  new CustomError(HttpServletResponse.SC_FORBIDDEN,"User doesn't have permissions", 10);
        CustomErrorObjectMapper.getResponse(response,customError);
    }



}
