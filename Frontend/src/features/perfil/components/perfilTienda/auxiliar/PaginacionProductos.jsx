import { Pagination } from "react-bootstrap";

const PaginacionProductos = ({ page, totalPages, setPage }) => {
    if (!totalPages || totalPages <= 1) return null;

    return (
        <div className="d-flex justify-content-center mt-4">
            <Pagination>
                <Pagination.Prev
                    onClick={() => setPage(p => Math.max(0, p - 1))}
                    disabled={page === 0}
                />

                {[...Array(totalPages)].map((_, i) => (
                    <Pagination.Item
                        key={i}
                        active={i === page}
                        onClick={() => setPage(i)}
                    >
                        {i + 1}
                    </Pagination.Item>
                ))}

                <Pagination.Next
                    onClick={() => setPage(p => Math.min(totalPages - 1, p + 1))}
                    disabled={page === totalPages - 1}
                />
            </Pagination>
        </div>
    );
};

export default PaginacionProductos;
