import i18n from '../../../i18n';
import {useEffect, useState} from "react";
import {ListGroup, Row, Tab, Tabs} from "react-bootstrap";
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
            axios.get(`/resources/car/getCar/` + id, {
                headers: {
                    "Authorization": token,
                }
            }).then((res) => {
                setEtag(res.headers.etag);
            }).catch((e) => ResponseErrorsHandler(e, dispatchDangerNotification));
        }
    }, [token]);

    const handleTabSelect = (key) => {
        if (key === "delete") {
            handleDeleteCar();
        } else if (key === "edit") {
            history.push("/editCar/" + id)
        } else {
            setActiveKey(key)
        }
    }

    const handleDeleteCar = () => {
        dispatchPermanentChangeDialog({
            confirmCallback: () => {
                deleteVehicle()
            },
        })
    }

    const deleteVehicle = () => {
        axios.put(`/resources/car/removeCar/` + id, {
            headers: {
                "Content-Type": "application/json",
                "If-Match": etag,
                "Authorization": token
            }
        }).then((res) => {
            dispatchSuccessNotification({message: i18n.t('vehicles.delete.success')});
            history.push("/vehicles");
        }).catch(err => {
            ResponseErrorsHandler(err, dispatchDangerNotification);
        });
    }

    return (
        <Row>
            <div className={"col-md-6 col-sm-12 col-xs-12"}>
                <img alt="vehicle" className="img-fluid" src={image}/>
            </div>

            <div className={"col-md-6 my-auto col-sm-12 col-xs-12"}>
                <ListGroup variant={"flush"}>
                    <ListGroup.Item className={"d-flex align-items-center"}>
                        <span className={"ml-3"}>{i18n.t('vehicles.brand')}</span>
                        <span className={"ml-3"}>{brand}</span>
                    </ListGroup.Item>
                    <ListGroup.Item className={"d-flex align-items-center"}>
                        <span className={"ml-3"}>{i18n.t('vehicles.model')}</span>
                        <span className={"ml-3"}>{model}</span>
                    </ListGroup.Item>
                    <ListGroup.Item className={"d-flex align-items-center"}>
                        <span className={"ml-3"}>{i18n.t('vehicles.production.year')}</span>
                        <span className={"ml-3"}>{productionYear}</span>
                    </ListGroup.Item>
                </ListGroup>
                {/*{currentRole === rolesConstant.admin && (*/}
                {/*    <>*/}
                {/*        <Tab eventKey="edit" tabClassName={"ml-auto"} title={i18n.t("edit")}/>*/}
                {/*        <Tab eventKey="delete" tabClassName={"ml-0"} title={i18n.t("delete")}/>*/}
                {/*    </>*/}
                {/*)}*/}
            </div>
        </Row>
    );
}

export default VehicleComponent;