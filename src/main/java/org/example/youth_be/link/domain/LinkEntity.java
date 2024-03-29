package org.example.youth_be.link.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.youth_be.common.entity.BaseEntity;

@Entity
@Table(name = "link")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LinkEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkId;

    @Column(nullable = false)
    private Long userId;

    private String title;

    @Column(length = 1000, nullable = false)
    private String linkUrl;

    @Builder
    public LinkEntity(Long linkId, Long userId, String title, String linkUrl) {
        this.linkId = linkId;
        this.userId = userId;
        this.title = title;
        this.linkUrl = linkUrl;
    }

}
