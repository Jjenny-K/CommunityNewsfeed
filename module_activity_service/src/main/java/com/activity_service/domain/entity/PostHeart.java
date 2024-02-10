package com.activity_service.domain.entity;

import com.activity_service.domain.core.BaseCreated;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "postHearts")
@Table(name = "postHearts")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PostHeart extends BaseCreated {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postHeartId")
    private Long id;

    @Column(name = "userId", nullable = false)
    private long userId;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

}
