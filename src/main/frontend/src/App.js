import './App.scss';
import React, {useState, useEffect} from 'react';
import {HashRouter, Route, Switch} from 'react-router-dom';
import {GuardProvider, GuardedRoute} from 'react-router-guards';
import jwt_decode from "jwt-decode";
import NotFound from "./components/errorpages/NotFound";
import Home from "./components/home/Home"
import {useLocale} from "./components/utils/login/LoginProvider";
import NavigationBar from "./components/bars/NavigationBar";
import Forbidden from "./components/errorpages/Forbidden";
import InternalError from "./components/errorpages/InternalError";
import Footer from "./components/bars/Footer";
import SignUp from "./components/mok/signup/SignUp"
import SignUpConfirmation from "./components/mok/signup/SignUpConfirmation"
import SignIn from "./components/mok/signin/SignIn";
import PasswordReset from "./components/mok/passwordreset/PasswordReset";
import PasswordResetConfirmation from "./components/mok/passwordreset/PasswordResetConfirmation";
import UserPage from "./components/mok/signin/UserPage";
import VehicleList from "./components/mos/vehicles/VehicleList";
import MyAccount from "./components/mok/accounts/MyAccount";
import AccountList from "./components/mok/accounts/AccountList";
import EditOwnAccount from "./components/mok/accounts/EditOwnAccount";

function App() {
    const {token, currentRole, setCurrentRole, setUsername} = useLocale();
    const [roles, setRoles] = useState();

    const tokenDecode = () => {
        if (token) {
            const decodeJwt = jwt_decode(token);
            const roles = decodeJwt['roles'].split(',');
            const login = decodeJwt['sub'];
            setRoles(roles);
            if (localStorage.getItem('currentRole') === null) {
                setCurrentRole(roles[0])
                localStorage.setItem('currentRole', roles[0])
            }
            setUsername(login)
            localStorage.setItem('username', login)
        }
    }

    useEffect(() => {
        tokenDecode();
    }, [token])

    let logged = !!token;

    const requireRoles = (to, from, next) => {
        next();
    };

    return (
        <HashRouter basename={process.env.REACT_APP_ROUTER_BASE || ''}>
            <div className="App">
                <div>
                    <NavigationBar roles={roles}/>
                    <GuardProvider guards={[requireRoles]} error={NotFound}>
                        <Switch>
                            <GuardedRoute exact path="/" component={Home} meta={{}}/>
                            <GuardedRoute exact path="/forbidden" component={Forbidden}/>
                            <GuardedRoute exact path="/internalError" component={InternalError}/>
                            <GuardedRoute exact path="/signUp" component={SignUp} meta={{}}/>
                            <GuardedRoute exact path="/signUp/confirm/:code" component={SignUpConfirmation} meta={{}}/>
                            <GuardedRoute exact path="/signIn" component={SignIn} meta={{}}/>
                            <GuardedRoute exact path="/signIn/resetPassword" component={PasswordReset} meta={{}}/>
                            <GuardedRoute exact path="/resetPassword/confirm/:code" component={PasswordResetConfirmation}
                                          meta={{}}/>
                            <GuardedRoute exact path="/userPage" component={UserPage} meta={{}}/>
                            <GuardedRoute exact path="/vehicles" component={VehicleList} meta={{}}/>
                            <GuardedRoute exact path="/myAccount" component={MyAccount} meta={{}}/>
                            <GuardedRoute exact path="/editOwnAccount" component={EditOwnAccount} meta={{}}/>
                            <GuardedRoute exact path="/accounts" component={AccountList} meta={{}}/>
                            <GuardedRoute exact path="/editOtherAccount" component={NotFound} meta={{}}/>
                            <GuardedRoute exact path="/vehicles/add" component={NotFound} meta={{}}/>
                            <GuardedRoute exact path="/accounts" component={NotFound} meta={{}}/>
                            <GuardedRoute exact path="/reportedPayments" component={NotFound} meta={{}}/>
                            <GuardedRoute exact path="/addPayment" component={InternalError} meta={{}}/>
                            <GuardedRoute exact path="/generateReport" component={Forbidden} meta={{}}/>
                            <GuardedRoute exact path="/lectureGroups" component={InternalError} meta={{}}/>
                            <GuardedRoute exact path="/addLectureGroup" component={Forbidden} meta={{}}/>
                            <GuardedRoute exact path="/timetableInstructor" component={NotFound} meta={{}}/>
                            <GuardedRoute exact path="/myPayments" component={Forbidden} meta={{}}/>
                            <GuardedRoute exact path="/reportPayment" component={NotFound} meta={{}}/>
                            <GuardedRoute exact path="/timetableTrainee" component={Forbidden} meta={{}}/>
                            <Route component={NotFound}/>
                        </Switch>
                    </GuardProvider>
                    <Footer roles={roles}/>
                </div>
            </div>
        </HashRouter>
    );
}

export default App;
