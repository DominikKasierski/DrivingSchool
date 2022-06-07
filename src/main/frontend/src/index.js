import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import NotificationProvider from "./components/utils/notifications/NotificationProvider";
import DialogProvider from "./components/utils/dialogs/DialogProvider";

ReactDOM.render(
    <React.StrictMode>
        <NotificationProvider>
            <DialogProvider>
                <App/>
            </DialogProvider>
        </NotificationProvider>
    </React.StrictMode>, document.getElementById('root')
);

reportWebVitals();
