import {useLocale} from "../login/LoginProvider";
import {useEffect, useState} from "react";
import {useDangerNotification, useSuccessNotification} from "../notifications/NotificationProvider";
import axios from "axios";
import {ResponseErrorsHandler} from "./ResponseErrorsHandler";
import {Dropdown} from "react-bootstrap";
import DropdownToggle from "react-bootstrap/DropdownToggle";
import {withNamespaces} from "react-i18next";

function LanguageChangeHandler(props) {
    const {t, i18n} = props
    const {token, setToken} = useLocale();
    const [etag, setETag] = useState();
    const availableLanguages = ['pl', 'en']

    const dispatchDangerNotification = useDangerNotification();
    const dispatchSuccessNotification = useSuccessNotification();

    const getEtag = async () => {
        const response = await axios.get(`/resources/account/getDetails`, {
            headers: {
                "Authorization": token,
            }
        })
        return response.headers.etag;
    };

    useEffect(() => {
        if (token) {
            getEtag().then(r => setETag(r));
        }
    }, [token, i18n.language]);

    const handleGuestClickPl = () => {
        setLanguage(i18n, "pl")
    }

    const handleGuestClickEn = () => {
        setLanguage(i18n, "en")
    }

    const handleUserClickPl = () => {
        axios.put(`/resources/account/editLanguage/pl`, {}, {
            headers: {
                "Authorization": token,
                "If-Match": etag
            }
        }).then(() => {
            handleGuestClickPl();
            dispatchSuccessNotification({message: i18n.t("language.change.success")})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification);
        })
    }

    const handleUserClickEn = () => {
        axios.put(`/resources/account/editLanguage/en`, {}, {
            headers: {
                "Authorization": token,
                "If-Match": etag
            }
        }).then(() => {
            handleGuestClickEn();
            dispatchSuccessNotification({message: i18n.t("language.change.success")})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification);
        })
    }

    return (
        <>
            <Dropdown>
                <DropdownToggle id="dropdown-basic" className="pl-0 pl-lg-2 pr-0 pr-lg-2 mr-3 dim" variant="Secondary">
                    <span
                        style={{
                            fontSize: "1.2rem",
                        }}>{i18n.t("language")} [{i18n.language.substring(0, 2).toUpperCase()}]</span>
                </DropdownToggle>

                <Dropdown.Menu>
                    <Dropdown.Item onClick={token !== null && token !== '' ? (handleUserClickPl) : (handleGuestClickPl)}>
                        {t(availableLanguages[0])} </Dropdown.Item>
                    <Dropdown.Item onClick={token !== null && token !== '' ? (handleUserClickEn) : (handleGuestClickEn)}>
                        {t(availableLanguages[1])}</Dropdown.Item>
                </Dropdown.Menu>
            </Dropdown>
        </>
    )
}

export function setLanguage(i18n, lang) {
    i18n.changeLanguage(lang)
}

export default withNamespaces()(LanguageChangeHandler);