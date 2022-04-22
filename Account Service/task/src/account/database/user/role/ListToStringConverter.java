package account.database.user.role;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class ListToStringConverter implements AttributeConverter<List<Role>, String> {
    @Override
    public String convertToDatabaseColumn(List<Role> attribute) {
        return attribute == null ?
                null : attribute.stream().map(Enum::toString).collect(Collectors.joining(","));
    }

    @Override
    public List<Role> convertToEntityAttribute(String dbData) {
        return dbData == null ? Collections.emptyList() :
                Arrays.stream(dbData.split(","))
                        .map(Role::valueOf)
                        .collect(Collectors.toList());
    }
}