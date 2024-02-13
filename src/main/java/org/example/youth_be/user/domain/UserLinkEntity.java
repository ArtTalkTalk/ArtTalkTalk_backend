package org.example.youth_be.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_link")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(length = 1000, nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String linkUrl;

    @Builder
    public UserLinkEntity(Long userId, String title, String linkUrl) {
        this.userId = userId;
        this.title = title;
        this.linkUrl = linkUrl;
    }

    public void updateLink(String title, String linkUrl) {
        this.title = title;
        this.linkUrl = linkUrl;
    }
}
