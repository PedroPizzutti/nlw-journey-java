package com.rocketseat.planner.trip;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private LocalDateTime starts_at;

    @Column(nullable = false)
    private LocalDateTime ends_at;

    @Column(nullable = false)
    private Boolean is_confirmed;

    @Column(nullable = false)
    private String owner_name;

    @Column(nullable = false)
    private String owner_email;

    public Trip(TripRequestPayload data){
        this.destination = data.destination();
        this.is_confirmed = false;
        this.owner_email = data.owner_email();
        this.owner_name = data.owner_name();
        this.starts_at = LocalDateTime.parse(data.starts_at(), DateTimeFormatter.ISO_DATE_TIME);
        this.ends_at =  LocalDateTime.parse(data.ends_at(), DateTimeFormatter.ISO_DATE_TIME);
    }
}
