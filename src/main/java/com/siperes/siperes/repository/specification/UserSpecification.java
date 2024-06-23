package com.siperes.siperes.repository.specification;

import com.siperes.siperes.enumeration.EnumRole;
import com.siperes.siperes.model.User;
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
