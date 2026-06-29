package com.bazarchinita.backend.configuracionpagos.entity;

import com.bazarchinita.backend.metodospago.entity.MetodoPago;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "configuracion_pagos")
public class ConfiguracionPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_configuracion_pago")
    private Integer idConfiguracionPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_metodo_pago", nullable = false)
    private MetodoPago metodoPago;

    @Column(name = "nombre_cuenta", length = 100)
    private String nombreCuenta;

    @Column(name = "entidad", length = 100)
    private String entidad;

    @Column(name = "numero_cuenta", length = 50)
    private String numeroCuenta;

    @Column(name = "url_qr", length = 255)
    private String urlQr;

    @Column(name = "instrucciones", length = 250)
    private String instrucciones;

    @Column(name = "estado")
    private Boolean estado = true;

    public ConfiguracionPago() {
    }

    public Integer getIdConfiguracionPago() {
        return idConfiguracionPago;
    }

    public void setIdConfiguracionPago(Integer idConfiguracionPago) {
        this.idConfiguracionPago = idConfiguracionPago;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getUrlQr() {
        return urlQr;
    }

    public void setUrlQr(String urlQr) {
        this.urlQr = urlQr;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}