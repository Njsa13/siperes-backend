package com.siperes.siperes.repository;


import com.siperes.siperes.model.ModificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ModificationRequestRepository extends JpaRepository<ModificationRequest, UUID> {
}
