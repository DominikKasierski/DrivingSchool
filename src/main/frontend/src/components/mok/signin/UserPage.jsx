import {useLocale} from "../../utils/login/LoginProvider";
import {useDangerNotification} from "../../utils/notifications/NotificationProvider";
import {useEffect} from "react";
import axios from "axios";
import {setLanguage} from "../../utils/handlers/LanguageChangeHandler";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {Container} from "react-bootstrap";
import Breadcrumb from "../../bars/Breadcrumb";
import {Link} from "react-router-dom";
import {withNamespaces} from "react-i18next";

function UserPage(props) {
    const {t, i18n} = props
    const {token, username} = useLocale();

    const dispatchDangerNotification = useDangerNotification();

    function getUserLanguage(token, i18n, showDanger = (() => {
    })) {
        if (token !== null && token !== '') {
            axios.get(`/resources/account/getDetails`, {
                headers: {
                    "Authorization": token,
                }
            }).then(res => {
                setLanguage(i18n, res.data.language);
            }).catch(err => ResponseErrorsHandler(err, showDanger))
        } else setLanguage(i18n, "en")
    }

    useEffect(() => {
        if (token) {
            getUserLanguage(token, i18n, dispatchDangerNotification);
        }
    }, []);

    return (
        <Container fluid className="mb-2">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link></li>
                <li className="breadcrumb-item active text-secondary" aria-current="page">{t('user.page')}</li>
            </Breadcrumb>
            <div>
                <h1 className={"display-2 font-weight-light p-5 ml-5 mt-5 text-white"}
                    style={{textShadow: "3px 3px #333"}}>{t('user.page.welcome')} {username}</h1>
            </div>
        </Container>
    );
}

export default withNamespaces()(UserPage);
