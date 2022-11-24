package com.example.demo.service;

import com.example.demo.entity.EmpleadoEntity;

import java.util.List;

public interface EmpleadoService {

    public List<EmpleadoEntity> findAll();

    public EmpleadoEntity findById(Long id);

    public EmpleadoEntity save(EmpleadoEntity empleado);

    public void deleteById(Long id);

    public EmpleadoEntity findByEmail(String email);

    public boolean existsByEmail(String email);
}
