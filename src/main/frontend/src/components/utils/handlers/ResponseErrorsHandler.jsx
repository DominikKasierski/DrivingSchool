import React from 'react';
import i18n from "../../../i18n";
import {notificationDuration} from "../constants/Constants";

export const ResponseErrorsHandler = (error, notifier, dispatchViolations = true, callbackAfter = ((x) => {
}), isAuthRequest = false) => {

    if (error.response) {
        let errorHandled = false;
        if (error.response.status === 404) {
            notifier({
                "dialogDuration": notificationDuration.INFINITY,
                "message": i18n.t("error.request.404")
            });
        }
        if (isAuthRequest && error.response.status === 401) {
            notifier({
                "message": i18n.t("error.request.401.login")
            });
            callbackAfter(error);
            return errorHandled;
        }
        if (error.response.status === 401) {
            notifier({
                "message": i18n.t("error.request.401.token")
            });
            callbackAfter(error);
            return errorHandled;
        }

        const responseData = error.response.data;

        if (responseData) {
            if (responseData.message === "error.rest.validation") {
                if (dispatchViolations) {
                    dispatchErrors(error, notifier)
                }
            } else if (responseData.message !== undefined) {
                errorHandled = true;
                notifier({
                    "message": i18n.t(responseData.message)
                })
            } else {
                notifier({
                    "title": i18n.t("error.request.unknown"),
                    "message": i18n.t(responseData)
                })
            }
        }

        callbackAfter(error);
        return errorHandled;
    } else if (error.request) {
        notifier({
            "message": i18n.t("error.server.no_response")
        });
    }
}

export const dispatchErrors = (error, dispatchDanger) => {
    if (isValidationConstraintException(error)) {
        for (const [key, value] of Object.entries(error.response.data.constraints)) {
            value.forEach(x => {
                dispatchDanger({
                    message: i18n.t(x.replaceAll('{', '').replaceAll('}', ''))
                });
            })
        }
    }
}

export const isValidationConstraintException = (error) => {
    const responseData = error.response.data;
    return responseData !== undefined && responseData.constraints !== undefined && responseData.message === "error.rest.validation";
}

