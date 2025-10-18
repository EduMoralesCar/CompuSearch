import React from "react";
import PropTypes from "prop-types";
import { Container, Row, Col } from "react-bootstrap";

const BannerHeader = ({ title, description, background }) => (
    <header
        className="text-white text-center py-5"
        style={{
            backgroundImage: `url(${background})`,
            backgroundSize: "cover",
            backgroundPosition: "center",
        }}
    >
        <Container>
            <Row className="justify-content-center">
                <Col md={10}>
                    <h1
                        className="display-6 fw-bold text-uppercase"
                        style={{ textShadow: "1px 1px 4px rgba(0,0,0,0.7)" }}
                    >
                        {title}
                    </h1>
                    <p
                        className="lead mb-0 fw-italic"
                        style={{ textShadow: "1px 1px 3px rgba(0,0,0,0.6)" }}
                    >
                        {description}
                    </p>
                </Col>
            </Row>
        </Container>
    </header>
);

BannerHeader.propTypes = {
    title: PropTypes.string.isRequired,
    description: PropTypes.string.isRequired,
    background: PropTypes.string.isRequired,
};

export default BannerHeader;
