import React from "react";
import { Row, Col } from "react-bootstrap";

const StatBox = ({ value, label }) => (
    <div className="text-center p-2 border rounded">
        <h4 className="mb-0 fw-bold">{value}</h4>
        <small className="text-muted">{label}</small>
    </div>
);

const PerfilStats = ({ stats }) => (
    <Row className="g-2">
        <Col xs={6}><StatBox value={stats.construcciones} label="Construc." /></Col>
        <Col xs={6}><StatBox value={stats.alertas} label="Alertas" /></Col>
    </Row>
);

export default PerfilStats;