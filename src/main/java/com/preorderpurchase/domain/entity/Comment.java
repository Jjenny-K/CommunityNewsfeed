package com.preorderpurchase.domain.entity;

import com.preorderpurchase.domain.core.BaseCreatedUpdated;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity(name = "comments")
@Table(name = "comments")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Comment extends BaseCreatedUpdated {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentId", nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private CustomUser user;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    @Column(name = "content", nullable = false)
    private String content;

}
