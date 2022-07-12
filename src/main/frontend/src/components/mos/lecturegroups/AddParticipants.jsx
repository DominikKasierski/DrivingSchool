import {withNamespaces} from "react-i18next";
import {useLocale} from "../../utils/login/LoginProvider";
import {Link, useHistory, useLocation} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import Breadcrumb from "../../bars/Breadcrumb";
import {Col, Container, Row} from "react-bootstrap";
import queryString from "query-string";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {useDangerNotification, useSuccessNotification} from "../../utils/notifications/NotificationProvider";

function AddParticipants(props) {
    const {t, i18n} = props
    const {token, setToken} = useLocale();
    const [etag, setETag] = useState();
    const [data, setData] = useState([
        {
            login: "",
            firstname: "",
            lastname: "",
            courseId: ""
        }
    ]);

    const dispatchSuccessNotification = useSuccessNotification();
    const dispatchDangerNotification = useDangerNotification();
    const history = useHistory()
    const location = useLocation();
    const parsedQuery = queryString.parse(location.search);
    let lectureGroupName = parsedQuery.lectureGroupName;
    let courseCategory = "";


    useEffect(() => {
        if (token) {
            getEtag();
        }
    }, [token]);

    function getEtag() {
        axios.get(`/resources/lectureGroup/getLectureGroup/` + parsedQuery.id, {
            headers: {
                "Authorization": token,
            }
        }).then((res) => {
            lectureGroupName = res.data.name;
            courseCategory = res.data.courseCategory;
            setETag(res.headers.etag);
            fetchData();
        }).catch((e) => ResponseErrorsHandler(e, dispatchDangerNotification));
    }

    const fetchData = () => {
        if (token) {
            axios.get(`/resources/lectureGroup/getTraineesForGroup/` + courseCategory, {
                headers: {
                    "Authorization": token,
                }
            }).then(r => {
                setData(r.data);
            }).catch(r => {
                if (r.response != null) {
                    if (r.response.status === 403) {
                        history.push("/errors/forbidden")
                    } else if (r.response.status === 500) {
                        history.push("/errors/internal")
                    }
                }
            });
        }
    }

    const addParticipant = (courseId) => {
        axios.put(`/resources/lectureGroup/assignToLectureGroup/` + parsedQuery.id + "/" + courseId, {}, {
            headers: {
                "If-Match": etag,
                "Authorization": token
            }
        }).then((res) => {
            history.push("/lectureGroups");
            dispatchSuccessNotification({message: i18n.t('add.participants.add.success')})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification);
        });
    };

    return (
        <div className="container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link></li>
                <li className="breadcrumb-item"><Link className={"text-dark"}
                                                      to="/">{t("navigation.bar.lecture.groups.list")}</Link></li>
                <li className="breadcrumb-item active text-secondary"
                    aria-current="page">{t("add.participants")}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={12} md={12} lg={12} xl={12} className={"floating py-3 mx-auto mb-3 mt-4"}>
                        <div className="py-2">
                            <h1 className="font-weight-light text-center">{t("add.participants")}</h1>
                        </div>

                        <div className="col-12 text-center mt-2 mb-4">
                            <span>{t("add.participants.selected.group")}: {lectureGroupName}</span>
                        </div>

                        <table className="table table-hover table-bordered mt-2">
                            <thead className="dim">
                            <tr>
                                <th className={"font-weight-normal text-center"} scope="col">{t("login")}</th>
                                <th className={"font-weight-normal text-center"} scope="col">{t("firstname")}</th>
                                <th className={"font-weight-normal text-center"} scope="col">{t("lastname")}</th>
                                <th className={"font-weight-normal text-center"}
                                    scope="col">{t("action")}</th>
                            </tr>
                            </thead>
                            <tbody>

                            {data.length > 0 && data.map((item) => (
                                <tr>
                                    <td className={"text-center align-middle"}>{item.login}</td>
                                    <td className={"text-center align-middle"}>{item.firstname}</td>
                                    <td className={"text-center align-middle"}>{item.lastname}</td>
                                    <td className={"text-center align-middle"}>
                                        <div className="d-flex justify-content-center align-items-center px-3">
                                            <button className="btn btn-dark btn-block dim"
                                                    onClick={() => addParticipant(item.courseId)}>
                                                {t('add')}
                                            </button>
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

export default withNamespaces()(AddParticipants);