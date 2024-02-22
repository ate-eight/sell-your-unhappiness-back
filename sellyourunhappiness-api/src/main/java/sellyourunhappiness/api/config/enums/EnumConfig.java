package sellyourunhappiness.api.config.enums;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sellyourunhappiness.core.board.domain.enums.BoardType;
import sellyourunhappiness.core.board_like.domain.enums.LikeType;

@Configuration
public class EnumConfig {
    @Bean
    public EnumBean enumBean() {
        EnumBean enumBean = new EnumBean();

        enumBean.put("BoardType", BoardType.class);
        enumBean.put("LikeType", LikeType.class);

        return enumBean;
    }
}

