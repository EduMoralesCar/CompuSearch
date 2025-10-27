import { Modal, Button } from "react-bootstrap";

const ModalConfirmacion = ({
    show,
    onHide,
    titulo = "Confirmar acción",
    mensaje = "¿Estás seguro de realizar esta acción?",
    onConfirmar,
    textoConfirmar = "Confirmar",
    textoCancelar = "Cancelar",
    variantConfirmar = "danger",
}) => {
    return (
        <Modal show={show} onHide={onHide} centered>
            <Modal.Header closeButton>
                <Modal.Title>{titulo}</Modal.Title>
            </Modal.Header>
            <Modal.Body>{mensaje}</Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>
                    {textoCancelar}
                </Button>
                <Button variant={variantConfirmar} onClick={onConfirmar}>
                    {textoConfirmar}
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ModalConfirmacion;
