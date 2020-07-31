package tz.or.mkapafoundation.whistleblower.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SuspectMapperTest {

    private SuspectMapper suspectMapper;

    @BeforeEach
    public void setUp() {
        suspectMapper = new SuspectMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(suspectMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(suspectMapper.fromId(null)).isNull();
    }
}
