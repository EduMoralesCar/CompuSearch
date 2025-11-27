const ProfileSelectorModal = ({ isOpen, onClose, navigate }) => {
    if (!isOpen) return null;

    return (
        <div className="modal d-block" tabIndex="-1" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
            <div className="modal-dialog modal-dialog-centered"> 
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title">Selecciona tu Perfil</h5>
                        <button type="button" className="btn-close" onClick={onClose}></button>
                    </div>
                    <div className="modal-body">
                        <p>¿A qué área deseas acceder?</p>
                    </div>
                    <div className="modal-footer">
                        <button
                            type="button"
                            className="btn btn-primary"
                            onClick={() => {
                                navigate("/perfil/tienda");
                                onClose();
                            }}
                        >
                            <i className="bi bi-shop me-2"></i> Mi perfil de Tienda
                        </button>
                        <button
                            type="button"
                            className="btn btn-secondary"
                            onClick={() => {
                                navigate("/perfil/usuario");
                                onClose();
                            }}
                        >
                            <i className="bi bi-person-fill-check me-2"></i> Mi Perfil de Usuario
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ProfileSelectorModal;