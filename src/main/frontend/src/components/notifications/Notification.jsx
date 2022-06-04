import React, {useState} from 'react';
import {Alert, ProgressBar} from "react-bootstrap";

export const notificationType = {
    SUCCESS: "success",
    WARNING: "warning",
    INFO: "info",
}

export const notificationDuration = {
    INFINITY: -1,
    MINUTE: 60_000,
    STANDARD: 7_000,
}

const Notification = (props) => {
    const [progress, setProgress] = useState(0);
    const [intervalId, setIntervalId] = useState(null);
    const [useEffectStart, setUseEffectStart] = useState(false);

    const handleIncreaseProgress = () => {
        if (props.notificationDuration > 0) {
            const id = setInterval(() => {
                setProgress(prev => {
                    if (prev === 100) {
                        clearInterval(id);
                        return prev;
                    }
                    return prev + 1;
                });
            }, (props.notificationDuration + 500) / 100);
            setIntervalId(id);
        }
    };

    const handlePauseProgress = () => {
        clearInterval(intervalId);
    };


    React.useEffect(() => {
    }, []);
}