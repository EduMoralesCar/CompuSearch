import React from "react";
import { Modal, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const AuthModalBuild = ({ show, onClose }) => {
  const navigate = useNavigate();

  return (
    <Modal show={show} onHide={onClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Acción no permitida</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        Debes iniciar sesión o registrarte para guardar o descargar tu armado.
      </Modal.Body>
      <Modal.Footer>
        <Button variant="primary" onClick={() => navigate("/login")}>
          Iniciar sesión
        </Button>
        <Button variant="secondary" onClick={onClose}>
          Cancelar
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default AuthModalBuild;
