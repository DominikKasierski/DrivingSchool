import {Link, useHistory, useParams} from "react-router-dom";
import {useDangerNotification, useSuccessNotification} from "../../utils/notifications/NotificationProvider";
import {usePermanentChangeDialog} from "../../utils/dialogs/DialogProvider";
import axios from "axios";
import {dispatchErrors, ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {validatorFactory, ValidatorType} from "../../utils/validators/Validators";
import Breadcrumb from "../../bars/Breadcrumb";
import {Col, Container, Row} from "react-bootstrap";
import {Form, Formik} from "formik";
import FormInput from "../../utils/form/FormInput";
import {withNamespaces} from "react-i18next";

function PasswordResetConfirmation(props) {
    const {t, i18n} = props
    let {code} = useParams();

    const dispatchSuccessNotification = useSuccessNotification();
    const dispatchDangerNotification = useDangerNotification();
    const dispatchPermanentChangeDialog = usePermanentChangeDialog();
    const history = useHistory();

    const initialValues = {
        password: '',
        repeatedPassword: ''
    }

    const handleConfirmation = (values, setSubmitting) => (
        dispatchPermanentChangeDialog({
            confirmCallback: () => handleSubmit(values, setSubmitting),
            cancelCallback: () => setSubmitting(false)
        })
    )

    const handleSubmit = (values, setSubmitting) => {
        axios.put("/resources/account/confirmPassword", {
            password: values.password,
            resetCode: code
        }).then((res) => {
            dispatchSuccessNotification({
                message: i18n.t('password.reset.confirmation.success')
            })
            history.push("/signIn");
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification, false, (error) => {
                dispatchErrors(error, dispatchDangerNotification);
            });
            setSubmitting(false);
        })
    }

    function validate(values) {
        const errors = {}

        const passwordErrors = validatorFactory(values.password, ValidatorType.PASSWORD)
        if (passwordErrors.length !== 0) errors.password = passwordErrors
        if (values.password !== values.repeatedPassword) errors.repeatedPassword = [t("form.validation.passwords.not.match")]

        return errors
    }

    return (
        <div className="mb-2 container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link></li>
                <li className="breadcrumb-item active text-secondary" aria-current="page">{t('password.reset.confirmation')}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={11} sm={10} md={8} lg={7} xl={6} className={"floating py-3 mx-auto mb-3 mt-4"}>
                        <div className="py-2">
                            <h1 className="font-weight-light text-center mb-4">{t("password.reset.confirmation")}</h1>
                            <Formik
                                initialValues={initialValues}
                                validate={validate}
                                onSubmit={(values, {setSubmitting}) => handleSubmit(values, setSubmitting)}>
                                <Form className={"row"}>
                                    <FormInput name="password" placeholder={t("password")} type="password"
                                               className="col-12 ml-4"/>
                                    <FormInput name="repeatedPassword" placeholder={t("repeatedPassword")} type="password"
                                               className="col-12 ml-4"/>

                                    <div className="col-12 d-flex justify-content-center mt-4">
                                        <button className="btn btn-dark btn-block dim" type="submit">
                                            {t('password.reset.confirmation')}
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

export default withNamespaces()(PasswordResetConfirmation);