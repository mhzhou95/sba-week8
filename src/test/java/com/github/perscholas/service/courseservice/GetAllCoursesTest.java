package com.github.perscholas.service.courseservice;

import com.github.perscholas.JdbcConfigurator;
import com.github.perscholas.model.Course;
import com.github.perscholas.model.CourseInterface;
import com.github.perscholas.service.CourseService;
import com.github.perscholas.utils.DirectoryReference;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author leonhunter
 * @created 02/12/2020 - 8:26 PM
 */
public class GetAllCoursesTest {
    @Before // TODO (OPTIONAL) - Use files to execute SQL commands
    public void setup() {
        DirectoryReference directoryReference = DirectoryReference.RESOURCE_DIRECTORY;
        File coursesSchemaFile = directoryReference.getFileFromDirectory("courses.create-table.sql");
        File studentsSchemaFile = directoryReference.getFileFromDirectory("students.create-table.sql");
        File coursesPopulatorFile = directoryReference.getFileFromDirectory("courses.populate-table.sql");
        File studentsPopulatorFile = directoryReference.getFileFromDirectory("students.populate-table.sql");
        File[] filesToExecute = new File[]{
                coursesSchemaFile,
                studentsSchemaFile,
                coursesPopulatorFile,
                studentsPopulatorFile
        };
    }

    @Test
    // given
    public void test() throws SQLException, ClassNotFoundException {
        JdbcConfigurator.initialize();

        // when
        // TODO - define `when` clause
        CourseService courseService = new CourseService();
        List<CourseInterface> list = courseService.getAllCourses();

        Course courseToCheck = new Course(1, "English", "Anderea Scamaden");
        // then
        // TODO - define `then` clause
        Course courseInDatabase = new Course(list.get(0).getId(), list.get(0).getName(), list.get(0).getInstructor());

        Assert.assertEquals("Course Id", courseToCheck.getId(), courseInDatabase.getId());
        Assert.assertEquals("Course Name", courseToCheck.getName(), courseInDatabase.getName());
        Assert.assertEquals("Instructor", courseToCheck.getInstructor(), courseInDatabase.getInstructor());
    }
}
