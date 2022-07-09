import {useLocale} from "../../utils/login/LoginProvider";
import {withNamespaces} from "react-i18next";
import {useEffect, useState} from "react";
import {useDangerNotification, useSuccessNotification} from "../../utils/notifications/NotificationProvider";
import {Link, useHistory} from "react-router-dom";
import axios from "axios";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {permissionsConstant} from "../../utils/constants/Constants";
import Breadcrumb from "../../bars/Breadcrumb";
import {Col, Container, DropdownButton, Dropdown, Row} from "react-bootstrap";
import DropdownToggle from "react-bootstrap/DropdownToggle";

function Underpayments(props) {
    const {t, i18n} = props
    const {token, setToken} = useLocale();
    const [data, setData] = useState([
        {
            courseCategory: "",
            login: "",
            firstname: "",
            lastname: "",
            price: "",
            paid: "",
        }
    ]);
    let courseCategory = permissionsConstant.A

    const dispatchSuccessNotification = useSuccessNotification();
    const dispatchDangerNotification = useDangerNotification();
    const history = useHistory()

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = (notification = false) => {
        if (token) {
            axios.get(`/resources/payment/getUnderpayments/` + courseCategory, {
                headers: {
                    "Authorization": token,
                }
            }).then(r => {
                setData(r.data)
                if (notification) {
                    dispatchSuccessNotification({message: i18n.t('underpayments.fetch.success')})
                }
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
                    aria-current="page">{t("navigation.bar.underpayments")}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={12} md={12} lg={12} xl={12} className={"floating py-3 mb-3 mt-4"}>
                        <div className="py-2">

                            <div className="col-md-12 d-flex align-items-center justify-content-between">
                                <div className="ml-5">
                                </div>

                                <h1 className="font-weight-light ml-5 pl-5">{t("underpayments.title")}</h1>

                                <Dropdown>
                                    <DropdownToggle id="dropdown-basic" className="pl-0 pl-lg-2 pr-0 pr-lg-2 dim"
                                                    variant="Secondary">
                                        <span>Wybierz kategorię</span>
                                    </DropdownToggle>
                                    <Dropdown.Menu>
                                        <Dropdown.Item as="button"
                                                       onClick={() => {
                                                           courseCategory = permissionsConstant.A;
                                                           fetchData(true)
                                                       }
                                                       }>A</Dropdown.Item>
                                        <Dropdown.Item as="button"
                                                       onClick={() => {
                                                           courseCategory = permissionsConstant.B;
                                                           fetchData(true)
                                                       }
                                                       }>B</Dropdown.Item>
                                        <Dropdown.Item as="button"
                                                       onClick={() => {
                                                           courseCategory = permissionsConstant.C;
                                                           fetchData(true)
                                                       }
                                                       }>C</Dropdown.Item>
                                    </Dropdown.Menu>
                                </Dropdown>
                            </div>

                            <div className="col-12 text-center mt-2 mb-4">
                                <span>{t("underpayments.selected.category")}: {data[0] ? data[0].courseCategory : t("no.results")}</span>
                            </div>
                        </div>

                        <table className="table table-hover table-bordered mt-2">
                            <thead className="dim">
                            <tr>
                                <th className={"font-weight-normal text-center align-middle"} scope="col">{t("firstname")}</th>
                                <th className={"font-weight-normal text-center align-middle"} scope="col">{t("lastname")}</th>
                                <th className={"font-weight-normal text-center align-middle"} scope="col">{t("course.price")}</th>
                                <th className={"font-weight-normal text-center align-middle"} scope="col">{t("course.paid")}</th>
                                <th className={"font-weight-normal text-center align-middle"} scope="col">{t("action")}</th>
                            </tr>
                            </thead>
                            <tbody>

                            {data.length > 0 && data.map((item) => (
                                <tr>
                                    <td className={"text-center align-middle"}>{item.firstname}</td>
                                    <td className={"text-center align-middle"}>{item.lastname}</td>
                                    <td className={"text-center align-middle"}>{item.price} {t("currency")}</td>
                                    <td className={"text-center align-middle"}>{item.paid} {t("currency")}</td>

                                    <td className={"text-center align-middle"}>
                                        <div className="d-flex justify-content-center align-items-center">
                                            <div className="btn-group mb-1">
                                                <button className="btn btn-dark btn-block dim"
                                                        onClick={() => history.push("/addPayment?login=" + item.login)}>
                                                    {t('underpayments.add.payment')}
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

export default withNamespaces()(Underpayments);