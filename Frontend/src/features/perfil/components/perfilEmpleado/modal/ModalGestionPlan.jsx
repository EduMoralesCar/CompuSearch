import { useState, useEffect } from "react";
import { Modal, Form, Button, Stack, InputGroup } from "react-bootstrap";

const ModalGestionPlan = ({ show, handleClose, plan, onSubmit }) => {
    const isEditing = !!plan;

    const [formData, setFormData] = useState({
        nombre: '',
        duracionMeses: 1,
        precioMensual: 0.01,
        descripcion: '',
        beneficios: ''
    });

    useEffect(() => {
        if (isEditing && plan) {
            setFormData({
                nombre: plan.nombre || '',
                duracionMeses: plan.duracionMeses || 1,
                precioMensual: plan.precioMensual ? parseFloat(plan.precioMensual) : 0.01,
                descripcion: plan.descripcion || '',
                beneficios: plan.beneficios || ''
            });
        } else {
            setFormData({
                nombre: '',
                duracionMeses: 1,
                precioMensual: 0.01,
                descripcion: '',
                beneficios: ''
            });
        }
    }, [plan, isEditing]);

    const handleChange = (e) => {
        const { name, value, type } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'number' ? parseFloat(value) : value,
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (formData.duracionMeses < 1 || formData.precioMensual <= 0) {
            console.error('Asegúrate de que la duración sea de al menos 1 mes y el precio sea positivo.');
            return;
        }

        onSubmit(formData);
    };

    return (
        <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>{isEditing ? `Editar Plan: ${plan.nombre}` : "Crear Nuevo Plan"}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form onSubmit={handleSubmit}>
                    <Form.Group className="mb-3" controlId="formNombre">
                        <Form.Label>Nombre del Plan</Form.Label>
                        <Form.Control
                            type="text"
                            name="nombre"
                            value={formData.nombre}
                            onChange={handleChange}
                            required
                            maxLength={100}
                        />
                    </Form.Group>

                    <Stack direction="horizontal" gap={3}>
                        <Form.Group className="mb-3 w-50" controlId="formDuracionMeses">
                            <Form.Label>Duración (Meses)</Form.Label>
                            <Form.Control
                                type="number"
                                name="duracionMeses"
                                value={formData.duracionMeses}
                                onChange={handleChange}
                                min="1"
                                required
                            />
                        </Form.Group>

                        <Form.Group className="mb-3 w-50" controlId="formPrecioMensual">
                            <Form.Label>Precio Mensual</Form.Label>
                            <InputGroup>
                                <InputGroup.Text>S/.</InputGroup.Text>
                                <Form.Control
                                    type="number"
                                    name="precioMensual"
                                    value={formData.precioMensual}
                                    onChange={handleChange}
                                    min="0.01"
                                    step="0.01"
                                    required
                                />
                            </InputGroup>
                        </Form.Group>
                    </Stack>

                    <Form.Group className="mb-3" controlId="formDescripcion">
                        <Form.Label>Descripción</Form.Label>
                        <Form.Control
                            as="textarea"
                            rows={2}
                            name="descripcion"
                            value={formData.descripcion}
                            onChange={handleChange}
                            required
                            maxLength={500}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formBeneficios">
                        <Form.Label>Beneficios (Opcional)</Form.Label>
                        <Form.Control
                            as="textarea"
                            rows={2}
                            name="beneficios"
                            value={formData.beneficios}
                            onChange={handleChange}
                            maxLength={500}
                            placeholder="Ingrese cada beneficio en una nueva línea o separado por comas."
                        />
                    </Form.Group>
                    <div className="d-flex justify-content-end">
                        <Button variant="primary" type="submit" className="mt-3">
                            {isEditing ? "Guardar Cambios" : "Crear Plan"}
                        </Button>
                    </div>
                </Form>
            </Modal.Body>
        </Modal>
    );
};

export default ModalGestionPlan;