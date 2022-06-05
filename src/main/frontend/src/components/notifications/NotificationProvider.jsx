import React, {createContext, useContext, useReducer} from 'react';
import Notification, {notificationDuration, notificationType} from "./Notification";
import './NotificationWrapper.scss'
import {v4} from "uuid";
import i18n from '../../i18n';

const NotificationContext = createContext();

const NotificationProvider = (props) => {
    const [state, dispatch] = useReducer((state, action) => {
        switch (action.type) {
            case "ADD_NOTIFICATION":
                return [...state, {...action.payload}];
            case "REMOVE_NOTIFICATION":
                return state.filter(e => e.id !== action.id);
            default:
                return state;
        }
    }, []);

    return (
        <NotificationContext.Provider value={dispatch}>
            <div id={"bottom-notification-wrapper"} className={"notification-wrapper position-fixed"}>
                {state.map((note) => {
                    return <Notification dispatch={dispatch} key={note.id} {...note} />
                })}
            </div>
            {props.children}
        </NotificationContext.Provider>
    )
};

export const useDangerNotification = () => {
    const dispatch = useContext(NotificationContext);

    return ({message, ...props}) => {
        dispatch({
            type: "ADD_NOTIFICATION",
            payload: {
                id: v4(),
                notificationType: notificationType.DANGER,
                notificationDuration: notificationDuration.INFINITY,
                title: i18n.t("notification.title.danger"),
                message: message,
                ...props
            }
        })
    }
};

export const useWarningNotification = () => {
    const dispatch = useContext(NotificationContext);

    return ({message, ...props}) => {
        dispatch({
            type: "ADD_NOTIFICATION",
            payload: {
                id: v4(),
                notificationType: notificationType.WARNING,
                notificationDuration: notificationDuration.STANDARD,
                title: i18n.t("notification.title.warning"),
                message: message,
                ...props
            }
        })
    }
};

export const useSuccessNotification = () => {
    const dispatch = useContext(NotificationContext);

    return ({message, ...props}) => {
        dispatch({
            type: "ADD_NOTIFICATION",
            payload: {
                id: v4(),
                notificationType: notificationType.SUCCESS,
                notificationDuration: notificationDuration.STANDARD,
                title: i18n.t("notification.title.success"),
                message: message,
                ...props
            }
        })
    }
};

export const useCustomNotification = () => {
    const dispatch = useContext(NotificationContext);

    return ({type, duration, message, title, ...props}) => {
        dispatch({
            type: "ADD_NOTIFICATION",
            payload: {
                id: v4(),
                notificationType: type,
                notificationDuration: duration,
                message: message,
                title: title,
                ...props
            }
        })
    }
};

export default NotificationProvider;