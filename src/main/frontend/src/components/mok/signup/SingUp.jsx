import {useDangerNotification, useSuccessNotification} from "../../utils/notifications/NotificationProvider";
import axios from "axios";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {validatorFactory, ValidatorType} from "../../utils/validators/Validators";
import {Form, Formik} from "formik";
import Breadcrumb from "../../bars/Breadcrumb";
import {Link, useHistory} from "react-router-dom";
import {Col, Container, Row} from "react-bootstrap";
import {withNamespaces} from "react-i18next";
import FormInput from "./FormInput";

function SignUp(props) {
    const {t, i18n} = props

    const dispatchDangerNotification = useDangerNotification();
    const dispatchSuccessNotification = useSuccessNotification();
    const history = useHistory();

    const initialValues = {
        login: '',
        emailAddress: '',
        password: '',
        repeatedPassword: '',
        firstname: '',
        lastname: '',
        phoneNumber: ''
    }

    function handleRegisterAccount(values, setSubmitting, {resetForm}) {
        setSubmitting(true);
        axios.post(`/resources/account/register`, {
            login: values.login,
            emailAddress: values.emailAddress,
            password: values.password,
            firstname: values.firstname,
            lastname: values.lastname,
            phoneNumber: values.phoneNumber
        }).then(res => {
            history.push("/");
            dispatchSuccessNotification({message: t("sign.up.success")})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification)
        }).finally(() => {
            setSubmitting(false)
        })
    }

    function validate(values) {
        const errors = {}

        const loginErrors = validatorFactory(values.login, ValidatorType.LOGIN)
        if (loginErrors.length !== 0) errors.login = loginErrors

        const emailAddressErrors = validatorFactory(values.emailAddress, ValidatorType.EMAIL_ADDRESS)
        if (emailAddressErrors.length !== 0) errors.emailAddress = emailAddressErrors

        const passwordErrors = validatorFactory(values.password, ValidatorType.PASSWORD)
        if (passwordErrors.length !== 0) errors.password = passwordErrors
        if (values.password !== values.repeatedPassword) errors.repeatedPassword = [t("form.validation.passwords.not.match")]

        const firstnameErrors = validatorFactory(values.firstname, ValidatorType.FIRSTNAME)
        if (firstnameErrors.length !== 0) errors.firstname = firstnameErrors

        const lastnameErrors = validatorFactory(values.lastname, ValidatorType.LASTNAME)
        if (lastnameErrors.length !== 0) errors.lastname = lastnameErrors

        const phoneNumberErrors = validatorFactory(values.phoneNumber, ValidatorType.PHONE_NUMBER)
        if (phoneNumberErrors.length !== 0) errors.phoneNumber = phoneNumberErrors

        return errors
    }

    return (
        <div className="container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link></li>
                <li className="breadcrumb-item active text-secondary" aria-current="page">{t("sign.up")}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={11} md={9} lg={8} xl={7} className={"floating py-3 mx-auto mb-3 mt-4"}>
                        <div className="py-2">
                            <h1 className="font-weight-light text-center">{t("sign.up.title")}</h1>

                            <div className="col-12 text-center mt-2 mb-4">
                                <span>{t("sign.up.required.fields")}</span>
                            </div>

                            <Formik
                                initialValues={initialValues}
                                validate={validate}
                                onSubmit={(values, {
                                    setSubmitting,
                                    resetForm
                                }) => handleRegisterAccount(values, setSubmitting, {resetForm})}>
                                <Form className="row">
                                    <FormInput name="login" placeholder={t("login")} type="text"/>
                                    <FormInput name="emailAddress" placeholder={t("emailAddress")} type="email"/>
                                    <FormInput name="password" placeholder={t("password")} type="password"/>
                                    <FormInput name="repeatedPassword" placeholder={t("repeatedPassword")} type="password"/>
                                    <FormInput name="firstname" placeholder={t("firstname")} type="text"/>
                                    <FormInput name="lastname" placeholder={t("lastname")} type="text"/>
                                    <FormInput name="phoneNumber" placeholder={t("phoneNumber")} type="text"/>

                                    <div className="col-12 d-flex justify-content-center mt-4">
                                        <button className="btn btn-dark dim" type="submit">
                                            {t('sign.up')}
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

export default withNamespaces()(SignUp);