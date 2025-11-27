import { Form, Button } from "react-bootstrap";

const FiltroProductos = ({ categorias, categoria, setCategoria, sort, setSort, limpiarFiltros }) => {
    return (
        <div className="d-flex flex-wrap gap-3 mb-4 p-3 bg-light rounded shadow-sm">

            {/* Categorías */}
            <Form.Select
                style={{ maxWidth: "250px" }}
                value={categoria ?? ""}
                onChange={(e) => setCategoria(e.target.value || "")}
            >
                <option value="">Todas las categorías</option>
                {(categorias ?? []).map(cat => (
                    <option key={cat.idCategoria} value={cat.nombre}>
                        {cat.nombre}
                    </option>
                ))}
            </Form.Select>

            {/* Sort */}
            <Form.Select
                style={{ maxWidth: "250px" }}
                value={sort}
                onChange={(e) => setSort(e.target.value)}
            >
                <option value="precio,asc">Precio: Menor a mayor</option>
                <option value="precio,desc">Precio: Mayor a menor</option>
                <option value="stock,asc">Stock: Menor a mayor</option>
                <option value="stock,desc">Stock: Mayor a menor</option>
            </Form.Select>

            {/* Limpiar */}
            <Button variant="secondary" onClick={limpiarFiltros}>
                Limpiar filtros
            </Button>

        </div>
    );
};

export default FiltroProductos;
