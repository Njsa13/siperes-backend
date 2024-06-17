package com.siperes.siperes.repository;

import com.siperes.siperes.model.CopyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CopyDetailRepository extends JpaRepository<CopyDetail, UUID> {
    @Modifying
    void deleteByCopyRecipeId(UUID copyRecipeId);
}
