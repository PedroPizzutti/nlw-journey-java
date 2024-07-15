package com.rocketseat.planner.participant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/participants")
public class ParticipantController {
    @Autowired
    private ParticipantRepository repository;

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID id) throws Exception{
        Optional<Participant> participant = this.repository.findById(id);
        if(participant.isEmpty()) {
            throw new Exception("Participante não encontrado!");
        }
        Participant rawParticipant = participant.get();
        rawParticipant.setIs_confirmed(true);
        this.repository.save(rawParticipant);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
