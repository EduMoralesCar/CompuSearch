
const FormatDate = (localDateTimeString) => {
    if (!localDateTimeString) return 'N/A';
    const date = new Date(localDateTimeString);
    if (isNaN(date)) return localDateTimeString;
    return new Intl.DateTimeFormat('es-ES', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
    }).format(date);
};

export default FormatDate;