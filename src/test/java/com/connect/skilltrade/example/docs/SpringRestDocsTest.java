package com.connect.skilltrade.example.docs;

import com.connect.skilltrade.example.docs.controller.request.SpringRestDocsRequest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SpringRestDocsTest {

    private RequestSpecification spec;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();
    }

    @DisplayName("Spring REST Docs 예제")
    @Test
    void sampleSpringRestDocs() {
        // given
        SpringRestDocsRequest request = new SpringRestDocsRequest("Spring REST Docs", 7);

        // when
        ExtractableResponse<Response> result = RestAssured
                .given(this.spec).log().all()
                .contentType("application/json")
                .body(request)
                .filter(document(
                        "spring-rest-docs/sample",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("field").type(STRING).description("Value"),
                                fieldWithPath("number").type(NUMBER).description("Number")
                        ),
                        responseFields(
                                fieldWithPath("status").type(BOOLEAN).description("Success or Failure"),
                                fieldWithPath("result").type(STRING).description("Result")
                        )
                ))
                .when()
                .port(this.port)
                .get("/play-ground/spring-rest-docs")
                .then().log().all()
                .extract();

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        JsonPath jsonPath = result.jsonPath();
        assertAll(
                () -> assertThat(jsonPath.getBoolean("status")).isTrue(),
                () -> assertThat(jsonPath.getString("result")).isEqualTo(String.format("Value: %s / Number: %d", request.field(), request.number()))
        );
    }
}
