import {withNamespaces} from "react-i18next";
import {useLocale} from "../../utils/login/LoginProvider";
import {Link, useHistory} from "react-router-dom";
import {useDangerNotification, useSuccessNotification} from "../../utils/notifications/NotificationProvider";
import {useEffect, useState} from "react";
import axios from "axios";
import Breadcrumb from "../../bars/Breadcrumb";
import {Col, Container, Row} from "react-bootstrap";
import VehicleComponent from "../../mos/vehicles/VehicleComponent";

function AccountList(props) {
    const {t, i18n} = props
    const {token, setToken} = useLocale();
    const [data, setData] = useState([
        {
            login: "",
            emailAddress: "",
            firstname: "",
            lastname: "",
            language: "",
            phoneNumber: "",
            enabled: false,
            confirmed: false,
        }
    ]);

    const history = useHistory()

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = () => {
        if (token) {
            axios.get(`/resources/account/getAccounts`, {
                headers: {
                    "Authorization": token,
                }
            }).then(r => {
                setData(r.data);
            }).catch(r => {
                if (r.response != null) {
                    if (r.response.status === 403) {
                        history.push("/errors/forbidden")
                    } else if (r.response.status === 500) {
                        history.push("/errors/internal")
                    }
                }
            });
        }
    }
    return (
        <div className="container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link></li>
                <li className="breadcrumb-item active text-secondary" aria-current="page">{t("navigation.bar.accounts.list")}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={12} md={12} lg={12} xl={12} className={"floating py-3 mx-auto mb-3 mt-4"}>
                        <div className="py-2">
                            <h1 className="font-weight-light text-center">{t("account.list.title")}</h1>
                        </div>

                        <table className="table table-hover table-bordered mt-2">
                            <thead className="dim">
                            <tr>
                                <th className={"font-weight-normal text-center"} scope="col">{t("login")}</th>
                                <th className={"font-weight-normal text-center"} scope="col">{t("emailAddress")}</th>
                                <th className={"font-weight-normal text-center"} scope="col">{t("firstname")}</th>
                                <th className={"font-weight-normal text-center"} scope="col">{t("lastname")}</th>
                                <th className={"font-weight-normal text-center"} scope="col">{t("phoneNumber")}</th>
                                <th className={"font-weight-normal text-center"} scope="col">{t("language")}</th>
                                <th className={"font-weight-normal text-center"} scope="col">{t("confirmed")}</th>
                                <th className={"font-weight-normal text-center"} scope="col">{t("enabled")}</th>
                                <th className={"font-weight-normal text-center"} scope="col">{t("action")}</th>
                            </tr>
                            </thead>
                            <tbody>

                            {data.length > 1 && data.map((item) => (
                                <tr>
                                    <td className={"text-center align-middle"}>{item.login}</td>
                                    <td className={"text-center align-middle"}>{item.emailAddress}</td>
                                    <td className={"text-center align-middle"}>{item.firstname}</td>
                                    <td className={"text-center align-middle"}>{item.lastname}</td>
                                    <td className={"text-center align-middle"}>{item.phoneNumber}</td>
                                    <td className={"text-center align-middle"}>{item.language}</td>
                                    <td className={"text-center align-middle"}>{item.confirmed ? t("yes") : t("no")}</td>
                                    <td className={"text-center align-middle"}>{item.enabled ? t("yes") : t("no")}</td>
                                    <td className={"text-center align-middle"}>
                                        <div className="d-flex justify-content-center align-items-center">
                                            <div className="btn-group mb-1">
                                                <button className="btn btn-dark btn-block dim"
                                                        onClick={() => history.push("/editOtherAccount?login=" + item.login)}>
                                                    {t('edit')}
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

export default withNamespaces()(AccountList);