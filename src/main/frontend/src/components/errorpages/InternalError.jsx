import {Col, Container, Row} from "react-bootstrap";
import i18n from '../../i18n';

function InternalError() {

    return (
        <Container className={"mt-5"}>
            <Row>
                <Col xs={12} sm={11} md={9} lg={9} className={"py-3 mx-auto text-center"}>
                    <h1 className={"display-1 font-weight-bold mb-0"}>{500}</h1>
                    <h1 className={"display-2"}>{i18n.t("internalError")}</h1>
                    <a href="/" className="btn btn-dark mt-4" role="button">{i18n.t('mainPage')}</a>
                </Col>
            </Row>
        </Container>
    )
}

export default InternalError;