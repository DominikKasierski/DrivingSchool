import {Link} from "react-router-dom";
import logo from "../../images/logo.svg"
import {withNamespaces} from "react-i18next";
import {Col, Container, Row} from "react-bootstrap";

function NotFound(props) {
    const {t, i18n} = props

    return(
        <div className={"container-fluid mt-3"}>
            <Container>
                <Row>
                    <Col xs={12} sm={11} md={9} lg={7} className={"floating-no-absolute py-4 mx-auto mb-2"}>
                        <div className="">
                            <img className="shadow p-3 mb-5 rounded img-fluid" src={logo} alt={t('cryingCat')}/>
                            <h1>404</h1>
                            <h1>{t('pageNotFound')}</h1>
                            <Link to="/">
                                {t('mainPage')}
                            </Link>
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    )
}

export default NotFound;