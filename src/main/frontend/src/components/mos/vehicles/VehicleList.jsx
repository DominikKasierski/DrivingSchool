import {withNamespaces} from "react-i18next";
import {useEffect, useState} from "react";
import axios from "axios";
import {Link, useHistory} from "react-router-dom";
import Breadcrumb from "../../bars/Breadcrumb";
import {Col, Container, Row} from "react-bootstrap";
import VehicleComponent from "./VehicleComponent";

//TODO: Dodać errory do tłumaczeń

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
            <Container className={"floating col-md-6 col-lg-5 col-12 py-3 mb-3 mt-4"}>
                <div className="row justify-content-center mb-3">
                    <h1 className="font-weight-light">{t("navigation.bar.vehicles.list")}</h1>
                </div>

                <div className={"row mb-2"}>
                    {data.length > 0 && data.map((item) => (
                        <VehicleComponent id={item.id} image={item.image} brand={item.brand} model={item.model}
                                          productionYear={item.productionYear}/>
                    ))}
                </div>
            </Container>
        </div>
    )

}

export default withNamespaces()(VehicleList);