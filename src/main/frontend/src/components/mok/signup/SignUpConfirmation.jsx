import {
    useDangerNotification, useSuccessNotification, useWarningNotification
} from "../../utils/notifications/NotificationProvider";
import {usePermanentChangeDialog} from "../../utils/dialogs/DialogProvider";
import {Link, useHistory, useParams} from "react-router-dom";
import i18n from "../../../i18n";
import axios from "axios";
import Breadcrumb from "../../bars/Breadcrumb";
import {Col, Container, Row} from "react-bootstrap";
import {withNamespaces} from "react-i18next";

function SignUpConfirmation() {
    let {code} = useParams();

    const dispatchSuccessNotification = useSuccessNotification();
    const dispatchDangerNotification = useDangerNotification();
    const dispatchPermanentChangeDialog = usePermanentChangeDialog();
    const history = useHistory();

    const handleConfirmation = () => (
        dispatchPermanentChangeDialog({
            confirmCallback: () => handleSubmit(),
            cancelCallback: () => {
            }
        })
    )

    const handleSubmit = () => {
        axios.put("/resources/account/confirmAccount/" + code, {}, {}).then((res) => {
            dispatchSuccessNotification({
                message: i18n.t('sign.up.confirmation.success')
            })
            history.push("/signIn");
        }).catch(err => {
            dispatchDangerNotification({
                message: i18n.t(err.response.data.message)
            })
        })
    }

    return (
        <div className="container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item">
                    <Link className={"text-dark"} to="/">{i18n.t("navigation.bar.main.page")}</Link>
                </li>
                <li className="breadcrumb-item active text-secondary" aria-current="page">{i18n.t('sign.up.confirmation')}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={11} md={9} lg={8} xl={7} className={"floating py-3 mx-auto mb-3 mt-4"}>
                        <div className="py-2">
                            <h1 className="font-weight-light text-center">{i18n.t("sign.up.confirmation.title")}</h1>

                            <div className="col-12 text-center mt-4 mb-4">
                                <span className={"text-center"}>{i18n.t('sign.up.confirmation.info')}</span>
                            </div>

                            <div className="col-12 d-flex justify-content-center mt-2">
                                <button className="btn btn-dark dim" type="button" onClick={() => handleConfirmation()}>
                                    {i18n.t('sign.up.confirmation')}
                                </button>
                            </div>
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default withNamespaces()(SignUpConfirmation);
