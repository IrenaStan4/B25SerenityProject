package b25.spartan.editor;

import org.junit.jupiter.api.DisplayName;
import utilities.SpartanNewBase;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utilities.SpartanUtil;

import java.util.Map;

import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Disabled
@SerenityTest
public class SpartanEditorPostTest extends SpartanNewBase {


    @DisplayName("Editor should be able to POST")
    @Test
    public void postSpartanAsEditor(){

        Map<String, Object> randomSpartanMap = SpartanUtil.getRandomSpartanMap();
        System.out.println("randomSpartanMap = " + randomSpartanMap);


        //send a post request as editor
        given()
                .auth().basic("editor","editor")
                .and()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(randomSpartanMap)
                .log().body()
        .when()
                .post("/spartans")
                .then().log().all();
    /*
                status code is 201
                content type is Json
                success message is A Spartan is Born!
                id is not null
                name is correct
                gender is correct
                phone is correct

                check location header ends with newly generated id
         */


        //status code is 201
        Ensure.that("Status code is 201", x -> x.statusCode(201));

        //content type is Json
        Ensure.that("Content-type is JSON", vR -> vR.contentType(ContentType.JSON));

        //A Spartan is Born!
        Ensure.that("success message is correct", vR -> vR.body("success",is("A Spartan is Born!")));

        //id is not null
        Ensure.that("ID is not null",vR -> vR.body("data.id",notNullValue()));

        //name is correct
        Ensure.that("name is correct",vR -> vR.body("data.name",is(randomSpartanMap.get("name"))));

        //gender is correct
        Ensure.that("gender is correct",vR -> vR.body("data.gender",is(randomSpartanMap.get("gender"))));

        //phone is correct
        Ensure.that("phone is correct",vR -> vR.body("data.phone",is(randomSpartanMap.get("phone"))));

        //check location header ends with newly generated id
        String id = lastResponse().jsonPath().getString("data.id");

        Ensure.that("check location header ends with newly generated id",
                    vR -> vR.header("Location",endsWith(id))
                );

    }


}
