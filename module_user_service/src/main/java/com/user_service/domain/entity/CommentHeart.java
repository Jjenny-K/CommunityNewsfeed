package com.user_service.domain.entity;

import com.user_service.domain.core.BaseCreated;
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

    @ManyToOne
    @JoinColumn(name = "userId")
    private CustomUser user;

    @ManyToOne
    @JoinColumn(name = "commentId")
    private Comment comment;

}
