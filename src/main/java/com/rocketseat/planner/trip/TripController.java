package com.rocketseat.planner.trip;

import com.rocketseat.planner.activity.*;
import com.rocketseat.planner.link.*;
import com.rocketseat.planner.participant.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {
    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private TripRepository repository;

    @PostMapping
    public ResponseEntity<TripResponse> createTrip(@RequestBody TripRequestPayload payload) {
        Trip newTrip = new Trip(payload);
        this.repository.save(newTrip);
        this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TripResponse(newTrip.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) throws Exception {
        Optional<Trip> trip = this.repository.findById(id);
        if(trip.isEmpty()) {
            throw new Exception("Viagem não encontrada!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(trip.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTrip(@PathVariable UUID id,
                                           @RequestBody TripRequestPayload payload) throws Exception {
        Optional<Trip> trip = this.repository.findById(id);
        if(trip.isEmpty()) {
            throw new Exception("Viagem não encontrada!");
        }
        Trip rawTrip = trip.get();
        rawTrip.setEnds_at(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
        rawTrip.setStarts_at(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
        rawTrip.setDestination(payload.destination());
        this.repository.save(rawTrip);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmTrip(@PathVariable UUID id,
                                            @RequestBody TripRequestPayload payload) throws Exception{
        Optional<Trip> trip = this.repository.findById(id);
        if(trip.isEmpty()) {
            throw new Exception("Viagem não encontrada!");
        }
        Trip rawTrip = trip.get();
        rawTrip.setIs_confirmed(true);
        this.repository.save(rawTrip);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID id,
                                                             @RequestBody ActivityRequestPayload payload) throws Exception {
        Optional<Trip> trip = this.repository.findById(id);
        if(trip.isEmpty()) {
            throw new Exception("Viagem não encontrada!");
        }
        Trip rawTrip = trip.get();
        ActivityResponse activityResponse = this.activityService.registerActivity(payload, rawTrip);
        return ResponseEntity.status(HttpStatus.CREATED).body(activityResponse);
    }

    @GetMapping("{id}/activities")
    public ResponseEntity<ActivityResponseData> getAllActivities(@PathVariable UUID id) {
        List<ActivityData> activityList = this.activityService.getAllActivitiesFromEvent(id);
        ActivityResponseData activityResponseData = new ActivityResponseData(activityList);
        return ResponseEntity.status(HttpStatus.OK).body(activityResponseData);
    }

    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id,
                                                     @RequestBody LinkRequestPayload payload) throws Exception {
        Optional<Trip> trip = this.repository.findById(id);
        if(trip.isEmpty()) {
            throw new Exception("Viagem não encontrada!");
        }
        Trip rawTrip = trip.get();
        LinkResponse linkResponse = this.linkService.registerLink(payload, rawTrip);
        return ResponseEntity.status(HttpStatus.CREATED).body(linkResponse);
    }

    @GetMapping("{id}/links")
    public ResponseEntity<LinkResponseData> getAllLinks(@PathVariable UUID id) {
        List<LinkData> linkList = this.linkService.getAllLinksFromEvent(id);
        LinkResponseData linkResponseData = new LinkResponseData(linkList);
        return ResponseEntity.status(HttpStatus.OK).body(linkResponseData);
    }

    @PostMapping("/{id}/invites")
    public ResponseEntity<Void> inviteParticipant(@PathVariable UUID id,
                                                  @RequestBody ParticipantRequestPayload payload) throws Exception {
        Optional<Trip> trip = this.repository.findById(id);
        if(trip.isEmpty()) {
            throw new Exception("Viagem não encontrada!");
        }
        Trip rawTrip = trip.get();
        this.participantService.registerParticipantToEvent(payload, rawTrip);
        if(rawTrip.getIs_confirmed()) {
            this.participantService.triggerConfirmationEmailToParticipant(payload.email());
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("{id}/participants")
    public ResponseEntity<ParticipantResponseData> getAllParticipants(@PathVariable UUID id) {
        List<ParticipantData> participantList = this.participantService.getAllParticipantsFromEvent(id);
        ParticipantResponseData participantResponseData = new ParticipantResponseData(participantList);
        return ResponseEntity.status(HttpStatus.OK).body(participantResponseData);
    }
}
