package springinfra.model.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a generic DTO. The word <Strong>Generic</Strong> means it can be a DTO with any possible fields, any name, any value, without any limitation.
 * The specific point of this class is the generated json of this class would be a flat json exactly as same as when we generate a json
 * from other DTO classes. it means the {@link #properties} filed will not be shown in the generated json, only inner key values will be exported.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIdentityInfo(generator = JSOGGenerator.class)
//@JsonSerialize(using = DTOSerializer.class)
public class GenericDto {

    private final Map<String, Object> properties = new HashMap<>();

    public Object getProperty(String key) {
        return properties.get(key);
    }

    @JsonAnySetter
    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return properties;
    }
}
