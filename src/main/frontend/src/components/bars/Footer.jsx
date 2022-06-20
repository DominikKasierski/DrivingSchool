import {useLocale} from "../utils/login/LoginProvider";
import AccessLevelChangeHandler from "../utils/handlers/AccessLevelChangeHandler";
import {withNamespaces} from "react-i18next";
import i18n from "../../i18n";

function Footer(props) {
    const {token, currentRole} = useLocale();
    const {roles} = props;

    return (
        <div className={"container-fluid d-flex position-fixed align-items-center footer"}>
            <div>
                {token !== null && token !== '' ?
                    <span className={"mr-3"}>{i18n.t("footer.role") + i18n.t(currentRole)}</span> : ""}
            </div>
            <div>
                {token !== null && token !== '' ?
                    <AccessLevelChangeHandler levels={roles}/> : ""}
            </div>
            <div className="ml-auto mr-2">
                <span>&copy; Dominik Kasierski</span>
            </div>
        </div>
    );
}

export default withNamespaces()(Footer);