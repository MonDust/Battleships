package pg.edu.pl.communication;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Message {
    private final String type;
    private final Map<String, Object> data;

    public Message(String type, Object value) {
        this.type = type;
        this.data = new HashMap<>();
        this.data.put("text", value);
    }

    public Message(String type, Map<String, Object> data) {
        this.type = type;
        this.data = data;
    }

    public String getType() { return type; }
    public Object get(String key) { return data.get(key); }
    public int getInt(String key) { return (Integer) data.get(key); }

    public String toJson() {
        return new Gson().toJson(Map.of("type", type, "data", data));
    }

    public static Message parse(String json) {
        var map = new Gson().fromJson(json, Map.class);
        String type = (String) map.get("type");
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        return new Message(type, data);
    }
}