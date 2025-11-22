import React, { useState, useEffect, useRef } from "react";
import { Container, Row, Col } from "react-bootstrap";

const TimelineSection = ({ items, timelineStyles }) => {
    const [isVisible, setIsVisible] = useState(false);
    const sectionRef = useRef(null);

    useEffect(() => {
        const observer = new IntersectionObserver(
            ([entry]) => {
                if (entry.isIntersecting) {
                    setIsVisible(true);
                }
            },
            { threshold: 0.2 }
        );

        if (sectionRef.current) {
            observer.observe(sectionRef.current);
        }

        return () => {
            if (sectionRef.current) {
                // eslint-disable-next-line react-hooks/exhaustive-deps
                observer.unobserve(sectionRef.current);
            }
        };
    }, []);

    const animationStyles = `
        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        @keyframes fadeInScale {
            from {
                opacity: 0;
                transform: scale(0.8);
            }
            to {
                opacity: 1;
                transform: scale(1);
            }
        }

        @keyframes slideInLeft {
            from {
                opacity: 0;
                transform: translateX(-50px);
            }
            to {
                opacity: 1;
                transform: translateX(0);
            }
        }

        @keyframes slideInRight {
            from {
                opacity: 0;
                transform: translateX(50px);
            }
            to {
                opacity: 1;
                transform: translateX(0);
            }
        }

        .timeline-title {
            opacity: 0;
            animation: ${isVisible ? 'fadeInUp 0.8s ease-out forwards' : 'none'};
        }

        .timeline-item-animated {
            opacity: 0;
        }

        .timeline-item-animated.visible {
            animation: fadeInScale 0.6s ease-out forwards;
        }

        .timeline-item-animated:nth-child(1).visible {
            animation-delay: 0.2s;
        }

        .timeline-item-animated:nth-child(2).visible {
            animation-delay: 0.4s;
        }

        .timeline-item-animated:nth-child(3).visible {
            animation-delay: 0.6s;
        }

        .timeline-item-animated:nth-child(4).visible {
            animation-delay: 0.8s;
        }

        .timeline-node {
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .timeline-node:hover {
            transform: scale(1.1) rotate(5deg);
            box-shadow: 0 8px 20px rgba(13, 110, 253, 0.4);
        }

        .timeline-line-h {
            animation: ${isVisible ? 'slideInLeft 1s ease-out forwards' : 'none'};
        }

        .timeline-line-v {
            animation: ${isVisible ? 'slideInRight 1s ease-out forwards' : 'none'};
        }
    `;

    return (
        <div className="bg-dark text-white py-5" ref={sectionRef}>
            <style>{timelineStyles}</style>
            <style>{animationStyles}</style>
            <Container>
                <h3 className="text-center mb-5 fw-bold timeline-title">
                    ¿CÓMO FUNCIONA COMPUSEARCH?
                </h3>

                <div className="d-none d-md-block timeline-container-h">
                    <Row>
                        {items.map((item, idx) => (
                            <Col
                                md={3}
                                key={idx}
                                className={`text-center timeline-item-animated ${isVisible ? 'visible' : ''}`}
                            >
                                <div className="timeline-node">
                                    <img src={item.img} alt={item.title} />
                                </div>
                                <h6 className="fw-bold h5 text-white mt-3">{item.title}</h6>
                            </Col>
                        ))}
                    </Row>
                </div>

                <div className="d-md-none timeline-container-v">
                    {items.map((item, idx) => (
                        <div
                            key={idx}
                            className={`timeline-item-v timeline-item-animated ${isVisible ? 'visible' : ''}`}
                        >
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
};

export default TimelineSection;
