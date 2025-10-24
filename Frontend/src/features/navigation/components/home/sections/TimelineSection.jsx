import React from "react";
import { Container, Row, Col } from "react-bootstrap";

const TimelineSection = ({ items, timelineStyles }) => (
    <div className="bg-dark text-white py-5">
        <style>{timelineStyles}</style>
        <Container>
            <h3 className="text-center mb-5 fw-bold">¿CÓMO FUNCIONA COMPUSEARCH?</h3>

            <div className="d-none d-md-block timeline-container-h">
                <div className="timeline-line-h"></div>
                <Row>
                    {items.map((item, idx) => (
                        <Col md={3} key={idx} className="text-center">
                            <div className="timeline-node">
                                <img src={item.img} alt={item.title} />
                            </div>
                            <h6 className="fw-bold h5 text-white mt-3">{item.title}</h6>
                        </Col>
                    ))}
                </Row>
            </div>

            <div className="d-md-none timeline-container-v">
                <div className="timeline-line-v"></div>
                {items.map((item, idx) => (
                    <div key={idx} className="timeline-item-v">
                        <div className="timeline-node">
                            <img src={item.img} alt={item.title} />
                        </div>
                        <h6 className="fw-bold text-white">{item.title}</h6>
                    </div>
                ))}
            </div>
        </Container>
    </div>
);

export default TimelineSection;
