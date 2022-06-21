import road from '../../images/road.jpg';
import motorcycle from '../../images/motorcycle.jpg';
import car from '../../images/car.jpg';
import truck from '../../images/truck.jpg';
import {Col, Container, Row} from "react-bootstrap";
import {withNamespaces} from "react-i18next";

function Home(props) {
    const {t, i18n} = props

    return (
        <Container fluid className={"dim pb-4"}>
            <style
                dangerouslySetInnerHTML={{__html: "body {background-image: unset;"}}/>
            <Row className={"px-0 justify-content-center"}>
                <img className="img-fluid w-100" src={road} alt="road"/>
                <h1 className={"display-4 my-3"}>{i18n.t("landing.page.courses")}</h1>
            </Row>
            <Container className={"dim"}>
                <Row>
                    <Col xs={12} className={"px-0 mt-4"}>
                        <div className={"homeGrid px-0"} style={{minHeight: "300px"}}>
                            <div className={"d-flex justify-content-center align-items-start imageLeft"}>
                                <img alt="aCategory" className={"img-fluid rounded"} src={motorcycle}/>
                            </div>
                            <div className={"d-flex justify-content-center align-items-start imageCenter"}>
                                <img alt="bCategory" className={"img-fluid rounded"} src={car}/>
                            </div>
                            <div className={"d-flex justify-content-center align-items-start imageRight"}>
                                <img alt="cCategory" className={"img-fluid rounded"} src={truck}/>
                            </div>
                            <div
                                className={"text-center h3 font-weight-light my-3 d-flex justify-content-center align-items-start textLeft"}>
                                {i18n.t("landing.page.left")}</div>
                            <div
                                className={"text-center h3 font-weight-light my-3 d-flex justify-content-center align-items-start textCenter"}>
                                {i18n.t("landing.page.center")}</div>
                            <div
                                className={"text-center h3 font-weight-light my-3 d-flex justify-content-center align-items-start textRight"}>
                                {i18n.t("landing.page.right")}</div>
                        </div>
                    </Col>
                </Row>
            </Container>
        </Container>
    );
}

export default withNamespaces()(Home);