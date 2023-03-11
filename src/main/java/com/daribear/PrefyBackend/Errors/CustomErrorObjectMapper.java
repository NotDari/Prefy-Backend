package com.daribear.PrefyBackend.Errors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

public class CustomErrorObjectMapper {
    public static HttpServletResponse getResponse(HttpServletResponse response, CustomError error){
        ErrorSchema errorSchema = new ErrorSchema(error.getMessage(), error.getCustomCode());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(errorSchema);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            response.getWriter().write(json);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return response;
    }
}
