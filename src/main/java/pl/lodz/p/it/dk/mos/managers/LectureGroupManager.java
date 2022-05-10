package pl.lodz.p.it.dk.mos.managers;

import pl.lodz.p.it.dk.common.utils.LoggingInterceptor;
import pl.lodz.p.it.dk.entities.LectureGroup;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mos.facades.LectureGroupFacade;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class LectureGroupManager {

    @Inject
    AccountManager accountManager;

    @Inject
    LectureGroupFacade lectureGroupFacade;

    @RolesAllowed("createLectureGroup")
    public void createLectureGroup(LectureGroup lectureGroup, String login) throws BaseException {
        lectureGroup.setCreatedBy(accountManager.findByLogin(login));
        lectureGroupFacade.create(lectureGroup);
    }
}
