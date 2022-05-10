package pl.lodz.p.it.dk.mos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.dk.entities.enums.CourseCategory;
import pl.lodz.p.it.dk.validation.annotations.ValueOfEnum;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewLectureGroupDto {

    @NotNull
    private String name;

    @NotNull
    @ValueOfEnum(enumClass = CourseCategory.class)
    private String courseCategory;

}
