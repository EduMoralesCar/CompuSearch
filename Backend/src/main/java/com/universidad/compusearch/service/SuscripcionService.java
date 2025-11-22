package com.universidad.compusearch.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.universidad.compusearch.entity.EstadoSuscripcion;
import com.universidad.compusearch.entity.Suscripcion;
import com.universidad.compusearch.repository.SuscripcionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@RequiredArgsConstructor
public class SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;

    // Guardar suscripcion
    public void guardarSuscripcion(Suscripcion suscripcion) {
        log.info("Guardando suscripcion de la tienda {}", suscripcion.getTienda().getNombre());
        suscripcionRepository.save(suscripcion);
    }

    public List<Suscripcion> buscarSuscripcionesActivasPorPlanId(Long idPlan) {
        return suscripcionRepository.findByPlanIdPlanAndEstado(idPlan, EstadoSuscripcion.ACTIVA);
    }
    /*
    // Crear checkout de suscripcion
    @Transactional
    public String crearCheckoutSuscripcion(Long idTienda, Long idPlan) {
        log.info("Iniciando creación de Checkout para tienda {} y plan {}", idTienda, idPlan);

        Tienda tienda = tiendaService.bucarPorId(idTienda);

        Plan plan = planService.findById(idPlan);

        if (tienda.getStripeCustomerId() == null) {
            throw new RuntimeException("La tienda no tiene Stripe Customer ID");
        }

        try {
            log.info("Creando Checkout Session para customer={} y price={}",
                        tienda.getStripeCustomerId(), plan.getStripePriceId());

            SessionCreateParams params = SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                        .setCustomer(tienda.getStripeCustomerId())
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setPrice(plan.getStripePriceId())
                                        .setQuantity(1L)
                                        .build())
                        // Cambiar a backend por el docker
                        .setSuccessUrl("http://localhost:8080/suscripcion/success?session_id={CHECKOUT_SESSION_ID}")
                        .setCancelUrl("http://localhost:8080/suscripcion/cancel")
                        .build();

            Session session = Session.create(params);

            log.info("Checkout Session creada correctamente: {}", session.getUrl());

            Suscripcion suscripcion = new Suscripcion();
            suscripcion.setPlan(plan);
            suscripcion.setTienda(tienda);
            suscripcion.setFechaInicio(LocalDateTime.now());
            suscripcion.setEstado(EstadoSuscripcion.PENDIENTE);
            suscripcion.setStripeClientSecret(session.getId());
            suscripcionRepository.save(suscripcion);

            return session.getUrl();

        } catch (Exception e) {
            log.error("Error creando Checkout Session: {}", e.getMessage());
            throw new RuntimeException("Error creando sesión de pago en Stripe");
        }
    }

    // Procesar evento
    @Transactional
    public void procesarEventoWebhookSubscription(Session session) {
        log.info("Procesando sesión confirmada: {}", session.getId());

        Suscripcion suscripcion = suscripcionRepository.findByStripeClientSecret(session.getId())
                .orElse(null);

        if (suscripcion == null) {
            log.error("No se encontró suscripción asociada al Checkout Session ID {}", session.getId());
            return;
        }

        try {
            String subscriptionId = session.getSubscription();
            Subscription subscription = Subscription.retrieve(subscriptionId);

            String invoiceId = subscription.getLatestInvoice();

            suscripcion.setStripeSubscriptionId(subscriptionId);
            suscripcion.setStripeLatestInvoiceId(invoiceId);
            suscripcion.setEstado(EstadoSuscripcion.ACTIVA);

            suscripcionRepository.save(suscripcion);

            log.info("Suscripción actualizada correctamente. ID={}", subscriptionId);

        } catch (Exception e) {
            log.error("Error leyendo datos del session: {}", e.getMessage());
        }
    }

    // Cancela la suscripcion (Método llamado desde la aplicación)
    @Transactional
    public void cancelarSuscripcion(Long idSuscripcion) {
        Suscripcion suscripcion = suscripcionRepository.findById(idSuscripcion)
                .orElseThrow(() -> new RuntimeException("Suscripción no encontrada"));

        if (suscripcion.getStripeSubscriptionId() == null) {
            throw new RuntimeException("La suscripción no tiene ID de Stripe");
        }

        try {
            Subscription subscription = Subscription.retrieve(suscripcion.getStripeSubscriptionId());
            subscription.cancel();

            suscripcion.setEstado(EstadoSuscripcion.CANCELADA);
            suscripcion.setFechaFin(LocalDateTime.now());
            suscripcionRepository.save(suscripcion);

            log.info("Suscripción {} cancelada correctamente", idSuscripcion);

        } catch (Exception e) {
            log.error("Error cancelando la suscripción en Stripe: {}", e.getMessage());
            throw new RuntimeException("No se pudo cancelar la suscripción");
        }
    }

    // 🟢 NUEVO MÉTODO - Marca la suscripción como cancelada, llamado desde el Webhook de Stripe
    @Transactional
    public void marcarSuscripcionCanceladaDesdeStripe(String stripeSubscriptionId) {
        log.warn("Procesando cancelación de suscripción desde Stripe: {}", stripeSubscriptionId);
        
        Suscripcion suscripcion = suscripcionRepository.findByStripeSubscriptionId(stripeSubscriptionId)
                .orElse(null);

        if (suscripcion == null) {
            log.error("No se encontró suscripción local con Stripe Subscription ID {}", stripeSubscriptionId);
            return;
        }
        
        suscripcion.setEstado(EstadoSuscripcion.CANCELADA);
        suscripcion.setFechaFin(LocalDateTime.now());
        suscripcionRepository.save(suscripcion);
        
        log.info("Suscripción local {} marcada como cancelada.", suscripcion.getIdSuscripcion());
    }

    // Actualiza el precio de la suscripcion en el siguiente pago
    @Transactional
    public void actualizarSuscripcionConNuevoPrecio(Long idSuscripcion, Long idNuevoPlan) {
        log.info("Iniciando cambio de plan/precio para Suscripción ID: {} al Plan ID: {}", idSuscripcion, idNuevoPlan);

        Suscripcion suscripcion = suscripcionRepository.findById(idSuscripcion)
                .orElseThrow(() -> new RuntimeException("Suscripción no encontrada"));

        Plan nuevoPlan = planService.findById(idNuevoPlan);

        if (suscripcion.getStripeSubscriptionId() == null) {
            throw new RuntimeException("La suscripción no tiene ID de Stripe para actualizar.");
        }

        if (nuevoPlan.getStripePriceId() == null) {
            throw new RuntimeException("El nuevo Plan no tiene Price ID de Stripe.");
        }

        try {
            productStripeService.actualizarPrecioSubscriptionStripe(
                        suscripcion.getStripeSubscriptionId(),
                        nuevoPlan.getStripePriceId());

            suscripcion.setPlan(nuevoPlan);
            suscripcionRepository.save(suscripcion);

            log.info(
                        "Suscripción {} actualizada localmente. El nuevo precio se aplicará en el próximo ciclo de facturación.",
                        idSuscripcion);

        } catch (Exception e) {
            log.error("Error al actualizar la suscripción {} en Stripe. Error: {}", idSuscripcion, e.getMessage());
            throw new RuntimeException("Fallo al actualizar la suscripción con el nuevo precio en Stripe.", e);
        }
    }

    @Transactional
    public void registrarPagoDesdeStripe(Invoice invoice) {

        log.info("Registrando pago de Stripe. Invoice ID={}", invoice.getId());

        try {
            String stripeSubscriptionId = invoice.getId();

            Suscripcion suscripcion = suscripcionRepository
                        .findByStripeSubscriptionId(stripeSubscriptionId)
                        .orElseThrow(() -> new RuntimeException(
                                "Suscripción no encontrada para Stripe Subscription ID: " + stripeSubscriptionId));

            String paymentIntentId = invoice.getId();

            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            BigDecimal monto = BigDecimal.valueOf(paymentIntent.getAmount())
                        .divide(BigDecimal.valueOf(100));

            Pago pago = new Pago();
            pago.setSuscripcion(suscripcion);
            pago.setIdOperacion(paymentIntentId);
            pago.setMonto(monto);
            pago.setFechaPago(LocalDateTime.now());
            pago.setExito(true);

            pagoRepository.save(pago);

            log.info("Pago registrado exitosamente. PaymentIntent={}", paymentIntentId);

        } catch (Exception e) {
            log.error("Error registrando pago desde Stripe: {}", e.getMessage());
            throw new RuntimeException("Error registrando pago: " + e.getMessage(), e);
        }
    }
     */
}