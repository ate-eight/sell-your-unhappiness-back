package sellyourunhappiness.core.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sellyourunhappiness.core.member.domain.enums.MemberType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column
    private MemberType grade;

    public Member(String name, String nickname, MemberType grade) {
        this.name = name;
        this.nickname = nickname;
        this.grade = grade;
    }

    public static Member create(String name, String nickname, MemberType grade) {
        return new Member(name, nickname, grade);
    }
}
