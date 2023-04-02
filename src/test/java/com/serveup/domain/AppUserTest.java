package com.serveup.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.serveup.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AppUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppUser.class);
        AppUser appUser1 = new AppUser();
        appUser1.setId(UUID.randomUUID());
        AppUser appUser2 = new AppUser();
        appUser2.setId(appUser1.getId());
        assertThat(appUser1).isEqualTo(appUser2);
        appUser2.setId(UUID.randomUUID());
        assertThat(appUser1).isNotEqualTo(appUser2);
        appUser1.setId(null);
        assertThat(appUser1).isNotEqualTo(appUser2);
    }
}
