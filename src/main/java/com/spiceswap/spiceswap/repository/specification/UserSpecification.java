package com.spiceswap.spiceswap.repository.specification;

import com.spiceswap.spiceswap.enumeration.EnumRole;
import com.spiceswap.spiceswap.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> hasUsernameAndRole(String username, EnumRole role) {
        return (root, query, criteriaBuilder) -> {
            Predicate usernamePredicate = criteriaBuilder.conjunction();
            if (username != null && !username.isEmpty()) {
                usernamePredicate = criteriaBuilder.like(root.get("userName"), "%" + username + "%");
            }
            Predicate rolePredicate = criteriaBuilder.equal(root.get("role"), role);
            return criteriaBuilder.and(usernamePredicate, rolePredicate);
        };
    }
}
