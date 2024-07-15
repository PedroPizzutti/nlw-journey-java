package com.rocketseat.planner.activity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityData(UUID id, String title, LocalDateTime occurs_at) {
}
