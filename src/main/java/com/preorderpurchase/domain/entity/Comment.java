package com.preorderpurchase.domain.entity;

import com.preorderpurchase.domain.core.BaseCreatedUpdated;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "comments")
@Table(name = "comments")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Comment extends BaseCreatedUpdated {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentId")
    private long id;

    @ManyToOne
    @JoinColumn(name = "commentUserId")
    private CustomUser commentUser;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    @Column(name = "comment", nullable = false)
    private String comment;

}
