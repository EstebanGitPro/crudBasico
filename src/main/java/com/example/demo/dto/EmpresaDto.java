package com.example.demo.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EmpresaDto {


    @NotNull
    @NotBlank(message = "no puede estar vacio")
    private String nombre;
}
