export function validarCompatibilidad(productos) {
    const errores = [];

    const cpu = productos["Procesador"];
    const motherboard = productos["Placa Madre"];
    const ram = productos["Memoria RAM"];
    const cooler = productos["Refrigeración CPU"];
    const gpu = productos["Tarjeta de Video"];
    const psu = productos["Fuente de Poder"];
    const almacenamiento = productos["Almacenamiento"];

    // Helper para obtener valor de un atributo
    const getAttr = (producto, nombre) => {
        if (!producto) return null;
        const attr = producto.detalles.find(d => d.nombreAtributo === nombre);
        return attr ? attr.valor : null;
    };

    // 1. CPU ↔ Placa Madre (Socket)
    if (cpu && motherboard) {
        const socketCPU = getAttr(cpu, "Socket CPU");
        const socketMB = getAttr(motherboard, "Socket Motherboard");
        if (socketCPU !== socketMB) {
            errores.push(`Socket incompatible entre CPU (${socketCPU}) y Placa Madre (${socketMB}).`);
        }
    }

    // 2. RAM ↔ Placa Madre (Tipo de RAM)
    if (ram && motherboard) {
        const tipoRam = getAttr(ram, "Tipo RAM");
        const tipoRamMB = getAttr(motherboard, "Tipo RAM Compatible");
        if (tipoRam !== tipoRamMB) {
            errores.push(`La RAM (${tipoRam}) no es compatible con la placa madre (${tipoRamMB}).`);
        }
    }

    // 3. Cooler ↔ CPU (Socket)
    if (cooler && cpu) {
        const socketCPU = getAttr(cpu, "Socket CPU");
        const socketsCooler = getAttr(cooler, "Compatibilidad Socket Cooler") || "";
        if (!socketsCooler.includes(socketCPU)) {
            errores.push(`El cooler no es compatible con el socket ${socketCPU} de la CPU.`);
        }
    }

    // 4. Almacenamiento ↔ Placa Madre (M.2)
    if (almacenamiento && motherboard) {
        const interfaz = getAttr(almacenamiento, "Interfaz Almacenamiento") || "";
        const puertosM2 = parseInt(getAttr(motherboard, "Puertos M.2") || "0", 10);
        if (interfaz.includes("NVMe") && puertosM2 <= 0) {
            errores.push(`El almacenamiento NVMe requiere un puerto M.2 y la placa madre no lo tiene.`);
        }
    }

    // 5. GPU ↔ Placa Madre (PCIe)
    if (gpu && motherboard) {
        const interfazGPU = getAttr(gpu, "Interfaz PCIe GPU");
        // Si quieres hacer una validación estricta, podrías comparar versiones aquí
        if (!interfazGPU) {
            errores.push(`❌La GPU no especifica interfaz PCIe.`);
        }
    }

    // 6. Fuente ↔ Consumo total
    if (psu) {
        const potenciaPSU = parseInt(getAttr(psu, "Potencia PSU") || "0", 10);
        let consumoTotal = 0;

        for (const key in productos) {
            if (key === "Fuente de Poder") continue;
            const prod = productos[key];
            const consumoAttr = prod.detalles.find(d => d.nombreAtributo.includes("Consumo"));
            if (consumoAttr) {
                const valorNum = parseInt(consumoAttr.valor);
                if (!isNaN(valorNum)) consumoTotal += valorNum;
            }
        }

        if (potenciaPSU < consumoTotal) {
            errores.push(`❌ La potencia de la fuente (${potenciaPSU} W) es menor al consumo total (${consumoTotal} W).`);
        }
    }


    // Resultado final
    return {
        compatible: errores.length === 0,
        errores
    };
}
