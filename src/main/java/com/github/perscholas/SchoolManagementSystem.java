package com.github.perscholas;

import com.github.perscholas.dao.CourseDao;
import com.github.perscholas.dao.StudentDao;
import com.github.perscholas.model.CourseInterface;
import com.github.perscholas.service.CourseService;
import com.github.perscholas.service.StudentService;
import com.github.perscholas.utils.IOConsole;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SchoolManagementSystem implements Runnable {
    private static final IOConsole console = new IOConsole();

    @Override
    public void run() {
            Boolean run = true;
            while(run) {
                String smsDashboardInput = getSchoolManagementSystemDashboardInput();
                if ("login".equals(smsDashboardInput.trim())) {
                    StudentDao studentService = new StudentService(DatabaseConnection.MARIADB);
                    String studentEmail = console.getStringInput("Enter your email:").trim();
                    String studentPassword = console.getStringInput("Enter your password:").trim();
                    Boolean isValidLogin = null;
                    try {
                        isValidLogin = studentService.validateStudent(studentEmail, studentPassword);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (isValidLogin) {
                        String studentDashboardInput;
                        do {
                            studentDashboardInput = getStudentDashboardInput();
                            if ("register".equals(studentDashboardInput.trim())) {
                                Integer courseId = null;
                                try {
                                    courseId = getCourseRegistryInput();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    studentService.registerStudentToCourse(studentEmail, courseId);
                                } catch (SQLException e) {
                                    console.println("ERROR!!! You are already registered for that Course");
                                    continue;
                                }
                            }
                            if ("view".equals(studentDashboardInput.trim())) {
                                //logic to view all student courses
                                try {
                                    List<Integer> listOfRegisteredCourses = studentService.getStudentCourses(studentEmail);
                                    console.println(new StringBuilder()
                                            .append("You are registered for these courses \n")
                                            .append(listOfRegisteredCourses.toString())
                                            .toString());
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            if("logout".equals(studentDashboardInput.trim())){
                                run = false;
                            }
                        } while (!studentDashboardInput.equals("logout"));
                    }
                    if(!isValidLogin){
                        console.println("ERROR!!! Invalid credentials");
                    }
                }
                if("exit".equals(smsDashboardInput.trim())){
                    run = false;
                }
            }
    }

    private String getSchoolManagementSystemDashboardInput() {
        return console.getStringInput(new StringBuilder()
                .append("Welcome to the School Management System Dashboard!")
                .append("\nFrom here, you can select any of the following options:")
                .append("\n\t[ login ], [ exit ]")
                .toString());
    }

    private String getStudentDashboardInput() {
        return console.getStringInput(new StringBuilder()
                .append("Welcome to the Student Dashboard!")
                .append("\nFrom here, you can select any of the following options:")
                .append("\n\t[ register ], [ view ], [ logout ]")
                .toString());
    }


    private Integer getCourseRegistryInput() throws SQLException {
        CourseDao courseService = new CourseService();
        List<CourseInterface> courses = courseService.getAllCourses();
        List<Integer> listOfCoursesIds = new ArrayList<>();
        for (CourseInterface course : courses){
            listOfCoursesIds.add(course.getId());
        }
        return console.getIntegerInput(new StringBuilder()
                .append("Welcome to the Course Registration Dashboard!")
                .append("\nFrom here, you can select any of the following options:")
                .append("\n\t" + listOfCoursesIds.toString())
                .toString());
    }
}
