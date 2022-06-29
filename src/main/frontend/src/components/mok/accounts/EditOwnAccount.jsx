import Breadcrumb from "../../bars/Breadcrumb";
import {Link, useHistory} from "react-router-dom";
import {Col, Container, Row, Tab, Tabs} from "react-bootstrap";
import {withNamespaces} from "react-i18next";
import {useLocale} from "../../utils/login/LoginProvider";
import {useState, useEffect} from "react";
import axios from "axios";
import {useDangerNotification, useSuccessNotification} from "../../utils/notifications/NotificationProvider";
import {usePermanentChangeDialog} from "../../utils/dialogs/DialogProvider";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {validatorFactory, ValidatorType} from "../../utils/validators/Validators";
import {Form, Formik} from "formik";
import FormInput from "../../utils/form/FormInput";

function EditOwnAccount(props) {
    const {t, i18n} = props
    const {token, setToken} = useLocale();
    const [etag, setETag] = useState();

    const dispatchSuccessNotification = useSuccessNotification();
    const dispatchDangerNotification = useDangerNotification();
    const dispatchPermanentChangeDialog = usePermanentChangeDialog();
    const history = useHistory();

    const getEtag = async () => {
        const response = await axios.get(`/resources/account/getDetails`, {
            headers: {
                "Authorization": token,
            }
        })
        return response.headers.etag;
    };

    useEffect(() => {
        if (token) {
            getEtag().then(r => setETag(r));
        }
    }, [token, i18n.language]);

    const handleEmailSubmit = (values, setSubmitting) => {
        dispatchPermanentChangeDialog({
            confirmCallback: () => {
                editEmailAddress(values, setSubmitting)
            },
            cancelCallback: () => {
                setSubmitting(false)
            },
        })
    }

    const handlePasswordSubmit = (values, setSubmitting) => {
        dispatchPermanentChangeDialog({
            confirmCallback: () => {
                editPassword(values, setSubmitting)
            },
            cancelCallback: () => {
                setSubmitting(false)
            },
        })
    }

    const handlePersonalDataSubmit = (values, setSubmitting) => {
        dispatchPermanentChangeDialog({
            confirmCallback: () => {
                editPersonalData(values, setSubmitting)
            },
            cancelCallback: () => {
                setSubmitting(false)
            },
        })
    }

    const editEmailAddress = (values, setSubmitting) => {
        axios.put(`/resources/account/editEmail`, {newEmailAddress: values.emailAddress}, {
            headers: {
                "Content-Type": "application/json",
                "If-Match": etag,
                "Authorization": token
            }
        }).then((res) => {
            history.push("/myAccount");
            dispatchSuccessNotification({message: i18n.t('edit.own.account.edit.email.address.success')})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification);
        });
        setSubmitting(false);
    };

    const editPassword = (values, setSubmitting) => {
        axios.put(`/resources/account/changePassword`, {oldPassword: values.oldPassword, newPassword: values.newPassword}, {
            headers: {
                "Content-Type": "application/json",
                "If-Match": etag,
                "Authorization": token
            }
        }).then((res) => {
            history.push("/myAccount");
            dispatchSuccessNotification({message: i18n.t('edit.own.account.edit.password.success')})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification);
        });
        setSubmitting(false);
    };

    const editPersonalData = (values, setSubmitting) => {
        axios.put(`/resources/account/edit`, {
            firstname: values.firstname,
            lastname: values.lastname,
            phoneNumber: values.phoneNumber
        }, {
            headers: {
                "Content-Type": "application/json",
                "If-Match": etag,
                "Authorization": token
            }
        }).then((res) => {
            history.push("/myAccount");
            dispatchSuccessNotification({message: i18n.t('edit.own.account.edit.personal.data.success')})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification);
        });
        setSubmitting(false);
    };

    function validateEmailAddress(values) {
        const errors = {}

        const emailAddressErrors = validatorFactory(values.emailAddress, ValidatorType.EMAIL_ADDRESS)
        if (emailAddressErrors.length !== 0) errors.emailAddress = emailAddressErrors

        return errors
    }

    function validatePassword(values) {
        const errors = {}

        const oldPasswordErrors = validatorFactory(values.oldPassword, ValidatorType.PASSWORD)
        if (oldPasswordErrors.length !== 0) errors.oldPassword = oldPasswordErrors

        const newPasswordErrors = validatorFactory(values.newPassword, ValidatorType.PASSWORD)
        if (newPasswordErrors.length !== 0) errors.newPassword = newPasswordErrors
        if (values.newPassword !== values.repeatedNewPassword) errors.repeatedNewPassword = [t("form.validation.passwords.not.match")]
        if (values.oldPassword === values.newPassword) errors.newPassword = [t("form.validation.passwords.same")]

        return errors
    }

    function validatePersonalData(values) {
        const errors = {}

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
                <li className="breadcrumb-item"><Link className={"text-dark"}
                                                      to="/myAccount">{t("navigation.bar.my.account")}</Link></li>
                <li className="breadcrumb-item active text-secondary" aria-current="page">{t("edit.own.account")}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={11} md={9} lg={8} xl={7} className={"floating py-3 mx-auto mb-3 mt-4"}>
                        <div className="py-2">
                            <h1 className="font-weight-light text-center">{t("edit.own.account")}</h1>

                            <div className="col-12 text-center mt-2 mb-4">
                                <span>{t("sign.up.required.fields")}</span>
                            </div>
                        </div>
                        <Tabs defaultActiveKey="email" className="justify-content-center dim nav-justified">
                            <Tab tabClassName={"text-white bg-transparent"} eventKey="email"
                                 title={t('edit.own.account.edit.email.address')}>
                                <Formik
                                    initialValues={{emailAddress: ''}}
                                    validate={validateEmailAddress}
                                    onSubmit={(values, {setSubmitting}) => handleEmailSubmit(values, setSubmitting)}>
                                    <Form className={"row"}>
                                        <FormInput name="emailAddress" placeholder={t("emailAddress")} type="email"
                                                   className="col-12 ml-4 mt-4"/>

                                        <div className="col-12 d-flex justify-content-center mt-4">
                                            <button className="btn btn-dark btn-block dim" type="submit">
                                                {t('change')}
                                            </button>
                                        </div>
                                    </Form>
                                </Formik>
                            </Tab>
                            <Tab tabClassName={"text-white bg-transparent"} eventKey="password"
                                 title={t('edit.own.account.edit.password')}>
                                <Formik
                                    initialValues={{
                                        oldPassword: '',
                                        newPassword: '',
                                        repeatedNewPassword: ''
                                    }}
                                    validate={validatePassword}
                                    onSubmit={(values, {setSubmitting}) => handlePasswordSubmit(values, setSubmitting)}>
                                    <Form className={"row"}>
                                        <FormInput name="oldPassword" placeholder={t("oldPassword")} type="password"
                                                   className="col-12 ml-4 mt-4"/>
                                        <FormInput name="newPassword" placeholder={t("newPassword")} type="password"
                                                   className="col-12 ml-4"/>
                                        <FormInput name="repeatedNewPassword" placeholder={t("repeatedNewPassword")}
                                                   type="password" className="col-12 ml-4"/>

                                        <div className="col-12 d-flex justify-content-center mt-4">
                                            <button className="btn btn-dark btn-block dim" type="submit">
                                                {t('change')}
                                            </button>
                                        </div>
                                    </Form>
                                </Formik>
                            </Tab>
                            <Tab tabClassName={"text-white bg-transparent"} eventKey="personalData"
                                 title={t('edit.own.account.edit.personal.data')}>
                                <Formik
                                    initialValues={{
                                        firstname: '',
                                        lastname: '',
                                        phoneNumber: ''
                                    }}
                                    validate={validatePersonalData}
                                    onSubmit={(values, {setSubmitting}) => handlePersonalDataSubmit(values, setSubmitting)}>
                                    <Form className={"row"}>
                                        <FormInput name="firstname" placeholder={t("firstname")} type="text"
                                                   className="col-12 ml-4 mt-4"/>
                                        <FormInput name="lastname" placeholder={t("lastname")} type="text"
                                                   className="col-12 ml-4"/>
                                        <FormInput name="phoneNumber" placeholder={t("phoneNumber")}
                                                   type="text" className="col-12 ml-4"/>

                                        <div className="col-12 d-flex justify-content-center mt-4">
                                            <button className="btn btn-dark btn-block dim" type="submit">
                                                {t('change')}
                                            </button>
                                        </div>
                                    </Form>
                                </Formik>
                            </Tab>
                        </Tabs>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default withNamespaces()(EditOwnAccount)