package com.example.demo.controllers;

import com.example.demo.dto.EmpleadoDto;
import com.example.demo.entity.EmpleadoEntity;
import com.example.demo.entity.EmpresaEntity;
import com.example.demo.service.EmpleadoService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/empleados")
public class EmpleadoController {

    private Logger log = LoggerFactory.getLogger(EmpleadoController.class);
    //traer el service interface
    @Autowired
    public EmpleadoService empleadoService;

    @Autowired
    public EmpresaService empresaService;

    @GetMapping
    public  ResponseEntity<?> listarEmpleados(){

        List<EmpleadoEntity> empelados = null;

        Map<String, Object> response = new HashMap<>();

        try {

            empelados = empleadoService.findAll();

        }catch (DataAccessException e){
            response.put("mensaje", "Error al realizar la consulta a la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("empleados", empelados);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }


    /*@GetMapping("/{id}")
    public EmpleadoEntity findById(@PathVariable Long id){
        return empleadoService.findById(id);
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        EmpleadoEntity empleadoDB = null;

        Map<String, Object> response = new HashMap<>();

        try {
            empleadoDB = empleadoService.findById(id);
            if(empleadoDB == null){
                response.put("mensaje","El empleadoDB con id: "+ id + " no se encuentra en la base de datos");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND );
            }

        }catch (DataAccessException e){

            response.put("Mensaje", "Error al realizar una consulta en la base de datos");
            response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return  new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        response.put("Empleados", empleadoDB);
        return  new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?> create( @Valid @RequestBody EmpleadoDto empleado, BindingResult result ){

        EmpleadoEntity empleadoDB = null;
        Map<String, Object>  response= new HashMap<>();

        if(result.hasErrors()){
            List<String>  erros = result.getFieldErrors()
                    .stream()
                    .map(err -> {
                        return "El campo '" + err.getField() + "' " + err.getDefaultMessage();
                    })
                    .collect(Collectors.toList());

            response.put("errors", erros);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        try{
            EmpresaEntity empresa = empresaService.findById(empleado.getEmpresa_id());
            if(empresa == null){
                response.put("mensaje", "La empresa con id: " + empleado.getEmpresa_id()  + " no existe.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
            }
            /*EmpleadoEntity empleadoConEmail = empleadoService.findByEmail(empleado.getEmail());
            if(empleadoConEmail != null){
                response.put("mensaje", "El email: " + empleado.getEmail() + " existe");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
            }*/
            // Refactorizacion
            boolean existEmail = empleadoService.existsByEmail(empleado.getEmail());
            if(existEmail){
                response.put("mensaje", "El email: " + empleado.getEmail() + " existe");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
            }


            EmpleadoEntity empleadoNew = new EmpleadoEntity(empleado.getNombre(), empleado.getApellido(),
                    empleado.getEmail(), empresa);
            empleadoDB = empleadoService.save(empleadoNew);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El empleado se a creado con exito");
        response.put("empleado", empleadoDB);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@Valid @RequestBody EmpleadoDto empleado, BindingResult result,
                                  @PathVariable Long id ){
        EmpleadoEntity empleadoDB = null;
        EmpleadoEntity empleadoCurrent;
        Map<String, Object>  response= new HashMap<>();

        if(result.hasErrors()){
            List<String>  erros = result.getFieldErrors()
                    .stream()
                    .map(err -> {
                        return "El campo '" + err.getField() + "' " + err.getDefaultMessage();
                    })
                    .collect(Collectors.toList());

            response.put("errors", erros);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            EmpresaEntity empresa = empresaService.findById(empleado.getEmpresa_id());
            if(empresa == null){
                response.put("mensaje", "La empresa con id: " + empleado.getEmpresa_id() + " no existe");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }

            empleadoDB = empleadoService.findById(id);
            if(empleadoDB == null){
                response.put("mensaje", "El empleado con id: " + id + " no existe");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }

            if(!empleadoDB.getEmail().equals(empleado.getEmail())){
                response.put("mensaje", "El email no coincide con el existente");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
            }

            empleadoCurrent = new EmpleadoEntity(empleado.getNombre(), empleado.getApellido(),
                    empleado.getEmail(), empresa);
            empleadoDB.setNombre(empleadoCurrent.getNombre());
            empleadoDB.setApellido(empleadoCurrent.getApellido());
            empleadoDB.setEmail(empleadoCurrent.getEmail());
            empleadoDB.setEmpresa(empleadoCurrent.getEmpresa());

            empleadoDB = empleadoService.save(empleadoDB);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("empleado", empleadoDB);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        empleadoService.deleteById(id);
    }
}



