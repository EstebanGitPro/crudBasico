package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EmpleadoDto {

    @NotNull
    @NotBlank
    private String nombre;


    @NotNull
    @NotBlank(message = "no puede estar vacio")
    private String apellido;

    @NotNull
    @NotBlank
    @Email
    private String email;


    @NotNull
    private Long empresa_id;





}
