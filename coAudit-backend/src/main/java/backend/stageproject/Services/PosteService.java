package backend.stageproject.Services;

import backend.stageproject.Dto.PosteDto;
import backend.stageproject.Entity.Data;
import backend.stageproject.Entity.Poste;
import backend.stageproject.Repository.PosteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PosteService {
    @Autowired
    private PosteRepository posteRepository;

    public Poste findOrCreatePoste(PosteDto posteDto) {
        return posteRepository.findByDesignation(posteDto.getDesignation())
                .orElseGet(() -> {
                    Poste newPoste = new Poste();
                    newPoste.setDesignation(posteDto.getDesignation());
                    generateCode(newPoste);
                    return posteRepository.save(newPoste);
                });
    }

    public Poste updatePoste(UUID id, PosteDto posteDto) {
        Poste existingPoste = posteRepository.findById(id)
                .orElseThrow(() ->new IllegalArgumentException("Poste not found with id " + id));

        existingPoste.setDesignation(posteDto.getDesignation());

        return posteRepository.save(existingPoste);
    }

    public List<Poste> getAllPoste() {
        return posteRepository.findAll();
    }

    public boolean deletePoste(UUID id) {
        boolean exist=posteRepository.existsById(id);

        if (exist){
           posteRepository.deleteById(id);
        }else {
            throw new IllegalStateException(
                    "Poste with id "+id+" does not exist "
            );
        };
        return exist;
    }


    public void generateCode(Poste poste) {

        String baseCode = "P-";

        Poste lastPoste = posteRepository.findTopByCodeStartingWithOrderByCodeDesc(baseCode);
        String lastCode = lastPoste != null ? lastPoste.getCode() : baseCode + "0000";

        // Extract the last number from the code
        String lastCodeNumberStr = lastCode.substring(baseCode.length());
        int lastCodeNumber = Integer.parseInt(lastCodeNumberStr);
        int newCodeNumber = lastCodeNumber + 1;

        // Generate the new code
        String newCode = baseCode + String.format("%04d", newCodeNumber);
        poste.setCode(newCode);
    }
}
