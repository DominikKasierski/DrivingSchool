import {useLocale} from "../../utils/login/LoginProvider";
import {
    useCustomNotification,
    useDangerNotification,
    useSuccessNotification
} from "../../utils/notifications/NotificationProvider";
import {Link, useHistory} from "react-router-dom";
import {notificationDuration, notificationType, REFRESH_TIME} from "../../utils/constants/Constants";
import axios from "axios";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import jwt_decode from "jwt-decode";
import {withNamespaces} from "react-i18next";
import Breadcrumb from "../../bars/Breadcrumb";
import {Col, Container, Row} from "react-bootstrap";
import {Form, Formik} from "formik";
import {validatorFactory, ValidatorType} from "../../utils/validators/Validators";
import FormInput from "../../utils/form/FormInput";

function SingIn(props) {
    const {token, setToken, saveToken, setUsername, setCurrentRole} = useLocale();
    const {t, i18n} = props

    const dispatchCustomNotification = useCustomNotification();
    const dispatchDangerNotification = useDangerNotification();
    const dispatchSuccessNotification = useSuccessNotification();
    const history = useHistory();

    const initialValues = {
        login: '',
        password: ''
    }

    const handleRefreshBox = () => {
        dispatchCustomNotification({
            "type": notificationType.WARNING,
            "duration": notificationDuration.LONG,
            "title": `${i18n.t("dialog.update.token.title")}`,
            "message": (<> {i18n.t("dialog.update.token.message")} <span className={"text-primary"}
                                                                         onClick={refreshToken}>{i18n.t("dialog.update.token.refresh")}</span></>)
        })
    }

    const refreshToken = (event) => {
        event.target.closest(".alert").querySelector(".close").click();
        if (localStorage.getItem("token")) {
            axios.post(`/resources/auth/updateToken`, localStorage.getItem("token"), {
                headers: {
                    "Authorization": `${localStorage.getItem("token")}`
                }
            }).then(response => {
                saveToken("Bearer " + response.data);
                clearTimeout(localStorage.getItem("timeoutId2") ?? 0)
                clearTimeout(localStorage.getItem("timeoutId3") ?? 0)
                schedule()
                dispatchSuccessNotification({message: i18n.t("dialog.update.token.success")})
            }).catch(
                e => ResponseErrorsHandler(e, dispatchDangerNotification)
            );
        }
    }

    const handleSubmit = async (values, setSubmitting) => {
        try {
            const res = await axios.post(`/resources/auth/login`, {
                login: values.login,
                password: values.password
            })
            saveToken("Bearer " + res.data)
            history.push("/userPage")
            schedule();
        } catch (ex) {
            ResponseErrorsHandler(ex, dispatchDangerNotification, true, (e) => {
            }, true)
            setSubmitting(false);
        }
    }

    const schedule = () => {
        const id = setTimeout(() => {
            handleRefreshBox();
        }, new Date(jwt_decode(localStorage.getItem("token")).exp * 1000) - new Date() - REFRESH_TIME);
        localStorage.setItem("timeoutId", id.toString())

        const id2 = setTimeout(() => {
            localStorage.removeItem("token")
            localStorage.removeItem("username")
            localStorage.removeItem("currentRole")
            setToken(null)
            setUsername('')
            setCurrentRole('')
            history.push("/")
        }, new Date(jwt_decode(localStorage.getItem("token")).exp * 1000) - new Date() + 2000);
        localStorage.setItem("timeoutId2", id2.toString())
    }

    function validate(values) {
        const errors = {}

        const loginErrors = validatorFactory(values.login, ValidatorType.LOGIN)
        if (loginErrors.length !== 0) errors.login = loginErrors

        const passwordErrors = validatorFactory(values.password, ValidatorType.PASSWORD)
        if (passwordErrors.length !== 0) errors.password = passwordErrors

        return errors
    }

    return (
        <div className="mb-2 container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link></li>
                <li className="breadcrumb-item active text-secondary" aria-current="page">{t('sign.in')}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={11} sm={10} md={8} lg={7} xl={6} className={"floating py-3 mx-auto mb-3 mt-4"}>
                        <div className="py-2">
                            <h1 className="font-weight-light text-center mb-4">{t("sign.in.title")}</h1>
                            <Formik
                                initialValues={initialValues}
                                validate={validate}
                                onSubmit={(values, {setSubmitting}) => handleSubmit(values, setSubmitting)}>
                                <Form className={"row"}>
                                    <FormInput name="login" placeholder={t("login")} type="text" className="col-12 ml-4"/>
                                    <FormInput name="password" placeholder={t("password")} type="password"
                                               className="col-12 ml-4"/>

                                    <div className="col-12 d-flex justify-content-center mt-4">
                                        <button className="btn btn-dark btn-block dim" type="submit">
                                            {t('sign.in')}
                                        </button>
                                    </div>

                                    <div className="col-12 d-flex justify-content-center mt-1">
                                        <button className="btn btn-dark btn-block dim" type="button"
                                                onClick={() => history.push("/resetPassword")}>
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

export default withNamespaces()(SingIn);