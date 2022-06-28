import {Link, useHistory} from "react-router-dom";
import {useEffect, useState} from "react";
import {Col, Container, Nav, Row} from "react-bootstrap";
import Breadcrumb from "../../bars/Breadcrumb";
import axios from "axios";
import {useLocale} from "../../utils/login/LoginProvider";
import {withNamespaces} from "react-i18next";
import {rolesConstant} from "../../utils/constants/Constants";

function MyAccount(props) {
    const {t, i18n} = props
    const {token, currentRole} = useLocale();
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
    const [roles, setRoles] = useState("");
    const [permissions, setPermissions] = useState("");
    const [hasCourse, setHasCourse] = useState(false);
    const [courseData, setCourseData] = useState([
        {
            courseCategory: "",
            price: "",
            paid: "",
        }
    ]);

    const history = useHistory();

    useEffect(() => {
        fetchData();
    }, [])

    const fetchData = () => {
        getCommonData();
        getRoles();
        if (currentRole === rolesConstant.instructor) {
            getPermissions();
        } else if (currentRole === rolesConstant.trainee) {
            getCourseData();
        }
    }

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

    const getRoles = () => {
        axios.get(`/resources/access/getAccesses`, {
            headers: {
                "Authorization": token,
            }
        }).then(r => {
            let data = "";
            for (let i = 0; i < r.data.accessesGranted.length; i++) {
                data += r.data.accessesGranted[i].accessType + ", ";
            }
            data = data.slice(0, data.length - 2)
            setRoles(data);
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

    const getCourseData = () => {
        setHasCourse(false);
        axios.get(`/resources/course/getBriefCourseInfo`, {
            headers: {
                "Authorization": token,
            }
        }).then(r => {
            setHasCourse(true);
            setCourseData(r.data);
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
                <li className="breadcrumb-item active text-secondary" aria-current="page">{t('navigation.bar.my.account')}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={11} md={8} lg={7} xl={6} className={"floating py-3 mx-auto mb-3 mt-4"}>
                        <div className="py-2">
                            <h1 className="font-weight-light text-center mb-4">{t("my.account.data")}</h1>

                            <div className="row">
                                <div className="col pr-0">
                                    <ul className="list-group list-group-flush font-weight-bold text-right">
                                        <li className="list-group-item">{t("login") + ":"}</li>
                                        <li className="list-group-item">{t("emailAddress") + ":"}</li>
                                        <li className="list-group-item">{t("firstname") + ":"}</li>
                                        <li className="list-group-item">{t("lastname") + ":"}</li>
                                        <li className="list-group-item">{t("phoneNumber") + ":"}</li>
                                        <li className="list-group-item">{t("language") + ":"}</li>
                                        <li className="list-group-item">{t("roles") + ":"}</li>
                                        {hasCourse && (
                                            <>
                                                <li className="list-group-item">{t("course.category") + ":"}</li>
                                                <li className="list-group-item">{t("course.price") + ":"}</li>
                                                <li className="list-group-item">{t("course.paid") + ":"}</li>
                                            </>
                                        )}
                                        {permissions.length !== 0 && (
                                            <>
                                                <li className="list-group-item">{t("permissions") + ":"}</li>
                                            </>
                                        )}
                                    </ul>
                                </div>
                                <div className="col pl-0">
                                    <ul className="list-group list-group-flush text-left">
                                        <li className="list-group-item">{commonData.login}</li>
                                        <li className="list-group-item">{commonData.emailAddress}</li>
                                        <li className="list-group-item">{commonData.firstname}</li>
                                        <li className="list-group-item">{commonData.lastname}</li>
                                        <li className="list-group-item">{commonData.phoneNumber}</li>
                                        <li className="list-group-item">{commonData.language}</li>
                                        <li className="list-group-item">{roles.split(", ").map(role => t(role)).join(", ")}</li>
                                        {hasCourse && (
                                            <>
                                                <li className="list-group-item">{courseData.courseCategory}</li>
                                                <li className="list-group-item">{courseData.price + " " + t("currency")}</li>
                                                <li className="list-group-item">{courseData.paid + " " + t("currency")}</li>
                                            </>

                                        )}
                                        {permissions.length !== 0 && (
                                            <>
                                                <li className="list-group-item">{permissions}</li>
                                            </>
                                        )}
                                    </ul>
                                </div>
                            </div>
                            <div className="col-12 d-flex justify-content-center mt-4">
                                <button className="btn btn-dark btn-block dim" onClick={fetchData}>
                                    {t('refresh')}
                                </button>
                            </div>

                            <div className="col-12 d-flex justify-content-center mt-2">
                                <button className="btn btn-dark btn-block dim" type="button"
                                        onClick={() => history.push("/editOwnAccount")}>
                                    {t('edit')}
                                </button>
                            </div>
                        </div>
                    </Col>
                </Row>
            </Container>
            {/*)}*/}
        </div>
    );
}

export default withNamespaces()(MyAccount);