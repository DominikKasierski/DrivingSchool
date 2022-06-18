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
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

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
                <DropdownToggle id="dropdown-basic" className="pl-0 pl-lg-2 pr-0 pr-lg-2 mr-3 dim" variant="Secondary">
                    <span
                        style={{
                            fontSize: "1.2rem",
                        }}>{i18n.t("language")} [{i18n.language.substring(0, 2).toUpperCase()}]</span>
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
                <Navbar expand="xl" className="main-navbar dim">
                    <Navbar.Brand>
                        <div className="dim d-flex flex-wrap justify-content-start align-items-center position-relative mr-5 mb-3"
                             style={{width: "min-content"}}>
                            <LinkContainer to="/">
                                <h4 className={"cursor-pointer"}>DrivingSchool
                                    <img src={"/tmpFavicon.ico"} className={"img-fluid ml-1"} style={{maxHeight: "40px"}}
                                         alt={"favicon"}/>
                                </h4>
                            </LinkContainer>
                            <sub className={"small position-absolute mb-0"}
                                 style={{fontSize: "1rem", left: "10px", bottom: "5px"}}>{t('navigation.bar.subtitle')}</sub>
                        </div>
                    </Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="mr-auto d-flex align-items-start align-items-lg-center dim">
                            {currentRole === rolesConstant.admin && (
                                <>
                                    <Dropdown>
                                        <DropdownToggle id="dropdown-basic" className="pl-0 pl-lg-2 pr-0 pr-lg-2 mr-3 dim"
                                                        variant="Secondary">
                                            <span style={{
                                                fontSize: "1.2rem",
                                                marginRight: "10px"
                                            }}>{t('navigation.bar.vehicles')}</span>
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
                                    <Dropdown>
                                        <DropdownToggle id="dropdown-basic" className="pl-0 pl-lg-2 pr-0 pr-lg-2 mr-3 dim"
                                                        variant="Secondary">
                                            <span style={{
                                                fontSize: "1.2rem",
                                                marginRight: "10px"
                                            }}>{t('navigation.bar.accounts')}</span>
                                        </DropdownToggle>

                                        <Dropdown.Menu>
                                            <Dropdown.Item as={Link} to="/accounts">
                                                {t('navigation.bar.accounts.list')}
                                            </Dropdown.Item>
                                            <Dropdown.Item as={Link} to="/manageInstructors">
                                                {t('navigation.bar.accounts.manage.instructors')}
                                            </Dropdown.Item>
                                        </Dropdown.Menu>
                                    </Dropdown>
                                    <Dropdown>
                                        <DropdownToggle id="dropdown-basic" className="pl-0 pl-lg-2 pr-0 pr-lg-2 mr-2 dim"
                                                        variant="Secondary">
                                            <span style={{
                                                fontSize: "1.2rem",
                                                marginRight: "10px"
                                            }}>{t('navigation.bar.payments')}</span>
                                        </DropdownToggle>

                                        <Dropdown.Menu>
                                            <Dropdown.Item as={Link} to="/payments">
                                                {t('navigation.bar.reported.payments')}
                                            </Dropdown.Item>
                                            <Dropdown.Item as={Link} to="/addPayment">
                                                {t('navigation.bar.add.payment')}
                                            </Dropdown.Item>
                                            <Dropdown.Item as={Link} to="/generateReport">
                                                {t('navigation.bar.generate.report')}
                                            </Dropdown.Item>
                                        </Dropdown.Menu>
                                    </Dropdown>
                                    <Dropdown>
                                        <DropdownToggle id="dropdown-basic" className="pl-0 pl-lg-2 pr-0 pr-lg-2 mr-2 dim"
                                                        variant="Secondary">
                                            <span style={{
                                                fontSize: "1.2rem",
                                                marginRight: "10px"
                                            }}>{t('navigation.bar.lecture.groups')}</span>
                                        </DropdownToggle>

                                        <Dropdown.Menu>
                                            <Dropdown.Item as={Link} to="/lectureGroups">
                                                {t('navigation.bar.lecture.groups.list')}
                                            </Dropdown.Item>
                                            <Dropdown.Item as={Link} to="/addLectureGroup">
                                                {t('navigation.bar.add.lecture.group')}
                                            </Dropdown.Item>
                                        </Dropdown.Menu>
                                    </Dropdown>
                                    <LinkContainer to="/timetable">
                                        <Nav.Link>{t('navigation.bar.timetable')}</Nav.Link>
                                    </LinkContainer>
                                </>
                            )}
                            {/*{currentRole === rolesConstant.instructor && (*/}

                            {/*)}*/}
                            {/*{currentRole === rolesConstant.trainee && (*/}

                            {/*)}*/}
                        </Nav>
                        <Nav className="navbar-right d-flex align-items-start align-items-lg-center">
                            <LanguageSwitcher t={t} i18n={i18n}/>
                            <Dropdown alignRight={true}>
                                <DropdownToggle id="dropdown-basic" className="pl-0 pl-lg-2 pr-0 pr-lg-2 mr-2 dim"
                                                variant="Secondary">
                                    <span style={{fontSize: "1.2rem", marginRight: "10px"}}>{username}</span>
                                </DropdownToggle>

                                <Dropdown.Menu>
                                    <Dropdown.Item as={Link} to="/myAccount">
                                        {t('navigation.bar.my.account')}
                                    </Dropdown.Item>
                                    <Dropdown.Item onSelect={handleLogout} as={Link} to="/">
                                        {t('navigation.bar.logout')}
                                    </Dropdown.Item>
                                </Dropdown.Menu>
                            </Dropdown>
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