import "../../css/NavigationBar.scss";
import {LinkContainer} from "react-router-bootstrap";
import {useLocale} from "../utils/login/LoginProvider";
import {useSuccessNotification} from "../utils/notifications/NotificationProvider";
import axios from "axios";
import {Dropdown, Nav, Navbar} from "react-bootstrap";
import DropdownToggle from "react-bootstrap/DropdownToggle";
import {Link, useHistory} from "react-router-dom";
import {withNamespaces} from "react-i18next";
import {rolesConstant} from "../utils/constants/Constants";
import LanguageChangeHandler from "../utils/handlers/LanguageChangeHandler";

function NavigationBar(props) {
    const {t, i18n} = props
    const {token, username, setToken, currentRole, setCurrentRole, setUsername} = useLocale();

    const dispatchSuccessNotification = useSuccessNotification();
    const history = useHistory();

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
        history.push("/signIn")
        dispatchSuccessNotification({message: t("logout.success")});
    }

    return (
        <>
            {token !== null && token !== '' ? (
                <Navbar expand="xl" className="main-navbar dim">
                    <Navbar.Brand>
                        <div className="dim d-flex flex-wrap justify-content-start align-items-center position-relative mr-5 mb-2"
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
                                            }}>{t('navigation.bar.payments')}</span>
                                        </DropdownToggle>

                                        <Dropdown.Menu>
                                            <Dropdown.Item as={Link} to="/reportedPayments">
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
                                        <DropdownToggle id="dropdown-basic" className="pl-0 pl-lg-2 pr-0 pr-lg-2 mr-3 dim"
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
                                    <LinkContainer to="/accounts">
                                        <Nav.Link className={"mr-3"}>{t('navigation.bar.accounts.list')}</Nav.Link>
                                    </LinkContainer>
                                    <LinkContainer to="/timetable">
                                        <Nav.Link>{t('navigation.bar.timetable')}</Nav.Link>
                                    </LinkContainer>
                                </>
                            )}
                            {currentRole === rolesConstant.instructor && (
                                <>
                                    <LinkContainer to="/vehicles">
                                        <Nav.Link className={"mr-3"}>{t('navigation.bar.vehicles')}</Nav.Link>
                                    </LinkContainer>
                                    <LinkContainer to="/timetableInstructor">
                                        <Nav.Link className={"mr-3"}>{t('navigation.bar.timetable')}</Nav.Link>
                                    </LinkContainer>
                                    <LinkContainer to="/cancelDrivingLesson">
                                        <Nav.Link>{t('navigation.bar.cancel.driving.lesson')}</Nav.Link>
                                    </LinkContainer>
                                </>
                            )}
                            {currentRole === rolesConstant.trainee && (
                                <>
                                    <LinkContainer to="/vehicles">
                                        <Nav.Link className={"mr-3"}>{t('navigation.bar.vehicles')}</Nav.Link>
                                    </LinkContainer>
                                    <LinkContainer to="/beginCourse">
                                        <Nav.Link className={"mr-3"}>{t('navigation.bar.begin.course')}</Nav.Link>
                                    </LinkContainer>
                                    <Dropdown>
                                        <DropdownToggle id="dropdown-basic" className="pl-0 pl-lg-2 pr-0 pr-lg-2 mr-3 dim"
                                                        variant="Secondary">
                                            <span style={{
                                                fontSize: "1.2rem",
                                                marginRight: "10px"
                                            }}>{t('navigation.bar.payments')}</span>
                                        </DropdownToggle>

                                        <Dropdown.Menu>
                                            <Dropdown.Item as={Link} to="/myPayments">
                                                {t('navigation.bar.reported.payments')}
                                            </Dropdown.Item>
                                            <Dropdown.Item as={Link} to="/reportPayment">
                                                {t('navigation.bar.report.payment')}
                                            </Dropdown.Item>
                                        </Dropdown.Menu>
                                    </Dropdown>
                                    <LinkContainer to="/timetableTrainee">
                                        <Nav.Link className={"mr-3"}>{t('navigation.bar.timetable')}</Nav.Link>
                                    </LinkContainer>
                                </>
                            )}
                        </Nav>
                        <Nav className="navbar-right d-flex align-items-start align-items-lg-center ">
                            <LanguageChangeHandler t={t} i18n={i18n}/>
                            <Dropdown alignRight={true}>
                                <DropdownToggle id="dropdown-basic" className="pl-0 pl-lg-2 pr-0 pr-lg-2 mr-3 dim"
                                                variant="Secondary">
                                    <span style={{fontSize: "1.2rem", marginRight: "10px"}}>{username}</span>
                                </DropdownToggle>

                                <Dropdown.Menu>
                                    <Dropdown.Item as={Link} to="/myAccount">
                                        {t('navigation.bar.my.account')}
                                    </Dropdown.Item>
                                    {currentRole === rolesConstant.trainee && (
                                        <Dropdown.Item as={Link} to="/drivingStatistics">
                                            {t('navigation.bar.driving.statistics')}
                                        </Dropdown.Item>
                                    )}
                                    <Dropdown.Item onSelect={handleLogout} as={Link} to="/">
                                        {t('navigation.bar.logout')}
                                    </Dropdown.Item>
                                </Dropdown.Menu>
                            </Dropdown>
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
            ) : (
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
                            <LinkContainer to="/">
                                <Nav.Link className={"mr-3"}>{t('navigation.bar.main.page')}</Nav.Link>
                            </LinkContainer>
                            <LinkContainer to="/vehicles">
                                <Nav.Link>{t('navigation.bar.vehicles')}</Nav.Link>
                            </LinkContainer>
                        </Nav>
                        <Nav className="navbar-right d-flex align-items-start align-items-lg-center dim">
                            <LanguageChangeHandler t={t} i18n={i18n}/>
                            <div className={"d-flex flex-nowrap flex-md-wrap mt-2 mt-lg-0 mb-2 mb-lg-0"}>
                                <LinkContainer to="/signUp">
                                    <Nav.Link className="ml-0 mr-3">
                                        <div>{t("sign.up")}</div>
                                    </Nav.Link>
                                </LinkContainer>
                                <LinkContainer to="/signIn">
                                    <Nav.Link className={"mr-3"}>
                                        <div>{t("sign.in")}</div>
                                    </Nav.Link>
                                </LinkContainer>
                            </div>
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
            )}
        </>
    )
}

export default withNamespaces()(NavigationBar);