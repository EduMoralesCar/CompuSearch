import React from "react";
import { Modal, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const AuthModal = ({
  show,
  onClose,
  title = "Acción no permitida",
  message = "Debes iniciar sesión o registrarte para continuar.",
  primaryAction = { label: "Iniciar sesión", path: "/login" },
  secondaryAction = { label: "Cancelar", onClick: null },
}) => {
  const navigate = useNavigate();

  const handlePrimary = () => {
    navigate(primaryAction.path);
    if (onClose) onClose();
  };

  const handleSecondary = () => {
    if (secondaryAction.onClick) secondaryAction.onClick();
    else if (onClose) onClose();
  };

  return (
    <Modal show={show} onHide={onClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>{title}</Modal.Title>
      </Modal.Header>
      <Modal.Body>{message}</Modal.Body>
      <Modal.Footer>
        <Button variant="primary" onClick={handlePrimary}>
          {primaryAction.label}
        </Button>
        <Button variant="secondary" onClick={handleSecondary}>
          {secondaryAction.label}
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default AuthModal;
