package pl.lodz.p.it.dk.common.interfaces;

import pl.lodz.p.it.dk.exceptions.BaseException;

@FunctionalInterface
public interface VoidMethodExecutor {
    void execute() throws BaseException;
}
