package api.com.hub.controller;

import api.com.hub.domain.respuesta.RespuestaDTO;
import api.com.hub.domain.respuesta.RespuestaActualizadaDTO;
import api.com.hub.domain.respuesta.RespuestaRepository;
import api.com.hub.domain.respuesta.Respuesta;
import api.com.hub.domain.respuesta.ListarRespuestasDTO;
import api.com.hub.domain.respuesta.RespuestaService;
import api.com.hub.domain.respuesta.RespuestaCreadaDTO;
import api.com.hub.domain.topico.TopicoRepository;
import api.com.hub.domain.usuario.UsuarioRepository;
import api.com.hub.infra.errors.ValidacionDeIntegridad;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
@RequestMapping("/respuesta")

public class RespuestaController {
    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RespuestaService respuestaService;
    @Autowired
    private RespuestaRepository repository;

  
    @PostMapping
    public ResponseEntity respuestaRegistrada (@RequestBody @Valid RespuestaDTO respuestaDTO) throws ValidacionDeIntegridad {
        var respuestaRegistrada = respuestaService.respuestaCreadaDTO(respuestaDTO);
        return ResponseEntity.ok(respuestaRegistrada);
    }


    @GetMapping("/respuestas")
    public ResponseEntity<Page<ListarRespuestasDTO>>  listarRespuestas(@PageableDefault(size = 10) Pageable paged){
        return ResponseEntity.ok(repository.findByActiveTrue(paged).map(ListarRespuestasDTO::new));
    }

    
    @PutMapping("/{id}")
    public ResponseEntity respuestaActualizada(@RequestBody @Valid RespuestaActualizadaDTO respuestaActualizadaDTO){
        Respuesta respuesta=repository.getReferenceById(respuestaActualizadaDTO.id());
        respuesta.respuestaActualizada(respuestaActualizadaDTO);
        return ResponseEntity.ok(new RespuestaCreadaDTO(respuesta.getId(),respuesta.getSolution(),
                respuesta.getAuthor().getId(),
                respuesta.getTopico().getId(),
                respuesta.getCreationDate()));
    }

    @DeleteMapping("/{id}")

    public ResponseEntity eliminarRespuesta(@PathVariable Long id){
        Respuesta respuesta = repository.getReferenceById(id);
        respuesta.diactivateResponse();
        return ResponseEntity.noContent().build();
    }

  
    @GetMapping("/{id}")
    public ResponseEntity <RespuestaCreadaDTO> respuestaCreada(@PathVariable Long id){
        Respuesta respuesta=repository.getReferenceById(id);
        var respuestaRegistrada = new RespuestaCreadaDTO(respuesta.getId(),
                respuesta.getSolution(),
                respuesta.getAuthor().getId(),
                respuesta.getTopico().getId(),
                respuesta.getCreationDate());
        return ResponseEntity.ok(respuestaRegistrada);
    }
}