package tz.or.mkapafoundation.whistleblower.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class WitnessMapperTest {

    private WitnessMapper witnessMapper;

    @BeforeEach
    public void setUp() {
        witnessMapper = new WitnessMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(witnessMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(witnessMapper.fromId(null)).isNull();
    }
}
