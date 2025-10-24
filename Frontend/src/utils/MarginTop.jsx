export const MarginTop = () => {
    const header = document.querySelector("header");
    const main = document.querySelector("main");
    if (header && main) {
        const height = header.offsetHeight;
        main.style.marginTop = `${height}px`;
    }
};
