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
    let days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];
    const [dateHeading, setDateHeading] = useState("");

    const dispatchDangerNotification = useDangerNotification();

    useEffect(() => {
        if (token) {
            debugger;
            getGroupCalendar(new Date());
            getEtag();
        }
    }, [token]);

    function getGroupCalendar(date) {
        debugger
        setDates(date);
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
        }).catch((e) => ResponseErrorsHandler(e, dispatchDangerNotification));
    }

    function startOfWeek(date) {
        let dupa = new Date(moment(date).startOf('isoWeek').toDate());
        return dupa;
    }

    function endOfWeek(date) {
        let dupa = new Date(moment(date).endOf('isoWeek').toDate());
        return dupa;
    }

    function setDates(date) {
        let dateOfFirstDayOfWeek = startOfWeek(date);
        let dateOfLastDayOfWeek = endOfWeek(dateOfFirstDayOfWeek);
        debugger;
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
                <li className="breadcrumb-item active text-secondary"
                    aria-current="page">{t("lecture.groups.add.lecture")}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={12} md={10} lg={10} xl={10} className={"floating pt-2 pb-0 mx-auto mb-5 mt-3"}>
                        <div className="py-2">
                            <Row className="text-center">
                                <Col className="mb-sm-3">
                                    <h1 className="font-weight-light">{t("lecture.groups.add.lecture")}</h1>
                                </Col>
                            </Row>

                            {/*<Row className="text-center">*/}
                            {/*    <div className="col-12 text-center mt-2">*/}
                            {/*        <span>{t("add.lecture.begin.date")}</span>*/}
                            {/*    </div>*/}
                            {/*</Row>*/}

                            {/*<Row className="text-center mb-3">*/}
                            {/*    <Col className="d-flex justify-content-center text-center">*/}
                            {/*        <CustomDatePicker setPickDate={setStartDate}*/}
                            {/*                          pickDate={startDate}*/}
                            {/*                          setEndDate={setEndDate}*/}
                            {/*                          currentEndDate={endDate}*/}
                            {/*                          className={"date-picker-custom mt-0"}*/}
                            {/*                          time={false}/>*/}
                            {/*    </Col>*/}
                            {/*</Row>*/}

                            {/*<Row className="justify-content-center">*/}
                            {/*    <Col sm={7} className="mt-4 mb-4">*/}
                            {/*        <button className="btn btn-block btn-dark dim"*/}
                            {/*                type="submit" disabled={!startDate || !endDate}*/}
                            {/*                onClick={() => getGroupCalendar(startDate)}>*/}
                            {/*            {i18n.t('add.lecture.display.calendar')}*/}
                            {/*        </button>*/}
                            {/*    </Col>*/}
                            {/*</Row>*/}
                            <Row className="text-center">
                                <Col className="mt-1 mb-2 d-inline-block">
                                    <button className="btn btn-dark dim" type="submit" onClick={() => {
                                        let dateString = dateHeading.substring(0, 10).replaceAll("/", "-");
                                        const date = new Date(moment(dateString, 'DD-MM-YYYY').subtract(1, 'd').add(1, 'h').toDate());
                                        debugger;
                                        getGroupCalendar(date);
                                    }
                                    }>
                                        Poprzedni tydzień
                                    </button>

                                    <span style={{fontSize: "1.2rem"}} className={"mx-4"}>{dateHeading}</span>

                                    <button className="btn btn-dark dim" type="submit" onClick={() => {
                                        let dateString = dateHeading.substring(0, 10).replaceAll("/", "-");
                                        const date = new Date(moment(dateString, 'DD-MM-YYYY').add(8, 'd').toDate());
                                        getGroupCalendar(date);
                                    }
                                    }>
                                        Następny tydzień
                                    </button>
                                </Col>
                            </Row>
                            <Row className="text-center">
                                <Col className="mt-4 mb-2">
                                    <Table striped>
                                        <thead>
                                        <tr>
                                            <th>{days[0]}</th>
                                            <th>{days[1]}</th>
                                            <th>{days[2]}</th>
                                            <th>{days[3]}</th>
                                            <th>{days[4]}</th>
                                            <th>{days[5]}</th>
                                            <th>{days[6]}</th>
                                        </tr>
                                        </thead>
                                    </Table>
                                </Col>
                            </Row>
                            <Row>
                                <Col className="mt-2 mb-2">
                                    {mondayEvents.length > 1 && mondayEvents.map((item) => (
                                        <TimetableEvent id={item.id}/>
                                    ))}
                                </Col>
                            </Row>
                            <Row>
                                <Col className="mt-2 mb-2">
                                    {tuesdayEvents.length > 1 && tuesdayEvents.map((item) => (
                                        <TimetableEvent id={item.id}/>
                                    ))}
                                </Col>
                            </Row>
                            <Row>
                                <Col className="mt-2 mb-2">
                                    {wednesdayEvents.length > 1 && wednesdayEvents.map((item) => (
                                        <TimetableEvent id={item.id}/>
                                    ))}
                                </Col>
                            </Row>
                            <Row>
                                <Col className="mt-2 mb-2">
                                    {thursdayEvents.length > 1 && thursdayEvents.map((item) => (
                                        <TimetableEvent id={item.id}/>
                                    ))}
                                </Col>
                            </Row>
                            <Row>
                                <Col className="mt-2 mb-2">
                                    {fridayEvents.length > 1 && fridayEvents.map((item) => (
                                        <TimetableEvent id={item.id}/>
                                    ))}
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
