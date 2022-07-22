import React from "react";
import {Card} from "react-bootstrap";
import moment from "moment";
import i18n from "i18next";

export default function TimetableEvent({title, startTime, endTime, instructor}) {
    let style = "";

    if (title === "LECTURE") {
        style = {borderColor: "rgb(24, 26, 27)", border: "3px solid"};
    } else {
        style = {backgroundColor: "rgb(211, 211, 211)", border: "3px black solid"};
    }

    const momentHelper = () => {
        return i18n.language === "pl" ? 'pl' : 'en';
    }

    return (
        <Card className="text-center mb-2" style={style}>
            <Card.Body className="p-2">
                <Card.Title>{i18n.t(title)}</Card.Title>
                <Card.Text>
                    <small className="text-muted d-block">{i18n.t("add.lecture.begin.date")}</small>
                    <span>{startTime ? moment(startTime).locale(momentHelper()).local().format('LLL').toString() : "-"}</span>
                    <small className="text-muted d-block mt-2">{i18n.t("add.lecture.end.date")}</small>
                    <span>{endTime ? moment(endTime).locale(momentHelper()).local().format('LLL').toString() : "-"}</span>
                    <small className="text-muted d-block mt-2">{i18n.t("add.lecture.instructor")}</small>
                    <span>{instructor}</span>
                </Card.Text>
            </Card.Body>
        </Card>
    )
}