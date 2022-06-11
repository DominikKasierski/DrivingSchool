import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import LoginProvider from "./components/utils/login/LoginProvider";
import DialogProvider from "./components/utils/dialogs/DialogProvider";
import NotificationProvider from "./components/utils/notifications/NotificationProvider";

ReactDOM.render(
    <React.StrictMode>
        <NotificationProvider>
            <DialogProvider>
                <LoginProvider>
                    <App/>
                </LoginProvider>
            </DialogProvider>
        </NotificationProvider>
    </React.StrictMode>,
    document.getElementById('root')
)
;

reportWebVitals();
