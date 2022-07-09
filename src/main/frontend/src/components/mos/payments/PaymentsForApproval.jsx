import {withNamespaces} from "react-i18next";
import {useLocale} from "../../utils/login/LoginProvider";
import {Link, useHistory} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import Breadcrumb from "../../bars/Breadcrumb";
import {Col, Container, Row} from "react-bootstrap";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {useDangerNotification, useSuccessNotification} from "../../utils/notifications/NotificationProvider";
import {dateConverter} from "../../../i18n";

function PaymentsForApproval(props) {
    const {t, i18n} = props
    const {token, setToken} = useLocale();
    const [data, setData] = useState([
        {
            login: "",
            firstname: "",
            lastname: "",
            courseCategory: "",
            value: "",
            traineeComment: "",
            creationDate: "",
        }
    ]);

    const dispatchDangerNotification = useDangerNotification();
    const history = useHistory()

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = () => {
        if (token) {
            axios.get(`/resources/payment/getPaymentsForApproval`, {
                headers: {
                    "Authorization": token,
                }
            }).then(r => {
                setData(r.data)
            }).catch(r => {
                if (r.response != null) {
                    if (r.response.status === 403) {
                        history.push("/errors/forbidden")
                    } else if (r.response.status === 500) {
                        history.push("/errors/internal")
                    } else {
                        ResponseErrorsHandler(r, dispatchDangerNotification);
                    }
                }
            });
        }
    }

    return (
        <div className="container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link>
                </li>
                <li className="breadcrumb-item active text-secondary"
                    aria-current="page">{t("navigation.bar.payments.for.approval")}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={12} md={12} lg={12} xl={12} className={"floating py-3 mx-auto mb-3 mt-4"}>
                        <div className="py-2">
                            <h1 className="font-weight-light text-center">{t("navigation.bar.payments.for.approval")}</h1>
                        </div>

                        <table className="table table-hover table-bordered mt-2">
                            <thead className="dim">
                            <tr>
                                <th className={"font-weight-normal text-center align-middle"} scope="col">{t("creationDate")}</th>
                                <th className={"font-weight-normal text-center align-middle"} scope="col">{t("firstname")}</th>
                                <th className={"font-weight-normal text-center align-middle"} scope="col">{t("lastname")}</th>
                                <th className={"font-weight-normal text-center align-middle"} scope="col">{t("value")}</th>
                                <th className={"font-weight-normal text-center align-middle"}
                                    scope="col">{t("courseCategory")}</th>
                                <th className={"font-weight-normal text-center align-middle"}
                                    scope="col">{t("traineeComment")}</th>
                                <th className={"font-weight-normal text-center align-middle"} scope="col">{t("action")}</th>
                            </tr>
                            </thead>
                            <tbody>

                            {data.length > 0 && data.map((item) => (
                                <tr>
                                    <td className={"text-center align-middle"}>{item.creationDate !== undefined ?
                                        dateConverter(item.creationDate.slice(0, -5)) : "-"}</td>
                                    <td className={"text-center align-middle"}>{item.firstname}</td>
                                    <td className={"text-center align-middle"}>{item.lastname}</td>
                                    <td className={"text-center align-middle"}>{item.value} {t("currency")}</td>
                                    <td className={"text-center align-middle"}>{item.courseCategory}</td>
                                    <td className={"text-center align-middle"}>{item.traineeComment !== undefined ?
                                        item.traineeComment : "-"}</td>
                                    <td className={"text-center align-middle"}>
                                        <div className="d-flex justify-content-center align-items-center">
                                            <div className="btn-group mb-1">
                                                <button className="btn btn-dark btn-block dim"
                                                        onClick={() => history.push("/editPayment?login=" + item.login)}>
                                                    {t('confirm/reject')}
                                                </button>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default withNamespaces()(PaymentsForApproval);