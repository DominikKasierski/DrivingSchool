import i18n from '../../../i18n';
import {useEffect, useState} from "react";
import {Card, Col, ListGroup, Row} from "react-bootstrap";
import {usePermanentChangeDialog} from "../../utils/dialogs/DialogProvider";
import {useHistory} from "react-router-dom";
import {useLocale} from "../../utils/login/LoginProvider";
import {useDangerNotification, useSuccessNotification} from "../../utils/notifications/NotificationProvider";
import {ResponseErrorsHandler} from "../../utils/handlers/ResponseErrorsHandler";
import axios from "axios";
import {rolesConstant} from "../../utils/constants/Constants";

function VehicleComponent({id, image, brand, model, productionYear}) {
    const [activeKey, setActiveKey] = useState("description")
    const {token, username, currentRole} = useLocale();
    const [etag, setEtag] = useState();

    const dispatchPermanentChangeDialog = usePermanentChangeDialog();
    const dispatchDangerNotification = useDangerNotification();
    const dispatchSuccessNotification = useSuccessNotification();
    const history = useHistory();

    useEffect(() => {
        if (currentRole === 'ADMIN') {
            getCarData();
        }
    }, [token]);

    const handleDelete = () => {
        dispatchPermanentChangeDialog({
            confirmCallback: () => {
                deleteVehicle()
            },
        })
    }

    const deleteVehicle = () => {
        axios.put(`/resources/car/removeCar/` + id, {}, {
            headers: {
                "Content-Type": "application/json",
                "If-Match": etag,
                "Authorization": token
            }
        }).then((res) => {
            dispatchSuccessNotification({message: i18n.t('vehicles.delete.success')});
            history.push("/userPage")
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification);
        });
    }

    function getCarData() {
        axios.get(`/resources/car/getCar/` + id, {
            headers: {
                "Authorization": token,
            }
        }).then((res) => {
            setEtag(res.headers.etag);
        }).catch((e) => ResponseErrorsHandler(e, dispatchDangerNotification));
    }

    return (
        <Row className={"mb-2 p-2"}>
            <Card style={{
                border: "none",
                background: "rgb(211, 211, 211)"
            }}>
                <div className={"card-img-top d-flex align-items-center"}>
                    <img alt="vehicle" className="img-fluid col-md-6 col-sm-12 col-xs-12" src={image}/>

                    <div className={"col-md-6 my-auto col-sm-12 col-xs-12"}>
                        <ListGroup variant={"flush"}>
                            <ListGroup.Item className={"d-flex align-items-center p-2"}>
                                <span className={"font-weight-bold"}>{i18n.t('vehicles.brand')}</span>
                                <span className={"ml-3"}>{brand}</span>
                            </ListGroup.Item>
                            <ListGroup.Item className={"d-flex align-items-center p-2"}>
                                <span className={"font-weight-bold"}>{i18n.t('vehicles.model')}</span>
                                <span className={"ml-3"}>{model}</span>
                            </ListGroup.Item>
                            <ListGroup.Item className={"d-flex align-items-center p-2"}>
                                <span className={"font-weight-bold"}>{i18n.t('vehicles.production.year')}</span>
                                <span className={"ml-3"}>{productionYear}</span>
                            </ListGroup.Item>
                            {currentRole === rolesConstant.admin && (
                                <>
                                    <ListGroup.Item className={"p-1"}>
                                        <button className="p-1 btn btn-block btn-dark dim"
                                                onClick={() => history.push("/editVehicle?id=" + id)}>
                                            {i18n.t("edit")}
                                        </button>
                                    </ListGroup.Item>
                                    <ListGroup.Item className={"p-1"}>
                                        <button className="p-1 btn-block btn btn-dark dim" onClick={handleDelete}>
                                            {i18n.t("delete")}
                                        </button>
                                    </ListGroup.Item>
                                </>
                            )}
                        </ListGroup>
                    </div>
                </div>
            </Card>
        </Row>
    );
}

export default VehicleComponent;