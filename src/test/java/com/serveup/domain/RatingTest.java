package com.serveup.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.serveup.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RatingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rating.class);
        Rating rating1 = new Rating();
        rating1.setId(UUID.randomUUID());
        Rating rating2 = new Rating();
        rating2.setId(rating1.getId());
        assertThat(rating1).isEqualTo(rating2);
        rating2.setId(UUID.randomUUID());
        assertThat(rating1).isNotEqualTo(rating2);
        rating1.setId(null);
        assertThat(rating1).isNotEqualTo(rating2);
    }
}
