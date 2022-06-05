import logo from './images/logo.svg';
import './App.css';
import {
    useDangerNotification,
    useSuccessNotification,
    useWarningNotification
} from "./components/notifications/NotificationProvider";

function App() {
    const dispatchDangerNotification = useDangerNotification();
    const dispatchWarningNotification = useWarningNotification();
    const dispatchSuccessNotification = useSuccessNotification();

    return (
        <div className="App pb-5">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
                <p>React</p>

                <button onClick={() => dispatchDangerNotification({
                    message: ("message, message, message, message, message, message"),
                })}>
                    Danger
                </button>
                <button onClick={() => dispatchWarningNotification({
                    message: ("message, message, message, message, message, message"),
                })}>
                    Warning
                </button>
                <button onClick={() => dispatchSuccessNotification({
                    message: ("message, message, message, message, message, message"),
                })}>
                    Success
                </button>

                <a
                    className="App-link"
                    href="https://reactjs.org"
                    target="_blank"
                    rel="noopener noreferrer"
                >
                    Learn React
                </a>
            </header>
        </div>
    );
}

export default App;
