import {withNamespaces} from "react-i18next";
import {useLocale} from "../../utils/login/LoginProvider";
import {Link, useHistory} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import Breadcrumb from "../../bars/Breadcrumb";
import {Col, Container, Row} from "react-bootstrap";
import {usePermanentChangeDialog} from "../../utils/dialogs/DialogProvider";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {useDangerNotification, useSuccessNotification} from "../../utils/notifications/NotificationProvider";
import {dateConverter} from "../../../i18n";
import {paymentStatus} from "../../utils/constants/Constants";

function MyPayments(props) {
    const {t, i18n} = props
    const {token, setToken} = useLocale();
    const [etag, setEtag] = useState();
    const [data, setData] = useState([
        {
            id: "",
            paymentStatus: "",
            courseId: "",
            value: "",
            traineeComment: "",
            adminComment: "",
            creationDate: "",
            modificationDate: ""
        }
    ]);

    const dispatchSuccessNotification = useSuccessNotification();
    const dispatchPermanentChangeDialog = usePermanentChangeDialog();
    const dispatchDangerNotification = useDangerNotification();
    const history = useHistory()

    useEffect(() => {
        fetchData();
        getEtag().then(r => setEtag(r));
    }, []);

    const fetchData = () => {
        if (token) {
            axios.get(`/resources/payment/getPaymentsHistory`, {
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

    const getEtag = async () => {
        const response = await axios.get(`/resources/course/getCourse`, {
            headers: {
                "Authorization": token,
            }
        })
        return response.headers.etag;
    };

    const cancelPayment = () => {
        if (token) {
            axios.put(`/resources/payment/cancelPayment`, {}, {
                headers: {
                    "Authorization": token,
                    "If-Match": etag,
                }
            }).then((res) => {
                history.push("/userPage");
                dispatchSuccessNotification({message: i18n.t('my.payments.cancel.success')})
            }).catch(err => {
                ResponseErrorsHandler(err, dispatchDangerNotification);
            });
        }
    }

    const handleCancel = () => {
        dispatchPermanentChangeDialog({
            confirmCallback: () => {
                cancelPayment()
            },
            cancelCallback: () => {
            },
        })
    }

    return (
        <div className="container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link>
                </li>
                <li className="breadcrumb-item active text-secondary"
                    aria-current="page">{t("navigation.bar.reported.payments")}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={12} md={12} lg={12} xl={12} className={"floating py-3 mx-auto mb-3 mt-4"}>
                        <div className="py-2">
                            <h1 className="font-weight-light text-center">{t("navigation.bar.reported.payments")}</h1>
                        </div>

                        <table className="table table-hover table-bordered mt-2">
                            <thead className="dim">
                            <tr>
                                <th className={"font-weight-normal text-center align-middle"}
                                    scope="col">{t("paymentStatus")}</th>
                                <th className={"font-weight-normal text-center align-middle"} scope="col">{t("creationDate")}</th>
                                <th className={"font-weight-normal text-center align-middle"} scope="col">{t("value")}</th>
                                <th className={"font-weight-normal text-center align-middle"}
                                    scope="col">{t("traineeComment")}</th>
                                <th className={"font-weight-normal text-center align-middle"} scope="col">{t("adminComment")}</th>
                                <th className={"font-weight-normal text-center align-middle"}
                                    scope="col">{t("modificationDate")}</th>
                                <th className={"font-weight-normal text-center align-middle"} scope="col">{t("action")}</th>
                            </tr>
                            </thead>
                            <tbody>

                            {data.length > 1 && data.slice(0).reverse().map((item) => (
                                <tr>
                                    <td className={"text-center align-middle"}>{t(item.paymentStatus)}</td>
                                    <td className={"text-center align-middle"}>{item.creationDate !== undefined ?
                                        dateConverter(item.creationDate.slice(0, -5)) : "-"}</td>
                                    <td className={"text-center align-middle"}>{item.value} {t("currency")}</td>
                                    <td className={"text-center align-middle"}>{item.traineeComment !== undefined ?
                                        item.traineeComment : "-"}</td>
                                    <td className={"text-center align-middle"}>{item.adminComment !== undefined ?
                                        item.adminComment : "-"}</td>
                                    <td className={"text-center align-middle"}>{item.modificationDate !== undefined && item.modificationDate !== "" ?
                                        dateConverter(item.modificationDate.slice(0, -5)) : "-"}</td>
                                    {item.paymentStatus === paymentStatus.IN_PROGRESS &&
                                        <td className={"text-center align-middle"}>
                                            <div className="d-flex justify-content-center align-items-center">
                                                <div className="btn-group mb-1">
                                                    <button className="btn btn-dark btn-block dim"
                                                            onClick={handleCancel}>
                                                        {t('dialog.button.cancel')}
                                                    </button>
                                                </div>
                                            </div>
                                        </td>
                                    }
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

export default withNamespaces()(MyPayments);