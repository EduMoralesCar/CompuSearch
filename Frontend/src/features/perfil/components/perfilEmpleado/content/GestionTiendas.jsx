import { useState, useCallback, useEffect } from 'react';
import { useTiendas } from "../../../hooks/useTiendas";
import TablaTiendas from "../table/TablaTiendas";
import ModalDetalleTienda from "../modal/ModalDetalleTienda";
import { FiRefreshCw } from "react-icons/fi";
import HeaderBase from '../auxiliar/HeaderBase';
import PaginacionBase from '../auxiliar/PaginacionBase';
import { Spinner, Row, Button, Card, InputGroup, FormControl } from 'react-bootstrap';

const PAGE_SIZE = 10;

const GestionTiendas = () => {
    const {
        obtenerTiendasPaginadas,
        obtenerTiendaPorId,
        actualizarEstado,
        actualizarVerificacion,
        loading,
        error
    } = useTiendas();

    const [tiendasData, setTiendasData] = useState({ content: [], totalPages: 0, number: 0 });
    const [page, setPage] = useState(0);
    const [filterName, setFilterName] = useState('');
    const [currentFilter, setCurrentFilter] = useState('');
    const [modalStore, setModalStore] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [detailsLoading, setDetailsLoading] = useState(false);

    const loadTiendas = useCallback(async (p = 0, filters = {}) => {
        const result = await obtenerTiendasPaginadas(p, PAGE_SIZE, filters);
        if (result.success) {
            setTiendasData(result.data);
            setPage(result.data.number);
        } else {
            console.error("Error al cargar tiendas:", result.error);
        }
    }, [obtenerTiendasPaginadas]);

    useEffect(() => {
        loadTiendas(page, { nombre: currentFilter });
    }, [page, currentFilter, loadTiendas]);

    const handleFilterChange = () => setCurrentFilter(filterName);

    const handleClearFilter = () => {
        setFilterName('');
        setCurrentFilter('');
        setPage(0);
    };

    const handleToggle = async (idUsuario, nuevoValor, type = 'estado') => {
        const action = type === 'estado' ? actualizarEstado : actualizarVerificacion;
        const result = await action(idUsuario, nuevoValor);
        if (result.success) {
            setTiendasData(prev => ({
                ...prev,
                content: prev.content.map(t =>
                    t.idUsuario === idUsuario ? { ...t, [type === 'estado' ? 'activo' : 'verificado']: nuevoValor } : t
                )
            }));
        } else {
            alert(`Fallo al actualizar ${type === 'estado' ? 'estado' : 'verificación'}: ${result.error}`);
        }
    };

    const viewDetails = async (idUsuario) => {
        setShowModal(true);
        setDetailsLoading(true);
        setModalStore(null);

        const result = await obtenerTiendaPorId(idUsuario, true);
        if (result.success) {
            setModalStore(result.data);
        } else {
            alert(`Fallo al cargar detalles: ${result.error}`);
            setShowModal(false);
        }

        setDetailsLoading(false);
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setModalStore(null);
    };

    return (
        <>
            <Card className="shadow-lg border-0">
                <HeaderBase title="Gestión de Tiendas">
                    <InputGroup>
                        <FormControl
                            type='text'
                            placeholder='Buscar por Nombre'
                            value={filterName}
                            onChange={(e) => setFilterName(e.target.value)}
                            disabled={loading}
                        />
                        <Button variant='primary' onClick={handleFilterChange} disabled={loading}>
                            {loading ? <Spinner animation="border" size="sm" /> : 'Buscar'}
                        </Button>
                        <Button variant="outline-secondary" onClick={handleClearFilter} disabled={loading}>
                            Limpiar
                        </Button>
                    </InputGroup>

                    <Button
                        variant="outline-secondary"
                        onClick={() => loadTiendas(page, { nombre: currentFilter })}
                        disabled={loading}
                        className="d-flex align-items-center justify-content-center"
                        style={{ width: "42px", height: "38px" }}
                    >
                        <FiRefreshCw size={18} />
                    </Button>
                </HeaderBase>

                {error && <div className="alert alert-danger">{error}</div>}

                <Card.Body>
                    <TablaTiendas
                        loading={loading}
                        tiendasData={tiendasData}
                        handleToggleEstado={(id, val) => handleToggle(id, val, 'estado')}
                        handleToggleVerificacion={(id, val) => handleToggle(id, val, 'verificacion')}
                        viewDetails={viewDetails}
                    />
                </Card.Body>

                <Card.Footer>
                    <Row className="align-items-center justify-content-center">
                        <PaginacionBase
                            page={page}
                            totalPages={tiendasData.totalPages || 1}
                            loading={loading}
                            onPageChange={(p) => setPage(p)}
                        />
                    </Row>
                </Card.Footer>
            </Card>

            <ModalDetalleTienda
                store={modalStore}
                show={showModal}
                detailsLoading={detailsLoading}
                onClose={handleCloseModal}
            />
        </>
    );
};

export default GestionTiendas;
