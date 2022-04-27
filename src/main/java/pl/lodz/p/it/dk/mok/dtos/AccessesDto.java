package pl.lodz.p.it.dk.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.dk.security.etag.EntityToSign;

import java.util.List;
import java.util.stream.Collectors;

//TODO: Zastanowić się czy nie ograniczyć getterów/setterów

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessesDto implements EntityToSign {

    private Long userId;
    private List<SingleAccessDto> accessesGranted;
    private List<SingleAccessDto> accessesRevoked;

    private static String stringify(List<SingleAccessDto> accesses) {
        return accesses.stream()
                .map(x -> x.getAccessType() + ":" + x.getVersion())
                .collect(Collectors.joining("|"));
    }

    @Override
    public String getMessageToSign() {
        return String.format("%d;%s;%s", userId, stringify(accessesGranted), stringify(accessesRevoked));
    }
}

