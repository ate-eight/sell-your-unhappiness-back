package sellyourunhappiness.api.config.enums;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sellyourunhappiness.core.board.domain.enums.BoardType;

@Configuration
public class EnumConfig {
    @Bean
    public EnumBean enumBean() {
        EnumBean enumBean = new EnumBean();

        enumBean.put("BoardType", BoardType.class);

        return enumBean;
    }
}

