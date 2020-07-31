package tz.or.mkapafoundation.whistleblower.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import tz.or.mkapafoundation.whistleblower.web.rest.TestUtil;

public class WitnessDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WitnessDTO.class);
        WitnessDTO witnessDTO1 = new WitnessDTO();
        witnessDTO1.setId(1L);
        WitnessDTO witnessDTO2 = new WitnessDTO();
        assertThat(witnessDTO1).isNotEqualTo(witnessDTO2);
        witnessDTO2.setId(witnessDTO1.getId());
        assertThat(witnessDTO1).isEqualTo(witnessDTO2);
        witnessDTO2.setId(2L);
        assertThat(witnessDTO1).isNotEqualTo(witnessDTO2);
        witnessDTO1.setId(null);
        assertThat(witnessDTO1).isNotEqualTo(witnessDTO2);
    }
}
