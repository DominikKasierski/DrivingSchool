import React from "react";
import {Row} from "react-bootstrap";

function Breadcrumb(props) {
    return (
        <Row>
            <nav aria-label="breadcrumb" className={"w-100"}>
                <ol className="breadcrumb" style={{}}>
                    {props.children}
                </ol>
            </nav>
        </Row>
    )
}

export default Breadcrumb;