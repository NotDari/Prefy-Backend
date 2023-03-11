package com.daribear.PrefyBackend.Security.AuthenticationHandlers;

import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.Errors.CustomError;
import com.daribear.PrefyBackend.Errors.CustomErrorObjectMapper;
import com.daribear.PrefyBackend.Errors.ErrorSchema;
import com.daribear.PrefyBackend.Errors.ErrorStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {




    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException, ServletException {
        CustomError customError = ErrorStorage.getCustomErrorFromType(ErrorStorage.ErrorType.NOAUTHATTEMPTED);
        ErrorSchema errorSchema = new ErrorSchema(customError.getMessage(), customError.getCustomCode());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(errorSchema);
            res.setStatus(customError.getHttpStatus());
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            res.getWriter().write(json);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }
}
