import { useEffect, useState } from "react";
import { Modal, Button, Form, Spinner, Alert } from "react-bootstrap";

const ModalFormularioEmpleado = ({
    show,
    handleClose,
    empleadoAEditar,
    onCreateOrUpdate,
    isSubmitting,
    submitError
}) => {
    const [formData, setFormData] = useState({
        username: '',
        nombre: '',
        apellido: '',
        email: '',
        rol: 'ADMIN',
        password: '',
    });
    // eslint-disable-next-line no-unused-vars
    const [passwordRequired, setPasswordRequired] = useState(false);

    useEffect(() => {
        if (empleadoAEditar) {
            setFormData({
                username: empleadoAEditar.username || '',
                nombre: empleadoAEditar.nombre || '',
                apellido: empleadoAEditar.apellido || '',
                email: empleadoAEditar.email || '',
                rol: empleadoAEditar.rol || 'ADMIN',
                password: '',
            });
            setPasswordRequired(false);
        } else {
            setFormData({
                username: '',
                nombre: '',
                apellido: '',
                email: '',
                rol: 'ADMIN',
                password: '',
            });
            setPasswordRequired(true);
        }
    }, [empleadoAEditar, show]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!formData.username || !formData.nombre || !formData.apellido || !formData.email || !formData.rol) {
            alert('Por favor, complete todos los campos obligatorios.');
            return;
        }

        const dataToSend = {
            ...formData,
        };

        if (!empleadoAEditar && formData.password.trim() === '') {
            alert('La contraseña es obligatoria para un nuevo empleado.');
            return;
        }

        if (empleadoAEditar && formData.password.trim() === '') {
            delete dataToSend.password;
        }

        onCreateOrUpdate(dataToSend);
    };

    const title = empleadoAEditar ?
        `Editar Empleado: ${empleadoAEditar.username}` :
        'Crear Nuevo Empleado';

    const handleCloseInternal = () => {
        if (!isSubmitting) {
            handleClose();
        }
    };


    return (
        <Modal show={show} onHide={handleCloseInternal} centered>
            <Modal.Header closeButton>
                <Modal.Title>{title}</Modal.Title>
            </Modal.Header>
            <Form onSubmit={handleSubmit}>
                <Modal.Body>
                    {submitError && <Alert variant="danger">{submitError}</Alert>}

                    <Form.Group className="mb-3" controlId="formUsername">
                        <Form.Label>Username (*)</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Ingrese nombre de usuario"
                            name="username"
                            value={formData.username}
                            onChange={handleChange}
                            required
                            disabled={isSubmitting}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formNombre">
                        <Form.Label>Nombre (*)</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Ingrese su nombre"
                            name="nombre"
                            value={formData.nombre}
                            onChange={handleChange}
                            required
                            disabled={isSubmitting}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formApellido">
                        <Form.Label>Apellido (*)</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Ingrese su apellido"
                            name="apellido"
                            value={formData.apellido}
                            onChange={handleChange}
                            required
                            disabled={isSubmitting}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3 col-md-6" controlId="formEmail">
                        <Form.Label>Email (*)</Form.Label>
                        <Form.Control
                            type="email"
                            placeholder="name@example.com"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            required
                            disabled={isSubmitting}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formRol">
                        <Form.Label>Rol (*)</Form.Label>
                        <Form.Select
                            name="rol"
                            value={formData.rol}
                            onChange={handleChange}
                            disabled={isSubmitting}
                        >
                            <option value="ADMIN">ADMIN</option>
                            <option value="MONITOREO">MONITORIO</option>
                            <option value="SOPORTE">SOPORTE</option>
                        </Form.Select>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="formPassword">
                        <Form.Label>
                            Contraseña {empleadoAEditar ? '(Dejar vacío para no cambiar)' : '(*)'}
                        </Form.Label>
                        <Form.Control
                            type="password"
                            placeholder={empleadoAEditar ? '••••••••' : 'Ingrese contraseña'}
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            required={!empleadoAEditar}
                            disabled={isSubmitting}
                        />
                        {empleadoAEditar && (
                            <Form.Text className="text-muted">
                                Si no desea cambiar la contraseña, déjelo en blanco.
                            </Form.Text>
                        )}
                    </Form.Group>

                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCloseInternal} disabled={isSubmitting}>
                        Cancelar
                    </Button>
                    <Button variant="primary" type="submit" disabled={isSubmitting}>
                        {isSubmitting ? (
                            <Spinner animation="border" size="sm" className="me-2" />
                        ) : (
                            empleadoAEditar ? 'Guardar Cambios' : 'Crear Empleado'
                        )}
                    </Button>
                </Modal.Footer>
            </Form>
        </Modal>
    );
};

export default ModalFormularioEmpleado;