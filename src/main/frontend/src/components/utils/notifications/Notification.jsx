import React, {useState} from 'react';
import {Alert, ProgressBar} from "react-bootstrap";

const Notification = (props) => {
    const [progress, setProgress] = useState(0);
    const [intervalId, setIntervalId] = useState(null);
    const [useEffectStarted, setUseEffectStarted] = useState(false);

    const handleIncreaseProgress = () => {
        if (props.notificationDuration > 0) {
            const id = setInterval(() => {
                setProgress(prev => {
                    if (prev === 105) {
                        clearInterval(id);
                        return prev;
                    }
                    return prev + 1;
                });
            }, (props.notificationDuration) / 100);
            setIntervalId(id);
        }
    };

    const handlePauseProgress = () => {
        clearInterval(intervalId);
    };

    const handleCloseNotification = () => {
        handlePauseProgress();
        setTimeout(() => {
            props.dispatch({
                type: "REMOVE_NOTIFICATION",
                id: props.id
            })
        }, 100);
    };

    React.useEffect(() => {
        if (progress === 105) {
            handleCloseNotification()
        }
    }, [progress]);

    React.useEffect(() => {
        handleIncreaseProgress();
        setUseEffectStarted(true);
    }, []);

    return (
        <Alert key={props.id} variant={props.notificationType} dismissible className={"text-left"}
                   onClose={handleCloseNotification}
                   onMouseEnter={() => {if(useEffectStarted) handlePauseProgress()}}
                   onMouseLeave={() => {if(useEffectStarted) handleIncreaseProgress()}}>
            {props.title ? <Alert.Heading as={"h5"} className={"font-weight-bold"}>{props.title}</Alert.Heading> : ""}
            <div style={{whiteSpace: "pre-line"}}>{props.message}</div>
            <ProgressBar now={progress} className={"alert-progress-bar"}/>
        </Alert>
    );
};

export default Notification;