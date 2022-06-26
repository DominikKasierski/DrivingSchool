import {Link, useHistory} from "react-router-dom";
import {useSuccessNotification} from "../../utils/notifications/NotificationProvider";
import {useEffect, useState} from "@types/react";
import {Breadcrumb, Col, Container, Row} from "react-bootstrap";
import axios from "axios";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {useLocale} from "../../utils/login/LoginProvider";

function MyAccount(props) {
    const {t, i18n} = props
    const {token, currentRole} = useLocale();
    const [roles, setRoles] = useState("");
    const [commonData, setCommonData] = useState([
        {
            login: "",
            emailAddress: "",
            firstname: "",
            lastname: "",
            phoneNumber: "",
            language: ""
        }
    ]);
    const [permissions, setPermissions] = useState("");
    const [hasCourse, setHasCourse] = useState(false);
    const [courseData, setCourseData] = useState([
        {
            courseCategory: "",
            price: "",
            paid: "",
        }
    ]);

    const dispatchSuccessNotification = useSuccessNotification();
    const history = useHistory();

    useEffect(() => {
        getCommonData();
        if (currentRole === 'INSTRUCTOR') {
            getPermissions();
        } else if (currentRole === 'TRAINEE') {

        }
    }, [])

    const getCommonData = () => {
        axios.get(`/resources/account/getDetails`, {
            headers: {
                "Authorization": token,
            }
        }).then(r => {
            setCommonData(r.data);
        }).catch(res => {
            if (res.response != null) {
                if (res.response.status === 403) {
                    history.push("/forbidden")
                } else if (res.response.status === 500) {
                    history.push("/internalError")
                }
            }
        });
    }

    const getPermissions = () => {
        axios.get(`/resources/access/getOwnPermissions`, {
            headers: {
                "Authorization": token,
            }
        }).then(r => {
            setPermissions(r.data);
        }).catch(res => {
            if (res.response != null) {
                if (res.response.status === 403) {
                    history.push("/forbidden")
                } else if (res.response.status === 500) {
                    history.push("/internalError")
                }
            }
        });
    }

    const getPermissions = () => {
        axios.get(`/resources/access/getOwnPermissions`, {
            headers: {
                "Authorization": token,
            }
        }).then(r => {
            setPermissions(r.data);
        }).catch(res => {
            if (res.response != null) {
                if (res.response.status === 403) {
                    history.push("/forbidden")
                } else if (res.response.status === 500) {
                    history.push("/internalError")
                }
            }
        });
    }

    return (
        <div className="container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link></li>
                <li className="breadcrumb-item active text-secondary" aria-current="page">{t("my.account")}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={11} md={9} lg={8} xl={7} className={"floating py-3 mx-auto mb-3 mt-4"}>
                        <div className="py-2">
                            <h1 className="font-weight-light text-center">{t("my.account.data")}</h1>

                            <div className="col-12 d-flex justify-content-center mt-4">
                                <button className="btn btn-dark btn-block dim" type="submit">
                                    {t('sign.in')}
                                </button>
                            </div>

                            <div className="col-12 d-flex justify-content-center mt-2">
                                <button className="btn btn-dark btn-block dim" type="button"
                                        onClick={() => history.push("/signIn/resetPassword")}>
                                    {t('sign.in.reset.password')}
                                </button>
                            </div>
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default withNamespaces()(SignUp);