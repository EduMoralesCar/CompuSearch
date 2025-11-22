import React from "react";
import { Row, Col } from "react-bootstrap";

const StatBox = ({ value, label }) => (
    <div className="text-center p-2 border rounded">
        <h4 className="mb-0 fw-bold">{value}</h4>
        <small className="text-muted">{label}</small>
    </div>
);

const PerfilStats = ({ builds = 0, incidentes = 0, solicitudes = 0 }) => {
    return (
        <Row className="g-2">
            <Col xs={4}>
                <StatBox value={builds} label="Builds." />
            </Col>
            <Col xs={4}>
                <StatBox value={incidentes} label="Report." />
            </Col>
            <Col xs={4}>
                <StatBox value={solicitudes} label="Solic." />
            </Col>
        </Row>
    );
};

export default PerfilStats;
