import {useDangerNotification, useSuccessNotification} from "../../utils/notifications/NotificationProvider";
import axios from "axios";
import Breadcrumb from "../../bars/Breadcrumb";
import {Link, useHistory} from "react-router-dom";
import {Col, Container, Row} from "react-bootstrap";
import {Form, Formik} from "formik";
import FormInput from "../../utils/form/FormInput";
import {validatorFactory, ValidatorType} from "../../utils/validators/Validators";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {withNamespaces} from "react-i18next";

function PasswordReset(props) {
    const {t, i18n} = props

    const dispatchDangerNotification = useDangerNotification();
    const dispatchSuccessNotification = useSuccessNotification();
    const history = useHistory();

    function handleSubmit(values, setSubmitting) {
        axios.put(`/resources/account/resetPassword/` + values.emailAddress).then(res => {
            history.push("/");
            dispatchSuccessNotification({message: i18n.t('password.reset.email.sent')})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification)
        }).finally(() => {
            setSubmitting(false)
        })
    }

    function validate(values) {
        const errors = {}

        const emailAddressErrors = validatorFactory(values.emailAddress, ValidatorType.EMAIL_ADDRESS)
        if (emailAddressErrors.length !== 0) errors.emailAddress = emailAddressErrors

        return errors
    }

    return (
        <div className="mb-2 container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link></li>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/signIn">{t('sign.in')}</Link></li>
                <li className="breadcrumb-item active text-secondary" aria-current="page">{t('sign.in.reset.password')}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={11} sm={10} md={8} lg={7} xl={6} className={"floating py-3 mx-auto mb-3 mt-4"}>
                        <div className="py-2">
                            <h1 className="font-weight-light text-center mb-4">{t("sign.in.reset.password")}</h1>
                            <Formik
                                initialValues={{emailAddress: ''}}
                                validate={validate}
                                onSubmit={(values, {setSubmitting}) => handleSubmit(values, setSubmitting)}>
                                <Form className={"row"}>
                                    <FormInput name="emailAddress" placeholder={t("emailAddress")} type="email"
                                               className="col-12 ml-4"/>

                                    <div className="col-12 d-flex justify-content-center mt-4">
                                        <button className="btn btn-dark btn-block dim" type="submit">
                                            {t('sign.in.reset.password')}
                                        </button>
                                    </div>
                                </Form>
                            </Formik>
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default withNamespaces()(PasswordReset);