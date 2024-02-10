package com.activity_service.domain.entity;

import com.activity_service.domain.core.BaseCreated;
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
    @Column(name = "followId", nullable = false)
    private long id;

    @Column(name = "followerUserId", nullable = false)
    private long followerUserId;

    @Column(name = "followingUserId", nullable = false)
    private long followingUserId;

}
