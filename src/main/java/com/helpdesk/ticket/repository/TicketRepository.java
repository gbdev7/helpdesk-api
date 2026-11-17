package com.helpdesk.ticket.repository;

import com.helpdesk.ticket.domain.Ticket;
import com.helpdesk.ticket.domain.TicketPriority;
import com.helpdesk.ticket.domain.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Técnicos e admins: todos os chamados
    @Query("""
        SELECT t FROM Ticket t 
        WHERE (:status IS NULL OR t.status = :status) 
          AND (:priority IS NULL OR t.priority = :priority)
    """)
    Page<Ticket> findByFilters(
            @Param("status") TicketStatus status,
            @Param("priority") TicketPriority priority,
            Pageable pageable
    );

    // Clientes: somente os chamados que eles abriram
    @Query("""
        SELECT t FROM Ticket t 
        WHERE t.customer.id = :customerId
          AND (:status IS NULL OR t.status = :status) 
          AND (:priority IS NULL OR t.priority = :priority)
    """)
    Page<Ticket> findByCustomerAndFilters(
            @Param("customerId") Long customerId,
            @Param("status") TicketStatus status,
            @Param("priority") TicketPriority priority,
            Pageable pageable
    );
}