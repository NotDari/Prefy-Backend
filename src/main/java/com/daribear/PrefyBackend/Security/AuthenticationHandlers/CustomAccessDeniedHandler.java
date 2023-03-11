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

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException e) {
        CustomError customError =  new CustomError(HttpServletResponse.SC_FORBIDDEN,"User doesn't have permissions", 10);
        CustomErrorObjectMapper.getResponse(response,customError);
        System.out.println("Sdad EXception:");
        //throw customError;

        // You can create your own repsonse here to handle method level access denied reponses..
        // Follow similar method to the bad credentials handler above.
    }



}
