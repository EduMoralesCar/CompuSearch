import { useState } from "react";
import { Card, Form, Button, Alert, Spinner, Image } from "react-bootstrap";

const TiendaLogoUploader = ({
    idTienda,
    actualizarLogo,
    fetchTiendaData,
    loading,
    updateStatus,
    setUpdateStatus
}) => {

    const [preview, setPreview] = useState(null);
    const [selectedFile, setSelectedFile] = useState(null);
    const [uploading, setUploading] = useState(false);

    const handleFileSelect = (e) => {
        const file = e.target.files[0];
        if (!file) return;

        setSelectedFile(file);
        setUpdateStatus(null);

        // Previsualización
        const reader = new FileReader();
        reader.onloadend = () => setPreview(reader.result);
        reader.readAsDataURL(file);
    };

    const handleUpload = async () => {
        if (!selectedFile || !idTienda) return;
        setUploading(true);

        const result = await actualizarLogo(idTienda, selectedFile);

        if (result.success) {
            setUpdateStatus({
                type: "success",
                message: "¡Logo actualizado con éxito!",
            });
            setPreview(null);
            setSelectedFile(null);
            await fetchTiendaData();
        } else {
            setUpdateStatus({ type: "error", message: result.error });
        }

        setUploading(false);
    };

    return (
        <Card className="shadow-sm border-0">
            <Card.Body>
                <h5 className="fw-semibold text-secondary mb-3">
                    Actualizar Logo
                </h5>

                {updateStatus && (
                    <Alert variant={updateStatus.type === "success" ? "success" : "danger"}>
                        {updateStatus.message}
                    </Alert>
                )}

                <Form.Group className="mb-3">
                    <Form.Label className="fw-medium">Seleccionar nuevo logo</Form.Label>
                    <Form.Control
                        type="file"
                        accept="image/*"
                        onChange={handleFileSelect}
                        disabled={loading || uploading}
                    />
                </Form.Group>

                {/* Vista previa */}
                {preview && (
                    <div className="text-center my-3">
                        <Image
                            src={preview}
                            alt="Vista previa"
                            rounded
                            fluid
                            className="border p-2 bg-light"
                            style={{ maxWidth: "200px" }}
                        />
                    </div>
                )}

                <Button
                    variant="primary"
                    className="w-100"
                    disabled={!selectedFile || uploading}
                    onClick={handleUpload}
                >
                    {uploading ? (
                        <>
                            <Spinner animation="border" size="sm" className="me-2" />
                            Subiendo...
                        </>
                    ) : (
                        "Subir Logo"
                    )}
                </Button>
            </Card.Body>
        </Card>
    );
};

export default TiendaLogoUploader;
