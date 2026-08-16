package com.anverraglobal.customer.domain;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class CustomerDomainTest {

    @Test
    void validIndividualCreation() {
        Customer customer = Customer.create(CustomerType.INDIVIDUAL, "John Doe", "john@example.com", "123 Main St", "{\"pan\":\"ABCDE1234F\"}", null, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        assertNotNull(customer);
        assertEquals(CustomerStatus.ACTIVE, customer.getStatus());
    }

    @Test
    void individualWithoutInfoRejected() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            Customer.create(CustomerType.INDIVIDUAL, "John Doe", "john@example.com", "123 Main St", null, null, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        );
        assertTrue(exception.getMessage().contains("individualInfo is required"));
    }

    @Test
    void validOrganizationCreation() {
        Customer customer = Customer.create(CustomerType.ORGANIZATION, "Acme Corp", "contact@acme.com", "456 Market St", null, "{\"gstin\":\"22AAAAA0000A1Z5\"}", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        assertNotNull(customer);
    }

    @Test
    void individualUpdateIndividualInfo() {
        Customer customer = Customer.create(CustomerType.INDIVIDUAL, "John Doe", "john@example.com", "123 Main St", "{\"pan\":\"ABCDE1234F\"}", null, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        customer.update("John Doe", "john@example.com", "123 Main St", "{\"pan\":\"NEWPAN1234F\"}", null);
        assertEquals("{\"pan\":\"NEWPAN1234F\"}", customer.getIndividualInfo());
        assertNull(customer.getBusinessInfo());
    }

    @Test
    void organizationUpdateBusinessInfo() {
        Customer customer = Customer.create(CustomerType.ORGANIZATION, "Acme Corp", "contact@acme.com", "456 Market St", null, "{\"gstin\":\"22AAAAA0000A1Z5\"}", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        customer.update("Acme Corp", "contact@acme.com", "456 Market St", null, "{\"gstin\":\"NEWGSTIN123\"}");
        assertEquals("{\"gstin\":\"NEWGSTIN123\"}", customer.getBusinessInfo());
        assertNull(customer.getIndividualInfo());
    }

    @Test
    void individualUpdateIndividualInfoMissingRejected() {
        Customer customer = Customer.create(CustomerType.INDIVIDUAL, "John Doe", "john@example.com", "123 Main St", "{\"pan\":\"ABCDE1234F\"}", null, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            customer.update("John Doe", "john@example.com", "123 Main St", null, null)
        );
        assertTrue(exception.getMessage().contains("individualInfo is required"));
    }

    @Test
    void organizationUpdateBusinessInfoMissingRejected() {
        Customer customer = Customer.create(CustomerType.ORGANIZATION, "Acme Corp", "contact@acme.com", "456 Market St", null, "{\"gstin\":\"22AAAAA0000A1Z5\"}", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            customer.update("Acme Corp", "contact@acme.com", "456 Market St", null, null)
        );
        assertTrue(exception.getMessage().contains("businessInfo is required"));
    }
}
