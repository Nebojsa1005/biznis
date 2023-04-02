package com.serveup.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.serveup.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CommentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Comment.class);
        Comment comment1 = new Comment();
        comment1.setId(UUID.randomUUID());
        Comment comment2 = new Comment();
        comment2.setId(comment1.getId());
        assertThat(comment1).isEqualTo(comment2);
        comment2.setId(UUID.randomUUID());
        assertThat(comment1).isNotEqualTo(comment2);
        comment1.setId(null);
        assertThat(comment1).isNotEqualTo(comment2);
    }
}
