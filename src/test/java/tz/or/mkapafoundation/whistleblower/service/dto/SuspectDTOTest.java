package tz.or.mkapafoundation.whistleblower.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import tz.or.mkapafoundation.whistleblower.web.rest.TestUtil;

public class SuspectDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SuspectDTO.class);
        SuspectDTO suspectDTO1 = new SuspectDTO();
        suspectDTO1.setId(1L);
        SuspectDTO suspectDTO2 = new SuspectDTO();
        assertThat(suspectDTO1).isNotEqualTo(suspectDTO2);
        suspectDTO2.setId(suspectDTO1.getId());
        assertThat(suspectDTO1).isEqualTo(suspectDTO2);
        suspectDTO2.setId(2L);
        assertThat(suspectDTO1).isNotEqualTo(suspectDTO2);
        suspectDTO1.setId(null);
        assertThat(suspectDTO1).isNotEqualTo(suspectDTO2);
    }
}
