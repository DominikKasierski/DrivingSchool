import {useLocale} from "../../utils/login/LoginProvider";
import {useDangerNotification, useSuccessNotification} from "../../utils/notifications/NotificationProvider";
import {Link, useHistory} from "react-router-dom";
import Breadcrumb from "../../bars/Breadcrumb";
import {Accordion, Button, Card, Col, Container, ListGroup, Row, Tab, Tabs} from "react-bootstrap";
import {withNamespaces} from "react-i18next";
import {useEffect, useState} from "react";
import axios from "axios";
import {usePermanentChangeDialog} from "../../utils/dialogs/DialogProvider";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {Form, Formik} from "formik";
import FormInput from "../../utils/form/FormInput";
import i18n from "../../../i18n";

function BeginCourse(props) {
    const {t, i18n} = props
    const {token, setToken} = useLocale();
    const [etag, setEtag] = useState();
    const [courseDetails, setCourseDetails] = useState([
        {
            id: "",
            courseCategory: "",
            price: NaN,
            lecturesHours: NaN,
            drivingHours: NaN,
        }
    ]);

    const dispatchDangerNotification = useDangerNotification();
    const dispatchSuccessNotification = useSuccessNotification();
    const dispatchPermanentChangeDialog = usePermanentChangeDialog();
    const history = useHistory();

    const fetchData = () => {
        axios.get(`/resources/courseDetails/getCoursesDetails`, {
            headers: {
                "Authorization": token,
            }
        }).then(r => {
            setCourseDetails(r.data);
        }).catch(res => {
            if (res.response != null) {
                if (res.response.status === 403) {
                    history.push("/forbidden")
                } else if (res.response.status === 500) {
                    history.push("/internalError")
                }
            }
        });
    }

    const getEtag = async () => {
        const response = await axios.get(`/resources/access/getTraineeAccess`, {
            headers: {
                "Authorization": token,
            }
        })
        return response.headers.etag;
    };

    useEffect(() => {
        if (token) {
            fetchData();
            getEtag().then(r => setEtag(r));
        }
    }, [token]);

    const handleCourseSubmit = (category) => {
        dispatchPermanentChangeDialog({
            confirmCallback: () => {
                beginCourse(category)
            },
            cancelCallback: () => {
            },
        })
    }

    const beginCourse = (category) => {
        axios.post(`/resources/course/createCourse/` + category, {}, {
            headers: {
                "Content-Type": "application/json",
                "If-Match": etag,
                "Authorization": token
            }
        }).then((res) => {
            history.push("/myAccount");
            dispatchSuccessNotification({message: i18n.t('begin.course.success')})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification);
        });
    };


    return (
        <div className="container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link></li>
                <li className="breadcrumb-item active text-secondary" aria-current="page">{t("navigation.bar.begin.course")}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={11} md={10} lg={9} xl={8} className={"floating py-3 mx-auto mb-3 mt-4"}>
                        <div className="py-2">
                            <h1 className="font-weight-light text-center">{t("begin.course.title1")}</h1>
                            <h1 className="font-weight-light text-center mb-5">{t("begin.course.title2")}</h1>


                            <Tabs defaultActiveKey="email" className="justify-content-center dim nav-justified">
                                {courseDetails.length > 1 && courseDetails.map((item) => (
                                    <Tab tabClassName={"text-white bg-transparent"} eventKey={item.courseCategory}
                                         title={item.courseCategory}>
                                        <ListGroup className="mt-4" variant={"flush"}>
                                            <ListGroup.Item className={"d-flex align-items-center p-2"}>
                                                <span className={"mx-auto"}><span
                                                    className="font-weight-bold">{i18n.t('course.price')}:</span> {item.price} {i18n.t('currency')}</span>
                                            </ListGroup.Item>
                                            <ListGroup.Item className={"d-flex align-items-center p-2"}>
                                                <span className={"mx-auto"}><span
                                                    className="font-weight-bold">{i18n.t('lecturesHours')}:</span> {item.lecturesHours}</span>
                                            </ListGroup.Item>
                                            <ListGroup.Item className={"d-flex align-items-center p-2"}>
                                                <span className={"mx-auto"}><span
                                                    className="font-weight-bold">{i18n.t('drivingHours')}:</span> {item.drivingHours}</span>
                                            </ListGroup.Item>
                                        </ListGroup>

                                        <div className="col-12 d-flex justify-content-center mt-5">
                                            <button className="btn btn-dark btn-block dim" type="submit"
                                                    onClick={() => beginCourse(item.courseCategory)}>
                                                {t('navigation.bar.begin.course')}
                                            </button>
                                        </div>
                                    </Tab>
                                ))}
                            </Tabs>
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default withNamespaces()(BeginCourse);