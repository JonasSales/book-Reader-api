package br.com.booksaas.book_reader.entities.order.entity;

public enum OrderStatus {
    SENT,
    PAID,
    PARTIALLY_PAID,
    LATE,
    ABANDONED,
    CANCELED,
    CHARGEBACK,
    PRE_AUTHORIZED,
    DISPUTE,
    DISPUTE_ALERT
}
