const ProductoImagen = ({ imagenUrl, nombre }) => (
    <div className="mb-3">
        <img
            src={imagenUrl || "https://via.placeholder.com/400?text=Sin+Imagen"}
            alt={nombre}
            className="img-fluid rounded border"
            style={{ maxHeight: '450px', width: '100%', objectFit: 'contain' }}
        />
    </div>
);

export default ProductoImagen;
