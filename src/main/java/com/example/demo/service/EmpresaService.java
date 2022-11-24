package com.example.demo.service;

import com.example.demo.entity.EmpresaEntity;

import java.util.List;

public interface EmpresaService {

    public List<EmpresaEntity> findAll();

    public EmpresaEntity findById(Long id);

    public EmpresaEntity save(EmpresaEntity empresaEntity);

    public void deleteById(Long id);
}
