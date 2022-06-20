import {useLocale} from "../login/LoginProvider";
import {useEffect, useState} from "react";
import {useDangerNotification, useSuccessNotification} from "../notifications/NotificationProvider";
import {useHistory} from "react-router-dom";
import {rolesConstant} from "../constants/Constants";
import axios from "axios";
import i18n from '../../../i18n';
import {ResponseErrorsHandler} from "./ResponseErrorsHandler";
import {Dropdown} from "react-bootstrap";
import DropdownToggle from "react-bootstrap/DropdownToggle";
import DropdownMenu from "react-bootstrap/DropdownMenu";
import {withNamespaces} from "react-i18next";

function AccessLevelChangeHandler(props) {
    const {setCurrentRole, token, currentRole} = useLocale();
    const [levels, setLevels] = useState([]);

    const dispatchDangerNotification = useDangerNotification();
    const dispatchSuccessNotification = useSuccessNotification();
    const history = useHistory();

    useEffect(() => {
        if (props.levels) {
            setLevels(props.levels)
        }
    }, [props.levels])

    const handleSelectLevel = (level) => {
        setCurrentRole(level);
        localStorage.setItem('currentRole', level);
        if (level !== rolesConstant.trainee && level !== currentRole) {
            handleChangeLevel(level);
            history.push("/")
        }
    }

    const handleChangeLevel = (e) => {
        axios.get(`/resources/access/switchAccessType/` + e, {
            headers: {
                "Authorization": token
            }
        }).then(() => dispatchSuccessNotification({message: i18n.t("footer.role.changed") + i18n.t(e)}))
            .catch((err) => ResponseErrorsHandler(err, dispatchDangerNotification))
    }

    return (

        <div className={"d-flex"}>
            {levels.length > 1 ?
                <Dropdown onSelect={handleSelectLevel}>
                    <DropdownToggle id="dropdown-basic" className={"footer py-0"} variant="Secondary">
                        <span>{i18n.t("footer.role.change")}</span>
                    </DropdownToggle>
                    <DropdownMenu>
                        {levels.map((level) => (
                            <Dropdown.Item eventKey={level} className="item">{i18n.t(level)}</Dropdown.Item>
                        ))}
                    </DropdownMenu>
                </Dropdown> : ""}
        </div>
    )
}

export default withNamespaces()(AccessLevelChangeHandler);