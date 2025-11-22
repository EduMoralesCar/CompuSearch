import { Modal, Button } from "react-bootstrap"

const ModalConfirmacion = ({ show, onHide, titulo, mensaje, onConfirmar, textoConfirmar, variantConfirmar = "primary" }) => {
    return (
        <Modal show={show} onHide={onHide} centered>
            <Modal.Header closeButton>
                <Modal.Title>{titulo}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p>{mensaje}</p>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>
                    Cancelar
                </Button>
                <Button variant={variantConfirmar} onClick={onConfirmar}>
                    {textoConfirmar}
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ModalConfirmacion;