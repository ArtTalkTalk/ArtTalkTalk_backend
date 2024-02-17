package org.example.youth_be.comment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.youth_be.common.entity.BaseEntity;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long artworkId;

    @Builder
    public CommentEntity(Long commentId, String contents, Long userId, Long artworkId) {
        this.commentId = commentId;
        this.contents = contents;
        this.userId = userId;
        this.artworkId = artworkId;
    }

    public boolean isOwner(Long userId) {
        return this.userId.equals(userId);
    }
}
