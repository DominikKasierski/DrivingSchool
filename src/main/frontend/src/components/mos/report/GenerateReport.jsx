import {withNamespaces} from "react-i18next";
import {useState} from "react";
import {useLocale} from "../../utils/login/LoginProvider";
import {
    useDangerNotification,
    useSuccessNotification,
    useWarningNotification
} from "../../utils/notifications/NotificationProvider";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import moment from 'moment';
import 'moment/locale/pl';
import 'moment/locale/en-gb';
import 'react-datepicker/dist/react-datepicker.css';
import axios from "axios";
import Breadcrumb from "../../bars/Breadcrumb";
import {Link} from "react-router-dom";
import {Col, Container, Row} from "react-bootstrap";
import CustomDatePicker from "../../utils/customs/CustomDatePicker";

function GenerateReport(props) {
    const {t, i18n} = props
    const {token} = useLocale();
    const [startDate, setStartDate] = useState(new Date());
    const [endDate, setEndDate] = useState(new Date());
    const [data, setData] = useState([
        {
            id: "",
            creationDate: "",
            paymentStatus: "",
            value: "",
            firstname: "",
            lastname: "",
        }
    ]);

    const dispatchSuccessNotification = useSuccessNotification();
    const dispatchDangerNotification = useDangerNotification();
    const dispatchWarningNotification = useWarningNotification();

    const momentHelper = () => {
        return i18n.language === "pl" ? 'pl' : 'en';
    }

    const handleGenerateReport = e => {
        axios.get(`/resources/payment/generateReport/` + moment(startDate).unix() + "/" + moment(endDate).unix(), {
            headers: {
                "Authorization": token,
            }
        }).then(res => {
            setData(res.data.payments);
            if (res.data.count !== 0) {
                dispatchSuccessNotification({message: i18n.t('generate.report.success')})
            } else {
                dispatchWarningNotification({message: i18n.t('generate.report.no.results')})
            }
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification);
        });
    }

    return (
        <div className="container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link></li>
                <li className="breadcrumb-item active text-secondary"
                    aria-current="page">{t("navigation.bar.generate.report")}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={12} md={11} lg={10} xl={10} className={"floating pt-2 pb-0 mx-auto mb-5 mt-3"}>
                        <div className="pt-2">
                            <Row className="text-center">
                                <Col className="mb-sm-3">
                                    <h1 className="font-weight-light">{t("generate.report.title")}</h1>
                                </Col>
                            </Row>
                            <Row className="text-center">
                                <Col className="d-flex justify-content-center">
                                    <CustomDatePicker setPickDate={setStartDate}
                                                      pickDate={startDate}
                                                      setEndDate={setEndDate}
                                                      currentEndDate={endDate}
                                                      report={true}
                                                      label={i18n.t('date.from')}/>
                                    <CustomDatePicker setPickDate={setEndDate}
                                                      pickDate={endDate}
                                                      minDate={startDate}
                                                      report={true}
                                                      label={i18n.t('date.to')}/>
                                </Col>
                            </Row>
                            <Row className="justify-content-center">
                                <Col sm={6} className="my-5">
                                    <button className="btn btn-block btn-dark dim"
                                            type="submit" onClick={handleGenerateReport} disabled={!startDate || !endDate}>
                                        {i18n.t('generate.report.generate')}
                                    </button>
                                </Col>
                            </Row>
                            <Row className="justify-content-center">
                                <table className="table table-hover table-bordered">
                                    <thead className="dim">
                                    {data.length > 1 &&
                                        <tr>
                                            <th className={"font-weight-normal text-center"} scope="col">{t("paymentStatus")}</th>
                                            <th className={"font-weight-normal text-center"} scope="col">{t("creationDate")}</th>
                                            <th className={"font-weight-normal text-center"} scope="col">{t("value")}</th>
                                            <th className={"font-weight-normal text-center"} scope="col">{t("firstname")}</th>
                                            <th className={"font-weight-normal text-center"} scope="col">{t("lastname")}</th>
                                        </tr>
                                    }
                                    </thead>
                                    <tbody>

                                    {data.length > 1 && data.sort(function (a, b) {
                                        return new Date(b.creationDate) - new Date(a.creationDate);
                                    }).map((item) => (
                                        <tr>
                                            <td className={"text-center align-middle"}>{t(item.paymentStatus)}</td>
                                            <td className={"text-center align-middle"}>{item.creationDate !== undefined && item.creationDate !== "" ?
                                                moment(item.creationDate).locale(momentHelper()).local().format('LLLL').toString() : "-"}</td>
                                            <td className={"text-center align-middle"}>{item.value} {t("currency")}</td>
                                            <td className={"text-center align-middle"}>{item.firstname}</td>
                                            <td className={"text-center align-middle"}>{item.lastname}</td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </Row>
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    );

}

export default withNamespaces()(GenerateReport)