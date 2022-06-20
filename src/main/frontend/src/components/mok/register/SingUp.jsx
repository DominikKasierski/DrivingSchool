import {
    useDangerNotification,
    useSuccessNotification,
    useWarningNotification
} from "../../utils/notifications/NotificationProvider";
import axios from "axios";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {validatorFactory, ValidatorType} from "../../utils/validators/Validators";
import {Form, Formik} from "formik";
import Breadcrumb from "../../bars/Breadcrumb";
import {Link} from "react-router-dom";
import {Col, Container, Row} from "react-bootstrap";
import {withNamespaces} from "react-i18next";
import FormInput from "./FormInput";

function SignUp(props) {
    const {t, i18n} = props

    const dispatchDangerNotification = useDangerNotification();
    const dispatchWarningNotification = useWarningNotification();
    const dispatchSuccessNotification = useSuccessNotification();

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
        const {repeatedPassword, ...dto} = values
        axios.post(`/resources/account/register`, {
            dto
        }).then(res => {
            dispatchSuccessNotification({message: t("sign.up.success")})
            resetForm()
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
                <li className="breadcrumb-item"><Link to="/">{t("navigation.bar.main.page")}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{t("sign.up")}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={11} md={9} lg={8} xl={7} className={"floating-no-absolute py-4 mx-auto mb-2"}>
                        <div className="py-2">
                            <h3 className="h3 text-center mt-3">{t("sign.up.title")}</h3>

                            <div className="col-12 text-center pt-2">
                                <div style={{color: "#7749F8", fontSize: 14, marginBottom: "1rem"}}>
                                    {t("sign.up.required.fields")}
                                </div>
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
                                    <div className="col-12 d-flex justify-content-center mb-3">
                                        <button className="btn btn-purple" type="submit">
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