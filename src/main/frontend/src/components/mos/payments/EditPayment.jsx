import Breadcrumb from "../../bars/Breadcrumb";
import {Link, useHistory, useLocation} from "react-router-dom";
import {Col, Container, Row} from "react-bootstrap";
import {Form, Formik} from "formik";
import {useFormikContext} from "formik"
import {withNamespaces} from "react-i18next";
import {useLocale} from "../../utils/login/LoginProvider";
import {useDangerNotification, useSuccessNotification} from "../../utils/notifications/NotificationProvider";
import axios from "axios";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {validatorFactory, ValidatorType} from "../../utils/validators/Validators";
import {useEffect, useState} from "react";
import OptionalFormInput from "../../utils/form/OptionalFormInput";
import queryString from "query-string";

function EditPayment(props) {
    const {t, i18n} = props
    const {token, setToken} = useLocale();
    const [etag, setEtag] = useState();
    const {submitForm} = useFormikContext() ?? {};
    let confirm = false;

    const dispatchSuccessNotification = useSuccessNotification();
    const dispatchDangerNotification = useDangerNotification();
    const history = useHistory();
    const location = useLocation();
    const parsedQuery = queryString.parse(location.search);

    const initialValues = {
        comment: ''
    }

    useEffect(() => {
        getEtag().then(r => setEtag(r));
    }, []);

    const getEtag = async () => {
        const response = await axios.get(`/resources/course/getCourse/` + parsedQuery.login, {
            headers: {
                "Authorization": token,
            }
        })
        return response.headers.etag;
    };

    function handleSubmit(values, setSubmitting) {
        setSubmitting(true);
        axios.post(`/resources/payment/` + confirm ? "confirmPayment" : "rejectPayment", {
            login: parsedQuery.login,
            comment: values.comment ? values.comment : null
        }, {
            headers: {
                "If-Match": etag,
                "Authorization": token
            }
        }).then(res => {
            history.push("/paymentsForApproval");
            if (confirm) {
                dispatchSuccessNotification({message: t("edit.payment.confirm.success")})
            } else {
                dispatchSuccessNotification({message: t("edit.payment.reject.success")})
            }
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification)
        }).finally(() => {
            setSubmitting(false)
        })
    }

    function validate(values) {
        const errors = {}

        if (values.comment !== '') {
            const commentErrors = validatorFactory(values.comment, ValidatorType.COMMENT)
            if (commentErrors.length !== 0) errors.comment = commentErrors
        }

        return errors
    }

    return (
        <div className="mb-2 container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link></li>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.payments.for.approval")}</Link></li>
                <li className="breadcrumb-item active text-secondary"
                    aria-current="page">{t('edit.payment')}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={11} sm={10} md={8} lg={7} xl={6} className={"floating py-3 mx-auto mb-3 mt-4"}>
                        <div className="py-2">
                            <h1 className="font-weight-light text-center mb-4">{t("edit.payment.title")}</h1>

                            <div className="col-12 text-center mt-2 mb-4">
                                <span>{t("edit.payment.comment.not.required")}</span>
                            </div>

                            <Formik
                                initialValues={initialValues}
                                validate={validate}
                                onSubmit={(values, {setSubmitting}) => handleSubmit(values, setSubmitting)}>
                                <Form className={"row"}>
                                    <OptionalFormInput name="comment" placeholder={t("report.payment.comment")} type="text"
                                                       className="col-12 ml-5" errorClassname="ml-2 text-danger mr-5"/>

                                    <div className="col-12 d-flex justify-content-center mt-3">
                                        <button className="btn btn-dark btn-block dim" onClick={() => {
                                            confirm = true;
                                            submitForm();
                                        }}>
                                            {t('edit.payment.confirm')}
                                        </button>
                                    </div>

                                    <div className="col-12 d-flex justify-content-center mt-2">
                                        <button className="btn btn-dark btn-block dim" onClick={() => {
                                            confirm = false;
                                            submitForm();
                                        }}>
                                            {t('edit.payment.reject')}
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

export default withNamespaces()(EditPayment);