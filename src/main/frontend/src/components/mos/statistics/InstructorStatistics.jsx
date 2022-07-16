import {withNamespaces} from "react-i18next";
import {useLocale} from "../../utils/login/LoginProvider";
import {useState} from "react";
import Breadcrumb from "../../bars/Breadcrumb";
import {Link, useHistory} from "react-router-dom";
import {Col, Container, Row} from "react-bootstrap";
import CustomDatePicker from "../../utils/pickers/CustomDatePicker";
import axios from "axios";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import moment from "moment";
import {useDangerNotification} from "../../utils/notifications/NotificationProvider";
import {Chart as ChartJS, ArcElement, Tooltip, Legend, Title} from 'chart.js';
import {Doughnut} from 'react-chartjs-2';

ChartJS.register(ArcElement, Tooltip, Legend, Title);

function InstructorStatistics(props) {
    const {t, i18n} = props
    const {token} = useLocale();
    const [startDate, setStartDate] = useState(new Date());
    const [endDate, setEndDate] = useState(new Date());
    const [instructors, setInstructors] = useState([]);
    const [theoreticalHours, setTheoreticalHours] = useState([]);
    const [practicalHours, setPracticalHours] = useState([]);

    const dispatchDangerNotification = useDangerNotification();
    const history = useHistory();

    const fetchData = () => {
        if (token) {
            axios.get(`/resources/course/getInstructorStatistics/` + moment(startDate).unix() + "/" + moment(endDate).unix(), {
                headers: {
                    "Authorization": token,
                }
            }).then(r => {
                setInstructors(r.data.instructors)
                setTheoreticalHours(r.data.numberOfTheoreticalHours)
                setPracticalHours(r.data.numberOfPracticalHours)
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

    const getChartData = (instructors, hours) => {
        return {
            labels: instructors,
            datasets: [
                {
                    label: '# of Votes',
                    data: hours,
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                        'rgba(255, 206, 86, 0.2)',
                        'rgba(75, 192, 192, 0.2)',
                        'rgba(153, 102, 255, 0.2)',
                        'rgba(255, 159, 64, 0.2)',
                    ],
                    borderColor: [
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)',
                        'rgba(255, 159, 64, 1)',
                    ],
                    borderWidth: 1,
                },
            ],
        }
    };

    const chartOptions = (title) => {
        return {
            plugins: {
                title: {
                    display: true,
                    text: title,
                    padding: {
                        bottom: 20
                    },
                    font: {
                        size: 14
                    },
                },
                legend: {
                    display: true,
                    position: 'bottom',
                    labels: {
                        padding: 15
                    },
                }
            }
        }
    };

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
                            {instructors.length > 0 && theoreticalHours.length > 0 && practicalHours.length &&
                                <Row className="justify-content-center">
                                    <Col sm={12} className="mt-1 mb-2">
                                        <div className="row justify-content-center">
                                            <div className="col-md-6"><Doughnut data={getChartData(instructors, theoreticalHours)}
                                                                                options={chartOptions(i18n.t('instructors.statistics.chart.theoretical.title'))}/>
                                            </div>
                                            <div className="col-md-6"><Doughnut data={getChartData(instructors, practicalHours)}
                                                                                options={chartOptions(i18n.t('instructors.statistics.chart.practical.title'))}/>
                                            </div>
                                        </div>
                                    </Col>
                                </Row>
                            }
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default withNamespaces()(InstructorStatistics);