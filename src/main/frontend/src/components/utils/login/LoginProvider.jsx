import React, {createContext, useEffect, useState} from "react";
import jwt_decode from "jwt-decode";
import {
    useCustomNotification, useDangerNotification, useSuccessNotification
} from "../notifications/NotificationProvider";
import i18n from "../../../i18n";
import axios from "axios";
import {ResponseErrorsHandler} from "../handlers/ResponseErrorsHandler";
import {notificationDuration, notificationType} from "../notifications/Notification";

const REFRESH_TIME = 60 * 1000;
const LoginContext = createContext();

export const LoginProvider = ({children}) => {
    const [token, setToken] = useState('');
    const [currentRole, setCurrentRole] = useState('');
    const [username, setUsername] = useState('');

    const dispatchCustomNotification = useCustomNotification();
    const dispatchDangerNotification = useDangerNotification();
    const dispatchSuccessNotification = useSuccessNotification();

    const handleRefreshBox = () => {
        dispatchCustomNotification({
            "type": notificationType.WARNING,
            "duration": notificationDuration.LONG,
            "title": `${i18n.t("dialog.update.token.title")}`,
            "message": (<> {i18n.t("dialog.update.token.message")} <span className={"text-primary"} style={{cursor: "pointer"}}
                                                                         onClick={refreshToken}>{i18n.t("dialog.update.token.refresh")}</span></>)
        })
    }

    const refreshToken = (event) => {
        event.target.closest(".alert").querySelector(".close").click();
        if (localStorage.getItem("token")) {
            axios.post(`/resources/auth/updateToken`, localStorage.getItem("token"), {
                headers: {
                    "Authorization": `${localStorage.getItem("token")}`
                }
            }).then(response => {
                saveToken("Bearer " + response.data);
                clearTimeout(localStorage.getItem("timeoutId2") ?? 0)
                clearTimeout(localStorage.getItem("timeoutId3") ?? 0)
                schedule()
                dispatchSuccessNotification({message: i18n.t("dialog.update.token.success")})
            }).catch(
                e => ResponseErrorsHandler(e, dispatchDangerNotification)
            );
        }
    }

    const schedule = () => {
        const id = setTimeout(() => {
            handleRefreshBox();
        }, new Date(jwt_decode(localStorage.getItem("token")).exp * 1000) - new Date() - REFRESH_TIME);
        localStorage.setItem("timeoutId", id.toString())

        const id3 = setTimeout(() => {
            localStorage.removeItem("token")
            localStorage.removeItem("username")
            localStorage.removeItem("currentRole")
            setToken(null)
            setUsername('')
            setCurrentRole('')
        }, new Date(jwt_decode(localStorage.getItem("token")).exp * 1000) - new Date() + 2000);
        localStorage.setItem("timeoutId3", id3.toString())
    }

    const validateTokenExpirationDate = () => {
        const storedToken = localStorage.getItem("token");
        if (storedToken === undefined || storedToken == null) return
        const decodedToken = jwt_decode(storedToken);
        const expirationDate = new Date(decodedToken.exp * 1000)
        if (expirationDate < new Date()) {
            localStorage.removeItem("token")
            localStorage.removeItem("username")
            localStorage.removeItem("currentRole")
            setToken(null)
            setUsername('')
            setCurrentRole('')
        } else {
            setToken(storedToken)
        }
        setCurrentRole(localStorage.getItem('currentRole'));
        setUsername(localStorage.getItem('username'));
    }

    const scheduleTokenRefresh = () => {
        const storedToken = localStorage.getItem("token");
        if (storedToken === undefined || storedToken == null) return
        schedule()
    }

    useEffect(() => {
        validateTokenExpirationDate();
        scheduleTokenRefresh();
    }, [])

    const saveToken = (value) => {
        setToken(value)
        localStorage.setItem("token", value)
    }

    const values = {
        token,
        setToken,
        saveToken,
        currentRole,
        setCurrentRole,
        username,
        setUsername
    }

    return (<LoginContext.Provider value={values}>{children}</LoginContext.Provider>);
}

export const useLocale = () => React.useContext(LoginContext);

export default LoginProvider;