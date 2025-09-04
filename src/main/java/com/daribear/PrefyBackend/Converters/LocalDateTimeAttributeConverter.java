package com.daribear.PrefyBackend.Converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Converter which converts from sql timestamps to localdatetimes and vice versa.
 * Since I'm using autoApply = true, it automatically converts for me.
 */
@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

    /**
     * Converts a local data timestamp into the sql timestamp which can be used in the database.
     * @param locDateTime the localdatatime to be converted
     * @return the new sql timestamp
     */
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime locDateTime) {
        return (locDateTime == null ? null : java.sql.Timestamp.valueOf(locDateTime));
    }

    /**
     * Converts the sql timestamp into the localdatetime which can be used in java or the application.
     * @param sqlTimestamp the timestamp to be converted
     * @return the localdatetime
     */
    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
        return (sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime());
    }
}