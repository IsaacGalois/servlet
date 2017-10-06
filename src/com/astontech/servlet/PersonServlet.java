package com.astontech.servlet;

import com.astontech.bo.Person;
import com.astontech.dao.PersonDAO;
import com.astontech.dao.mysql.PersonDAOImpl;
import common.helpers.ServletHelper;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class PersonServlet extends javax.servlet.http.HttpServlet {

    final static Logger logger = Logger.getLogger(PersonServlet.class);
    private static PersonDAO personDAO = new PersonDAOImpl();

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        switch(request.getParameter("formName")) {
            case "choosePerson":
                choosePerson(request);
                break;

            case "updatePerson":
                updatePerson(request);
                break;

            default:
                break;
        }

        //notes: generate person dropdown using JSTL (logic is same between forms)
        request.setAttribute("personList",personDAO.getPersonList());
        request.getRequestDispatcher("./person.jsp").forward(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        request.setAttribute("personList",personDAO.getPersonList());
        request.setAttribute("selectPerson",generatePersonDropDownHtml(0));
        request.getRequestDispatcher("./person.jsp").forward(request,response);

    }

    private static void choosePerson(HttpServletRequest request) {
        logger.info("Form #1 - Form Name=" + request.getParameter("formName"));
        ServletHelper.logRequestParams(request);

        //notes: everything comes back from the request as a string
        String personId = request.getParameter("selectPerson");

        Person person = personDAO.getPersonById(Integer.parseInt(personId));

        logger.info("Selected Person detail: "+person.toString());

        personToRequest(request, person);

        request.setAttribute("selectPerson",generatePersonDropDownHtml(person.getPersonId()));
    }

    private static void updatePerson(HttpServletRequest request) {
        logger.info("Form #2 - Form Name=" + request.getParameter("formName"));
        ServletHelper.logRequestParams(request);

        Person updatedPerson = new Person();
        requestToPerson(request, updatedPerson);

        logger.info(updatedPerson.toString());

        if(personDAO.updatePerson(updatedPerson))
            request.setAttribute("updateSuccessful","Person Updated in Database Successfully!");
        else
            request.setAttribute("updateSuccessful","Person Update FAILED!");

        //notes:   inefficient!! extra call to the database.
        updatedPerson = personDAO.getPersonById(updatedPerson.getPersonId());
        personToRequest(request, updatedPerson);

        String personIdStr = request.getParameter("personId");
        request.setAttribute("selectPerson",generatePersonDropDownHtml(Integer.parseInt(personIdStr)));

    }

    private static String generatePersonDropDownHtml(int selectedPersonId) {
//        <select name="selectPerson">
//            <option value='0'>(Select Person)</option>
//            <option value="1">Dan</option>
//            <option value="2">James</option>
//            <option value="3">Adrian</option>
//            <option value="4">Sean</option>
//        </select>

        StringBuilder strBld = new StringBuilder();
        strBld.append("<select name='selectPerson'>");
        strBld.append("<option value='0'>(Select Person)</option>");

        for(Person person : personDAO.getPersonList()) {
            if(person.getPersonId() != selectedPersonId)
                strBld.append("<option value='");
            else
                strBld.append("<option selected value='");
            strBld.append(person.getPersonId()).append("'>")
                    .append(person.GetFullName()).append("</option>");
        }

        strBld.append("</select>");

        return strBld.toString();
    }

    private static void personToRequest(HttpServletRequest request, Person person) {

        //notes: persist values
        request.setAttribute("personId", person.getPersonId());
        request.setAttribute("title", person.getTitle());
        request.setAttribute("firstName",person.getFirstName());
        request.setAttribute("lastName",person.getLastName());
        request.setAttribute("displayFirstName",person.getDisplayFirstName());
        request.setAttribute("gender",person.getGender());
    }

    private static void requestToPerson(HttpServletRequest request, Person person) {

        person.setPersonId(Integer.parseInt(request.getParameter("personId")));
        person.setTitle(request.getParameter("title"));
        person.setFirstName(request.getParameter("firstName"));
        person.setLastName(request.getParameter("lastName"));
        person.setDisplayFirstName(request.getParameter("displayFirstName"));
        person.setGender(request.getParameter("gender"));
    }

}
