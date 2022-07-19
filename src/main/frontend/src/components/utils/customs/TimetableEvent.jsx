import React from "react";
import {Card, CardGroup} from "react-bootstrap";

export default function TimetableEvent({id}) {
    return (
        <Card>
            <Card.Body>
                <Card.Title>{id}</Card.Title>
                    <Card.Text>
                        This is a wider card with supporting text below as a natural lead-in
                        to additional content. This content is a little bit longer.
                    </Card.Text>
            </Card.Body>
            {/*<Card.Footer>*/}
            {/*    <small className="text-muted">Last updated 3 mins ago</small>*/}
            {/*</Card.Footer>*/}
        </Card>
    )
}