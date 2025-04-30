package org.example.stage3.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Global response handler to standardize all API responses.
 * Applies to all responses from controller methods.
 */
@ControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {
    
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Process all responses from controller methods
        return true;
    }
    
    @Override
    public Object beforeBodyWrite(Object body, 
                                 MethodParameter returnType, 
                                 MediaType selectedContentType,
                                 Class<? extends HttpMessageConverter<?>> selectedConverterType, 
                                 ServerHttpRequest request, 
                                 ServerHttpResponse response) {
        
        // Check for a special header that might be set for 204 responses
        if (response.getHeaders().getFirst("X-Response-Status") != null && 
            response.getHeaders().getFirst("X-Response-Status").equals("204")) {
            return body; // Don't modify 204 responses
        }
        
        // If the response is already a StandardResponse, return it unchanged
        if (body instanceof StandardResponse) {
            return body;
        }
        
        // Special handling for null (from 204 No Content responses)
        if (body == null) {
            return null; // Allow null to pass through for 204 No Content responses
        }
        
        // Special handling for string error messages
        if (body instanceof String && ((String) body).contains("error")) {
            return new StandardResponse("error", null, body);
        }
        
        // Default case: wrap the response body in a success StandardResponse
        return new StandardResponse("success", body, null);
    }
}