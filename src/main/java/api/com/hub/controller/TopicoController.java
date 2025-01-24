package api.com.hub.controller;

import api.com.hub.domain.topico.RespuestaTopicoDTO;
import api.com.hub.domain.topico.TopicoService;
import api.com.hub.domain.topico.Topico;
import api.com.hub.domain.topico.TopicoDTO;
import api.com.hub.domain.topico.TopicoRepository;
import api.com.hub.domain.topico.ListarTopicosDTO;
import api.com.hub.domain.topico.TopicoActualizadoDTO;
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
@RequestMapping("/topico")
public class TopicoController {
    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TopicoService topicoService;

    @PostMapping("/topico")
    public ResponseEntity topicoRegistrado(@RequestBody @Valid TopicoDTO topicoDTO) throws ValidacionDeIntegridad {
        var topicoRegistrado = topicoService.topicoCreado(topicoDTO);
        return ResponseEntity.ok(topicoRegistrado);
    }

    
    @GetMapping("/topicos")
    public ResponseEntity<Page<ListarTopicosDTO>>  listarTopicos(@PageableDefault(size = 10) Pageable paged){
        return ResponseEntity.ok(topicoRepository.findByActiveTrue(paged).map(ListarTopicosDTO::new));
    }

  
    @PutMapping("/{id}")
    public ResponseEntity topicoActualizado (@RequestBody @Valid TopicoActualizadoDTO topicoActualizadoDTO){
        Topico topico= topicoRepository.getReferenceById(topicoActualizadoDTO.id());
        topico.topicoActualizado(topicoActualizadoDTO);
        return ResponseEntity.ok(new RespuestaTopicoDTO(topico.getId(),
                topico.getTitle(),topico.getMessage(),
                topico.getStatus(),topico.getAuthor().getId(),
                topico.getCourse(),topico.getDate()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminarTopico(@PathVariable Long id){
        Topico topico= topicoRepository.getReferenceById(id);
        topico.diactivateTopic();
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity <RespuestaTopicoDTO> respuestaTopico(@PathVariable Long id){
        Topico topico = topicoRepository.getReferenceById(id);
        var topicoId = new RespuestaTopicoDTO(topico.getId(),
                topico.getTitle(),
                topico.getMessage(),
                topico.getStatus(),
                topico.getAuthor().getId(),
                topico.getCourse(),
                topico.getDate());
        return ResponseEntity.ok(topicoId);
    }
}