package api.com.hub.domain.respuesta;

import api.com.hub.domain.topico.Topico;
import api.com.hub.domain.usuario.Usuario;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import java.time.LocalDateTime;

@Entity(name="Respuesta")
@Table(name = "respuestas")
@Getter
@Setter
@NoArgsConstructor
public class Respuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime creationDate;
    private String solution;
    @OneToOne
    @JoinColumn(name="author", referencedColumnName="id")
    private Usuario author;
    @OneToOne
    @JoinColumn(name="topico", referencedColumnName="id")
    private Topico topico;
    private boolean active;

    public Respuesta(Long id, String solution, Usuario usuario, Topico topico, LocalDateTime creationDate) {
        this.id=id;
        this.solution=solution;
        this.author=usuario;
        this.topico=topico;
        this.creationDate=LocalDateTime.now();
    }

    public void respuestaActualizada(RespuestaActualizadaDTO respuestaActualizadaDTO) {
        if (respuestaActualizadaDTO.solution() != null){
            this.solution=respuestaActualizadaDTO.solution();
        }
    }
    public void diactivateResponse(){

        this.active=false;
    }
}