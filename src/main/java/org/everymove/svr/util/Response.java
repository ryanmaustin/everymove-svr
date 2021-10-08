package org.everymove.svr.util;

import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Standard Api Response for ISTS Services. This Response always returns
 * an HTTP Outer Status of 200, but contains the true status code within the 
 * body of the response, along with a message that is either an error message,
 * a success message, or the expected object to be returned.
 */
public class Response extends ResponseEntity<Object> {

    private ResponseBody body;

    private Response() 
    {
        super(HttpStatus.OK);
        this.body = new ResponseBody();
    }

    @Override
    public ResponseBody getBody() 
    {
        return this.body;
    }

    @Override
    public HttpStatus getStatusCode() 
    {
        return HttpStatus.OK;
    }

    @Override
    public int getStatusCodeValue() 
    {
        return 200;
    }

    public static Response ok(Object message) 
    {
        return new Response().withStatusCode(200).withMessage(message);
    }

    public static Response error(int statusCode, Object message) 
    {
        return new Response().withStatusCode(statusCode).withMessage(message);
    }

    public Response withStatusCode(int statusCode) 
    {
        this.body.setStatusCode(statusCode);
        return this;
    }

    public Response withMessage(Object message) 
    {
        this.body.setMessage(message);
        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Response)
        {
            Response other = (Response) o;
            if (other.getBody() != null)
            {
                return this.body.statusCode == other.getBody().getStatusCode() &&
                    this.body.message.equals(other.getBody().getMessage());
            }
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.getBody().getStatusCode(), this.getBody().getMessage());
    }

    /**
     * The Body of an API Response that contains the actual status code and a message (object)
     */
    public static class ResponseBody 
    {
        private int statusCode;
        private Object message;

        public ResponseBody() 
        {
            // Empty Constructor
        }

        public void setStatusCode(int statusCode) 
        {
            this.statusCode = statusCode;
        }

        public int getStatusCode() 
        {
            return this.statusCode;
        }

        public void setMessage(Object message) 
        {
            this.message = message;
        }

        public Object getMessage() 
        {
            return this.message;
        }

        /**
         * Attempts to parse the contained message as the given clazz.
         */
        public <E> E getMessageAs(Class<E> clazz) throws JsonProcessingException 
        {
            ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
            return mapper.readValue(mapper.writeValueAsString(this.message), clazz);
        }
    }
}