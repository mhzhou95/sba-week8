package com.github.perscholas.service;

import com.github.perscholas.DatabaseConnection;
import com.github.perscholas.dao.StudentDao;
import com.github.perscholas.model.CourseInterface;
import com.github.perscholas.model.Student;
import com.github.perscholas.model.StudentInterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// TODO - Implement respective DAO interface
public class StudentService implements StudentDao {

    private final DatabaseConnection dbc;

    public StudentService(DatabaseConnection dbc) {
        this.dbc = dbc;
    }

    public StudentService() {
        this(DatabaseConnection.MARIADB);
    }

    @Override
    public List<StudentInterface> getAllStudents() throws SQLException {
        ResultSet result = dbc.executeQuery("SELECT * FROM `students`");
        List<StudentInterface> list = new ArrayList<>();
        try {
            while (result.next()) {
                String studentEmail = result.getString("email");
                String name = result.getString("name");
                String password = result.getString("password");
                StudentInterface student = new Student(studentEmail, name, password);
                list.add(student);
            }
        } catch(SQLException se) {
            throw new Error(se);
        }
        return list;
    }

    @Override
    public StudentInterface getStudentByEmail(String studentEmail) throws SQLException {
        ResultSet result = dbc.executeQuery("SELECT * FROM `students`"
                + " WHERE `email` = '" + studentEmail + "';");
        while(result.next() ){
            String email = result.getString("email");
            String name = result.getString("name");
            String password = result.getString("password");
            StudentInterface student = new Student(email, name, password);
            return student;
        }
        return null;
    }

    @Override
    public Boolean validateStudent(String studentEmail, String password) throws SQLException {
        StudentInterface student = getStudentByEmail(studentEmail);
        if( student !=null && student.getPassword().equals(password)){
            return true;
        }else return false;
    }

    @Override
    public void registerStudentToCourse(String studentEmail, int courseId) throws SQLException {
        String studentEmailValue = "'" + studentEmail + "'";
        String courseIdValue = "'" + courseId + "'";
        dbc.executeQuery("insert into `studentCourses`(`courseId`, `studentEmail`) values( "
                + courseIdValue + ", " + studentEmailValue + ")"
                );
    }

    @Override
    public List<CourseInterface> getStudentCourses(String studentEmail) throws SQLException {
        String studentEmailValue = "'" + studentEmail + "'";
        List<Integer> studentCourses = new ArrayList<>();
        ResultSet result = dbc.executeQuery("SELECT * FROM `studentCourses`" +
                "WHERE `studentEmail`=" + studentEmailValue
                );
        while( result.next()){
            studentCourses.add(result.getInt("courseId"));
        }
        CourseService courseService = new CourseService();
        List<CourseInterface> registeredCourses = courseService.getAllCourses().stream().filter( course -> studentCourses.contains(course.getId())).collect(Collectors.toList());
        return registeredCourses;
    }
}
