package org.example.youth_be.fixture;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.jackson.introspector.JacksonObjectArbitraryIntrospector;
import org.example.youth_be.artwork.domain.ArtworkEntity;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.javaGetter;

public class ArtworkEntityFixture {
    private static final FixtureMonkey monkey = FixtureMonkey.builder()
            .objectIntrospector(JacksonObjectArbitraryIntrospector.INSTANCE)
            .build();

    public static ArtworkEntity validCommentCountAny(Long artworkId) {
        return monkey.giveMeBuilder(ArtworkEntity.class)
                .set(javaGetter(ArtworkEntity::getArtworkId), artworkId)
                .setNotNull(javaGetter(ArtworkEntity::getCommentCount))
                .sample();
    }
}
