import {Col, Container, Row} from "react-bootstrap";
import i18n from '../../i18n';
import {withNamespaces} from "react-i18next";

function NotFound() {

    return (
        <Container className={"mt-5 brightened"}>
            <style dangerouslySetInnerHTML={{__html: "body {background-image: unset;"}}/>
            <Row>
                <Col xs={12} sm={11} md={9} lg={8} className={"py-3 mx-auto text-center"}>
                    <h1 className={"display-1 font-weight-bold mb-0"}>{404}</h1>
                    <h1 className={"display-2"}>{i18n.t("pageNotFound")}</h1>
                    <a href="/" className="btn btn-dark mt-4 dim" role="button">{i18n.t("main.page")}</a>
                </Col>
            </Row>
        </Container>
    )
}

export default withNamespaces()(NotFound);