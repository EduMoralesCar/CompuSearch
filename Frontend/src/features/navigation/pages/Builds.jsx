import React, { useState } from "react";
import { Container, Modal, Button } from "react-bootstrap";
import BannerHeader from "../components/BannerHeader";
import BuildsSummary from "../components/BuildsSummary";
import BuildsTable from "../components/BuildsTable";
import bannerBuilds from "../../../assets/banners/banner_builds.png";
import { useAuthStatus } from "../../../hooks/useAuthStatus";
import useCategorias from "../hooks/useCategorias";
import useProductosTienda from "../hooks/useProductoTienda";
import useBuilds from "../hooks/useBuilds";
import { categoriasMap } from "../utils/categoriasMap";
import BuildAuthModal from "../components/BuildAuthModal";
import BuildProductModal from "../components/BuildProductModal";

const Builds = () => {
    const { isAuthenticated, sessionReady, idUsuario } = useAuthStatus();

    const [showAuthModal, setShowAuthModal] = useState(false);
    const [showModal, setShowModal] = useState(false);
    const [categoriaSeleccionada, setCategoriaSeleccionada] = useState(null);
    const [page, setPage] = useState(0);
    const [armado, setArmado] = useState({});
    const [mensajeBuild, setMensajeBuild] = useState(null);

    const { categorias } = useCategorias("");
    const nombresCategorias = categorias.map((c) => c.nombre);

    const { productos, totalElements, loading, error } = useProductosTienda({
        categoria: categoriasMap[categoriaSeleccionada],
        page
    });

    const { guardarBuild, loading: loadingBuild } = useBuilds();

    const componentesObligatorios = [
        "Procesador",
        "Placa Madre",
        "Memoria RAM",
        "Almacenamiento",
        "Fuente de Poder"
    ];
    const componentesSeleccionados = Object.keys(armado);
    const faltantes = componentesObligatorios.filter(
        (c) => !componentesSeleccionados.includes(c)
    );
    const armadoIncompleto =
        componentesSeleccionados.length === 0 || faltantes.length > 0;

    const totalArmado = Object.values(armado).reduce(
        (acc, item) => acc + item.subtotal,
        0
    );

    const validarAutenticacion = () => {
        if (!sessionReady || !isAuthenticated) {
            setShowAuthModal(true);
            return false;
        }
        return true;
    };

    const handleAbrirModal = (categoria) => {
        setCategoriaSeleccionada(categoria);
        setPage(0);
        setShowModal(true);
    };

    const handleSeleccionarProducto = (producto) => {
        setArmado((prev) => ({
            ...prev,
            [categoriaSeleccionada]: {
                ...producto,
                cantidad: 1,
                subtotal: producto.precio
            }
        }));
        handleCerrarModal();
    };

    const handleReiniciarCategoria = (categoria) => {
        setArmado((prev) => {
            const nuevo = { ...prev };
            delete nuevo[categoria];
            return nuevo;
        });
    };

    const handleCerrarModal = () => {
        setShowModal(false);
        setCategoriaSeleccionada(null);
    };

    const handleGuardarBuild = async () => {
        if (!validarAutenticacion()) return;

        if (armadoIncompleto) {
            setMensajeBuild("Tu armado está incompleto. No se puede guardar.");
            return;
        }

        const buildData = {
            nombre: "Mi PC personalizada",
            compatible: true,
            costoTotal: totalArmado,
            idUsuario
        };

        const result = await guardarBuild(buildData);
        setMensajeBuild(
            result.success
                ? "¡Tu armado fue guardado exitosamente!"
                : "Error al guardar el armado. Intenta nuevamente."
        );
    };

    const handleDescargarBuild = () => {
        if (!validarAutenticacion()) return;
        console.log("Armado descargado");
        // Aquí iría la lógica real de descarga (PDF/Excel)
    };

    return (
        <section>
            <BannerHeader
                title="Arma tu computadora"
                description="Crea tu PC personalizada con los mejores componentes."
                background={bannerBuilds}
            />

            <BuildAuthModal show={showAuthModal} onClose={() => setShowAuthModal(false)} />

            <BuildProductModal
                show={showModal}
                onClose={handleCerrarModal}
                categoria={categoriaSeleccionada}
                productos={productos}
                totalElements={totalElements}
                loading={loading}
                error={error}
                onSelect={handleSeleccionarProducto}
                page={page}
                setPage={setPage}
            />

            <main>
                <Container className="my-5">
                    <section className="mb-4">
                        <BuildsSummary
                            totalArmado={totalArmado}
                            armadoIncompleto={armadoIncompleto}
                            faltantes={faltantes}
                            componentesObligatorios={componentesObligatorios}
                            mensajeBuild={mensajeBuild}
                            loadingBuild={loadingBuild}
                            onGuardarBuild={handleGuardarBuild}
                            onDescargarBuild={handleDescargarBuild}
                        />
                    </section>

                    <section>
                        <BuildsTable
                            categorias={nombresCategorias}
                            onSelectCategoria={handleAbrirModal}
                            armado={armado}
                            onReiniciarCategoria={handleReiniciarCategoria}
                        />
                    </section>
                </Container>
            </main>
        </section>
    );
};

export default Builds;
