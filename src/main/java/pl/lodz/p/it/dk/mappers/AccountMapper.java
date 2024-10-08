package pl.lodz.p.it.dk.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.lodz.p.it.dk.entities.Account;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mok.dtos.AccountDto;
import pl.lodz.p.it.dk.mok.dtos.PersonalDataDto;
import pl.lodz.p.it.dk.mok.dtos.RegisterAccountDto;

@Mapper
public interface AccountMapper {

    void toAccount(RegisterAccountDto registerAccountDto, @MappingTarget Account account);

    void toAccount(PersonalDataDto personalDataDto, @MappingTarget Account account) throws BaseException;

    AccountDto toAccountDto(Account account);
}
