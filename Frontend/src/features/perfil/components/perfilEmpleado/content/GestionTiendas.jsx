import { useState, useCallback, useEffect, useMemo } from 'react';
import { useTiendas } from "../../../hooks/useTiendas";
import TablaTiendas from "../table/TablaTiendas";
import ModalDetalleTienda from "../modal/ModalDetalleTienda"
import { FiRefreshCw } from "react-icons/fi";

import {
    Spinner,
    Row,
    Col,
    Button,
    Form,
    Card,
    InputGroup,
    Pagination,
    FormControl
} from 'react-bootstrap';

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

    const pageSize = 10;

    const loadTiendas = useCallback(async (p, filters) => {
        const result = await obtenerTiendasPaginadas(p, pageSize, filters);
        if (result.success) {
            setTiendasData(result.data);
            setPage(result.data.number);
        } else {
            console.error("Fallo al cargar tiendas:", result.error);
        }
    }, [obtenerTiendasPaginadas]);

    useEffect(() => {
        loadTiendas(page, { nombre: currentFilter });
    }, [page, currentFilter, loadTiendas]);

    const handleFilterChange = () => {
        setCurrentFilter(filterName);
        setPage(0);
    };

    const handleToggleEstado = async (idUsuario, nuevoEstado) => {
        const result = await actualizarEstado(idUsuario, nuevoEstado);
        if (result.success) {
            setTiendasData(prev => ({
                ...prev,
                content: prev.content.map(t =>
                    t.idUsuario === idUsuario ? { ...t, activo: nuevoEstado } : t
                )
            }));
        } else {
            alert(`Fallo al actualizar el estado: ${result.error}`);
        }
    };

    const handleToggleVerificacion = async (idUsuario, nuevoEstado) => {
        const result = await actualizarVerificacion(idUsuario, nuevoEstado);
        if (result.success) {
            setTiendasData(prev => ({
                ...prev,
                content: prev.content.map(t =>
                    t.idUsuario === idUsuario ? { ...t, verificado: nuevoEstado } : t
                )
            }));
        } else {
            alert(`Fallo al actualizar la verificación: ${result.error}`);
        }
    };

    const viewDetails = async (idUsuario) => {
        setShowModal(true);
        setDetailsLoading(true);
        setModalStore(null);

        const result = await obtenerTiendaPorId(idUsuario);
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

    const totalPages = tiendasData.totalPages || 1;

    const renderPagination = useMemo(() => {
        let items = [];
        const maxPagesToShow = 5;
        let startPage, endPage;

        if (totalPages <= maxPagesToShow) {
            startPage = 1;
            endPage = totalPages;
        } else {
            if (page <= Math.floor(maxPagesToShow / 2)) {
                startPage = 1;
                endPage = maxPagesToShow;
            } else if (page + Math.floor(maxPagesToShow / 2) >= totalPages) {
                startPage = totalPages - maxPagesToShow + 1;
                endPage = totalPages;
            } else {
                startPage = page - Math.floor(maxPagesToShow / 2) + 1;
                endPage = page + Math.ceil(maxPagesToShow / 2);
            }
        }

        for (let number = startPage; number <= endPage; number++) {
            items.push(
                <Pagination.Item
                    key={number}
                    active={number === page + 1}
                    onClick={() => setPage(number - 1)}
                    disabled={loading}
                >
                    {number}
                </Pagination.Item>,
            );
        }

        return (
            <div className="d-flex justify-content-center">
                <Pagination>
                    <Pagination.Prev
                        onClick={() => setPage(p => Math.max(0, p - 1))}
                        disabled={page === 0 || loading}
                    />
                    {items}
                    <Pagination.Next
                        onClick={() => setPage(p => Math.min(totalPages - 1, p + 1))}
                        disabled={page >= totalPages - 1 || loading}
                    />
                </Pagination>
            </div>
        );
    }, [page, totalPages, loading]);


    return (
        <>
            <Card className="shadow-lg border-0">
                <Card.Header as="h5" className="d-flex justify-content-between align-items-center bg-light text-primary">
                    Gestión de Tiendas

                    <div className="d-flex align-items-center" style={{ gap: "8px" }}>
                        <InputGroup>
                            <FormControl
                                type='text'
                                placeholder='Buscar por Nombre'
                                value={filterName}
                                onChange={(e) => setFilterName(e.target.value)}
                                disabled={loading}
                            />

                            <Button
                                variant='primary'
                                onClick={handleFilterChange}
                                disabled={loading}
                            >
                                {loading ? (
                                    <Spinner animation="border" size="sm" />
                                ) : 'Buscar'}
                            </Button>

                            <Button
                                variant="outline-secondary"
                                onClick={() => {
                                    setFilterName("");
                                    setCurrentFilter("");
                                    setPage(0);
                                }}
                                disabled={loading}
                            >
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
                    </div>
                </Card.Header>
            </Card>

            {error && (
                <div className="alert alert-danger" role="alert">
                    <i className="fas fa-exclamation-triangle me-2"></i>
                    <span className="fw-medium">Error:</span> {error}
                </div>
            )}

            <Card className="shadow-lg">
                <Card.Body>
                    <TablaTiendas
                        loading={loading}
                        tiendasData={tiendasData}
                        error={error}
                        handleToggleEstado={handleToggleEstado}
                        handleToggleVerificacion={handleToggleVerificacion}
                        viewDetails={viewDetails}
                    />
                </Card.Body>

                <Card.Footer>
                    <Row className="align-items-center">
                        {tiendasData.content.length > 0 && renderPagination}
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