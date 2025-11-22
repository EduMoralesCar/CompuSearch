import { Modal, Button, Card, ListGroup } from "react-bootstrap"

const ModalDetallePlan = ({ show, handleClose, plan }) => {
    if (!plan) return null;

    const renderBeneficios = (beneficiosString) => {
        if (!beneficiosString) return <p className="text-muted fst-italic">No hay beneficios detallados.</p>;

        const items = beneficiosString.split('\n').filter(item => item.trim() !== '');

        if (items.length > 0) {
            return (
                <ListGroup variant="flush">
                    {items.map((item, index) => (
                        <ListGroup.Item key={index} className="py-1">
                            {item.trim()}
                        </ListGroup.Item>
                    ))}
                </ListGroup>
            );
        }
        return <p>{beneficiosString}</p>;
    };

    return (
        <Modal show={show} onHide={handleClose} centered size="lg">
            <Modal.Header closeButton className="bg-primary text-white">
                <Modal.Title>Detalle del Plan: {plan.nombre}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <div className="row">
                    <div className="col-md-6 mb-3">
                        <Card className="shadow-sm border-primary">
                            <Card.Body>
                                <Card.Title className="text-primary">Información General</Card.Title>
                                <hr />
                                <ListGroup variant="flush">
                                    <ListGroup.Item>
                                        <strong>ID Plan:</strong> {plan.idPlan}
                                    </ListGroup.Item>
                                    <ListGroup.Item>
                                        <strong>Duración:</strong> {plan.duracionMeses} Meses
                                    </ListGroup.Item>
                                    <ListGroup.Item>
                                        <strong>Precio Mensual:</strong>
                                        <span className="fw-bold text-success ms-2">
                                            S/.{parseFloat(plan.precioMensual).toFixed(2)}
                                        </span>
                                    </ListGroup.Item>
                                    <ListGroup.Item>
                                        <strong>Estado:</strong>
                                        <span className={`badge ${plan.activo ? 'bg-success' : 'bg-secondary'} ms-2`}>
                                            {plan.activo ? 'Activo' : 'Inactivo'}
                                        </span>
                                    </ListGroup.Item>
                                </ListGroup>
                            </Card.Body>
                        </Card>
                    </div>
                    <div className="col-md-6 mb-3">
                        <Card className="shadow-sm h-100 border-info">
                            <Card.Body>
                                <Card.Title className="text-info">Métricas</Card.Title>
                                <hr />
                                <div className="d-flex justify-content-center align-items-center h-75">
                                    <div className="text-center">
                                        <h1 className="display-4 text-info">{plan.suscripciones || 0}</h1>
                                        <p className="lead">Suscripciones Activas</p>
                                    </div>
                                </div>
                            </Card.Body>
                        </Card>
                    </div>
                </div>

                <Card className="mt-3 shadow-sm">
                    <Card.Body>
                        <Card.Title className="text-primary">Descripción</Card.Title>
                        <hr />
                        <p>{plan.descripcion}</p>
                    </Card.Body>
                </Card>

                <Card className="mt-3 shadow-sm">
                    <Card.Body>
                        <Card.Title className="text-primary">Beneficios</Card.Title>
                        <hr />
                        {renderBeneficios(plan.beneficios)}
                    </Card.Body>
                </Card>

            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Cerrar
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ModalDetallePlan;