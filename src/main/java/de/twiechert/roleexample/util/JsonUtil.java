package de.twiechert.roleexample.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tayfun Wiechert <tayfun.wiechert@gmail.com>
 */
public class JsonUtil {

    private final static Logger logger = (Logger) LoggerFactory
            .getLogger(JsonUtil.class);

    private static ObjectMapper mapper = new ObjectMapper();

    static {
    }

    public static String objectToString(Object object) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);

        } catch (JsonProcessingException jsonProcessingException) {
            logger.debug("Error while serializing: {}", jsonProcessingException);
            return "empty";
        }
    }
}
