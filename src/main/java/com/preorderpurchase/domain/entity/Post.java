package com.preorderpurchase.domain.entity;

import com.preorderpurchase.domain.core.BaseCreatedUpdated;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity(name = "posts")
@Table(name = "posts")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post extends BaseCreatedUpdated {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postId", nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private CustomUser user;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

}
