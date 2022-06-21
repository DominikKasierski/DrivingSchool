import React from "react";
import {Row} from "react-bootstrap";

function Breadcrumb(props) {
    return (
        <Row>
            <nav aria-label="breadcrumb" className={"w-100"}>
                <ol className="breadcrumb" style={{backgroundColor: "rgb(211, 211, 211)", borderBottom: "3px white solid"}}>
                    {props.children}
                </ol>
            </nav>
        </Row>
    )
}

export default Breadcrumb;