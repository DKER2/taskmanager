package com.theawesomeengineer.taskmanager.mappers;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    private final TaskMapper mapper = Mappers.getMapper(TaskMapper.class);

    @Test
    void map_OffsetDateTimeToInstant_ShouldConvertCorrectly() {
        OffsetDateTime odt = OffsetDateTime.of(2024, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        Instant result = mapper.map(odt);

        assertNotNull(result);
        assertEquals(odt.toInstant(), result);
    }

    @Test
    void map_InstantToOffsetDateTime_ShouldConvertCorrectly() {
        Instant instant = Instant.parse("2024-01-01T12:00:00Z");
        OffsetDateTime result = mapper.map(instant);

        assertNotNull(result);
        assertEquals(instant, result.toInstant());
        assertEquals(ZoneOffset.UTC, result.getOffset());
    }

    @Test
    void map_NullInputs_ShouldReturnNull() {
        assertNull(mapper.map((Instant) null));
        assertNull(mapper.map((OffsetDateTime) null));
    }
}
