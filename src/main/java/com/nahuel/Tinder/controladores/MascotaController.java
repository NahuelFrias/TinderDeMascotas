package com.nahuel.Tinder.controladores;

import com.nahuel.Tinder.entidades.Mascota;
import com.nahuel.Tinder.entidades.Usuario;
import com.nahuel.Tinder.enumeraciones.Sexo;
import com.nahuel.Tinder.enumeraciones.Tipo;
import com.nahuel.Tinder.errores.ErrorServicio;
import com.nahuel.Tinder.servicios.MascotaServices;
import com.nahuel.Tinder.servicios.UsuarioServices;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
@Controller
@RequestMapping("mascota/")
public class MascotaController {

    @Autowired
    private UsuarioServices usuarioServicio;
    @Autowired
    private MascotaServices mascotaServicio;

    @GetMapping("/mis-mascotas")
    public String misMascotas(HttpSession session,
            @RequestParam(required = false) String id,
            ModelMap model) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/login";
        }
        List<Mascota> mascotas = mascotaServicio.buscarMascotasPorUsuario(login.getId());
        model.put("mascotas", mascotas);
        return "mascotas";
    }

    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String accion,
            ModelMap model) {

        if (accion == null) { // segun la accion modifico o creo
            accion = "Crear";
        }

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/login";
        }

        Mascota mascota = new Mascota();
        if (id != null && !id.isEmpty()) {
            try {
                mascota = mascotaServicio.buscarPorId(id);
            } catch (ErrorServicio ex) {
                Logger.getLogger(MascotaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        model.put("perfil", mascota);
        model.put("accion", accion);
        model.put("sexos", Sexo.values()); // me devuelve un array con todos los sexos
        model.put("tipos", Tipo.values()); // me devuelve un array con todos los tipos

        return "mascota.html";
    }

    @PostMapping("/actualizar-perfil")
    public String actualizar(ModelMap model,
            HttpSession session,
            MultipartFile archivo,
            @RequestParam @Nullable String id,
            @RequestParam @Nullable String nombre,
            @RequestParam @Nullable Tipo tipo,
            @RequestParam @Nullable Sexo sexo
    ) throws ErrorServicio {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/login";
        }
        try {
            if (id == null || id.isEmpty()) {
                mascotaServicio.agregarMascota(archivo, login.getId(), nombre, sexo, tipo);
            } else {
                mascotaServicio.modificar(archivo, login.getId(), id, nombre, sexo, tipo);
            }
            return "redirect:/inicio";
        } catch (ErrorServicio ex) {
            Mascota mascota = new Mascota();
            mascota.setId(id);
            mascota.setNombre(nombre);
            mascota.setSexo(sexo);
            mascota.setTipo(tipo);

            model.put("accion", "Actualizar");
            model.put("sexos", Sexo.values());
            model.put("tipos", Tipo.values());
            model.put("error", ex.getMessage());
            model.put("perfil", login);
            return "mascota.html";
        }
    }

    @PostMapping("/eliminar-perfil")
    public String eliminar(HttpSession session, @RequestParam String id) {
        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            mascotaServicio.eliminar(login.getId(), id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(MascotaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/mascotas/mis-mascotas";
    }
}
