import {withNamespaces} from "react-i18next";
import {useEffect, useState} from "react";
import axios from "axios";
import {Link, useHistory} from "react-router-dom";
import Breadcrumb from "../../bars/Breadcrumb";
import {Col, Container, Row} from "react-bootstrap";
import VehicleComponent from "./VehicleComponent";

function VehicleList(props) {
    const {t, i18n} = props
    const [data, setData] = useState([
        {
            id: "",
            image: "",
            brand: "",
            model: "",
            productionYear: "",
        }
    ]);

    const history = useHistory();

    const fetchData = () => {
        axios.get(`/resources/car/getCars`, {}).then(r => {
            setData(r.data);
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

    useEffect(() => {
        fetchData();
    }, []);

    return (
        <div className="container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link></li>
                <li className="breadcrumb-item active text-secondary" aria-current="page">{t("navigation.bar.vehicles")}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={10} sm={10} md={10} lg={9} xl={8} className={"floating px-4 pt-4 pb-0 mx-auto mb-5 mt-3"}>
                        <div className="row justify-content-center mb-2">
                            <h1 className="font-weight-light">{t("navigation.bar.vehicles.list")}</h1>
                        </div>

                        <div className="row">
                            {data.length > 1 && data.map((item) => (
                                <VehicleComponent id={item.id} image={item.image} brand={item.brand} model={item.model}
                                                  productionYear={item.productionYear}/>
                            ))}
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    )

}

export default withNamespaces()(VehicleList);