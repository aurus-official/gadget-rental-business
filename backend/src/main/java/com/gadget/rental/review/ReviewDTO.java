package com.gadget.rental.review;

import java.util.*;

import jakarta.validation.constraints.NotEmpty;

public record ReviewDTO(
        @NotEmpty String review,
        @NotEmpty String profileName,
        @NotEmpty double rate,
        @NotEmpty String location) {

}
