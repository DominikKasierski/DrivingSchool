import './App.scss';
import {HashRouter, Route, Switch} from 'react-router-dom';
import {useLocale} from "./components/utils/login/LoginProvider";
import jwt_decode from "jwt-decode";
import React, {useState, useEffect} from 'react';
import {GuardProvider, GuardedRoute} from 'react-router-guards';
import NotFound from "./components/errorpages/NotFound";
import Home from "./components/home/Home"
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
import EditOwnAccount from "./components/mok/accountedit/EditOwnAccount";
import EmailChangeConfirmation from "./components/mok/accountedit/EmailChangeConfirmation";
import EditOtherAccount from "./components/mok/accountedit/EditOtherAccount";
import MyPayments from "./components/mos/payments/MyPayments";
import ReportPayment from "./components/mos/payments/ReportPayment";
import PaymentsForApproval from "./components/mos/payments/PaymentsForApproval";
import EditPayment from "./components/mos/payments/EditPayment";
import Underpayments from "./components/mos/payments/Underpayments";
import AddPayment from "./components/mos/payments/AddPayment";
import AddVehicle from "./components/mos/vehicles/AddVehicle";
import EditVehicle from "./components/mos/vehicles/EditVehicle";
import BeginCourse from "./components/mos/course/BeginCourse";
import GenerateReport from "./components/mos/report/GenerateReport";
import LectureGroupList from "./components/mos/lecturegroups/LectureGroupList";
import AddParticipants from "./components/mos/lecturegroups/AddParticipants";

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
                            <GuardedRoute exact path="/changeEmail/confirm/:code" component={EmailChangeConfirmation} meta={{}}/>
                            <GuardedRoute exact path="/accounts" component={AccountList} meta={{}}/>
                            <GuardedRoute exact path="/editOtherAccount" component={EditOtherAccount} meta={{}}/>
                            <GuardedRoute exact path="/addVehicle" component={AddVehicle} meta={{}}/>
                            <GuardedRoute exact path="/editVehicle" component={EditVehicle} meta={{}}/>
                            <GuardedRoute exact path="/beginCourse" component={BeginCourse} meta={{}}/>
                            <GuardedRoute exact path="/myPayments" component={MyPayments} meta={{}}/>
                            <GuardedRoute exact path="/reportPayment" component={ReportPayment} meta={{}}/>
                            <GuardedRoute exact path="/paymentsForApproval" component={PaymentsForApproval} meta={{}}/>
                            <GuardedRoute exact path="/editPayment" component={EditPayment} meta={{}}/>
                            <GuardedRoute exact path="/underpayments" component={Underpayments} meta={{}}/>
                            <GuardedRoute exact path="/addPayment" component={AddPayment} meta={{}}/>
                            <GuardedRoute exact path="/generateReport" component={GenerateReport} meta={{}}/>
                            <GuardedRoute exact path="/lectureGroups" component={LectureGroupList} meta={{}}/>
                            <GuardedRoute exact path="/addParticipants" component={AddParticipants} meta={{}}/>
                            <GuardedRoute exact path="/addLectures" component={Forbidden} meta={{}}/>

                            <GuardedRoute exact path="/addLectureGroup" component={Forbidden} meta={{}}/>
                            <GuardedRoute exact path="/timetableInstructor" component={NotFound} meta={{}}/>
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
