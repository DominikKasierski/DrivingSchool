import "../../css/NavigationBar.scss";
import {LinkContainer} from "react-router-bootstrap";
import {useLocale} from "../utils/login/LoginProvider";
import {useEffect, useState} from "react";
import {useDangerNotification, useSuccessNotification} from "../utils/notifications/NotificationProvider";
import {ResponseErrorsHandler} from "../utils/handlers/ResponseErrorsHandler";
import axios from "axios";
import {Dropdown, Nav, Navbar} from "react-bootstrap";
import DropdownToggle from "react-bootstrap/DropdownToggle";
import {Link, useHistory} from "react-router-dom";
import {withNamespaces} from "react-i18next";
import {rolesConstant} from "../utils/constants/Constants";


function LanguageSwitcher(props) {
    const {t, i18n} = props
    const {token, setToken} = useLocale();
    const [etag, setETag] = useState();
    const availableLanguages = ['pl', 'en']

    const dispatchDangerNotification = useDangerNotification();
    const dispatchSuccessNotification = useSuccessNotification();

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
            debugger
        }
    }, [token, i18n.language]);

    const handleGuestClickPl = () => {
        setLanguage(i18n, "pl")
    }

    const handleGuestClickEn = () => {
        setLanguage(i18n, "en")
    }

    const handleUserClickPl = () => {
        axios.put(`/resources/account/editLanguage`, "pl", {
            headers: {
                "Authorization": token, "If-Match": etag
            }
        }).then(() => {
            handleGuestClickPl();
            dispatchSuccessNotification({message: i18n.t('language.change.success')})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification);
        })
    }

    const handleUserClickEn = () => {
        axios.put(`/resources/account/editLanguage`, "en", {
            headers: {
                "Authorization": token, "If-Match": etag
            }
        }).then(() => {
            handleGuestClickEn();
            dispatchSuccessNotification({message: i18n.t('language.change.success')})
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification);
        })
    }

    return (
        <>
            <Dropdown>
                <DropdownToggle id="dropdown-basic" className="dropButton pl-0 pl-lg-2 pr-0 pr-lg-2" variant="Secondary">
                    <span
                        style={{marginRight: "10px"}}>{i18n.t("language")} [{i18n.language.substring(0, 2).toUpperCase()}]</span>
                </DropdownToggle>

                <Dropdown.Menu>
                    <Dropdown.Item onClick={token !== null && token !== '' ? (handleUserClickPl) : (handleGuestClickPl)}>
                        {t(availableLanguages[0])} </Dropdown.Item>
                    <Dropdown.Item onClick={token !== null && token !== '' ? (handleUserClickEn) : (handleGuestClickEn)}>
                        {t(availableLanguages[1])}</Dropdown.Item>
                </Dropdown.Menu>
            </Dropdown>
        </>
    )
}

function NavigationBar(props) {
    const history = useHistory();
    const {t, divStyle, i18n} = props
    const {token, username, setToken, currentRole, setCurrentRole, setUsername} = useLocale();
    const [instructorAccess, setInstructorAccess] = useState(false)

    const dispatchDangerNotification = useDangerNotification();
    const dispatchSuccessNotification = useSuccessNotification();

    const handleLogout = () => {
        axios.get(`/resources/auth/logout`, {
            headers: {
                "Authorization": token,
            },
            validateStatus: (status) => {
                return (status >= 200 && status <= 299) || status === 401;
            }
        }).then((res) => {
        }).catch((err) => {
        });
        setToken('');
        setCurrentRole('');
        setUsername('');
        localStorage.removeItem('token')
        localStorage.removeItem('currentRole')
        localStorage.removeItem('username')
        clearTimeout(localStorage.getItem('timeoutId') ?? 0)
        history.push("/login")
        dispatchSuccessNotification({message: t("logout.success")});
    }

    return (
        <>
            {token !== null && token !== '' ? (
                <Navbar expand="xl" className="main-navbar">
                    <Navbar.Brand>
                        <div className="d-flex flex-wrap justify-content-start align-items-center position-relative mr-5 mb-3"
                             style={{width: "min-content"}}>
                            <LinkContainer to="/">
                                <h4 className={"cursor-pointer"}>DrivingSchool
                                    <img src={"/tmpFavicon.ico"} className={"img-fluid ml-1"} style={{maxHeight: "40px"}}
                                         alt={"favicon"}/>
                                </h4>
                            </LinkContainer>
                            <sub className={"small position-absolute mb-0"}
                                 style={{fontSize: "1rem", left: "10px", bottom:"5px"}}>{t('navigation.bar.subtitle')}</sub>
                        </div>
                    </Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="mr-auto d-flex align-items-start align-items-lg-center">
                            {currentRole === rolesConstant.admin && (
                                <>
                                    <Dropdown>
                                        <DropdownToggle id="dropdown-basic" className="dropButton pl-0 pl-lg-2 pr-0 pr-lg-2"
                                                        variant="Info">
                                            <span style={{fontSize: "1.2rem", marginRight: "10px"}}>{t('navigation.bar.vehicles')}</span>
                                        </DropdownToggle>

                                        <Dropdown.Menu>
                                            <Dropdown.Item as={Link} to="/vehicles">
                                                {t('navigation.bar.vehicles.list')}
                                            </Dropdown.Item>
                                            <Dropdown.Item as={Link} to="/vehicles/add">
                                                {t('navigation.bar.vehicles.add')}
                                            </Dropdown.Item>
                                        </Dropdown.Menu>
                                    </Dropdown>
                                </>
                            )}
                            {/*{currentRole === rolesConstant.instructor && (*/}

                            {/*)}*/}
                            {/*{currentRole === rolesConstant.trainee && (*/}

                            {/*)}*/}
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
            ) : (
                <Navbar><span>Navbar dla not logged</span></Navbar>
            )}
        </>
    )
}

export function setLanguage(i18n, lang) {
    i18n.changeLanguage(lang)
}

export default withNamespaces()(NavigationBar);