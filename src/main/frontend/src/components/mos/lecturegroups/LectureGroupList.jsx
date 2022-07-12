import {withNamespaces} from "react-i18next";
import {useLocale} from "../../utils/login/LoginProvider";
import {Link, useHistory, useLocation} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import Breadcrumb from "../../bars/Breadcrumb";
import {Col, Container, Row} from "react-bootstrap";

function LectureGroupList(props) {
    const {t, i18n} = props
    const {token, setToken} = useLocale();
    const {search} = useLocation();
    const [data, setData] = useState([
        {
            id: "",
            name: "",
            courseCategory: "",
            coursesNumber: "",
        }
    ]);

    const history = useHistory()
    const searchParams = new URLSearchParams(search);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = () => {
        if (token) {
            axios.get(`/resources/lectureGroup/getLectureGroups`, {
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

    return (
        <div className="container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link></li>
                <li className="breadcrumb-item active text-secondary"
                    aria-current="page">{t("navigation.bar.lecture.groups.list")}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={12} md={12} lg={12} xl={12} className={"floating py-3 mx-auto mb-3 mt-4"}>
                        <div className="py-2">
                            <h1 className="font-weight-light text-center">{t("navigation.bar.lecture.groups")}</h1>
                        </div>

                        <table className="table table-hover table-bordered mt-2">
                            <thead className="dim">
                            <tr>
                                <th className={"font-weight-normal text-center"} scope="col">{t("lecture.groups.group.name")}</th>
                                <th className={"font-weight-normal text-center"} scope="col">{t("courseCategory")}</th>
                                <th className={"font-weight-normal text-center"} scope="col">{t("lecture.groups.group.size")}</th>
                                <th className={"font-weight-normal text-center"}
                                    scope="col">{t("lecture.groups.add.participants")}</th>
                                <th className={"font-weight-normal text-center"}
                                    scope="col">{t("lecture.groups.add.lecture")}</th>
                            </tr>
                            </thead>
                            <tbody>

                            {data.length > 1 && data.map((item) => (
                                <tr>
                                    <td className={"text-center align-middle"}>{item.name}</td>
                                    <td className={"text-center align-middle"}>{item.courseCategory}</td>
                                    <td className={"text-center align-middle"}>{item.coursesNumber}</td>
                                    <td className={"text-center align-middle"}>
                                        <div className="d-flex justify-content-center align-items-center px-3">
                                            <button className="btn btn-dark btn-block dim"
                                                    onClick={() => {
                                                        searchParams.set("id", item.id);
                                                        searchParams.set("lectureGroupName", item.name);
                                                        history.push({
                                                            pathname: "/addParticipants",
                                                            search: searchParams.toString()
                                                        });
                                                    }}>
                                                {t('add')}
                                            </button>
                                        </div>
                                    </td>
                                    <td className={"text-center align-middle"}>
                                        <div className="d-flex justify-content-center align-items-center px-3">
                                            <button className="btn btn-dark btn-block dim"
                                                    onClick={() => history.push("/addLectures?id=" + item.id)}>
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

export default withNamespaces()(LectureGroupList);