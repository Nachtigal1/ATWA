package org.example;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.parsing.Parser;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Lab4Test {

    private static final String MOCK_URL      = "https://d594d49b-5364-4e86-a51d-8f6e9c852ae7.mock.pstmn.io";
    private static final String USER_SUCCESS   = "/user/success";
    private static final String USER_UNSUCCESS = "/user/unsuccess";
    private static final String STUDENT_INFO   = "/student/info";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = MOCK_URL;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader("Accept", "application/json")
                .build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    public void verifyGetSuccess() {
        given().get(USER_SUCCESS)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void verifyGetUnsuccess() {
        given().get(USER_UNSUCCESS)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void verifyGetStudentInfo() {
        given().get(STUDENT_INFO)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK);
    }
}