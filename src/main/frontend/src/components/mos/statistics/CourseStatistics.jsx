import {Link, useHistory} from "react-router-dom";
import {useEffect, useState} from "react";
import {Col, Container, Row} from "react-bootstrap";
import Breadcrumb from "../../bars/Breadcrumb";
import axios from "axios";
import {useLocale} from "../../utils/login/LoginProvider";
import {withNamespaces} from "react-i18next";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {useDangerNotification, useSuccessNotification} from "../../utils/notifications/NotificationProvider";

function CourseStatistics(props) {
    const {t, i18n} = props
    const {token} = useLocale();
    const [data, setData] = useState([
        {
            lecturesHours: "",
            carriedOutPracticalHours: "",
            scheduledPracticalHours: "",
            favouriteInstructor: "",
            favouriteCar: "",
        }
    ]);

    const dispatchSuccessNotification = useSuccessNotification();
    const dispatchDangerNotification = useDangerNotification();
    const history = useHistory();

    useEffect(() => {
        if (token) {
            fetchData();
        }
    }, [])

    const fetchData = (refresh = false) => {
        axios.get(`/resources/course/getCourseStatistics`, {
            headers: {
                "Authorization": token,
            }
        }).then(r => {
            setData(r.data);
            if (refresh) {
                dispatchSuccessNotification({message: i18n.t('course.statistics.refresh.success')})
            }
        }).catch(res => {
            if (res.response != null) {
                if (res.response.status === 403) {
                    history.push("/forbidden")
                } else if (res.response.status === 500) {
                    history.push("/internalError")
                } else {
                    ResponseErrorsHandler(res, dispatchDangerNotification);
                    history.push("/userPage");
                }
            }
        });
    }

    return (
        <div className="container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link></li>
                <li className="breadcrumb-item active text-secondary"
                    aria-current="page">{t('navigation.bar.course.statistics')}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={11} md={9} lg={9} xl={8} className={"floating py-3 mx-auto mb-3 mt-4"}>
                        <div className="py-2">
                            <h1 className="font-weight-light text-center mb-4">{t("navigation.bar.course.statistics")}</h1>

                            <div className="row">
                                <div className="col pr-0">
                                    <ul className="list-group list-group-flush font-weight-bold text-right">
                                        <li className="list-group-item">{t("course.statistics.lectures") + ":"}</li>
                                        <li className="list-group-item">{t("course.statistics.taken.lessons") + ":"}</li>
                                        <li className="list-group-item">{t("course.statistics.future.lessons") + ":"}</li>
                                        <li className="list-group-item">{t("course.statistics.instructor") + ":"}</li>
                                        <li className="list-group-item">{t("course.statistics.car") + ":"}</li>
                                    </ul>
                                </div>
                                <div className="col pl-0">
                                    <ul className="list-group list-group-flush text-left">
                                        <li className="list-group-item">{data.lecturesHours}</li>
                                        <li className="list-group-item">{data.carriedOutPracticalHours}</li>
                                        <li className="list-group-item">{data.scheduledPracticalHours}</li>
                                        <li className="list-group-item">{data.favouriteInstructor ? data.favouriteInstructor : t('course.statistics.no.results')}</li>
                                        <li className="list-group-item">{data.favouriteCar ? data.favouriteCar : t('course.statistics.no.results')}</li>
                                    </ul>
                                </div>
                            </div>
                            <div className="col-12 d-flex justify-content-center mt-4">
                                <button className="btn btn-dark btn-block dim" onClick={() => fetchData(true)}>
                                    {t('refresh')}
                                </button>
                            </div>
                        </div>
                    </Col>
                </Row>
            </Container>
            {/*)}*/}
        </div>
    );
}

export default withNamespaces()(CourseStatistics);