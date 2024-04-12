package com.juansa.msvcintervinientes.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "intervinientes")
public class Intervinientes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
