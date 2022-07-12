import {useLocale} from "../../utils/login/LoginProvider";
import {
    useDangerNotification,
    useSuccessNotification
} from "../../utils/notifications/NotificationProvider";
import {Link, useHistory, useLocation} from "react-router-dom";
import axios from "axios";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import {withNamespaces} from "react-i18next";
import Breadcrumb from "../../bars/Breadcrumb";
import {Col, Container, Row} from "react-bootstrap";
import {Form, Formik} from "formik";
import {validatorFactory, ValidatorType} from "../../utils/validators/Validators";
import FormInput from "../../utils/form/FormInput";

function CreateLectureGroup(props) {
    const {t, i18n} = props
    const {token, setToken} = useLocale();

    const dispatchDangerNotification = useDangerNotification();
    const dispatchSuccessNotification = useSuccessNotification();
    const history = useHistory();

    const initialValues = {
        name: '',
        courseCategory: ''
    }

    function addLectureGroup(values, setSubmitting) {
        setSubmitting(true);
        debugger;
        axios.post(`/resources/lectureGroup/createLectureGroup`, {
            name: values.name,
            courseCategory: values.courseCategory
        }, {
            headers: {
                "Authorization": token,
            }
        }).then(res => {
            history.push("/lectureGroups");
            dispatchSuccessNotification({message: t("create.lecture.group.success")})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification)
        }).finally(() => {
            setSubmitting(false)
        })
    }

    function validate(values) {
        const errors = {}

        const lectureGroupNameErrors = validatorFactory(values.name, ValidatorType.LECTURE_GROUP_NAME)
        if (lectureGroupNameErrors.length !== 0) errors.name = lectureGroupNameErrors

        const courseCategoryErrors = validatorFactory(values.courseCategory, ValidatorType.COURSE_CATEGORY)
        if (courseCategoryErrors.length !== 0) errors.courseCategory = courseCategoryErrors

        return errors
    }

    return (
        <div className="container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link></li>
                <li className="breadcrumb-item active text-secondary"
                    aria-current="page">{t("navigation.bar.create.lecture.group")}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={11} md={9} lg={8} xl={7} className={"floating py-3 mx-auto mb-3 mt-4"}>
                        <div className="py-2">
                            <h1 className="font-weight-light text-center">{t("create.lecture.group.title")}</h1>

                            <div className="col-12 text-center mt-2 mb-4">
                                <span>{t("required.fields")}</span>
                            </div>

                            <Formik
                                initialValues={initialValues}
                                validate={validate}
                                onSubmit={(values, {setSubmitting}) => addLectureGroup(values, setSubmitting)}>
                                <Form className="row">
                                    <FormInput name="name" placeholder={t("lecture.groups.group.name")} type="text"
                                               className="col-12 ml-4" errorClassname="ml-4 text-danger mr-5"/>
                                    <FormInput name="courseCategory" placeholder={t("courseCategory")} type="text"
                                               className="col-12 ml-4" errorClassname="ml-4 text-danger mr-5"/>

                                    <div className="col-12 d-flex justify-content-center mt-4">
                                        <button className="btn btn-block btn-dark dim" type="submit">
                                            {t('create.lecture.group.create')}
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

export default withNamespaces()(CreateLectureGroup);