package com.siperes.siperes.model;

import com.siperes.siperes.enumeration.EnumRole;
import com.siperes.siperes.enumeration.EnumStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(of = "username")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "is_verified_email", nullable = false, columnDefinition = "boolean default false")
    private Boolean isVerifiedEmail;

    @Column(nullable = false)
    private String password;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumStatus status;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "profile_image_link")
    private String profileImageLink;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "user", cascade =  {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<Token> tokens;

    @OneToMany(mappedBy = "user", cascade =  {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<Feedback> feedbacks;

    @OneToMany(mappedBy = "user", cascade =  {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<Recipe> recipes;

    @OneToMany(mappedBy = "user", cascade =  {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<RecipeReview> recipeReviews;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<EmailVerification> emailVerification;

    @ManyToMany
    @JoinTable(
            name = "bookmarks",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private Set<Recipe> bookmarks;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
