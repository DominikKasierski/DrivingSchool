package pl.lodz.p.it.dk.common.utils;

import lombok.extern.java.Log;
import pl.lodz.p.it.dk.exceptions.BaseException;
import pl.lodz.p.it.dk.mok.managers.ScheduledTasksManager;

import javax.annotation.security.RunAs;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timer;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Log
@Startup
@Singleton
@RunAs("System")
@Interceptors({LoggingInterceptor.class})
public class ScheduledTaskRunner {

    @Inject
    private ScheduledTasksManager scheduledTasksManager;

    @Schedule(hour = "*", minute = "0", second = "0",
            info = "Sprawdza czy w bazie danych znajdują się niezweryfikowane konta do usunięcia. " +
                    "Metoda wykonywana co godzinę począwszy od pełjnej godziny.", persistent = false)
    private void deleteUnverifiedAccounts(Timer time) throws BaseException {
        scheduledTasksManager.deleteUnverifiedAccounts(time);
    }
}
