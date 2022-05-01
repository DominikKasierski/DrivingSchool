package pl.lodz.p.it.dk.mos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.dk.security.etag.EntityToSign;

import java.util.Set;

//TODO: Dorobić Paymenty i Wykłady. Może zamiast courseDetailsId to kategorię kursu?

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto implements EntityToSign {

    private Long id;
    private Long accountId;
    private Long courseDetailsId;
    private Long lectureGroupId;
    private Set<PaymentDto> payments;
    private boolean paid;
    private boolean lecturesCompletion;
    private boolean courseCompletion;
    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%d", version);
    }
}
