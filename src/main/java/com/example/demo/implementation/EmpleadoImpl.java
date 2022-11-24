package com.example.demo.implementation;

import com.example.demo.entity.EmpleadoEntity;
import com.example.demo.repository.EmpleadoRepository;
import com.example.demo.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpleadoImpl implements EmpleadoService {

    @Autowired
    private EmpleadoRepository repository;

    @Override
    public List<EmpleadoEntity> findAll ( ) {

        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public EmpleadoEntity findById ( Long id ) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public EmpleadoEntity save ( EmpleadoEntity empleado ) {
        return repository.save(empleado);
    }

    @Override
    @Transactional
    public void deleteById ( Long id ) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public EmpleadoEntity findByEmail ( String email ) {
        return repository.findByEmail(email).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail ( String email ) {
        return repository.existsByEmail(email);
    }
}
