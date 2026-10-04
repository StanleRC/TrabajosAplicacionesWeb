package mx.edu.utez.proyecto1D.service;

import mx.edu.utez.proyecto1D.controller.dto.RequestEnvioDTO;
import mx.edu.utez.proyecto1D.controller.dto.ResponseEnvioDTO;
import mx.edu.utez.proyecto1D.controller.dto.RequestRentaDTO;
import mx.edu.utez.proyecto1D.controller.dto.ResponseRentaDTO;
import mx.edu.utez.proyecto1D.controller.dto.RequestHospedajeDTO;
import mx.edu.utez.proyecto1D.controller.dto.ResponseHospedajeDTO;
import mx.edu.utez.proyecto1D.exception.customExceptions.CustomBadRequestException;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    //Calcular costo de envío
    public ResponseEnvioDTO calcularCosto(RequestEnvioDTO payload) {
        if (payload.getPesoKg() > 50) {
            throw new CustomBadRequestException("No se aceptan paquetes que pesen más de 50 kg.");
        }
        if (payload.getLargoCm() > 150 || payload.getAnchoCm() > 150 || payload.getAltoCm() > 150) {
            throw new CustomBadRequestException("No se aceptan paquetes con alguna dimensión superior a 150 cm.");
        }

        double volumen = payload.getLargoCm() * payload.getAnchoCm() * payload.getAltoCm();
        if (volumen > 1000000) {
            throw new CustomBadRequestException("No se aceptan paquetes con un volumen superior a 1,000,000 cm3.");
        }


        double costoAcumulado = 80.0;

        costoAcumulado += (payload.getPesoKg() * 12.0);

        if (volumen > 50000) {
            costoAcumulado += 100.0;
        }

        if ("EXPRESS".equals(payload.getTipoEnvio())) {
            costoAcumulado *= 1.40;
        } else if ("MISMO_DIA".equals(payload.getTipoEnvio())) {
            costoAcumulado *= 1.70;
        }

        if (payload.getValorDeclarado() > 10000) {
            double costoSeguro = payload.getValorDeclarado() * 0.02;
            costoAcumulado += costoSeguro;
        }

        return new ResponseEnvioDTO(costoAcumulado);
    }

    //-------------------------------------------------------------------------------
    //Calcular renta
    public ResponseRentaDTO calcularRenta(RequestRentaDTO payload) {
        if (payload.getEdadConductor() < 18) {
            throw new CustomBadRequestException("El conductor es menor de 18 años.");
        }
        if (payload.getDiasRenta() > 30) {
            throw new CustomBadRequestException("La renta supera los 30 días.");
        }
        if (payload.getKilometrosEstimados() > 5000) {
            throw new CustomBadRequestException("Los kilómetros estimados superan los 5,000.");
        }
        if ("CAMIONETA".equals(payload.getTipoVehiculo()) && payload.getEdadConductor() < 25) {
            throw new CustomBadRequestException("No se permite rentar una CAMIONETA a conductores menores de 25 años.");
        }

        double costoDiario = 0;
        switch (payload.getTipoVehiculo()) {
            case "COMPACTO":
                costoDiario = 550.0;
                break;
            case "SEDAN":
                costoDiario = 700.0;
                break;
            case "SUV": costoDiario = 950.0;
                break;
            case "CAMIONETA": costoDiario = 1200.0;
                break;
        }

        double costoRenta = costoDiario * payload.getDiasRenta();

        int kilometrosIncluidos = payload.getDiasRenta() * 100;
        double costoKilometrosAdicionales = 0;
        if (payload.getKilometrosEstimados() > kilometrosIncluidos) {
            int kmsExtra = payload.getKilometrosEstimados() - kilometrosIncluidos;
            costoKilometrosAdicionales = kmsExtra * 4.0;
        }

        double cargoJoven = 0;
        if (payload.getEdadConductor() >= 18 && payload.getEdadConductor() <= 24) {
            cargoJoven = (costoRenta + costoKilometrosAdicionales) * 0.15;
        }

        double costoSeguro = 0;
        if (payload.getSeguroCompleto()) {
            costoSeguro = 180.0 * payload.getDiasRenta();
        }

        double descuento = 0;
        if (payload.getDiasRenta() >= 7) {
            descuento = costoRenta * 0.10;
        }

        double importeTotal = (costoRenta - descuento) + costoKilometrosAdicionales + cargoJoven + costoSeguro;
        return new ResponseRentaDTO(importeTotal);
    }

    //-------------------------------------------------------------------------------
    //Hospedaje
    public ResponseHospedajeDTO calcularHospedaje(RequestHospedajeDTO payload) {

        if (payload.getNumeroNoches() > 30) {
            throw new CustomBadRequestException("El número de noches supera el límite de 30 días.");
        }

        if ("INDIVIDUAL".equals(payload.getTipoHabitacion()) && payload.getNumeroHuespedes() > 1) {
            throw new CustomBadRequestException("Se seleccionó una habitación individual para más de una persona.");
        }
        if ("DOBLE".equals(payload.getTipoHabitacion()) && payload.getNumeroHuespedes() > 2) {
            throw new CustomBadRequestException("Se seleccionó una habitación doble para más de dos personas.");
        }
        if ("SUITE".equals(payload.getTipoHabitacion()) && payload.getNumeroHuespedes() > 4) {
            throw new CustomBadRequestException("Se seleccionó una suite para más de cuatro personas.");
        }

        double costoPorNoche = 0;
        switch (payload.getTipoHabitacion()) {
            case "INDIVIDUAL":
                costoPorNoche = 700.0;
                break;
            case "DOBLE":
                costoPorNoche = 1100.0;
                break;
            case "SUITE":
                costoPorNoche = 1800.0;
                break;
        }

        double costoHospedaje = costoPorNoche * payload.getNumeroNoches();
        if ("BAJA".equals(payload.getTemporada())) {
            costoHospedaje -= (costoHospedaje * 0.10);
        } else if ("ALTA".equals(payload.getTemporada())) {
            costoHospedaje += (costoHospedaje * 0.25);
        }

        if (payload.getNumeroNoches() >= 7) {
            costoHospedaje -= (costoHospedaje * 0.08);
        }

        double costoDesayuno = 0;
        if (payload.getIncluyeDesayuno()) {
            costoDesayuno = payload.getNumeroHuespedes() * payload.getNumeroNoches() * 150.0;
        }

        double costoEstacionamiento = 0;
        if (payload.getIncluyeEstacionamiento()) {
            costoEstacionamiento = payload.getNumeroNoches() * 100.0;
        }

        double subtotal = costoHospedaje + costoDesayuno + costoEstacionamiento;
        double impuesto = subtotal * 0.04;
        double costoTotal = subtotal + impuesto;

        return new ResponseHospedajeDTO(costoTotal);
    }
}