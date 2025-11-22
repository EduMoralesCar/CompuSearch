package com.universidad.compusearch.stripe;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.ProductUpdateParams;
import com.universidad.compusearch.entity.Plan;
import com.universidad.compusearch.exception.StripeException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductStripeService {

    public String crearProductoStripe(Plan plan) throws Exception {
        log.info("Iniciando la creación de Producto Stripe para el plan: {}", plan.getNombre());
        try {
            ProductCreateParams params = ProductCreateParams.builder()
                    .setName(plan.getNombre())
                    .setDescription(plan.getDescripcion())
                    .build();

            Product product = Product.create(params);
            log.info("Producto Stripe creado exitosamente. ID: {}", product.getId());
            return product.getId();
        } catch (Exception e) {
            log.error("Error al crear el Producto Stripe para el plan: {}. Error: {}", plan.getNombre(), e.getMessage(),
                    e);
            throw StripeException.errorCreateProduct();
        }
    }

    public String crearPrecioStripe(Plan plan, String productId) throws Exception {
        log.info("Iniciando la creación de Precio Stripe para el Producto ID: {} con monto: {}", productId,
                plan.getPrecioMensual());
        try {
            long amountInCents = plan.getPrecioMensual()
                    .multiply(new BigDecimal(100))
                    .longValue();

            PriceCreateParams priceParams = PriceCreateParams.builder()
                    .setCurrency("usd")
                    .setUnitAmount(amountInCents)
                    .setRecurring(
                            PriceCreateParams.Recurring.builder()
                                    .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
                                    .build())
                    .setProduct(productId)
                    .build();

            Price price = Price.create(priceParams);
            log.info("Precio Stripe creado exitosamente para Producto ID: {}. Precio ID: {}", productId, price.getId());
            return price.getId();
        } catch (Exception e) {
            log.error("Error al crear el Precio Stripe para el Producto ID: {}. Error: {}", productId, e.getMessage(),
                    e);
            throw StripeException.errorCreatePrice();
        }
    }

    public void actualizarProductoStripe(String productId, Plan plan) throws Exception {
        log.info("Iniciando la actualización del Producto Stripe ID: {} con nuevo nombre: {}", productId,
                plan.getNombre());

        try {
            ProductUpdateParams params = ProductUpdateParams.builder()
                    .setName(plan.getNombre())
                    .setDescription(plan.getDescripcion())
                    .build();

            Product product = Product.retrieve(productId);

            product.update(params);

            log.info("Producto Stripe ID: {} actualizado exitosamente.", productId);
        } catch (Exception e) {
            log.error("Error al actualizar el Producto Stripe ID: {}. Error: {}", productId, e.getMessage(), e);
            throw StripeException.errorUpdateProduct();
        }
    }

    public String crearNuevoPrecioStripe(Plan plan, String productId) throws Exception {
        log.warn(
                "Se ha solicitado crear un nuevo Price para el Producto ID: {} debido a un posible cambio de precio. Delegando a 'crearPrecioStripe'.",
                productId);
        return crearPrecioStripe(plan, productId);
    }
    /*
    public void actualizarPrecioSubscriptionStripe(String subscriptionId, String newPriceId) throws Exception {
        log.info("Actualizando Subscription ID: {} para usar el nuevo Price ID: {}", subscriptionId, newPriceId);
        
        try {
            Subscription subscription = Subscription.retrieve(subscriptionId);
    
            String subscriptionItemId = subscription.getItems().getData().get(0).getId();
    
            SubscriptionUpdateParams params = SubscriptionUpdateParams.builder()
                    .addItem(SubscriptionUpdateParams.Item.builder()
                            .setId(subscriptionItemId)
                            .setPrice(newPriceId)
                            .build())
                    .setProrationBehavior(SubscriptionUpdateParams.ProrationBehavior.NONE) 
                    .build();
    
            subscription.update(params);
            
            log.info("Subscription ID: {} actualizada en Stripe. Nuevo precio programado para el próximo ciclo.", subscriptionId);
        } catch (Exception e) {
            log.error("Error al actualizar la Subscription ID {} en Stripe al Price ID {}. Error: {}", subscriptionId, newPriceId, e.getMessage(), e);
            throw e;
        }
    } */
}
