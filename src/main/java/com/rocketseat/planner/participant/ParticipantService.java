package com.rocketseat.planner.participant;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import com.rocketseat.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {
    @Autowired
    private ParticipantRepository repository;

    public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip) {
        List<Participant> participants =
                participantsToInvite
                        .stream()
                        .map(email -> new Participant(email, "", trip))
                        .toList();

        this.repository.saveAll(participants);

        System.out.println(participants.getFirst().getId());
    }

    public void registerParticipantToEvent(ParticipantRequestPayload payload, Trip trip) {
        Participant newParticipant = new Participant(payload.email(), payload.name(), trip);
        this.repository.save(newParticipant);
    }

    public List<ParticipantData> getAllParticipantsFromEvent(UUID tripId) {
        return this.repository
                .findByTripId(tripId)
                .stream()
                .map(participant -> new ParticipantData(
                                            participant.getId(),
                                            participant.getName(),
                                            participant.getEmail(),
                                            participant.getIs_confirmed()
                                        )
                )
                .toList();
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId){};
    public void triggerConfirmationEmailToParticipant(String email){};
}
