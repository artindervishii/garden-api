package com.garden.api.reviews;

import jakarta.validation.constraints.NotNull;

public record ReviewStatusUpdateRequest(
        @NotNull ReviewStatus status
) {}
