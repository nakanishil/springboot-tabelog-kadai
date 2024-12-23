package com.example.nagoyameshi.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Table(name = "reservations")
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_reservations_users"))
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false, foreignKey = @ForeignKey(name = "fk_reservations_restaurants"))
    private Restaurant restaurant;
    
    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate; 
    
    @Column(name = "num_of_people", nullable = false)
    private Integer numOfPeople;
    
    @Column(name = "amount")
    private Integer amount;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
