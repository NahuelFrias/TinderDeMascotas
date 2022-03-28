package com.nahuel.Tinder.repositorios;

import com.nahuel.Tinder.entidades.Mascota;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MascotaRepositorio extends JpaRepository<Mascota, String>{ //tipo Mascota, clave primaria String
    
    @Query("SELECT c FROM Mascota c WHERE c.usario.id = :id")
    public List<Mascota> buscarMascotaPorUsuario (@Param("id") String id);
}
