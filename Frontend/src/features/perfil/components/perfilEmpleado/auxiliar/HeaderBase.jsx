import { Card } from "react-bootstrap";

const HeaderBase = ({ title, children, className = "" }) => {
    return (
        <Card.Header
            as="h5"
            className={`d-flex justify-content-between align-items-center bg-light text-primary ${className}`}
        >
            {title}

            <div className="d-flex align-items-center" style={{ gap: "8px" }}>
                {children}
            </div>
        </Card.Header>
    );
};

export default HeaderBase;
