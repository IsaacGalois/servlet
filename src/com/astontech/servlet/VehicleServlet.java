package com.astontech.servlet;

import com.astontech.bo.Vehicle;
import com.astontech.bo.VehicleMake;
import com.astontech.bo.VehicleModel;
import com.astontech.dao.VehicleDAO;
import com.astontech.dao.VehicleMakeDAO;
import com.astontech.dao.VehicleModelDAO;
import com.astontech.dao.mysql.VehicleDAOImpl;
import com.astontech.dao.mysql.VehicleMakeDAOImpl;
import com.astontech.dao.mysql.VehicleModelDAOImpl;
import common.helpers.ServletHelper;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class VehicleServlet extends javax.servlet.http.HttpServlet {

    final static Logger logger = Logger.getLogger(VehicleServlet.class);
    private static VehicleDAO vehicleDAO = new VehicleDAOImpl();
    private static VehicleMakeDAO vehicleMakeDAO = new VehicleMakeDAOImpl();
    private static VehicleModelDAO vehicleModelDAO = new VehicleModelDAOImpl();

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

//        ServletHelper.logRequestParams(request);

        switch(request.getParameter("formName")) {
            case "addVehicle":
                addVehicle(request);
                break;

            case "executeVehicle":
                if(request.getParameter("update") != null)
                    updateVehicle(request);
                else if(request.getParameter("delete") != null)
                    deleteVehicle(request);
                else
                    logger.error("Execute without update or delete");
                break;

            default:
                break;
        }

        //notes: generate dropdown using JSTL (logic is same between forms)
        request.setAttribute("vehicleMakeList",vehicleMakeDAO.getVehicleMakeList());
        request.setAttribute("vehicleModelList",vehicleModelDAO.getVehicleModelList());
        request.setAttribute("vehicleList",vehicleDAO.getVehicleList());
        request.getRequestDispatcher("./vehicle.jsp").forward(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        request.setAttribute("vehicleMakeList",vehicleMakeDAO.getVehicleMakeList());
        request.setAttribute("vehicleModelList",vehicleModelDAO.getVehicleModelList());
        request.setAttribute("vehicleList",vehicleDAO.getVehicleList());
        request.getRequestDispatcher("./vehicle.jsp").forward(request,response);
    }

    private static void addVehicle(HttpServletRequest request) {
        logger.info("Form #1 - Form Name=" + request.getParameter("formName"));

        Vehicle vehicle = requestToNewVehicle(request);
        logger.info("addVehicle: new Vehicle is: "+vehicle.toString());
        vehicleDAO.insertVehicle(vehicle);
    }

    private static void updateVehicle(HttpServletRequest request) {

        if (Integer.parseInt(request.getParameter("formId")) == Integer.parseInt(request.getParameter("vehicleId"))) {
            int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
            int updatedVehicleModelId;

//            ServletHelper.logRequestParams(request);

            Vehicle updatedVehicle = vehicleDAO.getVehicleById(vehicleId);

//            logger.error(updatedVehicle.toString());

            //find dirty fields
            if(!request.getParameter("licensePlate").isEmpty())
                updatedVehicle.setLicensePlate(request.getParameter("licensePlate"));
            if(!request.getParameter("vin").isEmpty())
                updatedVehicle.setVin(request.getParameter("vin"));
            if(!request.getParameter("year").isEmpty())
                updatedVehicle.setYear(Integer.parseInt(request.getParameter("year")));
            if(!request.getParameter("color").isEmpty())
                updatedVehicle.setColor(request.getParameter("color"));
            if(!request.getParameter("addVehicleModelSelection2").equals("0")) {
                updatedVehicleModelId = Integer.parseInt(request.getParameter("addVehicleModelSelection2"));
                updatedVehicle.setVehicleModel(vehicleModelDAO.getVehicleModelById(updatedVehicleModelId));
            }

//            logger.error(updatedVehicle.toString());

            if (vehicleDAO.updateVehicle(updatedVehicle))
                request.setAttribute("updateSuccessful", "Vehicle Updated in Database Successfully!");
            else
                request.setAttribute("updateSuccessful", "Vehicle Update FAILED!");
        }
    }

    public static void deleteVehicle(HttpServletRequest request) {
        if (Integer.parseInt(request.getParameter("formId")) == Integer.parseInt(request.getParameter("vehicleId"))) {
            logger.info("Deleted Vehicle: "+vehicleDAO.deleteVehicle(Integer.parseInt(request.getParameter("vehicleId"))));
        }
    }

    private static Vehicle requestToNewVehicle(HttpServletRequest request) {
//        ServletHelper.logRequestParams(request);

        //notes: everything comes back from the request as a string
        String vehicleMakeId = request.getParameter("addVehicleMakeSelection1");
        String vehicleModelId = request.getParameter("addVehicleModelSelection1");

        int year = Integer.parseInt(request.getParameter("year"));
        String licensePlate = request.getParameter("licensePlate");
        String vin = request.getParameter("vin");
        String color = request.getParameter("color");

        VehicleMake vehicleMake = vehicleMakeDAO.getVehicleMakeById(Integer.parseInt(vehicleMakeId));
        VehicleModel vehicleModel = vehicleModelDAO.getVehicleModelById(Integer.parseInt(vehicleModelId));

        logger.info("Selected VehicleMake detail: "+vehicleMake.toString());
        logger.info("Selected VehicleModel detail: "+vehicleModel.toString());

        return new Vehicle(year, licensePlate, vin, color, vehicleModel);
    }
}
