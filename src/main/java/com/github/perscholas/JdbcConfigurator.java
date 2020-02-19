package com.github.perscholas;

import com.github.perscholas.utils.DirectoryReference;
import com.github.perscholas.utils.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JdbcConfigurator {
    public static void initialize() throws SQLException, ClassNotFoundException {
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

    private static void useDatabaseSchema() throws SQLException {
        for( String sqlStatement : buildDatabaseSchema()){
            DatabaseConnection.MARIADB.executeQuery(sqlStatement);
        }
    }

    private static List<String> buildDatabaseSchema() {
        List<String> schemaList = new ArrayList<>();
        schemaList.add("DROP DATABASE IF EXISTS SBA_week8_management_system;");
        schemaList.add("create database SBA_week8_management_system;;");
        schemaList.add("use SBA_week8_management_system;");
        schemaList.add("CREATE TABLE student (" +
                "email VARCHAR(50) NOT NULL," +
                "name VARCHAR(50) NOT NULL," +
                "PASSWORD VARCHAR(50) NOT NULL," +
                "PRIMARY KEY (email));");
        schemaList.add("CREATE TABLE course (" +
                "id INT NOT NULL," +
                "NAME VARCHAR(50) NOT NULL," +
                "instructor VARCHAR(50) NOT NULL," +
                "PRIMARY KEY(id));");
        schemaList.add("CREATE TABLE studentCourses (" +
                "courseId INT NOT NULL," +
                "studentEmail VARCHAR(50) NOT NULL," +
                "PRIMARY KEY(courseId, studentEmail));"
                );
        schemaList.add("insert into Course (id, name, instructor) values (1, 'English', 'Anderea Scamaden');");
        schemaList.add("insert into Course (id, name, instructor) values (2, 'Mathematics', 'Eustace Niemetz');");
        schemaList.add("insert into Course (id, name, instructor) values (3, 'Anatomy', 'Reynolds Pastor');");
        schemaList.add("insert into Course (id, name, instructor) values (4, 'Organic Chemistry', 'Odessa Belcher');");
        schemaList.add("insert into Course (id, name, instructor) values (5, 'Physics', 'Dani Swallow');");
        schemaList.add("insert into Course (id, name, instructor) values (6, 'Digital Logic', 'Glenden Reilingen');");
        schemaList.add("insert into Course (id, name, instructor) values (7, 'Object Oriented Programming','Giselle Ardy');");
        schemaList.add("insert into Course (id, name, instructor) values (8, 'Data Structures', 'Carolan Stoller');");
        schemaList.add("insert into Course (id, name, instructor) values (9, 'Politics', 'Carmita De Maine');");
        schemaList.add("insert into Course (id, name, instructor) values (10, 'Art', 'Kingsly Doxsey');");
        schemaList.add("insert into Student (email, name, password) values ('hluckham0@google.ru', 'Hazel Luckham', 'X1uZcoIh0dj');");
        schemaList.add("insert into Student (email, name, password) values ('sbowden1@yellowbook.com', 'Sonnnie Bowden', 'SJc4aWSU');");
        schemaList.add("insert into Student (email, name, password) values ('qllorens2@howstuffworks.com', 'Quillan Llorens', 'W6rJuxd');");
        schemaList.add("insert into Student (email, name, password) values ('cstartin3@flickr.com', 'Clem Startin', 'XYHzJ1S');");
        schemaList.add("insert into Student (email, name, password) values ('tattwool4@biglobe.ne.jp', 'Thornie Attwool', 'Hjt0SoVmuBz');");
        schemaList.add("insert into Student (email, name, password) values ('hguerre5@deviantart.com', 'Harcourt Guerre', 'OzcxzD1PGs');");
        schemaList.add("insert into Student (email, name, password) values ('htaffley6@columbia.edu', 'Holmes Taffley', 'xowtOQ');");
        schemaList.add("insert into Student (email, name, password) values ('aiannitti7@is.gd', 'Alexandra Iannitti', 'TWP4hf5j');");
        schemaList.add("insert into Student (email, name, password) values ('ljiroudek8@sitemeter.com', 'Laryssa Jiroudek', 'bXRoLUP');");
        schemaList.add("insert into Student (email, name, password) values ('cjaulme9@bing.com', 'Cahra Jaulme', 'FnVklVgC6r6');");
        schemaList.add("insert into Student (email, name, password) values ('speed', 'speedwind', 'wind');");
        return schemaList;
    }


    private static void createDatabase() {
        DatabaseConnection.MARIADB.executeStatement("DROP DATABASE IF EXISTS management_system;");
        DatabaseConnection.MARIADB.executeStatement("CREATE DATABASE IF NOT EXISTS management_system;");
    }

    private static void useDatabase() {
        System.out.println("use db ran");
        DatabaseConnection.MARIADB.executeStatement("USE management_system;");
    }

    private static void createStudentTable() throws IOException {
        File creationStatementFile = DirectoryReference.RESOURCE_DIRECTORY.getFileFromDirectory("students.create-table.sql");
        BufferedReader br = new BufferedReader(new java.io.FileReader(creationStatementFile));
        String creationStatement;
        creationStatement = br.lines().collect(Collectors.joining());
        DatabaseConnection.MARIADB.executeStatement(creationStatement);
    }

    private static void createCourseTable() {
        File creationStatementFile = DirectoryReference.RESOURCE_DIRECTORY.getFileFromDirectory("courses.create-table.sql");
        FileReader fileReader = new FileReader(creationStatementFile.getAbsolutePath());
        String creationStatement = fileReader.toString();
        DatabaseConnection.MARIADB.executeStatement(creationStatement);
    }
}
