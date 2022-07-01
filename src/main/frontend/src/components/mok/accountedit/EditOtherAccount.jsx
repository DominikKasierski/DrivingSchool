import Breadcrumb from "../../bars/Breadcrumb";
import {Link, useHistory, useLocation} from "react-router-dom";
import {ButtonGroup, Col, Container, Row, Tab, Tabs} from "react-bootstrap";
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
import queryString from "query-string";
import {permissionsConstant, rolesConstant} from "../../utils/constants/Constants";

function EditOtherAccount(props) {
    const {t, i18n} = props
    const {token, setToken} = useLocale();
    const [etag, setETag] = useState();
    const [enabled, setEnabled] = useState(false);
    const [etagRole, setEtagRole] = useState();
    const [instructorEtag, setInstructorEtag] = useState();
    const [roles, setRoles] = useState("");
    const [permissions, setPermissions] = useState("");

    const dispatchSuccessNotification = useSuccessNotification();
    const dispatchDangerNotification = useDangerNotification();
    const dispatchPermanentChangeDialog = usePermanentChangeDialog();
    const history = useHistory();
    const location = useLocation();
    const parsedQuery = queryString.parse(location.search);

    const hasRole = (role) => {
        return roles.includes(role);
    }

    const hasPermission = (permission) => {
        return permissions.includes(permission);
    }

    const getEtag = async () => {
        const response = await axios.get(`/resources/account/getDetails/` + parsedQuery.login, {
            headers: {
                "Authorization": token,
            }
        })
        setEnabled(response.data.enabled);
        return response.headers.etag;
    };

    const getRoles = () => {
        axios.get(`/resources/access/getAccesses/` + parsedQuery.login, {
            headers: {
                "Authorization": token,
            }
        }).then(r => {
            setEtagRole(r.headers.etag)
            let data = "";
            for (let i = 0; i < r.data.accessesGranted.length; i++) {
                data += r.data.accessesGranted[i].accessType + ", ";
            }
            data = data.slice(0, data.length - 2)
            setRoles(data);
            if (data.includes(rolesConstant.instructor)) {
                getInstructorEtag().then(r => setInstructorEtag(r));
            }
        }).catch(res => {
            if (res.response != null) {
                if (res.response.status === 403) {
                    history.push("/forbidden")
                } else if (res.response.status === 500) {
                    history.push("/internalError")
                }
            }
        });
    }

    const getInstructorEtag = async () => {
        const response = await axios.get(`/resources/access/getInstructorAccess/` + parsedQuery.login, {
            headers: {
                "Authorization": token,
            }
        })
        setPermissions(response.data.permissions);
        return response.headers.etag;
    };

    useEffect(() => {
        fetchData();
    }, [token]);

    const fetchData = () => {
        if (token) {
            getEtag().then(r => setETag(r));
            getRoles();
        }
    }

    const handleAccountLock = () => {
        dispatchPermanentChangeDialog({
            confirmCallback: () => {
                lockAccount()
            },
            cancelCallback: () => {
            },
        })
    }

    const handleAccountUnlock = () => {
        dispatchPermanentChangeDialog({
            confirmCallback: () => {
                unlockAccount()
            },
            cancelCallback: () => {
            },
        })
    }

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

    const handleAccessGrant = (role) => {
        dispatchPermanentChangeDialog({
            confirmCallback: () => {
                grantAccess(role)
            },
            cancelCallback: () => {
            },
        })
    }

    const handleAccessRevoke = (role) => {
        dispatchPermanentChangeDialog({
            confirmCallback: () => {
                revokeAccess(role)
            },
            cancelCallback: () => {
            },
        })
    }

    const handleInstructorPermissionAdd = (permission) => {
        dispatchPermanentChangeDialog({
            confirmCallback: () => {
                addInstructorPermission(permission)
            },
            cancelCallback: () => {
            },
        })
    }

    const handleInstructorPermissionRemove = (permission) => {
        dispatchPermanentChangeDialog({
            confirmCallback: () => {
                removeInstructorPermission(permission)
            },
            cancelCallback: () => {
            },
        })
    }

    const lockAccount = () => {
        axios.put(`/resources/account/lock/` + parsedQuery.login, {}, {
            headers: {
                "If-Match": etag,
                "Authorization": token
            }
        }).then((res) => {
            history.push("/accounts");
            dispatchSuccessNotification({message: i18n.t('edit.other.account.lock.account.success')})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification);
        });
    };

    const unlockAccount = () => {
        axios.put(`/resources/account/unlock/` + parsedQuery.login, {}, {
            headers: {
                "If-Match": etag,
                "Authorization": token
            }
        }).then((res) => {
            history.push("/accounts");
            dispatchSuccessNotification({message: i18n.t('edit.other.account.unlock.account.success')})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification);
        });
    };

    const editEmailAddress = (values, setSubmitting) => {
        axios.put(`/resources/account/editEmail` + parsedQuery.login, {newEmailAddress: values.emailAddress}, {
            headers: {
                "Content-Type": "application/json",
                "If-Match": etag,
                "Authorization": token
            }
        }).then((res) => {
            history.push("/accounts");
            dispatchSuccessNotification({message: i18n.t('edit.other.account.edit.email.address.success')})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification);
        });
        setSubmitting(false);
    };

    const grantAccess = (role) => {
        axios.put(`/resources/access/grant/` + parsedQuery.login + "/" + role, {}, {
            headers: {
                "If-Match": etagRole,
                "Authorization": token
            }
        }).then((res) => {
            dispatchSuccessNotification({message: i18n.t('edit.other.account.grant.access.success')})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification);
        });
    };

    const revokeAccess = (role) => {
        axios.put(`/resources/access/revoke/` + parsedQuery.login + "/" + role, {}, {
            headers: {
                "If-Match": etagRole,
                "Authorization": token
            }
        }).then((res) => {
            dispatchSuccessNotification({message: i18n.t('edit.other.account.revoke.access.success')})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification);
        });
    };

    const addInstructorPermission = (permission) => {
        axios.put(`/resources/access/addInstructorPermission/` + parsedQuery.login + "/" + permission, {}, {
            headers: {
                "If-Match": instructorEtag,
                "Authorization": token
            }
        }).then((res) => {
            dispatchSuccessNotification({message: i18n.t('edit.other.account.add.instructor.permission.success')})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification);
        });
    };

    const removeInstructorPermission = (permission) => {
        axios.put(`/resources/access/removeInstructorPermission/` + parsedQuery.login + "/" + permission, {}, {
            headers: {
                "If-Match": instructorEtag,
                "Authorization": token
            }
        }).then((res) => {
            dispatchSuccessNotification({message: i18n.t('edit.other.account.remove.instructor.permission.success')})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification);
        });
    };

    function validateEmailAddress(values) {
        const errors = {}

        const emailAddressErrors = validatorFactory(values.emailAddress, ValidatorType.EMAIL_ADDRESS)
        if (emailAddressErrors.length !== 0) errors.emailAddress = emailAddressErrors

        return errors
    }

    return (
        <div className="container-fluid">
            <Breadcrumb>
                <li className="breadcrumb-item"><Link className={"text-dark"} to="/">{t("navigation.bar.main.page")}</Link></li>
                <li className="breadcrumb-item"><Link className={"text-dark"}
                                                      to="/myAccount">{t("navigation.bar.accounts.list")}</Link></li>
                <li className="breadcrumb-item active text-secondary" aria-current="page">{t("edit.other.account")}</li>
            </Breadcrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={11} md={9} lg={8} xl={7} className={"floating py-3 mx-auto mb-3 mt-4"}>
                        <div className="py-2">
                            <h1 className="font-weight-light text-center">{t("edit.account")}: {parsedQuery.login}</h1>

                            <div className="d-flex mx-auto justify-content-center mt-4 mb-3">

                                {enabled ?
                                    <button className="btn btn-dark col-3 dim mr-2" type="submit" onClick={handleAccountLock}>
                                        {t('edit.other.account.lock')}
                                    </button> :
                                    <button className="btn btn-dark col-3 dim mr-2" type="submit" onClick={handleAccountUnlock}>
                                        {t('edit.other.account.unlock')}
                                    </button>
                                }
                                <button className="btn btn-dark col-3 dim ml-2" type="submit" onClick={fetchData}>
                                    {t('refresh')}
                                </button>
                            </div>

                            <div className="col-12 text-center mt-1 mb-2">
                                <span>{t("required.fields")}</span>
                            </div>
                        </div>
                        <Tabs defaultActiveKey="email" className="justify-content-center dim nav-justified">
                            <Tab tabClassName={"text-white bg-transparent"} eventKey="email"
                                 title={t('edit.other.account.edit.email.address')}>
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
                            <Tab tabClassName={"text-white bg-transparent"} eventKey="roles"
                                 title={t('edit.other.account.edit.roles')}>
                                {hasRole(rolesConstant.trainee) ?
                                    <button className="btn btn-outline-danger brightened btn-block mt-4 py-2" type="submit"
                                            onClick={() => handleAccessRevoke(rolesConstant.trainee)}>
                                        {t('edit.other.account.revoke.trainee.role')}
                                    </button> :
                                    <button className="btn btn-outline-success brightened btn-block mt-4 py-2" type="submit"
                                            onClick={() => handleAccessGrant(rolesConstant.trainee)}>
                                        {t('edit.other.account.grant.trainee.role')}
                                    </button>
                                }
                                {hasRole(rolesConstant.instructor) ?
                                    <button className="btn btn-outline-danger brightened btn-block mt-3 py-2" type="submit"
                                            onClick={() => handleAccessRevoke(rolesConstant.instructor)}>
                                        {t('edit.other.account.revoke.instructor.role')}
                                    </button> :
                                    <button className="btn btn-outline-success brightened btn-block mt-3 py-2" type="submit"
                                            onClick={() => handleAccessGrant(rolesConstant.instructor)}>
                                        {t('edit.other.account.grant.instructor.role')}
                                    </button>
                                }
                                {hasRole(rolesConstant.admin) ?
                                    <button className="btn btn-outline-danger brightened btn-block mt-3 py-2" type="submit"
                                            onClick={() => handleAccessRevoke(rolesConstant.admin)}>
                                        {t('edit.other.account.revoke.admin.role')}
                                    </button> :
                                    <button className="btn btn-outline-success brightened btn-block mt-3 py-2" type="submit"
                                            onClick={() => handleAccessGrant(rolesConstant.admin)}>
                                        {t('edit.other.account.grant.admin.role')}
                                    </button>
                                }
                            </Tab>
                            {hasRole(rolesConstant.instructor) &&
                                <Tab tabClassName={"text-white bg-transparent"} eventKey="permissions"
                                     title={t('edit.other.account.edit.permissions')}>
                                    {hasPermission(permissionsConstant.A) ?
                                        <button className="btn btn-outline-danger brightened btn-block mt-4 py-2" type="submit"
                                                onClick={() => handleInstructorPermissionRemove(permissionsConstant.A)}>
                                            {t('edit.other.account.remove.a.permission')}
                                        </button> :
                                        <button className="btn btn-outline-success brightened btn-block mt-4 py-2" type="submit"
                                                onClick={() => handleInstructorPermissionAdd(permissionsConstant.A)}>
                                            {t('edit.other.account.add.a.permission')}
                                        </button>
                                    }
                                    {hasPermission(permissionsConstant.B) ?
                                        <button className="btn btn-outline-danger brightened btn-block mt-3 py-2" type="submit"
                                                onClick={() => handleInstructorPermissionRemove(permissionsConstant.B)}>
                                            {t('edit.other.account.remove.b.permission')}
                                        </button> :
                                        <button className="btn btn-outline-success brightened btn-block mt-3 py-2" type="submit"
                                                onClick={() => handleInstructorPermissionAdd(permissionsConstant.B)}>
                                            {t('edit.other.account.add.b.permission')}
                                        </button>
                                    }
                                    {hasPermission(permissionsConstant.C) ?
                                        <button className="btn btn-outline-danger brightened btn-block mt-3 py-2" type="submit"
                                                onClick={() => handleInstructorPermissionRemove(permissionsConstant.C)}>
                                            {t('edit.other.account.remove.c.permission')}
                                        </button> :
                                        <button className="btn btn-outline-success brightened btn-block mt-3 py-2" type="submit"
                                                onClick={() => handleInstructorPermissionAdd(permissionsConstant.C)}>
                                            {t('edit.other.account.add.c.permission')}
                                        </button>
                                    }
                                </Tab>
                            }
                        </Tabs>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default withNamespaces()(EditOtherAccount)