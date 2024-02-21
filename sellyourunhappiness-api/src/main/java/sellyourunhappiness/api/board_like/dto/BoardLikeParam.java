package sellyourunhappiness.api.board_like.dto;

import sellyourunhappiness.core.board_like.domain.enums.LikeType;
import sellyourunhappiness.core.config.converter.EnumConverterUtils;

public record BoardLikeParam(String test, LikeType likeType) {

    public BoardLikeParam(String test, String name) {  //reflection  << 동작 알아오기
        // 커스터 마이징 할 상황이 왔을떄 건드려 보기 힘들다
        this(test, EnumConverterUtils.ofName(LikeType.class, name)); //써본적이 있는데 결국엔 수정하심 liketype으로 바꿀 필요 없이 쓸곳이 생김
    } // requsetbody가 어떻게 들어가는걸로알고있는데 이렇게하는게 어떨까
    // 데이터를 전달하는 통로
    // 여기서바꾸자 약속

    // 미래 지향적 시니어가 경험으로 생각한 것
    // Optional이 더 좋은지 객체가 더 좋은지 보다 현재 상황에 맞추서 개발해봐라

}
