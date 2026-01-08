package com.tingeso.loan.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    public enum LoanStatus {
        ACTIVE,
        RETURNED,
        UNPAID_DEBT,
        TOOL_BROKE
    }

    // atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime returnDateExpected;

    private LocalDateTime realReturnDate;

    private int quantity;

    private int price;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime deliveryDate;

    @PrePersist
    protected void onCreate() {
        this.deliveryDate = LocalDateTime.now();
    }

    @Column(nullable = false)
    private String userRut;

    private Boolean delay = false;

    // relaciones (Decoupled: Using IDs instead of Objects)
    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "tool_id", nullable = false)
    private Long toolId;

    @Transient
    private String clientName;

    @Transient
    private String clientRut; // To hold the formatted RUT or similar if needed, or just use userRut

    @Transient
    private String toolName;

}
