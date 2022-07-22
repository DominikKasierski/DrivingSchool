import React from "react";

import {withNamespaces} from "react-i18next";
import {useLocale} from "../../utils/login/LoginProvider";
import Breadcrumb from "../../bars/Breadcrumb";
import {Link, useLocation} from "react-router-dom";
import {Col, Container, Row, Table} from "react-bootstrap";
import {useState, useEffect} from "react";
import queryString from "query-string";
import axios from "axios";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {useDangerNotification, useSuccessNotification} from "../../utils/notifications/NotificationProvider";
import moment from "moment";
import TimetableEvent from "../../utils/customs/TimetableEvent";
import {getDays} from "../../../i18n";

function AddLecture(props) {
    const {t, i18n} = props
    const {token, setToken} = useLocale();
    const [etag, setEtag] = useState();
    const [startDate, setStartDate] = useState(new Date());
    const [endDate, setEndDate] = useState(new Date());
    const location = useLocation();
    const parsedQuery = queryString.parse(location.search);
    const eventSchema = [
        {
            id: "",
            name: "",
            type: "",
            startTime: "",
            endTime: "",
        }
    ];
    const [mondayEvents, setMondayEvents] = useState([eventSchema]);
    const [tuesdayEvents, setTuesdayEvents] = useState([eventSchema]);
    const [wednesdayEvents, setWednesdayEvents] = useState([eventSchema]);
    const [thursdayEvents, setThursdayEvents] = useState([eventSchema]);
    const [fridayEvents, setFridayEvents] = useState([eventSchema]);
    let days = [t("monday"), t("tuesday"), t("wednesday"), t("thursday"), t("friday"), t("saturday"), t("sunday")];
    const [dateHeading, setDateHeading] = useState("");

    const dispatchDangerNotification = useDangerNotification();
    const dispatchSuccessNotification = useSuccessNotification();

    useEffect(() => {
        if (token) {
            getGroupCalendar(new Date());
            getEtag();
        }
    }, [token]);

    function getGroupCalendar(date, notification = false) {
        setDates(date);
        axios.get(`/resources/lectureGroup/getGroupCalendar/` + parsedQuery.id + "/" + moment(startOfWeek(date)).unix(), {
            headers: {
                "Authorization": token,
            }
        }).then((res) => {
            setMondayEvents(res.data.mondayEvents);
            setTuesdayEvents(res.data.tuesdayEvents);
            setWednesdayEvents(res.data.wednesdayEvents);
            setThursdayEvents(res.data.thursdayEvents);
            setFridayEvents(res.data.fridayEvents);
            if (notification) {
                dispatchSuccessNotification({message: i18n.t('lecture.groups.change.week.success')})
            }
        }).catch((e) => ResponseErrorsHandler(e, dispatchDangerNotification));
    }

    function startOfWeek(date) {
        return new Date(moment(date).startOf('isoWeek').toDate());
    }

    function endOfWeek(date) {
        return new Date(moment(date).endOf('isoWeek').toDate());
    }

    function setDates(date) {
        let dateOfFirstDayOfWeek = startOfWeek(date);
        let dateOfLastDayOfWeek = endOfWeek(dateOfFirstDayOfWeek);
        setDateHeading(padTo2Digits(dateOfFirstDayOfWeek.getDate()) + "/" + padTo2Digits(dateOfFirstDayOfWeek.getMonth() + 1) + "/" +
            dateOfFirstDayOfWeek.getFullYear() + " - " + padTo2Digits(dateOfLastDayOfWeek.getDate()) + "/" +
            padTo2Digits(dateOfLastDayOfWeek.getMonth() + 1) + "/" + dateOfLastDayOfWeek.getFullYear());
    }

    function padTo2Digits(num) {
        return num.toString().padStart(2, '0');
    }

    function getEtag() {
        axios.get(`/resources/lectureGroup/getLectureGroup/` + parsedQuery.id, {
            headers: {
                "Authorization": token,
            }
        }).then((res) => {
            setEtag(res.headers.etag);
        }).catch((e) => ResponseErrorsHandler(e, dispatchDangerNotification));
    }

    return (
        <div className="container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link></li>
                <li className="breadcrumb-item"><Link className={"text-dark"}
                                                      to="/lectureGroups">{t("navigation.bar.lecture.groups")}</Link></li>
                <li className="breadcrumb-item active text-secondary"
                    aria-current="page">{t("lecture.groups.add.lecture")}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={12} md={12} lg={12} xl={12} className={"floating pt-2 pb-0 mx-auto mb-5 mt-3"}>
                        <div className="py-2">
                            <Row className="text-center">
                                <Col className="mb-sm-3">
                                    <h1 className="font-weight-light">{t("lecture.groups.add.lecture")}</h1>
                                </Col>
                            </Row>
                            <Row className="text-center">
                                <Col className="mt-1 mb-2 d-inline-block">
                                    <button className="btn btn-dark dim" type="submit" onClick={() => {
                                        let dateString = dateHeading.substring(0, 10).replaceAll("/", "-");
                                        const date = new Date(moment(dateString, 'DD-MM-YYYY').add(1, 'm').subtract(1, 'd').add(1, 'h').toDate());
                                        debugger;
                                        getGroupCalendar(date, true);
                                    }
                                    }>
                                        {i18n.t("lecture.groups.previous.week")}
                                    </button>

                                    <span style={{fontSize: "1.2rem"}} className={"mx-4"}>{dateHeading}</span>

                                    <button className="btn btn-dark dim" type="submit" onClick={() => {
                                        let dateString = dateHeading.substring(0, 10).replaceAll("/", "-");
                                        const date = new Date(moment(dateString, 'DD-MM-YYYY').add(1, 'm').add(8, 'd').toDate());
                                        getGroupCalendar(date, true);
                                    }
                                    }>
                                        {i18n.t("lecture.groups.next.week")}
                                    </button>
                                </Col>
                            </Row>
                            <Row className="text-center mt-4 mb-2 px-2" style={{fontSize: "1.2rem"}}>
                                <Col className="dim py-2">
                                    <span>{days[0]}</span>
                                </Col>
                                <Col className="dim py-2">
                                    <span>{days[1]}</span>
                                </Col>
                                <Col className="dim py-2">
                                    <span>{days[2]}</span>
                                </Col>
                                <Col className="dim py-2">
                                    <span>{days[3]}</span>
                                </Col>
                                <Col className="dim py-2">
                                    <span>{days[4]}</span>
                                </Col>
                                <Col className="dim py-2">
                                    <span>{days[5]}</span>
                                </Col>
                                <Col className="dim py-2">
                                    <span>{days[6]}</span>
                                </Col>

                                <div className="divider"></div>
                            </Row>
                            <Row>
                                <Col className="mt-2 mb-2">
                                    {mondayEvents.length > 0 && mondayEvents.map((item) => (
                                        <TimetableEvent id={item.name}/>
                                    ))}
                                </Col>

                                <Col className="mt-2 mb-2">
                                    {tuesdayEvents.length > 0 && tuesdayEvents.map((item) => (
                                        <TimetableEvent id={item.name}/>
                                    ))}
                                </Col>

                                <Col className="mt-2 mb-2">
                                    {wednesdayEvents.length > 0 && wednesdayEvents.map((item) => (
                                        <TimetableEvent id={item.name}/>
                                    ))}
                                </Col>

                                <Col className="mt-2 mb-2">
                                    {thursdayEvents.length > 0 && thursdayEvents.map((item) => (
                                        <TimetableEvent id={item.name}/>
                                    ))}
                                </Col>
                                <Col className="mt-2 mb-2">
                                    {fridayEvents.length > 0 && fridayEvents.map((item) => (
                                        <TimetableEvent id={item.name}/>
                                    ))}
                                </Col>
                                <Col className="mt-2 mb-2">
                                </Col>
                                <Col className="mt-2 mb-2">
                                </Col>
                            </Row>
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default withNamespaces()(AddLecture);
