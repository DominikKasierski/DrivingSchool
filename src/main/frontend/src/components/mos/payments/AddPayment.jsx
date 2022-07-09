import {withNamespaces} from "react-i18next";
import {useLocale} from "../../utils/login/LoginProvider";
import {useState, useEffect} from "react";
import {useDangerNotification, useSuccessNotification} from "../../utils/notifications/NotificationProvider";
import {Link, useHistory, useLocation} from "react-router-dom";
import axios from "axios";
import queryString from "query-string";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {validatorFactory, ValidatorType} from "../../utils/validators/Validators";
import Breadcrumb from "../../bars/Breadcrumb";
import {Col, Container, Row} from "react-bootstrap";
import {Form, Formik} from "formik";
import FormInput from "../../utils/form/FormInput";
import OptionalFormInput from "../../utils/form/OptionalFormInput";

function AddPayment(props) {
    const {t, i18n} = props
    const {token, setToken} = useLocale();
    const [etag, setEtag] = useState();

    const dispatchSuccessNotification = useSuccessNotification();
    const dispatchDangerNotification = useDangerNotification();
    const history = useHistory();
    const location = useLocation();
    const parsedQuery = queryString.parse(location.search);

    const initialValues = {
        value: '',
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
        axios.put(`/resources/payment/addPayment/` + parsedQuery.login, {
            value: values.value,
            comment: values.comment ? values.comment : null
        }, {
            headers: {
                "If-Match": etag,
                "Authorization": token
            }
        }).then(res => {
            history.push("/underpayments");
            dispatchSuccessNotification({message: t("add.payment.success")})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification)
        }).finally(() => {
            setSubmitting(false)
        })
    }

    function validate(values) {
        const errors = {}

        const valueErrors = validatorFactory(values.value, ValidatorType.PRICE)
        if (valueErrors.length !== 0) errors.value = valueErrors

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
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.underpayments")}</Link>
                </li>
                <li className="breadcrumb-item active text-secondary"
                    aria-current="page">{t('underpayments.add.payment')}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={11} sm={10} md={8} lg={7} xl={6} className={"floating py-3 mx-auto mb-3 mt-4"}>
                        <div className="py-2">
                            <h1 className="font-weight-light text-center mb-4">{t("add.payment.title")}: {parsedQuery.login}</h1>

                            <div className="col-12 text-center mt-2 mb-4">
                                <span>{t("required.fields")}</span>
                            </div>

                            <Formik
                                initialValues={initialValues}
                                validate={validate}
                                onSubmit={(values, {setSubmitting}) => handleSubmit(values, setSubmitting)}>
                                <Form className={"row"}>
                                    <FormInput name="value" placeholder={t("report.payment.value") + " (" + t("currency") + ")"}
                                               type="text"
                                               className="col-12 ml-4" errorClassname="ml-4 text-danger mr-5"/>
                                    <OptionalFormInput name="comment" placeholder={t("report.payment.comment")} type="text"
                                                       className="col-12 ml-5" errorClassname="ml-2 text-danger mr-5"/>

                                    <div className="col-12 d-flex justify-content-center mt-4">
                                        <button className="btn btn-dark btn-block dim" type="submit">
                                            {t('underpayments.add.payment')}
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

export default withNamespaces()(AddPayment);