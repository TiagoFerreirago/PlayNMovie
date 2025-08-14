package com.th.playnmovie.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.th.playnmovie.security.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long>{

	Permission findByDescription(String description);
}
