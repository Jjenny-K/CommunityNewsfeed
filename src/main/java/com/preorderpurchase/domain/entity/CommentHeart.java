package com.preorderpurchase.domain.entity;

import com.preorderpurchase.domain.core.BaseCreated;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "commentHearts")
@Table(name = "commentHearts")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CommentHeart extends BaseCreated {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentHeartId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "commentHeartUserId")
    private CustomUser commentHeartUser;

    @ManyToOne
    @JoinColumn(name = "commentId")
    private Comment comment;

}
