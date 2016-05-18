package resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedHashMap;

/**
 * Created by Evgene on 31.03.2016.
 */



@JsonIgnoreProperties(ignoreUnknown = true)
public class Entity {

    private String type;

    private LinkedHashMap<String,String> value;

    public Entity(){}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LinkedHashMap<String, String> getValue() {
        return value;
    }

    public void setValue(LinkedHashMap<String, String> value) {
        this.value = value;
    }
}
