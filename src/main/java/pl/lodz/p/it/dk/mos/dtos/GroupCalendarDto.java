package pl.lodz.p.it.dk.mos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupCalendarDto {

    private List<EventDto> mondayEvents;
    private List<EventDto> tuesdayEvents;
    private List<EventDto> wednesdayEvents;
    private List<EventDto> thursdayEvents;
    private List<EventDto> fridayEvents;
}
