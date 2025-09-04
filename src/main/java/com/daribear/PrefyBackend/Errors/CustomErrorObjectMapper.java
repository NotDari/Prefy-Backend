package com.daribear.PrefyBackend.Errors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * Helper class to writing my custom error into the http response
 */
public class CustomErrorObjectMapper {
    /**
     * Writes the custom error into the response provided.
     * @param response response to write into
     * @param error error to write in
     * @return new response
     */
    public static HttpServletResponse getResponse(HttpServletResponse response, CustomError error){
        ErrorSchema errorSchema = new ErrorSchema(error.getMessage(), error.getCustomCode());
        try {
            //Write the response
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
