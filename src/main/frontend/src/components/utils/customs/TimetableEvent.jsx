import React from "react";
import {Card} from "react-bootstrap";
import moment from "moment";
import i18n from "i18next";

export default function TimetableEvent({title, startTime, endTime, participant, labelsType = 1}) {
    let style = "";

    if (title === "LECTURE") {
        style = {backgroundColor: "rgb(135,206,235)", border: "3px black solid"};
    } else {
        style = {backgroundColor: "rgb(144,238,144)", border: "3px black solid"};
    }

    const momentHelper = () => {
        return i18n.language === "pl" ? 'pl' : 'en';
    }

    return (
        <Card className="text-center mt-2 mb-2" style={style}>
            <Card.Body className="p-2">
                <Card.Title>{i18n.t(title)}</Card.Title>
                <Card.Text>
                    {labelsType === 1 &&
                        <small className="text-muted d-block font-italic">{i18n.t("add.lecture.begin.date")}</small>
                    }
                    {labelsType === 2 &&
                        <small className="text-muted d-block font-italic">{i18n.t("add.lecture.begin.date")}</small>
                    }
                    {labelsType === 3 &&
                        <small className="text-muted d-block font-italic">{i18n.t("add.lecture.begin.date")}</small>
                    }

                    <span>{startTime ? moment(startTime).locale(momentHelper()).local().format('LLL').toString() : "-"}</span>

                    {labelsType === 1 &&
                        <small className="text-muted d-block mt-2 font-italic">{i18n.t("add.lecture.end.date")}</small>
                    }
                    {labelsType === 2 &&
                        <small className="text-muted d-block mt-2 font-italic">{i18n.t("add.lecture.end.date")}</small>
                    }
                    {labelsType === 3 &&
                        <small className="text-muted d-block mt-2 font-italic">{i18n.t("add.lecture.end.date")}</small>
                    }

                    <span>{endTime ? moment(endTime).locale(momentHelper()).local().format('LLL').toString() : "-"}</span>

                    {labelsType === 1 &&
                        <small className="text-muted d-block mt-2 font-italic">{i18n.t("add.lecture.instructor")}</small>
                    }
                    {labelsType === 2 &&
                        <small className="text-muted d-block mt-2 font-italic">{i18n.t("add.lecture.instructor")}</small>
                    }
                    {labelsType === 3 &&
                        <small className="text-muted d-block mt-2 font-italic">{i18n.t("add.lecture.instructor")}</small>
                    }

                    <span>{participant}</span>
                </Card.Text>
            </Card.Body>
        </Card>
    )
}