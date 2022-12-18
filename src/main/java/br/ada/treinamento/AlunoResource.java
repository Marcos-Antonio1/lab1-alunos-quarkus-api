package br.ada.treinamento;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import br.ada.treinamento.dto.AlunoDto;

@Path("/alunos")
public class AlunoResource {
    
    
    private final Map <Long,AlunoDto> alunos = new HashMap<>();
    
    private static final Logger log = LoggerFactory.getLogger(AlunoResource.class);


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response cadastrarAluno(AlunoDto alunoDto){
        log.info("cadastrando aluno {} ",alunoDto);
        if(Objects.isNull(alunoDto)){
            return Response.status(Response.Status.BAD_REQUEST)
            .build();
        }

        if(alunoDto.getNome().isEmpty() ||  alunoDto.getId() <= 0){
            return Response.status(Response.Status.BAD_REQUEST)
            .build();
        }

        alunos.put(alunoDto.getId(), alunoDto);

        return Response.status(Response.Status.CREATED)
        .build();

    }

    @GET
    public Response bucarPorPrefixo(@QueryParam("prefixo") String prefixo){
        
        List<AlunoDto> alunosResposta;

        if(!Objects.isNull(prefixo)){
            log.info("Buscando aluno por prefixo {}", prefixo);
            alunosResposta = alunos.values().stream().filter(a -> a.getNome().startsWith(prefixo)).toList();
            return Response.status(Response.Status.OK).entity(alunosResposta).build();    
        }

        log.info("Listanto Alunos");
        alunosResposta = alunos.values().stream().toList();
        return Response.status(Response.Status.OK).entity(alunosResposta).build();
        
    }

    @GET
    @Path("/{id}")
    public Response buscarAlunoPorId(@PathParam("id") long id){
        log.info("Buscando aluno {}", id);
        if(Objects.isNull(id)){
            return Response.status(Response.Status.BAD_REQUEST)
            .build();
        }

        if(!alunos.containsKey(id)){
            return Response.status(Response.Status.NOT_FOUND).build();
        }


        return Response.status(Response.Status.OK).entity(alunos.get(id)).build();

    }

    @PUT
    @Path("/{id}")
    public Response alterarAluno(@PathParam("id") long id, AlunoDto alunoDto){
        log.info("Atualizando aluno {} ",id);
        var aluno = alunos.get(id);
        if(Objects.isNull(aluno)){
            return Response
            .status(Response.Status.NOT_FOUND)
            .build();
        }
        
        alunos.put(id, alunoDto);
        return Response.ok(alunoDto).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletarAluno(@PathParam("id") long id){
        log.info("Deletando aluno {} ",id);
        var aluno = alunos.get(id);
        if(Objects.isNull(aluno)){
            return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }

        alunos.remove(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
