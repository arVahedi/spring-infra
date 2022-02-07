package springinfra.model.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gladiator on 5/5/2017 AD.
 */

@JsonIdentityInfo(generator = JSOGGenerator.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonSerialize(using = DTOSerializer.class)
public class GenericDto {

    private Map<String, Object> properties = new HashMap<>();

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
