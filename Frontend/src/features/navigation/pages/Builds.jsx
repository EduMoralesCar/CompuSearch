import { useState, useEffect } from "react";
import { Container } from "react-bootstrap";
import BannerHeader from "../components/auxliar/BannerHeader";
import BuildsSummary from "../components/builds/info/BuildsSummary";
import BuildsTable from "../components/builds/info/BuildsTable";
import bannerBuilds from "../../../assets/banners/banner_builds.png";
import { useAuthStatus } from "../../../hooks/useAuthStatus";
import useCategorias from "../hooks/useCategorias";
import useBuilds from "../hooks/useBuilds";
import AuthModal from "../../../components/auth/AuthModal";
import BuildProductModal from "../components/builds/modal/BuildProductModal";
import BuildsModal from "../components/builds/modal/BuildsModal";
import { validarCompatibilidad } from "../validation/validarCompatibilidad"
import CompatibilidadModal from "../components/builds/modal/CompatibilidadModal"
import { useLocation } from "react-router-dom";

const Builds = () => {
    // Obtener si el usuario inicio sesion
    const { isAuthenticated, sessionReady, idUsuario } = useAuthStatus();

    // Modales
    const [showAuthModal, setShowAuthModal] = useState(false); // Autenticacion
    const [showModalProductos, setShowModalProductos] = useState(false); // Ver Productos
    const [showModalBuild, setShowModalBuild] = useState(false); // Cargar Builds

    // Datos de Hooks
    const { categorias } = useCategorias(); // Cargar categorias para elegir componentes

    // Productos
    const [categoriaSeleccionada, setCategoriaSeleccionada] = useState(null);
    const [productos, setProductos] = useState([]);
    const [totalElements, setTotalElements] = useState(0);
    const [loadingProductos, setLoadingProductos] = useState(false);
    const [errorProductos, setErrorProductos] = useState(null);

    const [showCompatModal, setShowCompatModal] = useState(false);
    const [erroresCompatibilidad, setErroresCompatibilidad] = useState([]);


    const [page, setPage] = useState(0); // Paginacion del modal de productos

    const [idBuild, SetIdBuild] = useState(null); // id de la build actual

    // Datos del estado de la build actualizandose
    const [buildActualizada, setBuildActualizada] = useState({});
    const [nombreActualizado, setNombreActualizado] = useState("");

    // Datos del estado de la build actual
    const [buildActual, setbuildActual] = useState({});
    const [nombreActual, setNombreActual] = useState("");

    const [mensajeBuild, setMensajeBuild] = useState(null); // Mensaje de log

    const nombresCategorias = categorias.map((c) => c.nombre); // Mapea las categorias

    const { search } = useLocation();
    const params = new URLSearchParams(search);
    const idBuildParametro = params.get("idBuild");
    

    // Funciones para las buiilds
    const {
        obtenerProductosBuilds,
        obtenerBuildPorId,
        crearBuild,
        exportarBuild,
        eliminarBuild,
        actualizarBuild,
        obtenerBuildsPorUsuario,
        loading: loadingBuild
    } = useBuilds();

    // Componentes minimos para guardar la build
    const componentesObligatorios = [
        "Procesador",
        "Placa Madre",
        "Memoria RAM",
        "Almacenamiento",
        "Fuente de Poder"
    ];

    // Saber si hay una build 
    const buildPresente = idBuild != null;

    // Obtiene las categorias de la build actualizada
    const catSeleccionadas = Object.keys(buildActualizada);

    // Obtiene las categorias que faltan en la build actualizada
    const catFaltantes = componentesObligatorios.filter(
        (c) => !catSeleccionadas.includes(c)
    );

    // Compara el tamaño de las categorias seleccionas y las faltantas
    // para determinar si la build esta incompleta
    const buildIncompleta = catSeleccionadas.length === 0 || catFaltantes.length > 0;

    useEffect(() => {
        const cargarBuildPorId = async () => {
            if (!isAuthenticated) return;

            if (idBuildParametro && idUsuario) {
                const result = await obtenerBuildPorId(idBuildParametro);
                if (result && result.success && result.data) {
                    const armado = convertirBuildADisplay(result.data.detalles);
                    setNombreActualizado(result.data.nombre);
                    setBuildActualizada(armado);
                    SetIdBuild(result.data.idBuild);
                    setbuildActual(armado);
                    setNombreActual(result.data.nombre);
                }
            }
        };

        cargarBuildPorId();
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const totalCosto = Object.values(buildActualizada).reduce(
        (acc, item) => acc + item.subtotal,
        0
    );

    const totalConsumo = Object.entries(buildActualizada).reduce((acc, [categoria, item]) => {
        if (categoria === "Fuente de Poder") return acc;

        const atributoConsumo = item.detalles.find((attr) =>
            attr.nombreAtributo.toLowerCase().includes("consumo")
        );

        if (!atributoConsumo) return acc;

        const valor = atributoConsumo.valor.replace(/[^\d.]/g, "");
        const consumoWatts = parseFloat(valor);

        return acc + (isNaN(consumoWatts) ? 0 : consumoWatts);
    }, 0);


    // Valida si el usuario inicio sesion
    const validarAutenticacion = () => {
        if (!sessionReady || !isAuthenticated) {
            setShowAuthModal(true);
            return false;
        }
        return true;
    };

    // Modal de productos
    const handleAbrirModalPro = async (categoria) => {
        setCategoriaSeleccionada(categoria);
        setPage(0);
        setShowModalProductos(true);
        await cargarProductos(categoria, 0);
    };

    const cargarProductos = async (categoria, pagina = 0) => {
        setLoadingProductos(true);
        setErrorProductos(null);

        try {
            const result = await obtenerProductosBuilds(categoria, pagina);

            if (result && result.success) {
                setProductos(result.data.content || []);
                setTotalElements(result.data.totalElements || 0);
            } else {
                setProductos([]);
                setTotalElements(0);
            }
            // eslint-disable-next-line no-unused-vars
        } catch (err) {
            setErrorProductos("Error al obtener productos");
        } finally {
            setLoadingProductos(false);
        }
    };

    // Selecciona un producto y actualiza la build actualizada
    const handleSeleccionarProducto = (producto) => {
        setBuildActualizada((prev) => ({
            ...prev,
            [categoriaSeleccionada]: {
                ...producto,
                cantidad: 1,
                subtotal: producto.precio
            }
        }));
        handleCerrarModalPro();
    };

    // Cerrar el modal de productos
    const handleCerrarModalPro = () => {
        setShowModalProductos(false);
        setCategoriaSeleccionada(null);
    };

    // Selecciona un producto y lo elimina la build actualizada
    const handleReiniciarCategoria = (categoria) => {
        setBuildActualizada((prev) => {
            const nuevo = { ...prev };
            delete nuevo[categoria];
            return nuevo;
        });
    };

    // Funcion para exportar la build
    const handleExport = async () => {
        if (!validarAutenticacion()) return;
        if (!buildPresente) {
            setMensajeBuild("Seleccione una build antes.")
        };

        const result = await exportarBuild(idBuild);

        setMensajeBuild(result.success ?
            "¡Build exportado con exito!" :
            "Error al exportar la build");
    };

    const resultadoCompat = validarCompatibilidad(buildActualizada);

    // Funcion para guardar la build
    const handleGuardarBuild = async () => {
        if (!validarAutenticacion()) return;

        if (buildIncompleta) {
            setMensajeBuild("Tu build está incompleta. Guarda los componentes principales.");
            return;
        }

        if (!nombreActualizado || nombreActualizado.trim() === "") {
            setMensajeBuild("Debes ingresar un nombre para tu build.");
            return;
        }

        if (!resultadoCompat.compatible) {
            setErroresCompatibilidad(resultadoCompat.errores);
            setShowCompatModal(true);
            return;
        }

        await guardarBuildFinal();
    };

    // Reestablece los componentes de la tabla a los de la build actual
    const handleReestablcerActual = () => {
        if (buildPresente) {
            setBuildActualizada(buildActual);
            setNombreActualizado(nombreActual);
        } else {
            setNombreActualizado("");
            setBuildActualizada({});
        }
    };

    // Reestablece los componentes de la tabla
    const handleRestablecerTotal = () => {
        setNombreActualizado("");
        setBuildActualizada({});
        SetIdBuild(null);
        setNombreActual("");
        setbuildActual({});
    }

    const guardarBuildFinal = async () => {
        const detalles = Object.values(buildActualizada).map(item => ({
            idProductoTienda: item.idProductoTienda,
            cantidad: item.cantidad,
            precioUnitario: item.precio,
            subTotal: item.subtotal
        }));

        const buildData = {
            nombre: nombreActualizado,
            compatible: resultadoCompat.compatible,
            costoTotal: totalCosto,
            idUsuario,
            detalles,
            consumoTotal: `${totalConsumo} W`,
        };

        const result = await crearBuild(buildData);

        if (result.success && result.data) {
            SetIdBuild(result.data.idBuild);
            setbuildActual(buildActualizada);
            setNombreActual(nombreActualizado);
        }

        setMensajeBuild(
            result.success
                ? "¡Tu armado fue guardado exitosamente!"
                : "Error al guardar el armado. Intenta nuevamente."
        );
    };

    // Actualiza la build
    const handleActualizar = async () => {
        if (!validarAutenticacion()) return;

        if (!buildPresente) {
            setMensajeBuild("Seleccione una build antes.")
        };

        if (buildIncompleta) {
            setMensajeBuild("Tu build está incompleta. Guarda los componentes principales.");
            return;
        }

        if (!nombreActualizado || nombreActualizado.trim() === "") {
            setMensajeBuild("Debes ingresar un nombre para tu build.");
            return;
        }

        if (!resultadoCompat.compatible) {
            setErroresCompatibilidad(resultadoCompat.errores);
            setShowCompatModal(true);
            return;
        }

        const detalles = Object.values(buildActualizada).map(item => ({
            idProductoTienda: item.idProductoTienda,
            cantidad: item.cantidad,
            precioUnitario: item.precio,
            subTotal: item.subtotal
        }));

        const buildData = {
            nombre: nombreActualizado,
            compatible: resultadoCompat.compatible,
            costoTotal: totalCosto,
            consumoTotal: `${totalConsumo} W`,
            idUsuario,
            detalles
        };

        const result = await actualizarBuild(idBuild, buildData);

        setMensajeBuild(
            result.success
                ? "¡Tu build fue actualizado exitosamente!"
                : "Error al actualizar tu build. Intenta nuevamente."
        );
    }

    // Elimina la build
    const handleEliminar = async () => {
        if (!validarAutenticacion()) return;

        if (!buildPresente) {
            setMensajeBuild("Seleccione una build antes.")
        };

        const result = await eliminarBuild(idBuild);

        if (result.success) {
            SetIdBuild(null);
            setbuildActual({});
            setNombreActual("");
            setBuildActualizada({});
            setNombreActualizado("");
        }

        setMensajeBuild(
            result.success
                ? "¡Tu armado fue eliminado exitosamente!"
                : "Error al eliminar el armado. Intenta nuevamente."
        );
    }


    const handleSeleccionarBuild = (build) => {
        const armado = convertirBuildADisplay(build.detalles);

        setNombreActualizado(build.nombre);
        setBuildActualizada(armado);
        SetIdBuild(build.idBuild);
        setbuildActual(armado);
        setNombreActual(build.nombre);
        setShowModalBuild(false);
    };


    const convertirBuildADisplay = (detalles) => {
        const armadoTransformado = {};

        detalles.forEach((item) => {
            const categoria = item.categoria;

            if (!categoria) return;

            armadoTransformado[categoria] = {
                idProductoTienda: item.idProductoTienda,
                nombreProducto: item.nombreProducto || "Componente",
                precio: item.precio ?? 0,
                cantidad: item.cantidad ?? 0,
                subtotal: item.subTotal ?? 0,
                stock: item.stock ?? 0,
                nombreTienda: item.nombreTienda || "Tienda",
                urlProducto: item.urlProducto || "",
                detalles: item.detalles || []
            };
        });

        return armadoTransformado;
    };

    return (
        <section>
            <BannerHeader
                title="Arma tu computadora"
                description="Crea tu PC personalizada con los mejores componentes."
                background={bannerBuilds}
            />

            <CompatibilidadModal
                show={showCompatModal}
                errores={erroresCompatibilidad}
                onConfirmar={() => {
                    setShowCompatModal(false);
                    guardarBuildFinal();
                }}
                onCancelar={() => setShowCompatModal(false)}
            />


            <BuildsModal
                show={showModalBuild}
                onClose={() => setShowModalBuild(false)}
                onSeleccionarBuild={handleSeleccionarBuild}
                idUsuario={idUsuario}
                obtenerBuildsPorUsuario={obtenerBuildsPorUsuario}
            />

            <AuthModal show={showAuthModal} onClose={() => setShowAuthModal(false)} 
                message="Debes iniciar sesión o registrarte para guardar o descargar tu armado."/>

            <BuildProductModal
                show={showModalProductos}
                onClose={handleCerrarModalPro}
                categoria={categoriaSeleccionada}
                productos={productos}
                totalElements={totalElements}
                loading={loadingProductos}
                error={errorProductos}
                onSelect={handleSeleccionarProducto}
                page={page}
                setPage={setPage}
            />

            <main>
                <Container className="my-5">
                    <section className="mb-4">
                        <BuildsSummary
                            totalCosto={totalCosto}
                            totalConsumo={totalConsumo}
                            resultadoCompat={resultadoCompat}
                            buildPresente={buildPresente}
                            armadoIncompleto={buildIncompleta}
                            faltantes={catFaltantes}
                            componentesObligatorios={componentesObligatorios}
                            mensajeBuild={mensajeBuild}
                            loadingBuild={loadingBuild}
                            onGuardarBuild={handleGuardarBuild}
                            onDescargarBuild={handleExport}
                            onActualizarBuild={handleActualizar}
                            onEliminarBuild={handleEliminar}
                            setShowModalBuild={setShowModalBuild}
                        />
                    </section>

                    <section>
                        <BuildsTable
                            categorias={nombresCategorias}
                            onSelectCategoria={handleAbrirModalPro}
                            armado={buildActualizada}
                            onReiniciarCategoria={handleReiniciarCategoria}
                            nombreBuild={nombreActualizado}
                            onCambiarNombreBuild={setNombreActualizado}
                            onResetArmado={handleReestablcerActual}
                            onRestablecerTabla={handleRestablecerTotal}
                        />
                    </section>
                </Container>
            </main>
        </section>
    );
};

export default Builds;
