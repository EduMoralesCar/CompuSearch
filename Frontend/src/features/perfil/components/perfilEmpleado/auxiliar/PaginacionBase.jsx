import { Pagination } from "react-bootstrap";

const PaginacionBase = ({
    page,
    totalPages,
    onPageChange,
    loading = false
}) => {
    if (totalPages <= 1) return null;

    const goTo = (p) => {
        if (!loading && p >= 0 && p < totalPages) {
            onPageChange(p);
        }
    };

    const items = [];

    for (let i = 0; i < totalPages; i++) {
        items.push(
            <Pagination.Item
                key={i}
                active={i === page}
                onClick={() => goTo(i)}
                disabled={loading}
            >
                {i + 1}
            </Pagination.Item>
        );
    }

    return (
        <Pagination className="mt-3">
            <Pagination.First onClick={() => goTo(0)} disabled={loading || page === 0} />
            <Pagination.Prev onClick={() => goTo(page - 1)} disabled={loading || page === 0} />

            {items}

            <Pagination.Next onClick={() => goTo(page + 1)} disabled={loading || page === totalPages - 1} />
            <Pagination.Last onClick={() => goTo(totalPages - 1)} disabled={loading || page === totalPages - 1} />
        </Pagination>
    );
};

export default PaginacionBase;
