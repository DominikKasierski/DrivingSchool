import {useLocale} from "../utils/login/LoginProvider";
import AccessLevelChangeHandler from "../utils/handlers/AccessLevelChangeHandler";
import {Col} from "react-bootstrap";
import {withNamespaces} from "react-i18next";

function Footer(props) {
    const {token, currentRole} = useLocale();
    const {roles} = props;

    return (
        <div className={"container-fluid d-flex position-fixed align-items-center pb-2 pt-1 footer"}>
                <div>
                    {token !== null && token !== '' ? <div><AccessLevelChangeHandler levels={roles}/></div> : ""}
                </div>
                <div className="ml-auto">
                    <span className={"px-2"}>&copy; Dominik Kasierski</span>
                </div>

            {/*<div className={"row"}>*/}
            {/*    <Col xs={12} className={"d-flex align-items-center flex-wrap flex-row"}>*/}
            {/*        <div>*/}
            {/*            {token !== null && token !== '' ? <div><AccessLevelChangeHandler levels={roles}/></div> : ""}*/}
            {/*        </div>*/}
            {/*        <div className={"d-none d-sm-flex"}>*/}
            {/*            {token !== null && token !== '' ? <span className={"px-2"}>|</span> : ""}*/}
            {/*            &copy; Dominik Kasierski*/}
            {/*        </div>*/}
            {/*    </Col>*/}
            {/*</div>*/}
        </div>
    );
}

export default withNamespaces()(Footer);