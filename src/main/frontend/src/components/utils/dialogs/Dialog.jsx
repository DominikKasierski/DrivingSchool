import React, {useState} from 'react';
import {Button, Modal} from "react-bootstrap";
import i18n from '../../../i18n';

function Dialog(props) {
    const [visibility, setVisibility] = useState(true);
    const {noCancel} = props

    const handleConfirm = () => {
        handleClose();
        props.confirmCallback();
    }

    const handleCancel = () => {
        handleClose();
        props.cancelCallback();
    }

    const handleClose = () => {
        setVisibility(false);
        setTimeout(() => {
            props.dispatch({
                type: "REMOVE_DIALOG", id: props.id
            })
        }, 100);
    };

    return (
        <Modal show={visibility} onHide={handleCancel} className={"text-dark"}>
            <Modal.Header closeButton>
                <Modal.Title>{props.title}</Modal.Title>
            </Modal.Header>
            <Modal.Body style={{whiteSpace: "pre-line"}}>{props.message}</Modal.Body>
            <Modal.Footer>
                {noCancel !== true &&
                    <Button variant="secondary" onClick={handleConfirm}>
                        {i18n.t("dialog.button.cancel")}
                    </Button>}
                <Button variant="purple" onClick={handleConfirm}>
                    {i18n.t(props.textButtonSave ?? "dialog.button.confirm")}
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default Dialog;