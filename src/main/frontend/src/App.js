import './App.css';
import {HashRouter, Route, Switch} from 'react-router-dom';
import {GuardProvider, GuardedRoute} from 'react-router-guards';
import NotFound from "./components/errorpages/NotFound";
import Home from "./components/home/Home"

function App() {

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
