import {withNamespaces} from "react-i18next";
import Breadcrumb from "../../bars/Breadcrumb";
import {Link} from "react-router-dom";
import {Col, Container, Dropdown, Row} from "react-bootstrap";
import moment from "moment";
import TimetableEvent from "../../utils/customs/TimetableEvent";
import DropdownToggle from "react-bootstrap/DropdownToggle";
import CustomDatePicker from "../../utils/customs/CustomDatePicker";
import {useState, useEffect} from "react";
import axios from "axios";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {useLocale} from "../../utils/login/LoginProvider";
import {useDangerNotification, useSuccessNotification} from "../../utils/notifications/NotificationProvider";
import {rolesConstant} from "../../utils/constants/Constants";

function Timetable(props) {
    const {t, i18n} = props
    const {token, setToken, username, currentRole} = useLocale();
    const [courseEtag, setCourseEtag] = useState();
    const [instructorEtag, setInstructorEtag] = useState();
    const eventSchema = [
        {
            id: "",
            title: "",
            participant: "",
            startTime: "",
            endTime: "",
        }
    ];
    const [dateHeading, setDateHeading] = useState("");
    const [mondayEvents, setMondayEvents] = useState([eventSchema]);
    const [tuesdayEvents, setTuesdayEvents] = useState([eventSchema]);
    const [wednesdayEvents, setWednesdayEvents] = useState([eventSchema]);
    const [thursdayEvents, setThursdayEvents] = useState([eventSchema]);
    const [fridayEvents, setFridayEvents] = useState([eventSchema]);
    let days = [t("monday"), t("tuesday"), t("wednesday"), t("thursday"), t("friday"), t("saturday"), t("sunday")];
    const [instructor, setInstructor] = useState("");
    const [instructorsData, setInstructorsData] = useState([
        {
            login: "",
            firstname: "",
            lastname: "",
            permissions: "",
        }
    ]);
    const [startDate, setStartDate] = useState(new Date().setMinutes(0));
    const [endDate, setEndDate] = useState(new Date().setMinutes(0));

    const dispatchDangerNotification = useDangerNotification();
    const dispatchSuccessNotification = useSuccessNotification();

    useEffect(() => {
        if (token && currentRole === rolesConstant.trainee) {
            getInstructors();
            getCalendar(new Date(), true);
            getCourseEtag();
        } else if (token && currentRole === rolesConstant.instructor) {
            getCalendar(new Date(), false);
            getInstructorEtag();
        }
    }, [token]);

    function getInstructors() {
        axios.get(`/resources/access/getInstructors`, {
            headers: {
                "Authorization": token,
            }
        }).then((res) => {
            setInstructorsData(res.data)
        }).catch((e) => ResponseErrorsHandler(e, dispatchDangerNotification));
    }

    function getCalendar(date, trainee = true, notification = false) {
        setDates(date);
        axios.get(`/resources/course/getCalendar/` + username + "/" + moment(startOfWeek(date)).unix() + "/" + trainee, {
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
                dispatchSuccessNotification({message: i18n.t('add.lecture.change.week.success')})
            }
        }).catch((e) => ResponseErrorsHandler(e, dispatchDangerNotification));
    }

    function getCourseEtag() {
        axios.get(`/resources/course/getCourse`, {
            headers: {
                "Authorization": token,
            }
        }).then((res) => {
            setCourseEtag(res.headers.etag);
        }).catch((e) => ResponseErrorsHandler(e, dispatchDangerNotification));
    }

    function getInstructorEtag() {
        axios.get(`/resources/access/getInstructorAccess/` + username, {
            headers: {
                "Authorization": token,
            }
        }).then((res) => {
            setInstructorEtag(res.headers.etag);
        }).catch((e) => ResponseErrorsHandler(e, dispatchDangerNotification));
    }

    function setDates(date) {
        let dateOfFirstDayOfWeek = startOfWeek(date);
        let dateOfLastDayOfWeek = endOfWeek(dateOfFirstDayOfWeek);
        setDateHeading(padTo2Digits(dateOfFirstDayOfWeek.getDate()) + "/" + padTo2Digits(dateOfFirstDayOfWeek.getMonth() + 1) + "/" +
            dateOfFirstDayOfWeek.getFullYear() + " - " + padTo2Digits(dateOfLastDayOfWeek.getDate()) + "/" +
            padTo2Digits(dateOfLastDayOfWeek.getMonth() + 1) + "/" + dateOfLastDayOfWeek.getFullYear());
    }

    function startOfWeek(date) {
        return new Date(moment(date).startOf('isoWeek').toDate());
    }

    function endOfWeek(date) {
        return new Date(moment(date).endOf('isoWeek').toDate());
    }

    function padTo2Digits(num) {
        return num.toString().padStart(2, '0');
    }

    function addDrivingLesson() {

    };

    return (
        <div className="container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link></li>
                <li className="breadcrumb-item active text-secondary" aria-current="page">{t("navigation.bar.timetable")}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={12} md={12} lg={12} xl={12} className={"floating pt-2 pb-0 mx-auto mb-5 mt-3"}>

                        <div className="py-2">

                            <div className="col-md-12 d-flex align-items-center justify-content-between ml-4">
                                <div className="ml-5">
                                </div>

                                <h1 className="font-weight-light ml-5">{t("navigation.bar.timetable")}</h1>

                                <Dropdown>
                                    <DropdownToggle id="dropdown-basic" className="pl-0 pl-lg-2 pr-0 pr-lg-2 dim"
                                                    variant="Secondary">
                                        <span>{instructor ? instructor : t("widok.instruktora")}</span>
                                    </DropdownToggle>
                                    <Dropdown.Menu>
                                        {instructorsData.length > 0 && instructorsData.map((item) => (
                                            <Dropdown.Item as="button"
                                                           onClick={() => {
                                                               setInstructor(item.firstname + " " + item.lastname);
                                                           }}>{item.firstname + " " + item.lastname}</Dropdown.Item>))}
                                    </Dropdown.Menu>
                                </Dropdown>
                            </div>

                            <Row className="text-center">
                                <Col className="mt-1 mb-2 d-inline-block">
                                    <button className="btn btn-dark dim" type="submit" onClick={() => {
                                        let dateString = dateHeading.substring(0, 10).replaceAll("/", "-");
                                        const date = new Date(moment(dateString, 'DD-MM-YYYY').add(1, 'm').subtract(1, 'd').add(1, 'h').toDate());
                                        getCalendar(date, true, true);
                                    }}>
                                        {i18n.t("add.lecture.previous.week")}
                                    </button>

                                    <span style={{fontSize: "1.2rem"}} className={"mx-4"}>{dateHeading}</span>

                                    <button className="btn btn-dark dim" type="submit" onClick={() => {
                                        let dateString = dateHeading.substring(0, 10).replaceAll("/", "-");
                                        const date = new Date(moment(dateString, 'DD-MM-YYYY').add(1, 'm').add(8, 'd').toDate());
                                        getCalendar(date, true, true);
                                    }}>
                                        {i18n.t("add.lecture.next.week")}
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
                            </Row>
                            <Row>
                                <Col>
                                    {mondayEvents.length > 0 && mondayEvents.map((item) => (
                                        <TimetableEvent title={item.title} startTime={item.startTime} endTime={item.endTime}
                                                        participant={item.participant}
                                                        labelsType={currentRole === rolesConstant.trainee ? 2 : 3}/>))}
                                </Col>

                                <Col>
                                    {tuesdayEvents.length > 0 && tuesdayEvents.map((item) => (
                                        <TimetableEvent title={item.title} startTime={item.startTime} endTime={item.endTime}
                                                        participant={item.participant}
                                                        labelsType={currentRole === rolesConstant.trainee ? 2 : 3}/>))}
                                </Col>

                                <Col>
                                    {wednesdayEvents.length > 0 && wednesdayEvents.map((item) => (
                                        <TimetableEvent title={item.title} startTime={item.startTime} endTime={item.endTime}
                                                        participant={item.participant}
                                                        labelsType={currentRole === rolesConstant.trainee ? 2 : 3}/>))}
                                </Col>

                                <Col>
                                    {thursdayEvents.length > 0 && thursdayEvents.map((item) => (
                                        <TimetableEvent title={item.title} startTime={item.startTime} endTime={item.endTime}
                                                        participant={item.participant}
                                                        labelsType={currentRole === rolesConstant.trainee ? 2 : 3}/>))}
                                </Col>
                                <Col>
                                    {fridayEvents.length > 0 && fridayEvents.map((item) => (
                                        <TimetableEvent title={item.title} startTime={item.startTime} endTime={item.endTime}
                                                        participant={item.participant}
                                                        labelsType={currentRole === rolesConstant.trainee ? 2 : 3}/>))}
                                </Col>
                                <Col>
                                </Col>
                                <Col>
                                </Col>
                            </Row>
                            {currentRole === rolesConstant.trainee ?
                                <Row className="mt-1 align-items-center">
                                    <div className="col">
                                        <hr/>
                                    </div>
                                    <div className="col-auto font-weight-bold">{i18n.t("timetable.add.driving.lesson")}</div>
                                    <div className="col">
                                        <hr/>
                                    </div>
                                </Row> :
                                <Row className="mt-1 align-items-center">
                                    <div className="col">
                                        <hr/>
                                    </div>
                                    <div className="col-auto font-weight-bold">{i18n.t("timetable.add.driving.lesson")}</div>
                                    <div className="col">
                                        <hr/>
                                    </div>
                                </Row>
                            }
                            <Row className="text-center">
                                <Col className="text-center my-3">
                                    <Dropdown>
                                        <DropdownToggle id="dropdown-basic" className="pl-0 pl-lg-2 pr-0 pr-lg-2 dim"
                                                        variant="Secondary">
                                            <span>{instructor ? instructor : t("add.lecture.select.instructor")}</span>
                                        </DropdownToggle>
                                        <Dropdown.Menu>
                                            {instructorsData.length > 0 && instructorsData.map((item) => (
                                                <Dropdown.Item as="button"
                                                               onClick={() => {
                                                                   setInstructor(item.firstname + " " + item.lastname);
                                                               }}>{item.firstname + " " + item.lastname}</Dropdown.Item>))}
                                        </Dropdown.Menu>
                                    </Dropdown>
                                </Col>
                            </Row>

                            <Row className="text-center">
                                <Col className="d-flex justify-content-center">
                                    <CustomDatePicker setPickDate={setStartDate}
                                                      pickDate={startDate}
                                                      setEndDate={setEndDate}
                                                      currentEndDate={endDate}
                                                      className={"date-picker-custom mt-0 mr-1"}
                                                      label={i18n.t("add.lecture.add.start.date")}/>
                                    <CustomDatePicker setPickDate={setEndDate}
                                                      pickDate={endDate}
                                                      minDate={startDate}
                                                      className={"date-picker-custom mt-0 ml-1"}
                                                      label={i18n.t("add.lecture.add.end.date")}/>
                                </Col>
                            </Row>

                            <Row className="justify-content-center">
                                <Col sm={6} className="mt-4 mb-3">
                                    <button className="btn btn-block btn-dark dim"
                                            type="submit" disabled={!startDate || !endDate} onClick={() => addDrivingLesson()}>
                                        {i18n.t('add')}
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

export default withNamespaces()(Timetable);