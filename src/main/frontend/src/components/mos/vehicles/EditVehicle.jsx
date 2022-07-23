import {useDangerNotification, useSuccessNotification} from "../../utils/notifications/NotificationProvider";
import axios from "axios";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {validatorFactory, ValidatorType} from "../../utils/validators/Validators";
import {Form, Formik} from "formik";
import Breadcrumb from "../../bars/Breadcrumb";
import {Link, useHistory, useLocation} from "react-router-dom";
import {Col, Container, Row} from "react-bootstrap";
import {withNamespaces} from "react-i18next";
import FormInput from "../../utils/form/FormInput";
import {useLocale} from "../../utils/login/LoginProvider";
import queryString from "query-string";
import {usePermanentChangeDialog} from "../../utils/dialogs/DialogProvider";
import {useEffect, useState} from "react";

function EditVehicle(props) {
    const {t, i18n} = props
    const [etag, setEtag] = useState();
    const {token, setToken} = useLocale();
    const [carData, setCarData] = useState([
        {
            id: "",
            model: "",
            brand: "",
            productionYear: "",
            image: "",
        }
    ]);

    const dispatchDangerNotification = useDangerNotification();
    const dispatchSuccessNotification = useSuccessNotification();
    const dispatchPermanentChangeDialog = usePermanentChangeDialog();
    const history = useHistory();
    const location = useLocation();
    const parsedQuery = queryString.parse(location.search);

    useEffect(() => {
        if (token) {
            getCarData();
        }
    }, [token]);

    function getCarData() {
        axios.get(`/resources/car/getCar/` + parsedQuery.id, {
            headers: {
                "Authorization": token,
            }
        }).then((res) => {
            setCarData(res.data);
            setEtag(res.headers.etag);
        }).catch((e) => ResponseErrorsHandler(e, dispatchDangerNotification));
    }

    function handleEditVehicle(values, setSubmitting) {
        setSubmitting(true);
        axios.put(`/resources/car/editCar`, {
            id: carData.id,
            image: values.image,
            brand: values.brand,
            model: values.model,
            productionYear: values.productionYear
        }, {
            headers: {
                "If-Match": etag,
                "Authorization": token,
            }
        }).then(res => {
            history.push("/vehicles");
            dispatchSuccessNotification({message: t("edit.vehicle.success")})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification)
        }).finally(() => {
            setSubmitting(false)
        })
    }

    function validate(values) {
        const errors = {}

        const brandErrors = validatorFactory(values.brand, ValidatorType.VEHICLE_BRAND)
        if (brandErrors.length !== 0) errors.brand = brandErrors

        const modelErrors = validatorFactory(values.model, ValidatorType.VEHICLE_MODEL)
        if (modelErrors.length !== 0) errors.model = modelErrors

        const productionYearErrors = validatorFactory(values.productionYear, ValidatorType.PRODUCTION_YEAR)
        if (productionYearErrors.length !== 0) errors.productionYear = productionYearErrors

        const imageErrors = validatorFactory(values.image, ValidatorType.IMAGE)
        if (imageErrors.length !== 0) errors.image = imageErrors

        return errors
    }

    return (
        <div className="container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link></li>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/vehicles">{t("navigation.bar.vehicles")}</Link>
                </li>
                <li className="breadcrumb-item active text-secondary" aria-current="page">{t("edit.vehicle")}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={11} md={9} lg={8} xl={7} className={"floating py-3 mx-auto mb-3 mt-4"}>
                        <div className="py-2">
                            <h1 className="font-weight-light text-center">{t("edit.vehicle.title")}: {carData.brand} {carData.model}</h1>

                            <div className="col-12 text-center mt-2 mb-4">
                                <span>{t("required.fields")}</span>
                            </div>
                            {carData.length !== 1 &&
                                <Formik
                                    initialValues={{
                                        brand: carData.brand,
                                        model: carData.model,
                                        productionYear: carData.productionYear,
                                        image: carData.image
                                    }}
                                    validate={validate}
                                    onSubmit={(values, {
                                        setSubmitting,
                                        resetForm
                                    }) => handleEditVehicle(values, setSubmitting)}>
                                    <Form className="row">
                                        <FormInput name="brand" placeholder={t("brand")} type="text"/>
                                        <FormInput name="model" placeholder={t("model")} type="text"/>
                                        <FormInput name="productionYear" placeholder={t("productionYear")} type="integer"/>
                                        <FormInput name="image" placeholder={t("image")} type="text"/>

                                        <div className="col-12 d-flex justify-content-center mt-4">
                                            <button className="btn btn-block btn-dark dim" type="submit">
                                                {t('edit')}
                                            </button>
                                        </div>
                                    </Form>
                                </Formik>
                            }
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default withNamespaces()(EditVehicle);