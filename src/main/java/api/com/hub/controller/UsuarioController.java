package api.com.hub.controller;

import api.com.hub.domain.usuario.Usuario;
import api.com.hub.domain.usuario.ActualizacionUsuarioDTO;
import api.com.hub.domain.usuario.RespuestaUsuarioDTO;
import api.com.hub.domain.usuario.UsuarioRepository;
import api.com.hub.domain.usuario.ListarUsuariosDTO;
import api.com.hub.domain.usuario.RegistroUsuarioDTO;
import api.com.hub.domain.usuario.UsuarioService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/usuarios")
    public ResponseEntity<Page<ListarUsuariosDTO>> listarUsuarios(@PageableDefault(size = 10) Pageable paged) {
        return ResponseEntity.ok(usuarioRepository.findByActiveTrue(paged).map(ListarUsuariosDTO::new));
    }

    @PutMapping("/{id}")
    public ResponseEntity actualizacionUsuario(@RequestBody @Valid ActualizacionUsuarioDTO actualizacionUsuarioDTO) {
        Usuario usuario = usuarioRepository.getReferenceById(actualizacionUsuarioDTO.id());
        usuario.actualizacionUsuario(actualizacionUsuarioDTO);
        return ResponseEntity.ok(new ActualizacionUsuarioDTO(usuario.getId(), usuario.getName(), usuario.getEmail()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminarUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.getReferenceById(id);
        usuario.deactivateUser();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaUsuarioDTO> registrarUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.getReferenceById(id);
        var usuarioDetail = new RespuestaUsuarioDTO(usuario.getId(), usuario.getName());
        return ResponseEntity.ok(usuarioDetail);
    }


    @RequestMapping("/registro")
    public class RegistroController {

        @Autowired
        private UsuarioRepository usuarioRepository;
        @Autowired
        private BCryptPasswordEncoder bCryptPasswordEncoder;

        @Autowired
        private UsuarioService usuarioService;

        @PostMapping

        public ResponseEntity<?> registrarUsuario(@RequestBody @Valid RegistroUsuarioDTO registroUsuarioDTO, UriComponentsBuilder uriComponentsBuilder) {
            try {
                RegistroUsuarioDTO usuario = usuarioService.registrarUsuario(registroUsuarioDTO);
                RespuestaUsuarioDTO respuestaUsuarioDTO;
                respuestaUsuarioDTO = new RespuestaUsuarioDTO(usuario.getId(), usuario.getName());
                URI url = uriComponentsBuilder.path("usuario/{id}").buildAndExpand(usuario.getId()).toUri();
                return ResponseEntity.created(url).body(respuestaUsuarioDTO);
            } catch (ConstraintViolationException ex) {
                return ResponseEntity.badRequest().body("Validation failed: " + ex.getMessage());
            }

        }

    }
}
