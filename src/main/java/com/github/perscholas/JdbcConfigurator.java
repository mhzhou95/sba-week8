package com.github.perscholas;

import com.github.perscholas.utils.DirectoryReference;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JdbcConfigurator {
    public static void initialize() throws SQLException, ClassNotFoundException, IOException {
        registerJdbcDriver();
        useDatabaseSchema();
//        createDatabase();
//        useDatabase();
//        createStudentTable();
//        createCourseTable();
    }

    private static void registerJdbcDriver() throws ClassNotFoundException {
        // Attempt to register JDBC Driver
        Class.forName("org.mariadb.jdbc.Driver");
    }

    private static void useDatabaseSchema() throws SQLException, IOException {
        for( String sqlStatement : buildDatabaseSchema()){
            DatabaseConnection.MARIADB.executeQuery(sqlStatement);
        }
    }

    private static List<String> buildDatabaseSchema() throws IOException {
        String dataBaseName = "SBA_week8_management_system";
        List<String> schemaList = new ArrayList<>();
        schemaList.add("DROP DATABASE IF EXISTS " + dataBaseName);
        schemaList.add(createDatabase(dataBaseName));
        schemaList.add(useDatabase());
        schemaList.add(createStudentsTable());
        schemaList.add(createCoursesTable());
        schemaList.add(createStudentCourseTable());
        String[] students = populateStudentsTable().split(";");
        for (String student : students){
            schemaList.add(student);
        }

        String[] courses = populateCoursesTable().split(";");
        for( String course : courses) {
            schemaList.add(course);
        }

        return schemaList;
    }


    private static String createDatabase(String dataBaseName) throws FileNotFoundException {
        StringBuilder createDatabase = new StringBuilder()
                .append("CREATE DATABASE " + dataBaseName);
        return createDatabase.toString();
    }

    private static String useDatabase() {
        StringBuilder useDatabase = new StringBuilder()
                .append("use SBA_week8_management_system;");
        return useDatabase.toString();
    }

    private static String createStudentsTable() throws IOException {
        File creationStatementFile = DirectoryReference.RESOURCE_DIRECTORY.getFileFromDirectory("students.create-table.sql");
        BufferedReader br = new BufferedReader(new java.io.FileReader(creationStatementFile));
        String creationStatement = br.lines().collect(Collectors.joining());
        return creationStatement;
    }

    private static String createCoursesTable() throws FileNotFoundException {
        File creationStatementFile = DirectoryReference.RESOURCE_DIRECTORY.getFileFromDirectory("courses.create-table.sql");
        BufferedReader br = new BufferedReader(new java.io.FileReader(creationStatementFile));
        String creationStatement = br.lines().collect(Collectors.joining());
        return creationStatement;
    }

    private static String createStudentCourseTable() throws FileNotFoundException {
        File creationStatementFile = DirectoryReference.RESOURCE_DIRECTORY.getFileFromDirectory("studentCourses.create-table.sql");
        BufferedReader br = new BufferedReader(new java.io.FileReader(creationStatementFile));
        String creationStatement = br.lines().collect(Collectors.joining());
        return creationStatement;
    }

    private static String populateStudentsTable() throws FileNotFoundException {
        File creationStatementFile = DirectoryReference.RESOURCE_DIRECTORY.getFileFromDirectory("students.populate-table.sql");
        BufferedReader br = new BufferedReader(new java.io.FileReader(creationStatementFile));
        String creationStatement = br.lines().collect(Collectors.joining());
        return creationStatement;
    }

    private static String populateCoursesTable() throws FileNotFoundException {
        File creationStatementFile = DirectoryReference.RESOURCE_DIRECTORY.getFileFromDirectory("courses.populate-table.sql");
        BufferedReader br = new BufferedReader(new java.io.FileReader(creationStatementFile));
        String creationStatement = br.lines().collect(Collectors.joining());
        return creationStatement;
    }
}
