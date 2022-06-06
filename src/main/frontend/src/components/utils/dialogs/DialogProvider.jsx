import React, {createContext, useContext, useReducer} from "react";
import Dialog from "./Dialog";
import {v4} from "uuid";
import i18n from '../../../i18n';

const DialogContext = createContext();

const DialogProvider = (props) => {
    const [state, dispatch] = useReducer((state, action) => {
        switch (action.type) {
            case "ADD_DIALOG":
                return [...state, {...action.payload}];
            case "REMOVE_DIALOG":
                return state.filter(e => e.id !== action.id);
            default:
                return state;
        }
    }, []);

    return (
        <DialogContext.Provider value={dispatch}>
            {state.map((note) => {
                return <Dialog dispatch={dispatch} key={note.id} {...note} />
            })}
            {props.children}
        </DialogContext.Provider>
    )
};

export const useDialog = () => {
    const dispatch = useContext(DialogContext);

    return ({title, message, confirmCallback = (() => {}), cancelCallback = (() => {}), ...props}) => {
        dispatch({
            type: "ADD_DIALOG",
            payload: {
                id: v4(),
                title: title,
                message: message,
                confirmCallback: confirmCallback,
                cancelCallback: cancelCallback,
                ...props
            }
        });
    }
};

export const usePermanentDialog = () => {
    const dispatch = useContext(DialogContext);

    return ({confirmCallback = (() => {}), cancelCallback = (() => {}), ...props}) => {
        dispatch({
            type: "ADD_DIALOG",
            payload: {
                id: v4(),
                title: i18n.t("dialog.permanent.title"),
                message: i18n.t("dialog.permanent.message"),
                confirmCallback: confirmCallback,
                cancelCallback: cancelCallback,
                ...props
            }
        });
    }
};