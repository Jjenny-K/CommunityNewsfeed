package com.activity_service.domain.entity;

import com.activity_service.domain.dto.UpdateUserDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.activity_service.domain.core.BaseCreatedUpdated;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Set;

@Entity(name = "users")
@Table(name = "users")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CustomUser extends BaseCreatedUpdated {

    @Id
    @Column(name = "userId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "email", nullable = false, unique = true, length = 50)
    private String email;

    @JsonIgnore
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "greeting", nullable = false, length = 100)
    private String greeting;

    @JsonIgnore
    @Column(name = "isActivated", nullable = false)
    private boolean isActivated;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "userAuthority",
            joinColumns = {@JoinColumn(name = "userId", referencedColumnName = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "authorityName", referencedColumnName ="authorityName")}
    )
    private Set<Authority> authorities;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, optional = false)
    private ProfileImage profileImage;

    public void updateUserInfo(UpdateUserDto updateUserDto) {
        if (updateUserDto.getName() != null) this.name = updateUserDto.getName();
        if (updateUserDto.getGreeting() != null) this.greeting = updateUserDto.getGreeting();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

}
