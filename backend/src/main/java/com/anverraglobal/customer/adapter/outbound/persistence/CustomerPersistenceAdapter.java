package com.anverraglobal.customer.adapter.outbound.persistence;

import com.anverraglobal.customer.application.port.outbound.CustomerRepositoryPort;
import com.anverraglobal.customer.domain.Customer;
import com.anverraglobal.customer.domain.CustomerStatus;
import com.anverraglobal.customer.domain.CustomerType;
import com.anverraglobal.organization.contracts.dto.OrganizationScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.postgresql.util.PGobject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerPersistenceAdapter implements CustomerRepositoryPort {

    private final CustomerRepository customerRepository;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CustomerPersistenceAdapter(CustomerRepository customerRepository, NamedParameterJdbcTemplate jdbcTemplate) {
        this.customerRepository = customerRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = new CustomerEntity();
        entity.setId(customer.getId());
        entity.setCustomerType(customer.getCustomerType().name());
        entity.setName(customer.getName());
        entity.setContactInfo(customer.getContactInfo());
        entity.setAddressInfo(customer.getAddressInfo());
        entity.setStatus(customer.getStatus().name());
        entity.setIndividualInfo(createJsonPGobject(customer.getIndividualInfo()));
        entity.setBusinessInfo(createJsonPGobject(customer.getBusinessInfo()));
        entity.setDealerId(customer.getDealerId());
        entity.setBranchId(customer.getBranchId());
        entity.setAgentId(customer.getAgentId());
        entity.setCreatedAt(customer.getCreatedAt());
        entity.setUpdatedAt(customer.getUpdatedAt());
        entity.setVersion(customer.getVersion());

        CustomerEntity saved = customerRepository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public Optional<Customer> findByIdAndScope(UUID id, OrganizationScope scope) {
        StringBuilder sql = new StringBuilder("SELECT * FROM customers WHERE id = :id");
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        
        applyScope(sql, params, scope);
        
        List<CustomerEntity> results = jdbcTemplate.query(sql.toString(), params, customerRowMapper());
        return results.stream().findFirst().map(this::mapToDomain);
    }

    @Override
    public Page<Customer> listByScope(OrganizationScope scope, String name, String customerType, String status, Pageable pageable) {
        StringBuilder sql = new StringBuilder("SELECT * FROM customers WHERE 1=1");
        MapSqlParameterSource params = new MapSqlParameterSource();
        applyScope(sql, params, scope);
        
        applyFilters(sql, params, name, customerType, status);
        
        // Sorting
        if (pageable.getSort().isSorted()) {
            sql.append(" ORDER BY ");
            pageable.getSort().forEach(order -> {
                String prop = order.getProperty();
                // simple allowlist
                if (List.of("name", "created_at", "status", "customer_type").contains(prop)) {
                    sql.append(prop).append(" ").append(order.getDirection().name()).append(", ");
                }
            });
            sql.setLength(sql.length() - 2);
        } else {
            sql.append(" ORDER BY created_at DESC");
        }
        
        sql.append(" LIMIT :limit OFFSET :offset");
        params.addValue("limit", pageable.getPageSize());
        params.addValue("offset", pageable.getOffset());
        
        List<CustomerEntity> entities = jdbcTemplate.query(sql.toString(), params, customerRowMapper());
        
        // Count query
        StringBuilder countSql = new StringBuilder("SELECT count(*) FROM customers WHERE 1=1");
        applyScope(countSql, params, scope);
        applyFilters(countSql, params, name, customerType, status);
        
        Long total = jdbcTemplate.queryForObject(countSql.toString(), params, Long.class);
        
        List<Customer> domainList = entities.stream().map(this::mapToDomain).toList();
        return new org.springframework.data.domain.PageImpl<>(domainList, pageable, total != null ? total : 0);
    }
    
    private void applyFilters(StringBuilder sql, MapSqlParameterSource params, String name, String customerType, String status) {
        if (name != null && !name.isBlank()) {
            sql.append(" AND name ILIKE :name");
            params.addValue("name", "%" + name + "%");
        }
        if (customerType != null && !customerType.isBlank()) {
            sql.append(" AND customer_type = :customerType");
            params.addValue("customerType", customerType);
        }
        if (status != null && !status.isBlank()) {
            sql.append(" AND status = :status");
            params.addValue("status", status);
        }
    }

    private void applyScope(StringBuilder sql, MapSqlParameterSource params, OrganizationScope scope) {
        if (scope.isGlobalAdmin()) {
            return;
        } else if (!scope.allowedAgentIds().isEmpty()) {
            sql.append(" AND agent_id IN (:allowedAgentIds)");
            params.addValue("allowedAgentIds", scope.allowedAgentIds());
        } else if (!scope.allowedBranchIds().isEmpty()) {
            sql.append(" AND branch_id IN (:allowedBranchIds)");
            params.addValue("allowedBranchIds", scope.allowedBranchIds());
        } else if (!scope.allowedCustomerIds().isEmpty()) {
            sql.append(" AND id IN (:allowedCustomerIds)");
            params.addValue("allowedCustomerIds", scope.allowedCustomerIds());
        } else {
            // Failsafe empty set if unknown role
            sql.append(" AND 1=0");
        }
    }

    private PGobject createJsonPGobject(String value) {
        if (value == null) return null;
        try {
            PGobject pgObject = new PGobject();
            pgObject.setType("jsonb");
            pgObject.setValue(value);
            return pgObject;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to convert JSON string to PGobject", e);
        }
    }

    private RowMapper<CustomerEntity> customerRowMapper() {
        return (rs, rowNum) -> {
            CustomerEntity entity = new CustomerEntity();
            entity.setId(rs.getObject("id", UUID.class));
            entity.setCustomerType(rs.getString("customer_type"));
            entity.setName(rs.getString("name"));
            entity.setContactInfo(rs.getString("contact_info"));
            entity.setAddressInfo(rs.getString("address_info"));
            entity.setStatus(rs.getString("status"));
            
            Object indInfo = rs.getObject("individual_info");
            if (indInfo instanceof PGobject) {
                entity.setIndividualInfo((PGobject) indInfo);
            }
            Object busInfo = rs.getObject("business_info");
            if (busInfo instanceof PGobject) {
                entity.setBusinessInfo((PGobject) busInfo);
            }
            
            entity.setDealerId(rs.getObject("dealer_id", UUID.class));
            entity.setBranchId(rs.getObject("branch_id", UUID.class));
            entity.setAgentId(rs.getObject("agent_id", UUID.class));
            entity.setCreatedAt(rs.getTimestamp("created_at").toInstant());
            entity.setUpdatedAt(rs.getTimestamp("updated_at").toInstant());
            entity.setVersion(rs.getLong("version"));
            return entity;
        };
    }

    private Customer mapToDomain(CustomerEntity entity) {
        return new Customer(
                entity.getId(),
                CustomerType.valueOf(entity.getCustomerType()),
                entity.getName(),
                entity.getContactInfo(),
                entity.getAddressInfo(),
                CustomerStatus.valueOf(entity.getStatus()),
                entity.getIndividualInfo() != null ? entity.getIndividualInfo().getValue() : null,
                entity.getBusinessInfo() != null ? entity.getBusinessInfo().getValue() : null,
                entity.getDealerId(),
                entity.getBranchId(),
                entity.getAgentId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getVersion()
        );
    }
}
