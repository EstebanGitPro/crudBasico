package com.example.demo.implementation;

import com.example.demo.entity.EmpresaEntity;
import com.example.demo.repository.EmpresaRepository;
import com.example.demo.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaImpl implements EmpresaService {

    @Autowired
    private EmpresaRepository repository;

    @Override
    public List<EmpresaEntity> findAll ( ) {
        return repository.findAll();
    }

    @Override
    public EmpresaEntity findById ( Long id ) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public EmpresaEntity save ( EmpresaEntity empresaEntity ) {
        return repository.save(empresaEntity);
    }

    @Override
    public void deleteById ( Long id ) {
        repository.deleteById(id);
    }
}
