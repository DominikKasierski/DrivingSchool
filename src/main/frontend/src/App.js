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

    // Kolorki w zależności od poziomu dostępu
    // const divStyle = () => {
    //     switch (currentRole) {
    //         case rolesConstant.admin:
    //             return {backgroundColor: "var(--admin-color)"};
    //         case rolesConstant.manager:
    //             return {backgroundColor: "var(--manager-color)"};
    //         case rolesConstant.client:
    //             return {backgroundColor: "var(--client-color)"};
    //     }
    // };

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
                            <GuardedRoute exact path="/vehicles" component={Forbidden} meta={{}}/>
                            <GuardedRoute exact path="/vehicles/add" component={NotFound} meta={{}}/>
                            <GuardedRoute exact path="/accounts" component={NotFound} meta={{}}/>
                            <GuardedRoute exact path="/manageInstructors" component={InternalError} meta={{}}/>
                            <GuardedRoute exact path="/reportedPayments" component={NotFound} meta={{}}/>
                            <GuardedRoute exact path="/addPayment" component={InternalError} meta={{}}/>
                            <GuardedRoute exact path="/generateReport" component={Forbidden} meta={{}}/>
                            <GuardedRoute exact path="/lectureGroups" component={InternalError} meta={{}}/>
                            <GuardedRoute exact path="/addLectureGroup" component={Forbidden} meta={{}}/>
                            <GuardedRoute exact path="/timetable" component={Forbidden} meta={{}}/>
                            <GuardedRoute exact path="/myAccount" component={Forbidden} meta={{}}/>
                            <GuardedRoute exact path="/signUp" component={NotFound} meta={{}}/>
                            <GuardedRoute exact path="/login" component={InternalError} meta={{}}/>
                            <GuardedRoute exact path="/myPayments" component={Forbidden} meta={{}}/>
                            <GuardedRoute exact path="/reportPayment" component={NotFound} meta={{}}/>
                            <GuardedRoute exact path="/bookDrivingLesson" component={Forbidden} meta={{}}/>
                            <GuardedRoute exact path="/cancelDrivingLesson" component={InternalError} meta={{}}/>
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
