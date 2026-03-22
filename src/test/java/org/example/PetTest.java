package org.example;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.parsing.Parser;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PetTest {

    private static final int    PET_ID     = 12;
    private static final String PET_NAME   = "Гліб Ліндер";
    private static final String GROUP_NAME = "122-23ск-1";

    private static final String baseUrl   = "https://petstore.swagger.io/v2";
    private static final String PET       = "/pet";
    private static final String PET_BY_ID = PET + "/{petId}";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    public void verifyCreatePet() {
        Map<String, ?> body = Map.of(
                "id",       PET_ID,
                "name",     PET_NAME,
                "status",   "available",
                "category", Map.of(
                        "id",   1,
                        "name", GROUP_NAME
                ),
                "tags", List.of(
                        Map.of("id", 1, "name", GROUP_NAME)
                )
        );
        given().body(body)
                .post(PET)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("id",            equalTo(PET_ID))
                .and()
                .body("name",          equalTo(PET_NAME))
                .and()
                .body("status",        equalTo("available"))
                .and()
                .body("category.name", equalTo(GROUP_NAME));
    }

    @Test(dependsOnMethods = "verifyCreatePet")
    public void verifyGetPet() {
        given().pathParam("petId", PET_ID)
                .get(PET_BY_ID)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("id",            equalTo(PET_ID))
                .and()
                .body("name",          equalTo(PET_NAME))
                .and()
                .body("category.name", equalTo(GROUP_NAME));
    }

    @Test(dependsOnMethods = "verifyGetPet")
    public void verifyUpdatePet() {
        Map<String, ?> body = Map.of(
                "id",       PET_ID,
                "name",     PET_NAME,
                "status",   "sold",
                "category", Map.of(
                        "id",   1,
                        "name", GROUP_NAME
                )
        );
        given().body(body)
                .put(PET)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("id",     equalTo(PET_ID))
                .and()
                .body("status", equalTo("sold"));
    }

    @Test(dependsOnMethods = "verifyUpdatePet")
    public void verifyGetUpdatedPet() {
        given().pathParam("petId", PET_ID)
                .get(PET_BY_ID)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("status",        equalTo("sold"))
                .and()
                .body("category.name", equalTo(GROUP_NAME));
    }
}