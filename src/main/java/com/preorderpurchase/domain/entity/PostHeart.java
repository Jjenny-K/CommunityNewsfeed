package com.preorderpurchase.domain.entity;

import com.preorderpurchase.domain.core.BaseCreated;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "postHearts")
@Table(name = "postHearts")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PostHeart extends BaseCreated {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postHeartId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "postHeartUserId")
    private CustomUser postHeartUser;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

}
