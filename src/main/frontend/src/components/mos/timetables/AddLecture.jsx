import React from "react";

import {withNamespaces} from "react-i18next";
import CustomTimetable from "../../utils/customs/CustomTimetable";
import {useLocale} from "../../utils/login/LoginProvider";
import Breadcrumb from "../../bars/Breadcrumb";
import {Link, useLocation} from "react-router-dom";
import {Col, Container, Row} from "react-bootstrap";
import CustomDatePicker from "../../utils/customs/CustomDatePicker";
import {useState} from "react";
import queryString from "query-string";
import {useEffect} from "@types/react";
import axios from "axios";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {useDangerNotification} from "../../utils/notifications/NotificationProvider";

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
    let days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];
    let dateOfFirstDayOfWeek = new Date();
    let dateOfLastDayOfWeek = new Date();

    const dispatchDangerNotification = useDangerNotification();


    useEffect(() => {
        if (token) {
            dateOfFirstDayOfWeek = startOfWeek(dateOfFirstDayOfWeek);
            dateOfLastDayOfWeek = (dateOfFirstDayOfWeek.getDate() + 7);
            getGroupCalendar();
            getEtag();
        }
    }, [token]);

    function startOfWeek(date) {
        let diff = date.getDate() - date.getDay() + (date.getDay() === 0 ? -6 : 1);
        return new Date(date.setDate(diff));
    }

    function getGroupCalendar() {
        axios.get(`/resources/lectureGroup/getGroupCalendar/` + parsedQuery.id + "/" + dateOfFirstDayOfWeek, {
            headers: {
                "Authorization": token,
            }
        }).then((res) => {
            setMondayEvents(res.data.mondayEvents);
            setTuesdayEvents(res.data.tuesdayEvents);
            setWednesdayEvents(res.data.wednesdayEvents);
            setThursdayEvents(res.data.thursdayEvents);
            setFridayEvents(res.data.fridayEvents);
        }).catch((e) => ResponseErrorsHandler(e, dispatchDangerNotification));
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
                <li className="breadcrumb-item active text-secondary"
                    aria-current="page">{t("navigation.bar.instructors.statistics")}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={12} md={10} lg={9} xl={9} className={"floating pt-2 pb-0 mx-auto mb-5 mt-3"}>
                        <div className="py-2">
                            <Row className="text-center">
                                <Col className="mb-sm-3">
                                    <h1 className="font-weight-light">{t("instructors.statistics.title")}</h1>
                                </Col>
                            </Row>
                            <Row className="text-center">
                                <Col sm={7} className="mt-2 mb-2">
                                    <CustomTimetable days={days}/>
                                </Col>
                            </Row>
                            <Row className="text-center">
                                <Col className="d-flex justify-content-center text-center">
                                    <CustomDatePicker setPickDate={setStartDate}
                                                      pickDate={startDate}
                                                      setEndDate={setEndDate}
                                                      currentEndDate={endDate}
                                                      label={i18n.t('date.from')}/>
                                    <CustomDatePicker setPickDate={setEndDate}
                                                      pickDate={endDate}
                                                      minDate={startDate}
                                                      label={i18n.t('date.to')}/>
                                </Col>
                            </Row>
                            <Row className="justify-content-center">
                                <Col sm={7} className="mt-5 mb-5">
                                    <button className="btn btn-block btn-dark dim"
                                            type="submit" onClick={fetchData} disabled={!startDate || !endDate}>
                                        {i18n.t('instructors.statistics.display.statistics')}
                                    </button>
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
