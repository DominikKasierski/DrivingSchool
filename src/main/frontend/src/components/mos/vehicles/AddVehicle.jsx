import {useDangerNotification, useSuccessNotification} from "../../utils/notifications/NotificationProvider";
import axios from "axios";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {validatorFactory, ValidatorType} from "../../utils/validators/Validators";
import {Form, Formik} from "formik";
import Breadcrumb from "../../bars/Breadcrumb";
import {Link, useHistory} from "react-router-dom";
import {Col, Container, Row} from "react-bootstrap";
import {withNamespaces} from "react-i18next";
import FormInput from "../../utils/form/FormInput";

function AddVehicle(props) {
    const {t, i18n} = props

    const dispatchDangerNotification = useDangerNotification();
    const dispatchSuccessNotification = useSuccessNotification();
    const history = useHistory();

    const initialValues = {
        courseCategory: '',
        brand: '',
        model: '',
        productionYear: '',
        registrationNumber: '',
        image: ''
    }

    function handleAddVehicle(values, setSubmitting) {
        setSubmitting(true);
        axios.post(`/resources/car/addCar`, {
            courseCategory: values.courseCategory,
            brand: values.brand,
            model: values.model,
            productionYear: values.productionYear,
            registrationNumber: values.registrationNumber,
            image: values.image
        }).then(res => {
            history.push("/");
            dispatchSuccessNotification({message: t("add.vehicle.success")})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification)
        }).finally(() => {
            setSubmitting(false)
        })
    }

    function validate(values) {
        const errors = {}

        const courseCategoryErrors = validatorFactory(values.courseCategory, ValidatorType.COURSE_CATEGORY)
        if (courseCategoryErrors.length !== 0) errors.courseCategory = courseCategoryErrors

        const brandErrors = validatorFactory(values.brand, ValidatorType.VEHICLE_NAME)
        if (brandErrors.length !== 0) errors.brand = brandErrors

        const modelErrors = validatorFactory(values.model, ValidatorType.VEHICLE_NAME)
        if (modelErrors.length !== 0) errors.model = modelErrors

        const productionYearErrors = validatorFactory(values.productionYear, ValidatorType.PRODUCTION_YEAR)
        if (productionYearErrors.length !== 0) errors.productionYear = productionYearErrors

        const registrationNumberErrors = validatorFactory(values.registrationNumber, ValidatorType.REGISTRATION_NUMBER)
        if (registrationNumberErrors.length !== 0) errors.registrationNumber = registrationNumberErrors

        const imageErrors = validatorFactory(values.image, ValidatorType.IMAGE)
        if (imageErrors.length !== 0) errors.image = imageErrors

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
                                <span>{t("required.fields")}</span>
                            </div>

                            <Formik
                                initialValues={initialValues}
                                validate={validate}
                                onSubmit={(values, {
                                    setSubmitting,
                                    resetForm
                                }) => handleAddVehicle(values, setSubmitting)}>
                                <Form className="row">
                                    <FormInput name="courseCategory" placeholder={t("courseCategory")} type="text"/>
                                    <FormInput name="brand" placeholder={t("brand")} type="text"/>
                                    <FormInput name="model" placeholder={t("model")} type="text"/>
                                    <FormInput name="productionYear" placeholder={t("productionYear")} type="integer"/>
                                    <FormInput name="registrationNumber" placeholder={t("registrationNumber")} type="text"/>
                                    <FormInput name="image" placeholder={t("image")} type="text"/>

                                    <div className="col-12 d-flex justify-content-center mt-4">
                                        <button className="btn btn-block btn-dark dim" type="submit">
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

export default withNamespaces()(AddVehicle);