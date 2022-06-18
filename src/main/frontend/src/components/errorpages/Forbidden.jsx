import {Col, Container, Row} from "react-bootstrap";
import i18n from '../../i18n';

function Forbidden() {

    return (
        <Container className={"mt-5 brightened"}>
            <Row>
                <Col xs={12} sm={11} md={9} lg={9} className={"py-3 mx-auto text-center"}>
                    <h1 className={"display-1 font-weight-bold mb-0"}>{403}</h1>
                    <h1 className={"display-2"}>{i18n.t("pageForbidden")}</h1>
                    <a href="/" className="btn btn-dark mt-4 dim" role="button">{i18n.t('mainPage')}</a>
                </Col>
            </Row>
        </Container>
    )
}

export default Forbidden;