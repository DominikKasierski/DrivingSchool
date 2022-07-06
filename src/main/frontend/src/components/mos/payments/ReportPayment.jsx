import Breadcrumb from "../../bars/Breadcrumb";
import {Link, useHistory} from "react-router-dom";
import {Col, Container, Row} from "react-bootstrap";
import {Form, Formik} from "formik";
import FormInput from "../../utils/form/FormInput";
import {withNamespaces} from "react-i18next";
import {useLocale} from "../../utils/login/LoginProvider";
import {useDangerNotification, useSuccessNotification} from "../../utils/notifications/NotificationProvider";
import {usePermanentChangeDialog} from "../../utils/dialogs/DialogProvider";
import axios from "axios";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {validatorFactory, ValidatorType} from "../../utils/validators/Validators";

function ReportPayment(props) {
    const {t, i18n} = props
    const {token, setToken} = useLocale();

    const dispatchSuccessNotification = useSuccessNotification();
    const dispatchDangerNotification = useDangerNotification();
    const history = useHistory()

    const initialValues = {
        value: NaN,
        comment: ''
    }

    function handleSubmit(values, setSubmitting) {
        setSubmitting(true);
        axios.post(`/resources/payment/createPayment`, {
            value: values.value,
            comment: values.comment
        }, {
            headers: {
                "Authorization": token,
            }
        }).then(res => {
            history.push("/myPayments");
            dispatchSuccessNotification({message: t("report.payment.report.success")})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification)
        }).finally(() => {
            setSubmitting(false)
        })
    }

    function validate(values) {
        const errors = {}

        const priceErrors = validatorFactory(values.price, ValidatorType.PRICE)
        if (priceErrors.length !== 0) errors.price = priceErrors

        const commentErrors = validatorFactory(values.comment, ValidatorType.COMMENT)
        if (commentErrors.length !== 0) errors.comment = commentErrors

        return errors
    }

    return (
        <div className="mb-2 container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link></li>
                <li className="breadcrumb-item active text-secondary"
                    aria-current="page">{t('navigation.bar.report.payment')}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={11} sm={10} md={8} lg={7} xl={6} className={"floating py-3 mx-auto mb-3 mt-4"}>
                        <div className="py-2">
                            <h1 className="font-weight-light text-center mb-4">{t("report.payment.title")}</h1>

                            <div className="col-12 text-center mt-2 mb-4">
                                <span>{t("required.fields")}</span>
                            </div>

                            <Formik
                                initialValues={initialValues}
                                validate={validate}
                                onSubmit={(values, {setSubmitting}) => handleSubmit(values, setSubmitting)}>
                                <Form className={"row"}>
                                    <FormInput name="price" placeholder={t("price") + " (" + t("currency") + ")"} type="text"
                                               className="col-12 ml-4" errorClassname="ml-4 text-danger mr-5"/>
                                    <FormInput name="comment" placeholder={t("comment")} type="text"
                                               className="col-12 ml-4" errorClassname="ml-4 text-danger mr-5"/>

                                    <div className="col-12 d-flex justify-content-center mt-4">
                                        <button className="btn btn-dark btn-block dim" type="submit">
                                            {t('report.payment.report')}
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

export default withNamespaces()(ReportPayment);