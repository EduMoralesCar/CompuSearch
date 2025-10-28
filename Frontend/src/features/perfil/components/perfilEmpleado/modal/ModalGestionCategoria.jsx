import { useState, useEffect } from "react";
import { Modal, Button, Form, Alert } from "react-bootstrap";

const ModalGestionCategoria = ({ show, handleClose, categoria, onSubmit }) => {
    const [nombre, setNombre] = useState("");
    const [descripcion, setDescripcion] = useState("");
    const [nombreImagen, setNombreImagen] = useState("");
    const [errors, setErrors] = useState({});

    useEffect(() => {
        if (categoria) {
            setNombre(categoria.nombre || "");
            setDescripcion(categoria.descripcion || "");
            setNombreImagen(categoria.nombreImagen || "");
        } else {
            setNombre("");
            setDescripcion("");
            setNombreImagen("");
        }
        setErrors({});
    }, [categoria, show]);

    const validate = () => {
        const newErrors = {};

        if (!nombre.trim()) newErrors.nombre = "El nombre es obligatorio";
        if (!descripcion.trim()) newErrors.descripcion = "La descripción es obligatoria";
        if (!nombreImagen.trim()) newErrors.nombreImagen = "La ruta de la imagen es obligatoria";

        if (categoria) {
            if (
                nombre === (categoria.nombre || "") &&
                descripcion === (categoria.descripcion || "") &&
                nombreImagen === (categoria.nombreImagen || "")
            ) {
                newErrors.general = "No se realizaron cambios en los datos de la categoría";
            }
        }

        return newErrors;
    };


    const handleSubmit = async (e) => {
        e.preventDefault();
        const validationErrors = validate();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
            return;
        }

        try {
            await onSubmit({ nombre, descripcion, nombreImagen });
        } catch (err) {
            setErrors({ general: err || "Error al guardar la categoría" });
        }
    };

    return (
        <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>{categoria ? "Editar Categoría" : "Crear Nueva Categoría"}</Modal.Title>
            </Modal.Header>
            <Form onSubmit={handleSubmit}>
                <Modal.Body>
                    {errors.general && <Alert variant="danger">{errors.general}</Alert>}

                    <Form.Group className="mb-3" controlId="formNombre">
                        <Form.Label>Nombre</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Ingrese el nombre"
                            value={nombre}
                            onChange={(e) => setNombre(e.target.value)}
                            isInvalid={!!errors.nombre}
                        />
                        <Form.Control.Feedback type="invalid">{errors.nombre}</Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formDescripcion">
                        <Form.Label>Descripción</Form.Label>
                        <Form.Control
                            as="textarea"
                            rows={3}
                            placeholder="Ingrese la descripción"
                            value={descripcion}
                            onChange={(e) => setDescripcion(e.target.value)}
                            isInvalid={!!errors.descripcion}
                        />
                        <Form.Control.Feedback type="invalid">{errors.descripcion}</Form.Control.Feedback>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formImagen">
                        <Form.Label>Ruta de Imagen</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Ingrese la ruta de la imagen"
                            value={nombreImagen}
                            onChange={(e) => setNombreImagen(e.target.value)}
                            isInvalid={!!errors.nombreImagen}
                        />
                        <Form.Control.Feedback type="invalid">{errors.nombreImagen}</Form.Control.Feedback>
                    </Form.Group>
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Cancelar
                    </Button>
                    <Button type="submit" variant="primary">
                        {categoria ? "Actualizar" : "Crear"}
                    </Button>
                </Modal.Footer>
            </Form>
        </Modal>
    );
};

export default ModalGestionCategoria;
