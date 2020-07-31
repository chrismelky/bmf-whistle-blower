package tz.or.mkapafoundation.whistleblower.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ComplainMapperTest {

    private ComplainMapper complainMapper;

    @BeforeEach
    public void setUp() {
        complainMapper = new ComplainMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(complainMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(complainMapper.fromId(null)).isNull();
    }
}
