package au.com.dius.pact.model;

import java.util.Map;

/**
 * Pact Consumer
 */
public class Consumer {
    public Consumer(final String name) {
        this.name = name;
    }

    public static Provider fromMap(Map map) {
        if (map != null && map.get("name") != null) {
            return new Provider(map.get("name").toString());
        } else return new Provider("provider");
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
}
