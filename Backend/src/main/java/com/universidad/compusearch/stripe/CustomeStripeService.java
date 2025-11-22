package com.universidad.compusearch.stripe;

import org.springframework.stereotype.Service;

import com.universidad.compusearch.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import com.universidad.compusearch.entity.Tienda;
import com.universidad.compusearch.repository.TiendaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomeStripeService {

    private final TiendaRepository tiendaRepository;

    @Transactional
    public String crearCustomerSiNoExiste(Tienda tienda) {
        if (tienda.getStripeCustomerId() != null) {
            log.info("La tienda {} ya tiene un Stripe Customer ID: {}", tienda.getNombre(),
                    tienda.getStripeCustomerId());
            return tienda.getStripeCustomerId();
        }

        try {
            log.info("Creando Stripe Customer para la tienda {}", tienda.getNombre());

            CustomerCreateParams params = CustomerCreateParams.builder()
                    .setEmail(tienda.getEmail())
                    .setName(tienda.getNombre())
                    .putMetadata("tiendaId", String.valueOf(tienda.getIdUsuario()))
                    .build();

            Customer customer = Customer.create(params);

            tienda.setStripeCustomerId(customer.getId());
            tiendaRepository.save(tienda);

            log.info("Stripe Customer creado correctamente. ID: {}", customer.getId());

            return customer.getId();

        } catch (Exception e) {
            log.error("Error al crear el Stripe Customer: {}", e.getMessage(), e);
            throw StripeException.errorCreateCustomer();
        }
    }

    public Customer obtenerCustomer(Tienda tienda) {
        if (tienda.getStripeCustomerId() == null) {
            log.warn("La tienda {} no tiene asociado un Stripe Customer ID", tienda.getNombre());
            return null;
        }

        try {
            return Customer.retrieve(tienda.getStripeCustomerId());
        } catch (Exception e) {
            log.error("Error al obtener el Customer de Stripe: {}", e.getMessage());
            throw StripeException.errorObtainCustomer();
        }
    }
}
