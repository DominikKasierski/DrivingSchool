package pl.lodz.p.it.dk.common.interfaces;

import pl.lodz.p.it.dk.exceptions.BaseException;

@FunctionalInterface
public interface ReturnMethodExecutor<T> {
    T execute() throws BaseException;
}
