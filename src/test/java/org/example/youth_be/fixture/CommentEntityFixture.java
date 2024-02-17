package org.example.youth_be.fixture;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.jackson.introspector.JacksonObjectArbitraryIntrospector;
import org.example.youth_be.comment.domain.CommentEntity;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.javaGetter;

public class CommentEntityFixture {
    private static final FixtureMonkey monkey = FixtureMonkey.builder()
            .objectIntrospector(JacksonObjectArbitraryIntrospector.INSTANCE)
                .build();
    public static CommentEntity validIdAny() {
        return monkey.giveMeBuilder(CommentEntity.class)
                .setNotNull(javaGetter(CommentEntity::getCommentId))
                .sample();
    }

    public static CommentEntity validUserIdAny(Long userId) {
        return monkey.giveMeBuilder(CommentEntity.class)
                .set(javaGetter(CommentEntity::getUserId), userId)
                .sample();
    }
}
