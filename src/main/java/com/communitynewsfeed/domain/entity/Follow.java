package com.communitynewsfeed.domain.entity;

import com.communitynewsfeed.domain.core.BaseCreated;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "follows")
@Table(name = "follows")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Follow extends BaseCreated {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "followId")
    private long id;

    @ManyToOne
    @JoinColumn(name = "followerUserId")
    private CustomUser followerUser;

    @ManyToOne
    @JoinColumn(name = "followingUserId")
    private CustomUser followingUser;

}
