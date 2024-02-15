package sellyourunhappiness.core.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -819886468L;

    public static final QUser user = new QUser("user");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath profileURL = createString("profileURL");

    public final StringPath refreshToken = createString("refreshToken");

    public final EnumPath<sellyourunhappiness.core.user.domain.enums.Role> role = createEnum("role", sellyourunhappiness.core.user.domain.enums.Role.class);

    public final EnumPath<sellyourunhappiness.core.user.domain.enums.SocialType> socialType = createEnum("socialType", sellyourunhappiness.core.user.domain.enums.SocialType.class);

    public final EnumPath<sellyourunhappiness.core.user.domain.enums.UserStatus> status = createEnum("status", sellyourunhappiness.core.user.domain.enums.UserStatus.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

