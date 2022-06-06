import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import NotificationProvider from "./components/utils/notifications/NotificationProvider";

ReactDOM.render(
    <React.StrictMode>
        <NotificationProvider>
            <App/>
        </NotificationProvider>
    </React.StrictMode>, document.getElementById('root')
);

reportWebVitals();
