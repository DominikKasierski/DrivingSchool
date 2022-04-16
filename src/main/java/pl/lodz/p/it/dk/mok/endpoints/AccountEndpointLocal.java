package pl.lodz.p.it.dk.mok.endpoints;

import pl.lodz.p.it.dk.common.interfaces.TransactionStarter;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mok.dtos.AccountDto;
import pl.lodz.p.it.dk.mok.dtos.NewEmailDto;
import pl.lodz.p.it.dk.mok.dtos.PersonalDataDto;
import pl.lodz.p.it.dk.mok.dtos.RegisterAccountDto;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

@Local
public interface AccountEndpointLocal extends TransactionStarter {

    @PermitAll
    void registerAccount(RegisterAccountDto registerAccountDto) throws BaseException;

    @PermitAll
    void confirmAccount(String code) throws BaseException;

    @PermitAll
    void updateAuthInfo(String login, String language) throws BaseException;

    @PermitAll
    void updateAuthInfo(String login) throws BaseException;

    @RolesAllowed("lockAccount")
    void lockAccount(String login) throws BaseException;

    @RolesAllowed("unlockAccount")
    void unlockAccount(String login) throws BaseException;

    @RolesAllowed("getOwnAccountDetails")
    AccountDto getOwnAccountDetails() throws BaseException;

    @RolesAllowed("getOtherAccountDetails")
    AccountDto getOtherAccountDetails(String login) throws BaseException;

    @RolesAllowed("getAllAccounts")
    List<AccountDto> getAllAccounts() throws BaseException;

    @RolesAllowed("editPersonalData")
    void editPersonalData(PersonalDataDto accountPersonalDetailsDto) throws BaseException;

    @RolesAllowed("editOwnEmail")
    void editOwnEmail(NewEmailDto newEmailDto) throws BaseException;

    @RolesAllowed("editOtherEmail")
    void editOtherEmail(String login, NewEmailDto newEmailDto) throws BaseException;

    @PermitAll
    void confirmEmail(String code) throws BaseException;
}
