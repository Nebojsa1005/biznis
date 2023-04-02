package com.serveup.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.serveup.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JobEmployeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobEmployee.class);
        JobEmployee jobEmployee1 = new JobEmployee();
        jobEmployee1.setId(1L);
        JobEmployee jobEmployee2 = new JobEmployee();
        jobEmployee2.setId(jobEmployee1.getId());
        assertThat(jobEmployee1).isEqualTo(jobEmployee2);
        jobEmployee2.setId(2L);
        assertThat(jobEmployee1).isNotEqualTo(jobEmployee2);
        jobEmployee1.setId(null);
        assertThat(jobEmployee1).isNotEqualTo(jobEmployee2);
    }
}
