package com.example.inmobiliarialab3.model;

public class Pago {
    private int idPago;
    private double monto;
    private String detalle;
    private boolean estado;
    private int idContrato;
    private Contrato contrato;
    private String fechaPago;

    public Pago() {
    }

    public Pago(int idPago, double monto, String detalle, boolean estado, int idContrato, Contrato contrato, String fechaPago) {
        this.idPago = idPago;
        this.monto = monto;
        this.detalle = detalle;
        this.estado = estado;
        this.idContrato = idContrato;
        this.contrato = contrato;
        this.fechaPago = fechaPago;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }
}
