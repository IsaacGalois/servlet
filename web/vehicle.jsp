<%--
  Created by IntelliJ IDEA.
  User: alexsperry
  Date: 10/4/17
  Time: 11:56 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c1" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link rel="stylesheet" href="./static/css/site.css" />
    <script src="./jquery.js"></script>

    <title>Vehicle Management</title>
</head>
<body>
    Form #1:
    Add Vehicle:
    <div class="border">
        <div id="formline">
            <form name="addVehicle" action="./vehicle" method ="post">
                <input type="hidden" name="formName" value="addVehicle" />

                <div>
                    <select name="addVehicleMakeSelection1" class="add-selector-1 make-selector" id="cascade-make-selector">
                        <option value='0'>-- Select Make --</option>

                        <c:forEach var="vehicleMake" items="${vehicleMakeList}">
                            <c:choose>
                                <c:when test="${vehicleMake.vehicleMakeId == vehicleMakeId}">
                                    <option selected value="${vehicleMake.vehicleMakeId}">
                                            ${vehicleMake.vehicleMakeName}
                                    </option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${vehicleMake.vehicleMakeId}">
                                            ${vehicleMake.vehicleMakeName}
                                    </option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <select name="addVehicleModelSelection1" class="add-selector-1 model-selector 1" id="cascading-model-selector">
                        <option value='0'>-- Select Model --</option>

                        <c:forEach var="vehicleModel" items="${vehicleModelList}">
                            <c:choose>
                                <c:when test="${vehicleModel.vehicleModelId == vehicleModelId}">
                                    <option selected value="${vehicleModel.vehicleModelId}" class="${vehicleModel.vehicleMake.vehicleMakeId} cascade" id="model${vehicleModel.vehicleModelId}">
                                            ${vehicleModel.vehicleModelName}
                                    </option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${vehicleModel.vehicleModelId}" class="${vehicleModel.vehicleMake.vehicleMakeId} cascade" id="model${vehicleModel.vehicleModelId}">
                                            ${vehicleModel.vehicleModelName}
                                    </option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>

                <%--name="addVehiclePlateSelection"--%>

                <div><span class="add-lbl">Plate:</span><input type="text" name="licensePlate" value="" placeholder="License Plate" class="add-input"/></div>
                <div><span class="add-lbl">VIN:</span><input type="text" name="vin" value="" placeholder="VIN" class="add-input"/></div>
                <div><span class="add-lbl">Year:</span><input type="number" name="year" value="" placeholder="Year" class="add-input"/></div>
                <div><span class="add-lbl">Color:</span><input type="text" name="color" value="" placeholder="Color" class="add-input"/></div>

                <div><button type="submit" class="add-btn">Add Vehicle</button></div>

            </form>
        </div>
    </div>

    Form Set:
    Update & Delete Vehicle
    <div class="formset" id="vForms">

        <h1>
            <span class="lbl">Plate</span><span class="lbl">VIN</span><span class="lbl">Year</span><span class="lbl">Color</span><span class="lbl">Make</span><span class="lbl">Model</span>
        </h1>

        <hr class="break">

        <c1:forEach var="vehicle" items="${vehicleList}">
            <form name="executeVehicle" action="./vehicle" method="post">
                <input type="hidden" name="formName" value="executeVehicle" />
                <input type="hidden" name="formId" value="${vehicle.vehicleId}" />
                <input type="hidden" name="vehicleId" value="${vehicle.vehicleId}" />


                <input type="text" name="licensePlate" value="${plate}" placeholder="${vehicle.licensePlate}" />
                <input type="text" name="vin" value="${vin}" placeholder="${vehicle.vin}" />
                <input type="number" name="year" value="${year}" placeholder="${vehicle.year}" />
                <input type="text" name="color" value="${color}" placeholder="${vehicle.color}" />

                <select name="addVehicleMakeSelection2" class="add-selector-2">
                    <option value='0'>-- Select Make --</option>
                    <c:forEach var="vehicleMake" items="${vehicleMakeList}">
                        <c:choose>
                            <c:when test="${vehicleMake.vehicleMakeId == vehicle.vehicleModel.vehicleMake.vehicleMakeId}">
                                <option selected value="${vehicleMake.vehicleMakeId}">
                                        ${vehicleMake.vehicleMakeName}
                                </option>
                            </c:when>
                            <c:otherwise>
                                <option value="${vehicleMake.vehicleMakeId}">
                                        ${vehicleMake.vehicleMakeName}
                                </option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>

                <select name="addVehicleModelSelection2" class="add-selector-2">
                    <option value='0'>-- Select Model --</option>

                    <c:forEach var="vehicleModel" items="${vehicleModelList}">
                        <c:choose>
                            <c:when test="${vehicleModel.vehicleModelId == vehicle.vehicleModel.vehicleModelId}">
                                <option selected value="${vehicleModel.vehicleModelId}">
                                        ${vehicleModel.vehicleModelName}
                                </option>
                            </c:when>
                            <c:otherwise>
                                <option value="${vehicleModel.vehicleModelId}">
                                        ${vehicleModel.vehicleModelName}
                                </option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>

                <button type="submit" name ="update" value="upd">Update</button>
                <button type="submit" name ="delete" value="del">Delete</button>
            </form>
        </c1:forEach>
    </div>
<script type="text/javascript">
//    window.onload = function() {
//        var selection = document.getElementsByClassName('make-selector')[0];
//        var selectedValue = selection.options[selection.selectedIndex];
//
//        for (var i in document.getElementsByClassName('model-selector').options) {
//            if (!i.hasClass(selectedValue)) {
//                this.hide();
//            }
//        };
//    }

    $(document).ready(function() {
        $('#cascade-make-selector').on('change', function() {
            $('#cascading-model-selector').children('option').show();
            var selection = $('#cascade-make-selector option:selected').val();
            console.log("selection: "+selection);
            $('.cascade').each(function() {
               if(!$(this).hasClass(selection)) {
//                   console.log("selection: "+selection);
//                   console.log("Pass?: "+!$(this).hasClass(selection)+" Name: "+$(this).text()+", class: "+$(this).attr('class').split(/\s+/));
                   $(this).hide();
               }
            });
        });
    });
</script>

</body>
</html>
