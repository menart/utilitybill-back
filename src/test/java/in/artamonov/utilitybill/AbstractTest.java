package in.artamonov.utilitybill;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public abstract class AbstractTest {
    @Autowired
    private ObjectMapper jsonObjectMapper;

    protected <T> T loadObj(String type, Class<T> clazz) {
        String className = clazz.getSimpleName();
        String fileName = "objects/" + type + "/" + className + ".json";
        try {
            String obj =
                    Files.readString(Paths.get(
                            Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).toURI()));
            return (T) jsonObjectMapper.readValue(obj, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
