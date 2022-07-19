import React from "react";

import {withNamespaces} from "react-i18next";
import CustomTimetable from "../../utils/customs/CustomTimetable";
import {useLocale} from "../../utils/login/LoginProvider";
import Breadcrumb from "../../bars/Breadcrumb";
import {Link, useLocation} from "react-router-dom";
import {Card, CardGroup, Col, Container, Row, Table} from "react-bootstrap";
import CustomDatePicker from "../../utils/customs/CustomDatePicker";
import {useState, useEffect} from "react";
import queryString from "query-string";
import axios from "axios";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {useDangerNotification} from "../../utils/notifications/NotificationProvider";
import moment from "moment";
import TimetableEvent from "../../utils/customs/TimetableEvent";


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
    let days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"];
    let dateOfFirstDayOfWeek = new Date();
    let dateOfLastDayOfWeek = new Date();
    let dateHeading = "";

    const dispatchDangerNotification = useDangerNotification();

    useEffect(() => {
        if (token) {
            getGroupCalendar(new Date());
            getEtag();
        }
    }, [token]);

    function getGroupCalendar(date) {
        axios.get(`/resources/lectureGroup/getGroupCalendar/` + parsedQuery.id + "/" + startOfWeek(date).getTime(), {
            headers: {
                "Authorization": token,
            }
        }).then((res) => {
            setMondayEvents(res.data.mondayEvents);
            setTuesdayEvents(res.data.tuesdayEvents);
            setWednesdayEvents(res.data.wednesdayEvents);
            setThursdayEvents(res.data.thursdayEvents);
            setFridayEvents(res.data.fridayEvents);
            dateHeading = setDates(date);
        }).catch((e) => ResponseErrorsHandler(e, dispatchDangerNotification));
    }

    function startOfWeek(date) {
        return moment(date).startOf('isoWeek').toDate();
    }

    function endOfWeek(date) {
        return moment(date).endOf('isoWeek').toDate();
    }

    function setDates(date) {
        dateOfFirstDayOfWeek = startOfWeek(date);
        dateOfLastDayOfWeek = endOfWeek(dateOfFirstDayOfWeek);

        return padTo2Digits(dateOfFirstDayOfWeek.getDate()) + "/" + padTo2Digits(dateOfFirstDayOfWeek.getMonth()) + "/" +
            dateOfFirstDayOfWeek.getFullYear() + " - " + padTo2Digits(dateOfLastDayOfWeek.getDate()) + "/" +
            padTo2Digits(dateOfLastDayOfWeek.getMonth()) + "/" + dateOfLastDayOfWeek.getFullYear();
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
                <li className="breadcrumb-item active text-secondary"
                    aria-current="page">{t("lecture.groups.add.lecture")}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={12} md={10} lg={9} xl={9} className={"floating pt-2 pb-0 mx-auto mb-5 mt-3"}>
                        <div className="py-2">
                            <Row className="text-center">
                                <Col className="mb-sm-3">
                                    <h1 className="font-weight-light">{t("lecture.groups.add.lecture")}</h1>
                                </Col>
                            </Row>
                            <Row className="text-center">
                                <Col className="d-flex justify-content-center text-center">
                                    <CustomDatePicker setPickDate={setStartDate}
                                                      pickDate={startDate}
                                                      setEndDate={setEndDate}
                                                      currentEndDate={endDate}
                                                      time={false}
                                                      label={i18n.t('begin.date')}/>
                                </Col>
                            </Row>
                            <Row className="justify-content-center">
                                <Col sm={7} className="mt-5 mb-5">
                                    <button className="btn btn-block btn-dark dim"
                                            type="submit" disabled={!startDate || !endDate}
                                            onClick={() => getGroupCalendar(startDate)}>
                                        {i18n.t('add.lecture.display.calendar')}
                                    </button>
                                </Col>
                            </Row>
                            {dateHeading !== "" &&
                                <>
                                    <Row className="text-center">
                                        <Col className="mt-2 mb-2 d-inline-block">
                                            <button className="btn btn-dark dim" type="submit" onClick={() => {
                                                getGroupCalendar(dateOfFirstDayOfWeek.getDate() - 7);
                                            }
                                            }>
                                                Poprzedni tydzień
                                            </button>

                                            <span className={"mx-5"}>{dateHeading}</span>

                                            <button className="btn btn-dark dim" type="submit" onClick={() => {
                                                getGroupCalendar(dateOfFirstDayOfWeek.getDate() + 7);
                                            }
                                            }>
                                                Następny tydzień
                                            </button>
                                        </Col>
                                    </Row>
                                    <Row className="text-center">
                                        <Col className="mt-2 mb-2">
                                            <Table striped>
                                                <thead>
                                                <tr>
                                                    <th>{"Jacek"}</th>
                                                    <th>{"Jacek"}</th>
                                                    <th>{"Jacek"}</th>
                                                    <th>{"Jacek"}</th>
                                                    <th>{"Jacek"}</th>
                                                </tr>
                                                </thead>
                                            </Table>
                                        </Col>
                                    </Row>
                                    <Row>
                                        <Col className="mt-2 mb-2">
                                            {mondayEvents.length > 0 && mondayEvents.map((item) => (
                                                <TimetableEvent id={item.id}/>
                                            ))}
                                        </Col>
                                    </Row>
                                    <Row>
                                        <Col className="mt-2 mb-2">
                                            {tuesdayEvents.length > 0 && tuesdayEvents.map((item) => (
                                                <TimetableEvent id={item.id}/>
                                            ))}
                                        </Col>
                                    </Row>
                                    <Row>
                                        <Col className="mt-2 mb-2">
                                            {wednesdayEvents.length > 0 && wednesdayEvents.map((item) => (
                                                <TimetableEvent id={item.id}/>
                                            ))}
                                        </Col>
                                    </Row>
                                    <Row>
                                        <Col className="mt-2 mb-2">
                                            {thursdayEvents.length > 0 && thursdayEvents.map((item) => (
                                                <TimetableEvent id={item.id}/>
                                            ))}
                                        </Col>
                                    </Row>
                                    <Row>
                                        <Col className="mt-2 mb-2">
                                            {fridayEvents.length > 0 && fridayEvents.map((item) => (
                                                <TimetableEvent id={item.id}/>
                                            ))}
                                        </Col>
                                    </Row>
                                </>
                            }
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default withNamespaces()(AddLecture);
