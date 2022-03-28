package com.nahuel.Tinder.repositorios;

import com.nahuel.Tinder.entidades.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonaRepositorio extends JpaRepository<Zona, String> { // clave primaria String
    
}
