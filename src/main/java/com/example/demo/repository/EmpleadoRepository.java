package com.example.demo.repository;

import com.example.demo.entity.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, Long> {

    //@Query("select e from EmpleadoEntity e where e.email=?1")
    public Optional<EmpleadoEntity> findByEmail( String email);

    public boolean existsByEmail ( String email);

}