package sellyourunhappiness.api.config.enums;

import lombok.NoArgsConstructor;
import sellyourunhappiness.core.config.converter.EnumType;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public class EnumBean {

    private Map<String, List<String>> factory = new LinkedHashMap<>();

    public void put(String key, Class<? extends EnumType> e) {
        factory.put(key, toValues(e));
    }

    private List<String> toValues(Class<? extends EnumType> e) {
        return Arrays.stream(e.getEnumConstants())
                .map(EnumType::getName)
                .collect(Collectors.toList());
    }

    public List<String> get(String key) {
        if(factory.get(key) == null){
            return new LinkedList<>();
        }
        return factory.get(key);
    }
}


