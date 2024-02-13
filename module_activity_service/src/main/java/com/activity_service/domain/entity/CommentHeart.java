package com.activity_service.domain.entity;

import com.activity_service.domain.core.BaseCreated;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "commentHearts")
@Table(name = "commentHearts")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CommentHeart extends BaseCreated {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentHeartId")
    private Long id;

    @Column(name = "userId", nullable = false)
    private long userId;

    @ManyToOne
    @JoinColumn(name = "commentId", nullable = false)
    private Comment comment;

}
