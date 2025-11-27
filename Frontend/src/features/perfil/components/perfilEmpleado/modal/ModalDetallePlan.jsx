import { Modal, Button, Card, ListGroup } from "react-bootstrap";

const InfoGeneralCard = ({ plan }) => (
    <Card className="shadow-sm border-primary mb-3">
        <Card.Body>
            <Card.Title className="text-primary">Información General</Card.Title>
            <hr />
            <ListGroup variant="flush">
                <ListGroup.Item><strong>ID Plan:</strong> {plan.idPlan}</ListGroup.Item>
                <ListGroup.Item><strong>Duración:</strong> {plan.duracionMeses} Meses</ListGroup.Item>
                <ListGroup.Item>
                    <strong>Precio Mensual:</strong>
                    <span className="fw-bold text-success ms-2">S/.{parseFloat(plan.precioMensual).toFixed(2)}</span>
                </ListGroup.Item>
                <ListGroup.Item>
                    <strong>Estado:</strong>
                    <span className={`badge ms-2 ${plan.activo ? 'bg-success' : 'bg-secondary'}`}>
                        {plan.activo ? 'Activo' : 'Inactivo'}
                    </span>
                </ListGroup.Item>
            </ListGroup>
        </Card.Body>
    </Card>
);

const MetricCard = ({ suscripciones }) => (
    <Card className="shadow-sm h-100 border-info mb-3">
        <Card.Body className="d-flex justify-content-center align-items-center">
            <div className="text-center">
                <h1 className="display-4 text-info">{suscripciones || 0}</h1>
                <p className="lead">Suscripciones Activas</p>
            </div>
        </Card.Body>
    </Card>
);

const DescripcionCard = ({ descripcion }) => (
    <Card className="shadow-sm mt-3">
        <Card.Body>
            <Card.Title className="text-primary">Descripción</Card.Title>
            <hr />
            <p>{descripcion}</p>
        </Card.Body>
    </Card>
);

const BeneficiosCard = ({ beneficios }) => {
    if (!beneficios) return <p className="text-muted fst-italic">No hay beneficios detallados.</p>;

    const items = beneficios.split('\n').filter(item => item.trim() !== '');
    return items.length > 0 ? (
        <ListGroup variant="flush">
            {items.map((item, index) => (
                <ListGroup.Item key={index} className="py-1">{item.trim()}</ListGroup.Item>
            ))}
        </ListGroup>
    ) : <p>{beneficios}</p>;
};

const ModalDetallePlan = ({ show, handleClose, plan }) => {
    if (!plan) return null;

    return (
        <Modal show={show} onHide={handleClose} centered size="lg">
            <Modal.Header closeButton className="bg-primary text-white">
                <Modal.Title>Detalle del Plan: {plan.nombre}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <div className="row">
                    <div className="col-md-6">
                        <InfoGeneralCard plan={plan} />
                    </div>
                    <div className="col-md-6">
                        <MetricCard suscripciones={plan.suscripciones} />
                    </div>
                </div>

                <DescripcionCard descripcion={plan.descripcion} />
                <Card className="shadow-sm mt-3">
                    <Card.Body>
                        <Card.Title className="text-primary">Beneficios</Card.Title>
                        <hr />
                        <BeneficiosCard beneficios={plan.beneficios} />
                    </Card.Body>
                </Card>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>Cerrar</Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ModalDetallePlan;
