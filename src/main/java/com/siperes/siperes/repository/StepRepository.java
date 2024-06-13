package com.siperes.siperes.repository;

import com.siperes.siperes.model.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StepRepository extends JpaRepository<Step, UUID> {
    Optional<Step> findFirstByStepSlug(String stepSlug);
}
