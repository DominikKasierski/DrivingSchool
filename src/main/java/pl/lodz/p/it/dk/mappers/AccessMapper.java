package pl.lodz.p.it.dk.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.lodz.p.it.dk.entities.Access;
import pl.lodz.p.it.dk.entities.Account;
import pl.lodz.p.it.dk.mok.dtos.AccessDto;
import pl.lodz.p.it.dk.mok.dtos.AccessesDto;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface AccessMapper {

    //TODO: Sprawdzic czy dziala tak samo bez Mappings

    @Mappings({
            @Mapping(target = "accessType", source = "accessType"),
            @Mapping(target = "version", source = "version")
    })
    AccessDto toAccessDto(Access access);

    /**
     * Przeprowadza proces mapowania z klasy Account na klasę RolesDto
     *
     * @param account obiekt klasy Account
     * @return obiekt klasy RolesDto
     */
    default AccessesDto toAccessesDto(Account account) {
        List<AccessDto> accessesGranted = account.getAccesses().stream()
                .filter(Access::isActivated)
                .map(this::toAccessDto)
                .collect(Collectors.toList());
        List<AccessDto> accessesRevoked = account.getAccesses().stream()
                .filter(x -> !x.isActivated())
                .map(this::toAccessDto)
                .collect(Collectors.toList());
        return new AccessesDto(account.getId(), accessesGranted, accessesRevoked);
    }
}
