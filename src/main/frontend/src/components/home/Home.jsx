import i18n from '../../i18n';
import road from '../../images/road.jpg';
import register from '../../images/register.jpg';
import cars from '../../images/cars.jpg';
import {Col, Container, Row} from "react-bootstrap";

function Home() {

    return (
        <Container fluid className={"pb-4 mb-2"}>
            <Row className={"px-0"}>
                <img className="img-fluid w-100" src={road} alt="First slide"/>
            </Row>
            <Container>
                <Row>
                    <Col xs={12} className={"px-0 px-md-3"}>
                        <div className={"pb-3 homeGrid px-0 px-md-3"} style={{minHeight: "300px"}}>
                            <div
                                className={"text-center h3 my-4 d-flex justify-content-center align-items-start textLeft"}>{i18n.t("landingPageLeft")}</div>
                            <div
                                className={"text-center h3 my-4 d-flex justify-content-center align-items-start textRight"}>{i18n.t("landingPageRight")}</div>
                            <div className={"d-flex justify-content-center align-items-start imageLeft"}>
                                <img alt="cat" className={"img-fluid"} src={cars}/>
                            </div>
                            <div className={"d-flex justify-content-center align-items-start imageRight"}>
                                <img alt="hand" className={"img-fluid"} src={register}/>
                            </div>
                        </div>
                    </Col>
                </Row>
            </Container>
        </Container>
    );
}

export default Home;