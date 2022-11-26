package com.example.demo.controllers;

import com.example.demo.dto.EmpresaDto;
import com.example.demo.entity.EmpresaEntity;
import com.example.demo.service.EmpresaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {
    private Logger log = LoggerFactory.getLogger(EmpresaController.class);
    //traer el service interface
    @Autowired
    public EmpresaService empresaServices;

    @GetMapping
    public ResponseEntity<?> listar(){
        List<EmpresaEntity> empresas = null;

        Map<String, Object> response = new HashMap<>();

        try{
            empresas = empresaServices.findAll();
        } catch (DataAccessException e){
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("empresas", empresas);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id ){
        EmpresaEntity empresaDB = null;

        Map<String, Object> response = new HashMap<>();

        try {
            empresaDB = empresaServices.findById(id);
            if(empresaDB == null){
                response.put("mensaje", "La empresa con id: " + id + " no se encuentra en la base de datos");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }

        }catch (DataAccessException e){

            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("empresas", empresaDB);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

    }
    /*public EmpresaEntity findById(@PathVariable Long id){
        return empresaServices.findById(id);
    }*/

    @PostMapping
    public ResponseEntity<?> create( @Valid @RequestBody EmpresaDto empresa, BindingResult result ) {
        EmpresaEntity empresaDb = null;
        EmpresaEntity empresaNew = new EmpresaEntity(empresa.getNombre());
        Map<String, Object> response = new HashMap<>();
        if(result.hasErrors()){
           List<String> errors = result.getFieldErrors()
                   .stream()
                   .map(err -> {
                       return "El campo '" + err.getField() + "' " + err.getDefaultMessage();
                   }).collect(Collectors.toList());
           response.put("errors", errors);
           return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
       }

       try{
           empresaDb = empresaServices.save(empresaNew);
       } catch (DataAccessException e){
           response.put("mensaje", "Error al realizar el insert en la base de datos");
           response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

           return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
       }

       response.put("mensaje", "La empresa se a creado con exito");
       response.put("empresa", empresaDb);

       return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit( @Valid @RequestBody EmpresaDto empresa, BindingResult result,
                                   @PathVariable Long id){
        EmpresaEntity empresaDb = null;
        EmpresaEntity empresaCurrent = new EmpresaEntity(empresa.getNombre());
        Map<String, Object> response = new HashMap<>();
        if(result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> {
                        return "El campo '" + err.getField() + "' " + err.getDefaultMessage();
                    }).collect(Collectors.toList());
            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try{
            empresaDb = empresaServices.findById(id);
            if(empresaDb == null){
                response.put("mensaje", "La empresa con id: " + id + " no se encuentra en la base de datos");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }

            empresaDb.setNombre(empresaCurrent.getNombre());
            empresaDb = empresaServices.save(empresaDb);
        } catch (DataAccessException e){
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La empresa se a creado con exito");
        response.put("empresa", empresaDb);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public  ResponseEntity<?> delete(@PathVariable Long id){
        EmpresaEntity empresaDB = null;

        Map<String, Object> response = new HashMap<>();

        try {
            empresaDB =  empresaServices.findById(id);
            if(empresaDB == null){

                response.put("mensaje", "La empresa con id: " + id + " no se encuentra en la base de datos");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
            empresaServices.deleteById(id);
        } catch (DataAccessException e){

            response.put("mensaje", "Error al realizar la acción en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);

    }
}
