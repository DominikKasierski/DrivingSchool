import './App.css';
import React, {useState, useEffect} from 'react';
import {HashRouter, Route, Switch} from 'react-router-dom';
import {GuardProvider, GuardedRoute} from 'react-router-guards';
import jwt_decode from "jwt-decode";
import NotFound from "./components/errorpages/NotFound";
import Home from "./components/home/Home"
import {useLocale} from "./components/utils/login/LoginProvider";

function App() {
    const {token, currentRole, setCurrentRole, setUsername} = useLocale();
    const [roles, setRoles] = useState();

    const tokenDecode = () => {
        if (token) {
            const decodeJwt = jwt_decode(token);
            const roles = decodeJwt['roles'].split(',');
            const login = decodeJwt['sub'];
            console.log(roles)
            console.log(login)
            setRoles(roles);
            debugger
            if (localStorage.getItem('currentRole') === null) {
                setCurrentRole(roles[0])
                localStorage.setItem('currentRole', roles[0])
            }
            setUsername(login)
            localStorage.setItem('username', login)
            debugger
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
            <div className="App pb-5">
                <div>
                    {/*<NavigationBar roles={roles} divStyle={divStyle}/>*/}
                    <GuardProvider guards={[requireRoles]} error={NotFound}>
                        <Switch>
                            <GuardedRoute exact path="/" component={Home} meta={{}}/>
                        </Switch>
                        <Route component={NotFound}/>
                    </GuardProvider>
                </div>
            </div>
        </HashRouter>
    );
}

export default App;
