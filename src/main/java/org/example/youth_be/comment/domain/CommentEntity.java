package org.example.youth_be.comment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long artworkId;

    @Builder
    public CommentEntity(Long commentId, String content, Long userId, Long artworkId) {
        this.commentId = commentId;
        this.content = content;
        this.userId = userId;
        this.artworkId = artworkId;
    }
}
